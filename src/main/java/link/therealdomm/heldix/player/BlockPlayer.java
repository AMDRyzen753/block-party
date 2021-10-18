package link.therealdomm.heldix.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.handler.CoinAPIHandler;
import link.therealdomm.heldix.handler.MessageHandler;
import link.therealdomm.heldix.model.StatsModel;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * An adaptor class to handle player related things very quickly.
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Data
public class BlockPlayer {

    private static final Map<UUID, BlockPlayer> PLAYER_MAP = new LinkedHashMap<>();

    /**
     * remove an user from the cache
     * @param uuid of the player to be removed
     */
    public static void remove(UUID uuid) {
        PLAYER_MAP.remove(uuid);
    }

    /**
     * get a {@link List} of all {@link BlockPlayer} currently in cache
     * @return the list of players
     */
    public static List<BlockPlayer> getPlayers() {
        return new ArrayList<>(PLAYER_MAP.values());
    }

    /**
     * create a new {@link BlockPlayer} object or get the existing one
     * @param uuid of the player
     * @return the {@link BlockPlayer} object
     */
    public static BlockPlayer getPlayer(UUID uuid) {
        if (PLAYER_MAP.containsKey(uuid)) {
            return PLAYER_MAP.get(uuid);
        }
        if (Bukkit.getPlayer(uuid) == null) {
            return null;
        }
        BlockPlayer player = new BlockPlayer(Bukkit.getPlayer(uuid));
        PLAYER_MAP.put(uuid, player);
        return player;
    }

    /**
     * create a new {@link BlockPlayer} object or get the existing one
     * @param player the player obejct to be converted
     * @return the {@link BlockPlayer} object
     */
    public static BlockPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    /**
     * check if the player was registered
     * @param uuid of the player to check
     * @return true if player was registered
     */
    public static boolean hasPlayer(UUID uuid) {
        return PLAYER_MAP.containsKey(uuid);
    }

    private final UUID uuid;
    private final String name;
    private final String displayName;
    private int currentLevel = 0;
    private StatsModel statsModel;
    private boolean inGame = true;
    private int coins = 0;

    /**
     * default constructor to create new instance
     * @param player the player to be converted to {@link BlockPlayer}
     */
    public BlockPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.displayName = player.getDisplayName();
        BlockPartyPlugin.getInstance().getStatsRepo().getStats(player.getUniqueId(), statsModel -> {
            this.statsModel = statsModel;
            if (!this.statsModel.isExists()) {
                BlockPartyPlugin.getInstance().getStatsRepo().createPlayer(player.getUniqueId());
            }
        });
    }

    /**
     * teleports the player to the specified location
     * @param location to teleport to
     */
    public void teleport(Location location) {
        Player player;
        if ((player = Bukkit.getPlayer(this.uuid)) != null) {
            player.teleport(location);
        }
    }

    /**
     * check if the block underneath the player equals the requested
     * @param material of the block
     * @param color of the block (the sub-id)
     * @return true if block matches
     */
    @SuppressWarnings("deprecation") //suppress the deprecation warning for Block#getData
    public boolean checkBlock(Material material, int color) {
        Player player;
        if ((player = Bukkit.getPlayer(this.uuid)) != null) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if (block.getType().equals(Material.AIR) &&
                    player.getLocation().subtract(0, 1, 0).getBlock()
                    .getRelative(BlockFace.DOWN).getType().equals(material)) {
                block = player.getLocation().subtract(0, 1, 0).getBlock().getRelative(BlockFace.DOWN);
            }
            return block.getType().equals(material) && block.getData() == color; //may break if not used with 1.8->1.12
        }
        return false;
    }

    /**
     * add a specified amount of points to the players stats
     * @param points to be added
     */
    public void addPoints(int points) {
        this.statsModel.add(StatsModel.StatEntry.POINTS, points);
    }

    /**
     * increments the amount of deaths in the stats by one
     */
    public void addDeath() {
        this.statsModel.increment(StatsModel.StatEntry.DEATHS);
    }

    /**
     * increments the amount of won games in the stats by one
     */
    public void addWonGame() {
        this.statsModel.increment(StatsModel.StatEntry.WON_GAMES);
    }

    /**
     * increments the amount of played games in the stats by one
     */
    public void addPlayedGame() {
        this.statsModel.increment(StatsModel.StatEntry.GAMES_PLAYED);
    }

    /**
     * increments the top level by one if the current level is higher than the one saved in the stats
     */
    public void checkTopLevel() {
        if (this.currentLevel > this.statsModel.getTopLevel()) {
            this.statsModel.increment(StatsModel.StatEntry.TOP_LEVEL);
        }
    }

    /**
     * increments the current level by one
     */
    public void addLevel() {
        this.currentLevel++;
    }

    /**
     * adds the coins every level the player does not get eliminated
     */
    public void addCoins() {
        int coins = BlockPartyPlugin.getInstance().getMainConfig().getCoinsPerAbsolvedLevel();
        this.coins += coins;
    }

    /**
     * adds the coins finally and sends the player a messages how many coins he has got on top
     */
    public void dispatchCoins() {
        Player player;
        if ((player = Bukkit.getPlayer(this.uuid)) != null) {
            CoinAPIHandler.addCoins(player, this.coins);
            player.sendMessage(MessageHandler.getMessage("coins.added.amount", this.coins));
        }
    }

    /**
     * send the specified message to the player
     * @param message to be sent
     */
    public void sendMessage(String message) {
        Player player;
        if ((player = Bukkit.getPlayer(this.uuid)) != null) {
            player.sendMessage(message);
        }
    }

    public void playPing() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        String sound;
        if (version.contains("v1_8")) {
            sound = "LEVEL_UP";
        } else {
            sound = "ENTITY_PLAYER_LEVELUP";
        }
        Player player;
        if ((player = Bukkit.getPlayer(this.uuid)) != null) {
            player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
        }
    }

}
