package link.therealdomm.heldix.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.model.StatsModel;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Data
public class BlockPlayer {

    private static final Map<UUID, BlockPlayer> PLAYER_MAP = new LinkedHashMap<>();

    public static List<BlockPlayer> getPlayers() {
        return new ArrayList<>(PLAYER_MAP.values());
    }

    public static BlockPlayer getPlayer(UUID uuid) {
        if (PLAYER_MAP.containsKey(uuid)) {
            return PLAYER_MAP.get(uuid);
        }
        if (Bukkit.getPlayer(uuid) != null) {
            return null;
        }
        BlockPlayer player = new BlockPlayer(Bukkit.getPlayer(uuid));
        PLAYER_MAP.put(uuid, player);
        return player;
    }

    public static BlockPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    private final UUID uuid;
    private final String name;
    private final String displayName;
    private int currentLevel = 0;
    private StatsModel statsModel;
    private boolean inGame = true;

    public BlockPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.displayName = player.getDisplayName();
        BlockPartyPlugin.getInstance().getStatsRepo()
                .getStats(this.uuid, (statsModel) -> BlockPlayer.this.statsModel = statsModel);
    }

    public void teleport(Location location) {
        Player player;
        if ((player = Bukkit.getPlayer(this.uuid)) != null) {
            player.teleport(location);
        }
    }

    /** suppress the deprecation warning for {@link Block#getData()} **/
    @SuppressWarnings("deprecation")
    public boolean checkBlock(Material material, int color) {
        Player player;
        if ((player = Bukkit.getPlayer(this.uuid)) != null) {
            Block block = player.getLocation().getBlock();
            return block.getType().equals(material) && block.getData() == color; //will only work for 1.8 -> 1.12
        }
        return false;
    }

    public void addPoints(int points) {
        this.statsModel.add(StatsModel.StatEntry.POINTS, points);
    }

    public void addDeath() {
        this.statsModel.add(StatsModel.StatEntry.DEATHS, 1);
    }

    public void addWonGame() {
        this.statsModel.add(StatsModel.StatEntry.WON_GAMES, 1);
    }

    public void addPlayedGame() {
        this.statsModel.add(StatsModel.StatEntry.GAMES_PLAYED, 1);
    }

    public void checkTopLevel() {
        if (this.currentLevel > this.statsModel.getTopLevel()) {
            this.statsModel.add(StatsModel.StatEntry.TOP_LEVEL, 1);
        }
    }

}
