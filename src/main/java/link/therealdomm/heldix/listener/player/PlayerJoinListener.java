package link.therealdomm.heldix.listener.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.game.GameState;
import link.therealdomm.heldix.game.InGameState;
import link.therealdomm.heldix.game.LobbyGameState;
import link.therealdomm.heldix.player.BlockPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        event.setJoinMessage(null);
        player.teleport(BlockPartyPlugin.getInstance().getMainConfig().getLobbyLocation().toLocation());
        if (GameState.getCurrentGameState(LobbyGameState.class) == null) {
            if (GameState.getCurrentGameState(InGameState.class) != null) {
                BlockPartyPlugin.getInstance().getSpectatorHandler().setSpectator(player);
            }
            return;
        }
        BlockPlayer.getPlayer(player);
        if (BlockPartyPlugin.getInstance().getMainConfig().getMinPlayers() <= Bukkit.getOnlinePlayers().size()) {
            if (Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).getLobbyCountdown() == null) {
                Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).getWaitingTask().cancel();
                Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).startCountdown();
            }
        }
        if (BlockPartyPlugin.getInstance().getMainConfig().getMaxPlayers() <= Bukkit.getOnlinePlayers().size()) {
            if (Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).getLobbyCountdown()
                    .getRemainingTime() > BlockPartyPlugin.getInstance().getMainConfig().getFullRoundTimer()) {
                Objects.requireNonNull(GameState.getCurrentGameState(LobbyGameState.class)).getLobbyCountdown()
                        .setRemainingTime(BlockPartyPlugin.getInstance().getMainConfig().getFullRoundTimer());
            }
        }
    }

}
