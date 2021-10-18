package link.therealdomm.heldix.repo;

import link.therealdomm.heldix.model.StatsModel;
import link.therealdomm.heldix.util.mysql.MySQLConnector;
import link.therealdomm.heldix.util.mysql.MySQLResult;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * a wrapper for the stats sql connection
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class StatsRepo {

    private final MySQLConnector connector;

    /**
     * the default constructor
     * @param connector to be used
     */
    public StatsRepo(MySQLConnector connector) {
        this.connector = connector;
        this.createTable();
    }

    /**
     * creates the table if it does not exist
     */
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS block_party_stats (" +
                "uuid VARCHAR(36), " +
                "won_games INT, " +
                "points INT, " +
                "deaths INT, " +
                "top_level INT, " +
                "games_played INT, " +
                "PRIMARY KEY(uuid)) " +
                "ENGINE=InnoDB DEFAULT CHARSET=latin1";
        this.connector.asyncUpdate(sql);
    }

    /**
     * creates a new player in the database
     * @param uuid of the player to create
     */
    public void createPlayer(UUID uuid) {
        String sql = "INSERT INTO block_party_stats (uuid, won_games, points, deaths, top_level, games_played) " +
                "values (?, ?, ?, ?, ?, ?)";
        this.connector.asyncUpdate(sql, uuid.toString(), 0, 0, 0, 0, 0);
    }

    /**
     * get the stats of a player
     * @param uuid of the player
     * @param statsConsumer the stats when returned from async db request
     */
    public void getStats(UUID uuid, Consumer<StatsModel> statsConsumer) {
        String sql = "SELECT won_games, points, deaths, top_level, games_played FROM block_party_stats WHERE uuid=?";
        this.connector.asyncQuery((result -> {
            if (result != null && !result.getResults().isEmpty()) {
                MySQLResult.Result queryResult = result.getResults().get(0);
                StatsModel statsModel = new StatsModel();
                statsModel.setUuid(uuid);
                statsModel.setExists(true);
                statsModel.setWonGames(queryResult.getInteger("won_games"));
                statsModel.setPoints(queryResult.getInteger("points"));
                statsModel.setDeaths(queryResult.getInteger("deaths"));
                statsModel.setTopLevel(queryResult.getInteger("top_level"));
                statsModel.setGamesPlayed(queryResult.getInteger("games_played"));
                statsConsumer.accept(statsModel);
            } else {
                StatsModel statsModel = new StatsModel();
                statsModel.setUuid(uuid);
                statsConsumer.accept(statsModel);
            }
        }), sql, uuid.toString());
    }

    /**
     * updates the stats async
     * @param statsModel to be updated
     */
    public void updateStats(StatsModel statsModel) {
        String sql = "UPDATE block_party_stats SET won_games=?, points=?, deaths=?, top_level=?, games_played=? " +
                "WHERE uuid=?";
        this.connector.asyncUpdate(sql, statsModel.getWonGames(), statsModel.getPoints(),
                statsModel.getDeaths(), statsModel.getTopLevel(), statsModel.getGamesPlayed(),
                statsModel.getUuid().toString());
    }

}
