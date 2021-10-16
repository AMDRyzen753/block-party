package link.therealdomm.heldix.countdown;

import org.bukkit.Bukkit;

/**
 * @author TheRealDomm
 * @since 16.10.2021
 */
public class EndingCountdown extends Countdown {

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {
        Bukkit.shutdown();
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage("Ended. Shutting down in " + this.getRemainingTime());
    }
}
