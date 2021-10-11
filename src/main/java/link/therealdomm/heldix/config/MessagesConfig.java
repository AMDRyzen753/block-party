package link.therealdomm.heldix.config;

import link.therealdomm.heldix.util.configuration.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
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
    }};

}
