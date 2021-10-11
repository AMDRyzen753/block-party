package link.therealdomm.heldix.countdown;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.game.GameState;
import link.therealdomm.heldix.handler.MessageHandler;
import org.bukkit.Bukkit;

import java.util.Arrays;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class LobbyCountdown extends Countdown {

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        GameState.getCurrentGameState().onNextGameState();
    }

    @Override
    public void run() {
        if (Arrays.asList(BlockPartyPlugin.getInstance().getMainConfig().getAnnounceTimes())
                .contains(this.getRemainingTime())) {
            Bukkit.broadcastMessage(MessageHandler.getMessage("lobby.countdown.format", this.getRemainingTime()));
        }
    }
}
