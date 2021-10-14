package link.therealdomm.heldix.util.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author TheRealDomm
 * @since 14.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerLocation {

    private String world = "world";
    private double x = 0;
    private double y = 100;
    private double z = 0;
    private float yaw = 0;
    private float pitch = 0;

    public static PlayerLocation fromLocation(Location l) {
        return new PlayerLocation(
                l.getWorld().getName(),
                l.getX(),
                l.getY(),
                l.getZ(),
                l.getYaw(),
                l.getPitch()
        );
    }

    public Location toLocation() {
        return new Location(
                Bukkit.getWorld(this.world),
                this.x,
                this.y,
                this.z,
                this.yaw,
                this.pitch
        );
    }

}
