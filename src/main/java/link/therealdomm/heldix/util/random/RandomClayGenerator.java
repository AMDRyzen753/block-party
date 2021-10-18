package link.therealdomm.heldix.util.random;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.util.platform.PlatformType;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class RandomClayGenerator {

    /**
     * searches for a random platform in the schematics folder
     * @return the new {@link PlatformType}
     */
    public static PlatformType getFieldByColor() {
        File[] files  = new File(BlockPartyPlugin.getInstance().getDataFolder(), "schematics")
                .listFiles((dir, name) -> new File(dir, name).isDirectory());
        if (files != null) {
            List<File> possibles = Arrays.stream(files).collect(Collectors.toList());
            int schematicFolder = ThreadLocalRandom.current().nextInt(0, possibles.size() - 1);
            File file = possibles.get(schematicFolder);
            if (!file.isDirectory()) {
                return null;
            }
            String colorString = file.getName().split("_")[0];
            int color;
            try {
                color = Integer.parseInt(colorString);
            } catch (Exception e) {
                color = 0;
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
