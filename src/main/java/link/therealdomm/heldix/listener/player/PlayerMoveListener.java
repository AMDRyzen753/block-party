package link.therealdomm.heldix.listener.player;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.game.GameState;
import link.therealdomm.heldix.game.InGameState;
import link.therealdomm.heldix.handler.MessageHandler;
import link.therealdomm.heldix.handler.SpectatorHandler;
import link.therealdomm.heldix.model.StatsModel;
import link.therealdomm.heldix.player.BlockPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

/**
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (BlockPartyPlugin.getInstance().getSpectatorHandler().getSpectators().contains(event.getPlayer())) {
            if (event.getPlayer().getLocation().getY() <= 0) {
                event.getPlayer().teleport(
                        BlockPartyPlugin.getInstance().getMainConfig().getSpecLocation().toLocation()
                );
            }
            return;
        }
        if (GameState.getCurrentGameState(InGameState.class) == null) {
            return;
        }
        if (event.getPlayer().getLocation().getY() <= 0) {
            BlockPlayer player = BlockPlayer.getPlayer(event.getPlayer());
            player.setInGame(false);
            player.addDeath();
            StatsModel statsModel = player.getStatsModel();
            BlockPartyPlugin.getInstance().getStatsRepo().updateStats(statsModel);
            player.teleport(BlockPartyPlugin.getInstance().getMainConfig().getSpecLocation().toLocation());
            BlockPartyPlugin.getInstance().getSpectatorHandler().setSpectator(Bukkit.getPlayer(player.getUuid()));
            player.sendMessage(MessageHandler.getMessage("ingame.eliminated"));
            player.dispatchCoins();
            Objects.requireNonNull(GameState.getCurrentGameState(InGameState.class)).getLastGone().add(player);
        }
    }

}
