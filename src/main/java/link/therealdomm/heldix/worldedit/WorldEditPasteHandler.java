package link.therealdomm.heldix.worldedit;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.world.World;
import link.therealdomm.heldix.BlockPartyPlugin;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

/**
 * simple implementation to paste files with worldedit
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class WorldEditPasteHandler {

    /**
     * pastes a schematic file to the specified location
     * @param file to paste
     * @param location to paste file to
     * @return true if no exception was encountered
     */
    public static boolean pasteFile(File file, Location location) {
        try {
            Vector to = new Vector(location.getX(), location.getY(), location.getZ());
            World weWorld = new BukkitWorld(location.getWorld());
            EditSession editSession = Objects.requireNonNull(ClipboardFormats.findByFile(file))
                    .load(file).paste(weWorld, to, false, true, null);
            editSession.flushQueue();
            return true;
        } catch (IOException e) {
            BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
            return false;
        }
    }

}
