package link.therealdomm.heldix.listener.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.game.GameState;
import link.therealdomm.heldix.game.LobbyGameState;
import link.therealdomm.heldix.player.BlockPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BlockPlayer.remove(player.getUniqueId());
        BlockPartyPlugin.getInstance().getSpectatorHandler().removeSpectator(player);
        event.setQuitMessage(null);
        if (GameState.getCurrentGameState(LobbyGameState.class) == null) {
            return;
        }
        if (BlockPartyPlugin.getInstance().getMainConfig().getMinPlayers() > Bukkit.getOnlinePlayers().size()) {
            if (Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).getLobbyCountdown() != null) {
                Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).getLobbyCountdown().cancel();
                Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).setLobbyCountdown(null);
                Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).setWaitingTask();
            }
        }
    }

}
