package link.therealdomm.heldix.command.setup;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.util.location.PlayerLocation;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 16.10.2021
 */
@Getter
public class SetGameSubCommand implements SetupSubCommand {

    private final String name = "setgame";
    private final String permission = "command.setup.setgame";
    private final String description = "";

    @Override
    public void onCommand(Player player, String[] strings) {
        Location location = player.getLocation();
        BlockPartyPlugin.getInstance().getMainConfig().setGameLocation(PlayerLocation.fromLocation(location));
        BlockPartyPlugin.getInstance().saveMainConfig();
        player.sendMessage("Spawn location for the game has been successfully set!");
    }
}
