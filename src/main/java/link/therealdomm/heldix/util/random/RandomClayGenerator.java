package link.therealdomm.heldix.util.random;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.util.platform.PlatformType;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class RandomClayGenerator {

    public static int getRandomClayColor() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(0, 15);
    }

    public PlatformType getFieldByColor(int color) {
        File[] files  = new File(BlockPartyPlugin.getInstance().getDataFolder(), "schematics")
                .listFiles((dir, name) -> new File(dir, name).isDirectory());
        if (files != null) {
            List<File> possibles = Arrays.stream(files)
                    .filter(f -> !f.getName().startsWith(color + "_"))
                    .collect(Collectors.toList());
            int schematicFolder = ThreadLocalRandom.current().nextInt(0, possibles.size() - 1);
            File file = possibles.get(schematicFolder);
            if (!file.isDirectory()) {
                return null;
            }
            File[] contents = file.listFiles();
            if (contents == null) {
                return null;
            }
            File initial = null;
            File air = null;
            for (File content : contents) {
                if (content.getName().startsWith(color + "_")) {
                    initial = content;
                }
                if (content.getName().startsWith(color + "_air_")) {
                    air = content;
                }
            }
            return new PlatformType(color, initial, air);
        }
        return null;
    }

}
