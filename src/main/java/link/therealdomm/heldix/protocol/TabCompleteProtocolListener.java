package link.therealdomm.heldix.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import link.therealdomm.heldix.BlockPartyPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

/**
 * the implementation for tab complete packet adapter
 *
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class TabCompleteProtocolListener extends PacketAdapter {

    public TabCompleteProtocolListener(JavaPlugin javaPlugin) {
        super(javaPlugin, PacketType.Play.Client.TAB_COMPLETE);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (!BlockPartyPlugin.getInstance().getMainConfig().isDisableTabComplete()) {
            return;
        }
        PacketContainer packetContainer = event.getPacket();
        String message = packetContainer.getSpecificModifier(String.class).read(0).toLowerCase(Locale.ROOT);
        if (message.startsWith("/") && !message.contains(" ")) {
            if (event.getPlayer().hasPermission("tabcomplete.bypass")) {
                event.setCancelled(false);
                return;
            }
            boolean setCancelled = true;
            for (String tabCompletableCommand : BlockPartyPlugin.getInstance().getMainConfig().getTabCompletableCommands()) {
                if (message.contains(tabCompletableCommand.toLowerCase(Locale.ROOT))) {
                    setCancelled = false;
                    break;
                }
            }
            event.setCancelled(setCancelled);
        }
    }
}
