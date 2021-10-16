package link.therealdomm.heldix.config;

import link.therealdomm.heldix.util.configuration.Config;
import link.therealdomm.heldix.util.location.PasteLocation;
import link.therealdomm.heldix.util.location.PlayerLocation;
import link.therealdomm.heldix.util.mysql.MySQLData;
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
public class MainConfig implements Config {

    private double configVersion = 1.0D;
    private MySQLData data = new MySQLData();
    private int coinsPerAbsolvedLevel = 100;
    private int pointsPerLevel = 10;
    private int minPlayers = 2;
    private int maxPlayers = 24;
    private boolean usePremiumKick = false;
    private int lobbyTimer = 60;
    private int fullRoundTimer = 10;
    private Integer[] announceTimes = {60, 30, 20, 10, 5, 3, 2, 1};
    private boolean disableTabComplete = true;
    private PasteLocation pasteLocation = new PasteLocation();
    private String[] tabCompletableCommands = new String[]{"help", "start", "coins"};
    private PlayerLocation specLocation = new PlayerLocation();
    private PlayerLocation gameLocation = new PlayerLocation();
    private PlayerLocation lobbyLocation = new PlayerLocation();
    private double initialSeconds = 10.0D;
    private double minimalSeconds = 0.5D;
    private double decreaseSeconds = 0.5D;

}
