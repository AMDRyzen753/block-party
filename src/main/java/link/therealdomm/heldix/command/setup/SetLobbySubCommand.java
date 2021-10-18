package link.therealdomm.heldix.command.setup;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.util.location.PlayerLocation;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * the sub command implementation to set the spawn location in lobby phase
 *
 * @author TheRealDomm
 * @since 16.10.2021
 */
@Getter
public class SetLobbySubCommand implements SetupSubCommand {

    private final String name = "setlobby";
    private final String permission = "command.setup.setlobby";
    private final String description = "";

    @Override
    public void onCommand(Player player, String[] strings) {
        Location location = player.getLocation();
        BlockPartyPlugin.getInstance().getMainConfig().setLobbyLocation(PlayerLocation.fromLocation(location));
        BlockPartyPlugin.getInstance().saveMainConfig();
        player.sendMessage("§aSpawn location for lobby phase was set successfully.");
    }
}
