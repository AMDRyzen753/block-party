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
    private Integer gamesPlayed;

    public void add(StatEntry entry, int amount) {
        switch (entry) {
            case WON_GAMES:
                this.wonGames = this.wonGames+amount;
                break;
            case POINTS:
                this.points = this.points+amount;
                break;
            case DEATHS:
                this.deaths = this.deaths+amount;
                break;
            case TOP_LEVEL:
                this.topLevel = this.topLevel+amount;
                break;
            case GAMES_PLAYED:
                this.gamesPlayed = this.gamesPlayed+amount;
                break;
        }
    }

    public enum StatEntry {
        WON_GAMES,
        POINTS,
        DEATHS,
        TOP_LEVEL,
        GAMES_PLAYED
    }

}
