package link.therealdomm.heldix.game;

import link.therealdomm.heldix.enumeration.EnumGameState;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public abstract class GameState {

    @Getter @Setter private static GameState currentGameState = null;

    public static void initialize() {
        new LobbyGameState();
    }

    @Getter private final EnumGameState gameState;

    public GameState(EnumGameState gameState) {
        this.gameState = gameState;
    }

    public abstract void onNextGameState();

}
