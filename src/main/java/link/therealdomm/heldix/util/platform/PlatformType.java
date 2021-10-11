package link.therealdomm.heldix.util.platform;

import link.therealdomm.heldix.worldedit.WorldEditPasteHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.io.File;

/**
 * @author TheRealDomm
 * @since 11.10.2021
 */
@Data
@RequiredArgsConstructor
public class PlatformType {

    private final int color;
    private final File initialField;
    private final File airField;

    public void paste(File file, Location location) {
        WorldEditPasteHandler.pasteFile(file, location);
    }

}
