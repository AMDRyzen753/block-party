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
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

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
    }

    public void setupTask() {
        Location paste = BlockPartyPlugin.getInstance().getMainConfig().getPasteLocation().toLocation();
        PlatformType platformType = RandomClayGenerator.getFieldByColor(RandomClayGenerator.getRandomClayColor());
        if (platformType == null) {
            this.setupTask();
            return;
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
                continue;
            }
            if (!player.checkBlock(Material.STAINED_CLAY, color)) {
                player.setInGame(false);
                player.addDeath();
                StatsModel statsModel = player.getStatsModel();
                BlockPartyPlugin.getInstance().getStatsRepo().updateStats(statsModel);
            } else {
                player.addPoints(BlockPartyPlugin.getInstance().getMainConfig().getPointsPerLevel());
                player.checkTopLevel();
            }
        }
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
}
