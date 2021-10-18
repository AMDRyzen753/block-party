package link.therealdomm.heldix.command;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.game.GameState;
import link.therealdomm.heldix.game.LobbyGameState;
import link.therealdomm.heldix.handler.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * a command to skip waiting time of 60 seconds during lobby time
 *
 * @author TheRealDomm
 * @since 15.10.2021
 */
public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        LobbyGameState gameState;
        if ((gameState = GameState.getCurrentGameState(LobbyGameState.class)) != null) {
            if (BlockPartyPlugin.getInstance().getMainConfig().getMinPlayers() > Bukkit.getOnlinePlayers().size()) {
                sender.sendMessage(MessageHandler.getMessage("lobby.waiting"));
                return true;
            }
            if (!gameState.isStarted()) {
                if (gameState.getLobbyCountdown() != null && gameState.getLobbyCountdown().getRemainingTime() > 10) {
                    gameState.getLobbyCountdown().setRemainingTime(10);
                    gameState.setStarted(true);
                    sender.sendMessage(MessageHandler.getMessage("game.started"));
                }
            } else {
                sender.sendMessage(MessageHandler.getMessage("lobby.started_already"));
            }
        }
        return true;
    }
}
