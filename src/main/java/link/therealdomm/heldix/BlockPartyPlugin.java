package link.therealdomm.heldix;

import com.google.gson.Gson;
import link.therealdomm.heldix.config.MainConfig;
import link.therealdomm.heldix.handler.CoinAPIHandler;
import link.therealdomm.heldix.mysql.MySQLConnector;
import link.therealdomm.heldix.repo.StatsRepo;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.*;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Getter
@Author("TheRealDomm")
@Plugin(name = "BlockParty", version = "@VERSION")
public class BlockPartyPlugin extends JavaPlugin {

    @Getter private static BlockPartyPlugin instance;

    private MainConfig mainConfig;
    private MySQLConnector connector;
    private StatsRepo statsRepo;

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
        File configFile = new File(this.getDataFolder(), "config.json");
        if (!configFile.exists()) {
            try {
                if (!configFile.createNewFile()) {
                    throw new IllegalStateException("Could not create config file!");
                }
                else {
                    Writer writer = new FileWriter(configFile);
                    new Gson().toJson(new MainConfig(), writer);
                }
                this.mainConfig = new Gson().fromJson(new BufferedReader(new FileReader(configFile)), MainConfig.class);
            } catch (IOException e) {
                throw new IllegalStateException("Could not create config file!");
            }
        }
        this.connector = new MySQLConnector(this.mainConfig.getData());
        this.statsRepo = new StatsRepo(this.connector);

    }

    @Override
    public void onDisable() {

    }
}
