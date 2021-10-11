package link.therealdomm.heldix.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.model.StatsModel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Getter
public class BlockPlayer {

    private static final Map<UUID, BlockPlayer> PLAYER_MAP = new LinkedHashMap<>();

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

    public BlockPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.displayName = player.getDisplayName();
        BlockPartyPlugin.getInstance().getStatsRepo()
                .getStats(this.uuid, (statsModel) -> BlockPlayer.this.statsModel = statsModel);
    }

}
