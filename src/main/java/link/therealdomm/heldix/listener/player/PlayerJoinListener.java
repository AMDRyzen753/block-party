package link.therealdomm.heldix.listener.player;

import link.therealdomm.heldix.player.BlockPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        BlockPlayer blockPlayer = BlockPlayer.getPlayer(player);
    }

}
