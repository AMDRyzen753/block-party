package link.therealdomm.heldix.game;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.countdown.LobbyCountdown;
import link.therealdomm.heldix.enumeration.EnumGameState;
import link.therealdomm.heldix.handler.MessageHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * the implementation of the game state for the time in lobby phase
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Getter
public class LobbyGameState extends GameState {

    @Setter private boolean started = false;
    private BukkitTask waitingTask;
    @Setter private LobbyCountdown lobbyCountdown;

    public LobbyGameState() {
        super(EnumGameState.LOBBY);
        setCurrentGameState(this);
        this.setWaitingTask();
    }

    /**
     * creates the task that is responsible for the waiting time when not enough player are online
     */
    public void setWaitingTask() {
        AtomicInteger i = new AtomicInteger(10);
        this.waitingTask = Bukkit.getScheduler().runTaskTimer(
                BlockPartyPlugin.getInstance(),
                () -> {
                    if (i.get() <= 0) {
                        Bukkit.broadcastMessage(MessageHandler.getMessage("lobby.waiting"));
                        i.set(10);
                    }
                    i.getAndDecrement();
                },
                0,
                20
        );
    }

    /**
     * starts the {@link LobbyCountdown}
     */
    public void startCountdown() {
        this.waitingTask.cancel();
        this.lobbyCountdown = new LobbyCountdown();
        this.lobbyCountdown.startCountdown(BlockPartyPlugin.getInstance().getMainConfig().getLobbyTimer());
    }

    @Override
    public void onNextGameState() {
        this.lobbyCountdown.cancel();
        new InGameState();
    }

    @Override
    public void disable() {
        if (this.lobbyCountdown != null) {
            this.lobbyCountdown.cancel();
        }
        else {
            this.waitingTask.cancel();
        }
    }
}
