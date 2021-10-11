package link.therealdomm.heldix.game;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.countdown.LobbyCountdown;
import link.therealdomm.heldix.enumeration.EnumGameState;
import link.therealdomm.heldix.handler.MessageHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Getter
public class LobbyGameState extends GameState {

    private BukkitTask waitingTask;
    private LobbyCountdown lobbyCountdown;

    public LobbyGameState() {
        super(EnumGameState.LOBBY);
        setCurrentGameState(this);
        this.setWaitingTask();
    }

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

    public void startCountdown() {
        this.lobbyCountdown = new LobbyCountdown();
        this.lobbyCountdown.startCountdown(BlockPartyPlugin.getInstance().getMainConfig().getLobbyTimer());
    }

    @Override
    public void onNextGameState() {
        new InGameState();
    }
}
