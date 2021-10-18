package link.therealdomm.heldix.command.setup;

import org.bukkit.entity.Player;

/**
 * the setup sub command interface for easy sub command registration
 *
 * @author TheRealDomm
 * @since 16.10.2021
 */
public interface SetupSubCommand {

    /**
     * describes the first arg in main command
     * @return the sub command name
     */
    String getName();

    /**
     * the permission to execute the sub command
     * @return the permission as {@link String}
     */
    String getPermission();

    /**
     * get the command description
     * @return the description as {@link String}
     */
    String getDescription();

    /**
     * will be called everytime a subcommand is executed
     * @param player the player that executes the command
     * @param strings the parameters of the sub command
     */
    void onCommand(Player player, String[] strings);

}
