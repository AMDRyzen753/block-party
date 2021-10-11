package link.therealdomm.heldix.util.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
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

    public static PasteLocation fromLocation(Location l) {
        return new PasteLocation(
                l.getWorld().getName(),
                l.getX(),
                l.getY(),
                l.getZ()
        );
    }

    public Location toLocation() {
        return new Location(
                Bukkit.getWorld(this.world),
                this.x,
                this.y,
                this.z
        );
    }

}
