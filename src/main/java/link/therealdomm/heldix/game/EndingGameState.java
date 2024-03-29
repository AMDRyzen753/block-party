package link.therealdomm.heldix.game;

import link.therealdomm.heldix.countdown.EndingCountdown;
import link.therealdomm.heldix.enumeration.EnumGameState;

/**
 * the implementation of the game state at the end of the game round
 *
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class EndingGameState extends GameState {

    public EndingGameState() {
        super(EnumGameState.ENDING);
        setCurrentGameState(this);
        EndingCountdown endingCountdown = new EndingCountdown();
        endingCountdown.startCountdown(10);
    }

    @Override
    public void onNextGameState() {
        /* can be safely ignored, last game state */
    }

    @Override
    public void disable() {

    }
}
