package link.therealdomm.heldix;

import link.therealdomm.heldix.command.SetupCommand;
import link.therealdomm.heldix.command.StartCommand;
import link.therealdomm.heldix.config.MainConfig;
import link.therealdomm.heldix.config.MessagesConfig;
import link.therealdomm.heldix.game.GameState;
import link.therealdomm.heldix.handler.CoinAPIHandler;
import link.therealdomm.heldix.handler.SpectatorHandler;
import link.therealdomm.heldix.listener.block.BlockBreakListener;
import link.therealdomm.heldix.listener.block.BlockBurnListener;
import link.therealdomm.heldix.listener.block.BlockIgniteListener;
import link.therealdomm.heldix.listener.block.BlockPlaceListener;
import link.therealdomm.heldix.listener.entity.*;
import link.therealdomm.heldix.listener.inventory.InventoryClickListener;
import link.therealdomm.heldix.listener.inventory.InventoryDragListener;
import link.therealdomm.heldix.listener.player.*;
import link.therealdomm.heldix.listener.world.WeatherChangeListener;
import link.therealdomm.heldix.protocol.ProtocolLibHandler;
import link.therealdomm.heldix.repo.StatsRepo;
import link.therealdomm.heldix.util.configuration.ConfigLoader;
import link.therealdomm.heldix.util.mysql.MySQLConnector;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.dependency.SoftDependsOn;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.File;
import java.util.logging.Level;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Getter
@Author("TheRealDomm")
@Plugin(name = "BlockParty", version = "1.0.0")
@Commands({
        @Command(name = "start", permission = "command.start"), @Command(name = "setup", permission = "command.setup")
})
public class BlockPartyPlugin extends JavaPlugin {

    @Getter @Setter private static boolean setupEnabled = false;
    @Getter private static BlockPartyPlugin instance;

    private MainConfig mainConfig;
    private MessagesConfig messagesConfig;
    private MySQLConnector connector;
    private StatsRepo statsRepo;
    private ProtocolLibHandler protocolLibHandler;
    private SpectatorHandler spectatorHandler;

    @Override
    public void onEnable() {
        if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
            throw new IllegalStateException("Could not create data folder for BlockParty!");
        }
        instance = this;
        if (this.getServer().getPluginManager().getPlugin("CoinAPI") != null) {
            CoinAPIHandler.setActive(true);
            this.getLogger().info("Hooked into CoinAPI!");
        } else {
            this.getLogger().warning("CoinAPI not found! Ignoring it!");
        }
        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            this.protocolLibHandler = new ProtocolLibHandler(this);
        } else {
            this.getLogger().warning("Did not found ProtocolLib! TabComplete option is disabled!");
        }
        File configFile = new File(this.getDataFolder(), "config.json");
        this.mainConfig = ConfigLoader.load(MainConfig.class, configFile);
        File messageFile = new File(this.getDataFolder(), "messages.json");
        this.messagesConfig = ConfigLoader.load(MessagesConfig.class, messageFile);
        this.connector = new MySQLConnector(this.mainConfig.getData());
        this.statsRepo = new StatsRepo(this.connector);
        this.spectatorHandler = new SpectatorHandler();
        this.registerListeners(
                BlockBreakListener.class,
                BlockBurnListener.class,
                BlockIgniteListener.class,
                BlockPlaceListener.class,
                CreatureSpawnListener.class,
                EntityDamageListener.class,
                EntityExplodeListener.class,
                EntityInteractListener.class,
                HangingBreakListener.class,
                HangingPlaceListener.class,
                InventoryClickListener.class,
                InventoryDragListener.class,
                FoodLevelListener.class,
                PlayerDropItemListener.class,
                PlayerInteractAtEntityListener.class,
                PlayerInteractEntityListener.class,
                PlayerInteractListener.class,
                PlayerLoginListener.class,
                PlayerMoveListener.class,
                PlayerJoinListener.class,
                PlayerQuitListener.class,
                PlayerSpawnListener.class,
                WeatherChangeListener.class
        );
        this.getCommand("setup").setExecutor(new SetupCommand());
        this.getCommand("start").setExecutor(new StartCommand());
        GameState.initialize();
    }

    @Override
    public void onDisable() {

    }

    @SafeVarargs
    public final void registerListeners(Class<? extends Listener>... classes) {
        for (Class<? extends Listener> clazz : classes) {
            try {
                Listener listener = clazz.newInstance();
                this.getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException e) {
                this.getLogger().log(Level.WARNING, "", e);
            }
        }
    }

    public void saveMainConfig() {
        File file = new File(this.getDataFolder(), "config.json");
        ConfigLoader.save(this.mainConfig, file);
    }

    public void saveMessageConfig() {
        File file = new File(this.getDataFolder(), "messages.json");
        ConfigLoader.save(this.messagesConfig, file);
    }

}
