package link.therealdomm.heldix.config;

import link.therealdomm.heldix.util.MySQLData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MainConfig {

    private MySQLData data = new MySQLData();
    private int coinsPerAbsolvedLevel = 100;
    private int minPlayers = 2;
    private int maxPlayers = 24;
    private boolean usePremiumKick = false;
    private int lobbyTimer = 60;
    private int fullRoundTimer = 10;
    private boolean disableTabComplete = true;
    private String[] tabCompletableCommands = new String[]{"help", "start", "coins"};

}
