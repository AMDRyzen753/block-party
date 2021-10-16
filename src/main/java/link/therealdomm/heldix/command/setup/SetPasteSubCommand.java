package link.therealdomm.heldix.command.setup;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.util.location.PasteLocation;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TheRealDomm
 * @since 16.10.2021
 */
@Getter
public class SetPasteSubCommand implements SetupSubCommand {

    private final String name = "setpaste";
    private final String permission = "command.setup.setpaste";
    private final String description = "";

    @Override
    public void onCommand(Player player, String[] strings) {
        Location location = player.getLocation();
        BlockPartyPlugin.getInstance().getMainConfig().setPasteLocation(PasteLocation.fromLocation(location));
        BlockPartyPlugin.getInstance().saveMainConfig();
        player.sendMessage("§aSchematic paste location was successfully set.");
    }
}
