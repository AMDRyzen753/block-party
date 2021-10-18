package link.therealdomm.heldix.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * a simple handler for the ProtocolLib plugin
 *
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class ProtocolLibHandler {

    public ProtocolLibHandler(JavaPlugin javaPlugin) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new TabCompleteProtocolListener(javaPlugin));
    }

}
