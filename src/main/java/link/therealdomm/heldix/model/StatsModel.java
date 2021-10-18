package link.therealdomm.heldix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * the model class for the player stats saved in a MySQL database
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsModel {

    private UUID uuid;
    private boolean exists = false;
    private Integer wonGames = 0;
    private Integer points = 0;
    private Integer deaths = 0;
    private Integer topLevel = 0;
    private Integer gamesPlayed = 0;

    /**
     * increments a specific stats entry
     * @param entry the entry to increment by one
     */
    public void increment(StatEntry entry) {
        switch (entry) {
            case WON_GAMES:
                this.wonGames++;
                break;
            case POINTS:
                this.points++;
                break;
            case DEATHS:
                this.deaths++;
                break;
            case TOP_LEVEL:
                this.topLevel++;
                break;
            case GAMES_PLAYED:
                this.gamesPlayed++;
                break;
        }
    }

    /**
     * ad a specific amount to a stats entry
     * @param entry to add amount to
     * @param amount to add
     */
    public void add(StatEntry entry, int amount) {
        switch (entry) {
            case WON_GAMES:
                this.wonGames += amount;
                break;
            case POINTS:
                this.points += amount;
                break;
            case DEATHS:
                this.deaths += amount;
                break;
            case TOP_LEVEL:
                this.topLevel += amount;
                break;
            case GAMES_PLAYED:
                this.gamesPlayed += amount;
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
