package link.therealdomm.heldix.handler;

import link.therealdomm.heldix.BlockPartyPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * the handler class for the Heldix.NET CoinAPI
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class CoinAPIHandler {

    @Getter @Setter private static boolean active = false;

    /**
     * adds the specified amount of coins to the player
     * @param player the coins should be added to
     * @param amount that should be added
     */
    public static void addCoins(Player player, int amount) {
        if (!active) {
            return;
        }
        try {
            Class<?> coinApiClass = Class.forName("de.reminios.coinapi.CoinAPI");
            Method addCoinMethod = coinApiClass.getMethod("addCoins", String.class, double.class);
            addCoinMethod.invoke(coinApiClass, player.getName(), amount);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
    }

}
