package link.therealdomm.heldix.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import link.therealdomm.heldix.BlockPartyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author TheRealDomm
 * @since 18.10.2021
 */
public class ActionBarHandler {

    public static void sendActionBar(Player player, String message) {
        try {
            PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.CHAT);
            packetContainer.getBytes().write(0, (byte) 2);
            packetContainer.getChatComponents().write(0, WrappedChatComponent.fromText(message));
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        } catch (Exception e) {
            BlockPartyPlugin.getInstance().getLogger().info("Could not display action bar to " + player.getName());
        }
    }

    public static BukkitTask sendStop() {
        return Bukkit.getScheduler().runTaskTimer(
                BlockPartyPlugin.getInstance(),
                () -> {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        ActionBarHandler.sendActionBar(onlinePlayer,
                                BlockPartyPlugin.getInstance().getMessagesConfig().getCountDownStop());
                    }
                },
                0,
                10
        );
    }

    public static void sendCountDown(int seconds, int color) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ActionBarHandler.sendActionBar(onlinePlayer, ActionBarHandler.buildMessage(seconds, color));
        }
    }

    private static String buildMessage(int seconds, int color) {
        StringBuilder first = new StringBuilder();
        StringBuilder last = new StringBuilder();
        for (int i = 0; i < seconds; i++) {
            first.append(BlockPartyPlugin.getInstance().getMessagesConfig().getCountDownSymbol());
            last.append(BlockPartyPlugin.getInstance().getMessagesConfig().getCountDownSymbol());
        }
        String colorMessage = BlockPartyPlugin.getInstance().getMessagesConfig()
                .getCountDownFormat()
                .replace("%color_name%", BlockPartyPlugin.getInstance().getMainConfig()
                        .getColorNameMapping().getOrDefault(color, "???"));
        return first + " " + colorMessage + " " + last;
    }

}
