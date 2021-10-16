package link.therealdomm.heldix.command;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.command.setup.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author TheRealDomm
 * @since 16.10.2021
 */
public class SetupCommand implements CommandExecutor {

    private final Map<String, SetupSubCommand> commandMap = new LinkedHashMap<>();

    public SetupCommand() {
        this.registerCommands(
                SetGameSubCommand.class,
                SetLobbySubCommand.class,
                SetPasteSubCommand.class,
                SetSpecSubCommand.class,
                ToggleSubCommand.class
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (strings.length == 0) {
            this.sendHelp(sender);
            return true;
        }
        if (!BlockPartyPlugin.isSetupEnabled()) {
            sender.sendMessage("§cPlease enable setup mode first. (/setup toggle)");
            return true;
        }
        SetupSubCommand setupSubCommand = this.getCommand(strings[0]);
        if (setupSubCommand == null) {
            this.sendHelp(sender);
            return true;
        }
        if (!sender.hasPermission(setupSubCommand.getPermission())) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return true;
        }
        String[] cutOff = this.cutArgs(strings);
        setupSubCommand.onCommand(player, cutOff);
        return true;
    }

    public SetupSubCommand getCommand(String command) {
        if (!this.commandMap.containsKey(command.toLowerCase(Locale.ROOT))) {
            return null;
        }
        try {
            return this.commandMap.get(command.toLowerCase(Locale.ROOT));
        } catch (Exception e) {
            return null;
        }
    }

    public String[] cutArgs(String[] initial) {
        if (initial == null || initial.length <= 1) {
            return new String[0];
        }
        List<String> commands = new LinkedList<>();
        for (int i = 1; i < initial.length; i++) {
            try {
                commands.add(initial[i]);
            } catch (Exception e) {
                BlockPartyPlugin.getInstance().getLogger().info("Could not find argument for position " + i);
            }
        }
        return commands.toArray(new String[0]);
    }

    @SafeVarargs
    public final void registerCommands(Class<? extends SetupSubCommand>... classes) {
        for (Class<? extends SetupSubCommand> clazz : classes) {
            try {
                SetupSubCommand subCommand = clazz.newInstance();
                this.commandMap.put(subCommand.getName().toLowerCase(Locale.ROOT), subCommand);
            } catch (Exception e) {
                BlockPartyPlugin.getInstance().getLogger().warning("Could not register setup command " +
                        clazz.getSimpleName());
            }
        }
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§cPossible commands:");
        for (SetupSubCommand value : this.commandMap.values()) {
            sender.sendMessage(" §a- §e/" + value.getName() + " §8- §7" + value.getDescription());
        }
    }

}
