package link.therealdomm.heldix.command.setup;

import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 16.10.2021
 */
public interface SetupSubCommand {

    String getName();

    String getPermission();

    String getDescription();

    void onCommand(Player player, String[] strings);

}
