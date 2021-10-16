package link.therealdomm.heldix.game;

import com.google.gson.internal.Primitives;
import link.therealdomm.heldix.enumeration.EnumGameState;
import lombok.Getter;
import lombok.Setter;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public abstract class GameState {

    @Getter @Setter private static GameState currentGameState = null;

    public static  <T extends GameState> T getCurrentGameState(Class<? extends T> type) {
        if (currentGameState.getClass().getSimpleName().equals(type.getSimpleName())) {
            return Primitives.wrap(type).cast(currentGameState);
        }
        return null;
    }

    public static void initialize() {
        new LobbyGameState();
    }

    public static void reset() {
        currentGameState.disable();
        currentGameState = null;
    }

    @Getter private final EnumGameState gameState;

    public GameState(EnumGameState gameState) {
        this.gameState = gameState;
    }

    public abstract void onNextGameState();

    public abstract void disable();

}
