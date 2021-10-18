package link.therealdomm.heldix.listener.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

/**
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class PlayerSpawnListener implements Listener {

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(BlockPartyPlugin.getInstance().getMainConfig().getLobbyLocation().toLocation());
    }

}
