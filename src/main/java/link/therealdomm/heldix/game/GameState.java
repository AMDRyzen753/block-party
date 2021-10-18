package link.therealdomm.heldix.game;

import com.google.gson.internal.Primitives;
import link.therealdomm.heldix.enumeration.EnumGameState;
import lombok.Getter;
import lombok.Setter;

/**
 * an abstract implementation for the various states during the game
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
public abstract class GameState {

    @Getter @Setter private static GameState currentGameState = null;

    /**
     * gets the current game state as the implementing class
     * @param type the implementing class of the current assumed game state
     * @param <T> the assumed implementing state
     * @return the current state if matches the type else null
     */
    public static  <T extends GameState> T getCurrentGameState(Class<? extends T> type) {
        if (currentGameState.getClass().getSimpleName().equals(type.getSimpleName())) {
            return Primitives.wrap(type).cast(currentGameState);
        }
        return null;
    }

    /**
     * initializes the first game state
     */
    public static void initialize() {
        new LobbyGameState();
    }

    /**
     * resets the current game state and can be reinitialized by a call of {@link GameState#initialize()}
     */
    public static void reset() {
        currentGameState.disable();
        currentGameState = null;
    }

    @Getter private final EnumGameState gameState;

    /**
     * the default constructor for game states
     * @param gameState to assign
     */
    public GameState(EnumGameState gameState) {
        this.gameState = gameState;
    }

    /**
     * can be called to execute the next game state
     */
    public abstract void onNextGameState();

    /**
     * will reset all login in this game state and disables it
     */
    public abstract void disable();

}
