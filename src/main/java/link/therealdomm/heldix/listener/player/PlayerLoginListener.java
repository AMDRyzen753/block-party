package link.therealdomm.heldix.listener.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.game.GameState;
import link.therealdomm.heldix.game.LobbyGameState;
import link.therealdomm.heldix.handler.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class PlayerLoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!BlockPartyPlugin.getInstance().getMainConfig().isUsePremiumKick()) {
            return;
        }
        Player player = event.getPlayer();
        if (GameState.getCurrentGameState(LobbyGameState.class) == null) {
            return;
        }
        if (Bukkit.getOnlinePlayers().size() > BlockPartyPlugin.getInstance().getMainConfig().getMaxPlayers()) {
            if (player.hasPermission("blockparty.premiumkick")) {
                List<Player> possiblePlayers = Bukkit.getOnlinePlayers().stream()
                        .filter(p -> !p.hasPermission("blockparty.premiumkick")).collect(Collectors.toList());
                if (!possiblePlayers.isEmpty()) {
                    possiblePlayers.get(0).kickPlayer(MessageHandler.getMessage("kicked.by.premium"));
                    event.allow();
                } else {
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageHandler.getMessage("round.full"));
                }
            } else {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, MessageHandler.getMessage("round.full.get_premium"));
            }
        }
    }

}
