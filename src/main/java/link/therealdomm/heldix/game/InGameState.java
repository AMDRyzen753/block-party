package link.therealdomm.heldix.game;

import com.google.common.util.concurrent.AtomicDouble;
import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.enumeration.EnumGameState;
import link.therealdomm.heldix.handler.ActionBarHandler;
import link.therealdomm.heldix.handler.MessageHandler;
import link.therealdomm.heldix.model.StatsModel;
import link.therealdomm.heldix.player.BlockPlayer;
import link.therealdomm.heldix.util.platform.PlatformType;
import link.therealdomm.heldix.util.random.RandomClayGenerator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class InGameState extends GameState {

    private final double decrease = BlockPartyPlugin.getInstance().getMainConfig().getDecreaseSeconds();
    private final double minimal = BlockPartyPlugin.getInstance().getMainConfig().getMinimalSeconds();
    private Double time = BlockPartyPlugin.getInstance().getMainConfig().getInitialSeconds();
    private int run = 0;
    private BukkitTask task;
    private BukkitTask actionBarTask;
    @Getter private List<BlockPlayer> lastGone = new ArrayList<>();

    public InGameState() {
        super(EnumGameState.IN_GAME);
        setCurrentGameState(this);
        BlockPlayer.getPlayers().forEach(BlockPlayer::addPlayedGame);
        this.teleportInGame();
        Bukkit.getScheduler().runTaskLater(
                BlockPartyPlugin.getInstance(),
                this::setupTask,
                20*5L
        );
    }

    /**
     * set's up the task for pasting random new schematics
     */
    public void setupTask() {
        Location paste = BlockPartyPlugin.getInstance().getMainConfig().getPasteLocation().toLocation();
        PlatformType platformType = RandomClayGenerator.getFieldByColor();
        if (platformType == null) {
            this.setupTask();
            return;
        }
        for (BlockPlayer player : BlockPlayer.getPlayers()) {
            if (player.isInGame()) {
                Player bukkit = Bukkit.getPlayer(player.getUuid());
                if (bukkit != null) {
                    bukkit.getInventory().setItem(4, new ItemStack(Material.STAINED_CLAY,
                            1, (byte) platformType.getColor()));
                }
            }
        }
        platformType.paste(platformType.getInitialField(), paste);
        if (this.run >= 1) {
            if (this.time > this.minimal) {
                this.time = this.time - this.decrease;
            }
        }
        this.run++;
        AtomicDouble atomicDouble = new AtomicDouble(0);
        if (this.actionBarTask != null) {
            this.actionBarTask.cancel();
            this.actionBarTask = null;
        }
        this.task = Bukkit.getScheduler().runTaskTimer(
                BlockPartyPlugin.getInstance(),
                () -> {
                    ActionBarHandler.sendCountDown(Long.valueOf(Math.round(this.time-atomicDouble.get())).intValue(), platformType.getColor());
                    if (atomicDouble.get() == this.time) {
                        this.task.cancel();
                        this.actionBarTask = ActionBarHandler.sendStop();
                        platformType.paste(platformType.getAirField(), paste);
                        this.checkPlayers(platformType.getColor());
                        return;
                    }
                    atomicDouble.getAndAdd(0.5D);
                },
                0,
                10
        );
    }

    /**
     * checks if the players are standing on the right color
     * @param color the sub id of the clay block to check
     */
    public void checkPlayers(int color) {
        this.lastGone.clear();
        for (BlockPlayer player : BlockPlayer.getPlayers()) {
            if (!player.isInGame()) {
                continue;
            }
            if (!player.checkBlock(Material.STAINED_CLAY, color)) {
                player.setInGame(false);
                player.addDeath();
                StatsModel statsModel = player.getStatsModel();
                BlockPartyPlugin.getInstance().getStatsRepo().updateStats(statsModel);
                this.teleportSpec(player);
                BlockPartyPlugin.getInstance().getSpectatorHandler().setSpectator(Bukkit.getPlayer(player.getUuid()));
                player.sendMessage(MessageHandler.getMessage("ingame.eliminated"));
                player.dispatchCoins();
                this.lastGone.add(player);
            } else {
                player.addLevel();
                player.addPoints(BlockPartyPlugin.getInstance().getMainConfig().getPointsPerLevel());
                player.addCoins();
                player.checkTopLevel();
                player.sendMessage(MessageHandler.getMessage("ingame.passed"));
                player.playPing();
            }
        }
        int left = (int) BlockPlayer.getPlayers().stream().filter(BlockPlayer::isInGame).count();
        if (left < 2) {
            Optional<BlockPlayer> optional = BlockPlayer.getPlayers().stream().filter(BlockPlayer::isInGame).findFirst();
            if (!optional.isPresent()) {
                StringBuilder builder = new StringBuilder();
                for (BlockPlayer player : this.lastGone) {
                    builder.append(player.getDisplayName()).append(", ");
                }
                String substring;
                try {
                    substring = builder.substring(0, builder.toString().length() - 2);
                } catch (Exception e) {
                    substring = "niemand";
                }
                for (BlockPlayer player : BlockPlayer.getPlayers()) {
                    if (this.lastGone.contains(player)) {
                        player.sendMessage(MessageHandler.getMessage("self.won"));
                        player.addWonGame();
                        player.dispatchCoins();
                        BlockPartyPlugin.getInstance().getStatsRepo().updateStats(player.getStatsModel());
                    } else {
                        player.sendMessage(MessageHandler.getMessage("multiple.won", substring));
                    }
                }
                BlockPartyPlugin.getInstance().getLogger().severe("Round had multiple winners. (" + substring + ")");
                this.onNextGameState();
                return;
            }
            BlockPlayer player = optional.get();
            player.sendMessage(MessageHandler.getMessage("self.won"));
            player.dispatchCoins();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getUniqueId().equals(player.getUuid())) {
                    onlinePlayer.sendMessage(MessageHandler.getMessage("other.won", player.getDisplayName()));
                }
            }
            player.addWonGame();
            BlockPartyPlugin.getInstance().getStatsRepo().updateStats(player.getStatsModel());
            this.onNextGameState();
            return;
        }
        Bukkit.getScheduler().runTaskLater(BlockPartyPlugin.getInstance(), this::setupTask, 20*2L);
    }

    /**
     * teleports all players to a specific location
     * @param location to teleport players to
     */
    public void teleportPlayers(Location location) {
        for (BlockPlayer player : BlockPlayer.getPlayers()) {
            player.teleport(location);
        }
    }

    /**
     * teleports all players to the in game location
     */
    public void teleportInGame() {
        Location location = BlockPartyPlugin.getInstance().getMainConfig().getGameLocation().toLocation();
        this.teleportPlayers(location);
    }

    /**
     * teleports a single player to the spectator location
     * @param player the {@link BlockPlayer} to be teleported
     */
    public void teleportSpec(BlockPlayer player) {
        Location location = BlockPartyPlugin.getInstance().getMainConfig().getSpecLocation().toLocation();
        player.teleport(location);
    }

    @Override
    public void onNextGameState() {
        new EndingGameState();
    }

    @Override
    public void disable() {
        this.task.cancel();
    }
}
