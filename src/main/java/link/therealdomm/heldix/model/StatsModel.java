package link.therealdomm.heldix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsModel {

    private UUID uuid;
    private Integer rank;
    private Integer wonGames;
    private Integer points;
    private Integer deaths;
    private Integer topLevel;

}
