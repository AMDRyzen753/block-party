package link.therealdomm.heldix.handler;

import de.reminios.coinapi.CoinAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class CoinAPIHandler {

    @Getter @Setter private static boolean active = false;

    public static void addCoins(Player player, int amount) {
        if (!active) {
            return;
        }
        CoinAPI.addCoins(player.getName(), amount);
    }

}
