package link.therealdomm.heldix.handler;

import link.therealdomm.heldix.BlockPartyPlugin;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class MessageHandler {

    public static String getMessage(String key, Object... replacements) {
        String message = BlockPartyPlugin.getInstance().getMessagesConfig()
                .getMessages().getOrDefault(key, "Message " + key + " not found!");
        int i = 0;
        for (Object replacement : replacements) {
            message = message.replaceAll("\\{" + i +"}", replacement.toString());
            i++;
        }
        return message;
    }

}
