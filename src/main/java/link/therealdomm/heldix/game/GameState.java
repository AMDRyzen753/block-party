package link.therealdomm.heldix.game;

import link.therealdomm.heldix.enumeration.EnumGameState;
import lombok.Getter;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public abstract class GameState {

    @Getter private final EnumGameState gameState;

    public GameState(EnumGameState gameState) {
        this.gameState = gameState;
    }



}
