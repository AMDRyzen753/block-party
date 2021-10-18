package link.therealdomm.heldix.config;

import link.therealdomm.heldix.util.configuration.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * the data holder class for the messages.json
 *
 * @author TheRealDomm
 * @since 11.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MessagesConfig implements Config {

    private double configVersion = 1.0D;
    private Map<String, String> messages = new HashMap<String, String>() {{
        put("prefix", "&7[&cBlockParty&7]");
        put("lobby.countdown.format", "%prefix% &cDie Runde startet in {0} Sekunden.");
        put("lobby.waiting", "%prefix% &7Warte auf Spieler...");
        put("game.started", "%prefix% &aDu hast die Runde gestartet.");
        put("lobby.started_already", "%prefix% &cDie Runde wurde bereits gestartet.");
        put("server.restarting", "%prefix% &eDer Server startet in {0} Sekunden neu.");
        put("ingame.eliminated", "%prefix% &aDu bist ausgeschieden.");
        put("ingame.passed", "%prefix% &aDu bist eine Runde weiter.");
        put("self.won", "%prefix% &aDu hast diese Runde gewonnen.");
        put("other.won", "%prefix% &aDie Runde hat {0} gewonnen!");
        put("multiple.won", "%prefix% &aDie Spieler {0} haben die Runde gewonnen.");
        put("kicked.by.premium", "%prefix% &aDu wurdest gekickt da ein Premiumspieler die volle Runde betreten hat.");
        put("round.full", "%prefix% &cDiese Runde ist voll!");
        put("round.full.get_premium", "%prefix% &cDiese Runde ist voll. Kaufe dir Premium um volle Runden betreten zu können.");
        put("coins.added.amount", "%prefix% &aDir wurden {0} Coins hinzugefügt.");
    }};

    private String countDownSymbol = "§0#";
    private String countDownFormat = "§b§l%color_name%";
    private String countDownStop = "§c§lSTOP";

}
