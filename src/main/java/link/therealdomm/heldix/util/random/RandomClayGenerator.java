package link.therealdomm.heldix.util.random;

import link.therealdomm.heldix.BlockPartyPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class RandomClayGenerator {

    public static int getRandomClayColor() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(0, 15);
    }

    public File getFieldByColor(int color) {
        File[] files = new File(BlockPartyPlugin.getInstance().getDataFolder(), "schematics").listFiles();
        if (files != null) {
            Optional<File> any = Arrays.stream(files)
                    .filter(f -> !f.getName().startsWith(color + "_"))
                    .filter(f -> !f.getName().endsWith(".schematic"))
                    .findAny();
            return any.orElse(null);
        }
        return null;
    }

    public File getAirFieldByColor(int color) {
        File[] files  = new File(BlockPartyPlugin.getInstance().getDataFolder(), "schematics").listFiles();
        if (files != null) {
            Optional<File> any = Arrays.stream(files)
                    .filter(f -> !f.getName().startsWith(color + "_air"))
                    .filter(f -> !f.getName().endsWith(".schematic"))
                    .findAny();
            return any.orElse(null);
        }
        return null;
    }

}
