package link.therealdomm.heldix.repo;

import link.therealdomm.heldix.model.StatsModel;
import link.therealdomm.heldix.util.mysql.MySQLConnector;
import link.therealdomm.heldix.util.mysql.MySQLResult;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class StatsRepo {

    private final MySQLConnector connector;

    public StatsRepo(MySQLConnector connector) {
        this.connector = connector;
        this.createTable();
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS block_party_stats (" +
                "uuid VARCHAR(36), " +
                "won_games BIGINT, " +
                "points BIGINT, " +
                "deaths BIGINT, " +
                "top_level BIGINT, " +
                "games_played BIGINT, " +
                "PRIMARY KEY(uuid)) " +
                "ENGINE=InnoDB DEFAULT CHARSET=latin1";
        this.connector.asyncUpdate(sql);
    }

    public void createPlayer(UUID uuid) {
        String sql = "INSERT INTO block_party_stats (uuid, won_games, points, deaths, top_level, games_played) " +
                "values (?, ?, ?, ?, ?, ?)";
        this.connector.asyncUpdate(sql, uuid.toString(), 0, 0, 0, 0, 0);
    }

    public void getStats(UUID uuid, Consumer<StatsModel> statsConsumer) {
        String sql = "SELECT won_games, points, deaths, top_level, games_played, " +
                "(SELECT COUNT(*) FROM block_party_stats WHERE points>x.points) AS rank " +
                "FROM block_party_stats x WHERE x.uuid=?";
        this.connector.asyncQuery((result -> {
            if (result != null && !result.getResults().isEmpty()) {
                MySQLResult.Result queryResult = result.getResults().get(0);
                StatsModel statsModel = new StatsModel();
                statsModel.setUuid(uuid);
                statsModel.setRank(queryResult.getInteger("rank"));
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

    public void updateStats(StatsModel statsModel) {
        String sql = "UPDATE block_party_stats SET won_games=?, points=?, deaths=?, top_level=?, games_played=? " +
                "WHERE uuid=?";
        this.connector.asyncUpdate(sql, statsModel.getWonGames(), statsModel.getPoints(),
                statsModel.getDeaths(), statsModel.getTopLevel(), statsModel.getGamesPlayed(),
                statsModel.getUuid().toString());
    }

}
