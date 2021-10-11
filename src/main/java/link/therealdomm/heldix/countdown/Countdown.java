package link.therealdomm.heldix.countdown;

import link.therealdomm.heldix.BlockPartyPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public abstract class Countdown implements Runnable {

    @Getter private boolean running = false;

    @Getter private int initialTime;

    @Getter @Setter private int remainingTime;

    @Getter private BukkitTask task;

    public void startCountdown(int remainingTime) {
        this.running = true;
        this.initialTime = remainingTime;
        this.remainingTime = remainingTime;
        this.onStart();
        this.task = Bukkit.getScheduler().runTaskTimer(
                BlockPartyPlugin.getInstance(),
                () -> {
                    if (Countdown.this.remainingTime <= 0) {
                        Countdown.this.cancel();
                        Countdown.this.onEnd();
                        return;
                    }
                    Countdown.this.run();
                    Countdown.this.remainingTime--;
                },
                0,
                20
        );
    }

    public abstract void onStart();

    public abstract void onEnd();

    public void cancel() {
        this.running = false;
        this.task.cancel();
    }

}
