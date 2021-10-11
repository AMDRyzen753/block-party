package link.therealdomm.heldix.game;

import link.therealdomm.heldix.enumeration.EnumGameState;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class InGameState extends GameState {

    public InGameState() {
        super(EnumGameState.IN_GAME);
        setCurrentGameState(this);
    }

    @Override
    public void onNextGameState() {
        new EndingGameState();
    }
}
