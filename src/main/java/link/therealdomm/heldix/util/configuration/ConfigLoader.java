package link.therealdomm.heldix.util.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Cleanup;

import java.io.*;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
public class ConfigLoader {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(Config config, File file) {
        try {
            @Cleanup BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            GSON.toJson(config, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save config to " + file.getAbsolutePath(), e);
        }
    }

    public static  <T extends Config> T update(Class<? extends T> clazz, File file) {
        if (file == null || !file.exists()) {
            throw new IllegalStateException("Could not update config, file not found!");
        }
        try {
            T config = clazz.newInstance();
            double version = config.getConfigVersion();
            config = GSON.fromJson(new BufferedReader(new FileReader(file)), clazz);
            if (version > config.getConfigVersion()) {
                config.setConfigVersion(version);
                save(config, file);
            }
            return config;
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException("Could not update config!");
        }
    }

    public static  <T extends Config> T load(Class<? extends T> clazz, File file) {
        if (file == null) {
            throw new IllegalStateException("Could not load config, file does not exist!");
        }
        try {
            T config = clazz.newInstance();
            double version = config.getConfigVersion();
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IllegalStateException("Could not create new config at " + file.getAbsolutePath());
                } else {
                    save(config, file);
                }
            }
            config = GSON.fromJson(new BufferedReader(new FileReader(file)), clazz);
            if (version > config.getConfigVersion()) {
                config = update(clazz, file);
            }
            return config;
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException("Could not load config " + file.getAbsolutePath(), e);
        }
    }

}
