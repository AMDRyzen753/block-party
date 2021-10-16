package link.therealdomm.heldix.game;

import com.google.common.util.concurrent.AtomicDouble;
import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.enumeration.EnumGameState;
import link.therealdomm.heldix.model.StatsModel;
import link.therealdomm.heldix.player.BlockPlayer;
import link.therealdomm.heldix.util.platform.PlatformType;
import link.therealdomm.heldix.util.random.RandomClayGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.stream.Collectors;

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

    public InGameState() {
        super(EnumGameState.IN_GAME);
        setCurrentGameState(this);
        BlockPlayer.getPlayers().forEach(BlockPlayer::addPlayedGame);
        this.teleportInGame();
    }

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
        this.task = Bukkit.getScheduler().runTaskTimer(
                BlockPartyPlugin.getInstance(),
                () -> {
                    if (atomicDouble.get() == this.time) {
                        this.task.cancel();
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

    public void checkPlayers(int color) {
        for (BlockPlayer player : BlockPlayer.getPlayers()) {
            if (!player.isInGame()) {
                player.sendMessage("bist anscheinend ned drin!");
                continue;
            }
            if (!player.checkBlock(Material.STAINED_CLAY, color)) {
                player.setInGame(false);
                player.addDeath();
                StatsModel statsModel = player.getStatsModel();
                BlockPartyPlugin.getInstance().getStatsRepo().updateStats(statsModel);
                this.teleportSpec();
                player.sendMessage("Nope bist raus!");
            } else {
                player.addPoints(BlockPartyPlugin.getInstance().getMainConfig().getPointsPerLevel());
                player.checkTopLevel();
                player.sendMessage("Jo, bist weiter!");
            }
        }
        int left = (int) BlockPlayer.getPlayers().stream().filter(BlockPlayer::isInGame).count();
        if (left < 2) {
            Optional<BlockPlayer> optional = BlockPlayer.getPlayers().stream().filter(BlockPlayer::isInGame).findFirst();
            if (!optional.isPresent()) {
                BlockPartyPlugin.getInstance().getLogger().severe("Error while getting winner, starting over...!");
                Bukkit.getScheduler().runTaskLater(BlockPartyPlugin.getInstance(), this::setupTask, 20*5L);
                return;
            }
            BlockPlayer player = optional.get();
            player.sendMessage("You won!");
            Bukkit.broadcastMessage(player.getDisplayName() + " won!");
            this.onNextGameState();
            return;
        }
        Bukkit.getScheduler().runTaskLater(BlockPartyPlugin.getInstance(), this::setupTask, 20*5L);
    }

    public void teleportPlayers(Location location) {
        for (BlockPlayer player : BlockPlayer.getPlayers()) {
            player.teleport(location);
        }
    }

    public void teleportInGame() {
        Location location = BlockPartyPlugin.getInstance().getMainConfig().getGameLocation().toLocation();
        this.teleportPlayers(location);
    }

    public void teleportSpec() {
        Location location = BlockPartyPlugin.getInstance().getMainConfig().getSpecLocation().toLocation();
        this.teleportPlayers(location);
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
