package link.therealdomm.heldix.util.platform;

import link.therealdomm.heldix.worldedit.WorldEditPasteHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.io.File;

/**
 * a simple implementation to save the color and platform file for paste during the game
 *
 * @author TheRealDomm
 * @since 11.10.2021
 */
@Data
@RequiredArgsConstructor
public class PlatformType {

    private final int color;
    private final File initialField;
    private final File airField;

    /**
     * pastes the specified file with world edit to the specified location
     * @param file to paste
     * @param location where the file should be pasted
     */
    public void paste(File file, Location location) {
        WorldEditPasteHandler.pasteFile(file, location);
    }

}
