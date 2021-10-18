package link.therealdomm.heldix.command.setup;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.util.location.PlayerLocation;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * the sub command implementation to set the default spectator location
 *
 * @author TheRealDomm
 * @since 16.10.2021
 */
@Getter
public class SetSpecSubCommand implements SetupSubCommand {

    private final String name = "setspec";
    private final String permission = "command.setup.setspec";
    private final String description = "";

    @Override
    public void onCommand(Player player, String[] strings) {
        Location location = player.getLocation();
        BlockPartyPlugin.getInstance().getMainConfig().setSpecLocation(PlayerLocation.fromLocation(location));
        BlockPartyPlugin.getInstance().saveMainConfig();
        player.sendMessage("§aSpectator spawn location was successfully set!");
    }
}
