package link.therealdomm.heldix.command.setup;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.game.GameState;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * the sub command implementation that toggles the setup mode
 *
 * @author TheRealDomm
 * @since 16.10.2021
 */
@Getter
public class ToggleSubCommand implements SetupSubCommand {

    private final String name = "toggle";
    private final String permission = "command.setup.toggle";
    private final String description = "";

    @Override
    public void onCommand(Player player, String[] strings) {
        if (BlockPartyPlugin.isSetupEnabled()) {
            player.sendMessage("§cSetup was disabled!");
            BlockPartyPlugin.setSetupEnabled(false);
            GameState.initialize();
        }
        else {
            BlockPartyPlugin.setSetupEnabled(true);
            GameState.reset();
            player.sendMessage("§aSetup was enabled!");
        }
    }
}
