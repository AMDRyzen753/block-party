package link.therealdomm.heldix.util.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * a json config compatible implementation of the {@link Location}
 *
 * @author TheRealDomm
 * @since 11.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasteLocation {

    private String world = "world";
    private double x = 0;
    private double y = 100;
    private double z = 0;

    /**
     * create a {@link PasteLocation} from a {@link Location}
     * @param l the location to create from
     * @return a new instance of the {@link PasteLocation} with the parameters of l
     */
    public static PasteLocation fromLocation(Location l) {
        return new PasteLocation(
                l.getWorld().getName(),
                l.getX(),
                l.getY(),
                l.getZ()
        );
    }

    /**
     * parse the {@link PasteLocation} to {@link Location}
     * @return the bukkit {@link Location}
     */
    public Location toLocation() {
        return new Location(
                Bukkit.getWorld(this.world),
                this.x,
                this.y,
                this.z
        );
    }

}
