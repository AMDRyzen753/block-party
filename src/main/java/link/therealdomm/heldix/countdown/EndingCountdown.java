package link.therealdomm.heldix.countdown;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.handler.CloudHandler;
import link.therealdomm.heldix.handler.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * the countdown implementation for the end of the game round
 *
 * @author TheRealDomm
 * @since 16.10.2021
 */
public class EndingCountdown extends Countdown {

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            CloudHandler.sendPlayerToLobby(onlinePlayer);
        }
        Bukkit.getScheduler().runTaskLater(BlockPartyPlugin.getInstance(), Bukkit::shutdown, 20*10L);
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage(MessageHandler.getMessage("server.restarting", this.getRemainingTime()));
    }
}
