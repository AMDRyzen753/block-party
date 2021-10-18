package link.therealdomm.heldix.handler;

import link.therealdomm.heldix.player.BlockPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * handler class for the spectators during game
 *
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class SpectatorHandler {

    private List<Player> spectators = new ArrayList<>();

    /**
     * removes a spectator from the spec list
     * @param player to be removed
     */
    public void removeSpectator(Player player) {
        this.spectators.remove(player);
    }

    /**
     * adds a player to spectator list and makes him invisible for the players still in game
     * @param player to be added as spectator
     */
    public void setSpectator(Player player) {
        if (player == null) {
            return;
        }
        player.getInventory().clear();
        player.setAllowFlight(true);
        this.spectators.add(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (this.spectators.contains(onlinePlayer)) {
                player.showPlayer(onlinePlayer);
            } else {
                if (BlockPlayer.hasPlayer(onlinePlayer.getUniqueId())) {
                    if (BlockPlayer.getPlayer(onlinePlayer).isInGame()) {
                        onlinePlayer.hidePlayer(player);
                    }
                }
            }
        }
    }



}
