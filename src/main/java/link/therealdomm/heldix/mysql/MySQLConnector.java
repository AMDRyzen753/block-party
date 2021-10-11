package link.therealdomm.heldix.mysql;

import link.therealdomm.heldix.BlockPartyPlugin;
import link.therealdomm.heldix.util.MySQLData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class MySQLConnector {

    private static final String URL = "jdbc:mysql://%s:%d/%s";
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private AtomicBoolean openable = new AtomicBoolean(false);
    private final List<Connection> pendingConnections = new ArrayList<>();
    private final MySQLData data;
    private final ExecutorService executorService;

    public MySQLConnector(MySQLData data) {
        this.data = data;
        try {
            Class.forName(DRIVER_CLASS);
            Connection connection;
            if ((connection = this.getConnection()) != null) {
                this.closeConnection(connection);
                this.openable.set(true);
            } else {
                throw new IllegalStateException("Could not open connection to the database! Please check you credentials.");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not find mysql driver!");
        }
        final ThreadFactory builder = (Runnable r) -> {
            final Thread thread = new Thread(r, "Async Database Thread");
            thread.setDaemon(true);
            return thread;
        };
        this.executorService = Executors.newSingleThreadExecutor(builder);
    }

    public MySQLResult query(String sql, Object... replacements) {
        if (!this.openable.get()) {
            throw new IllegalStateException("Can not open any database connection!");
        }
        try {
            Connection connection = this.getConnection();
            if (connection == null) {
                throw new IllegalStateException("Can not open the connection to the database!");
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < replacements.length; i++) {
                preparedStatement.setObject(i+1, replacements[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            MySQLResult mySQLResult = MySQLResult.build(resultSet);
            preparedStatement.close();
            this.closeConnection(connection);
            return mySQLResult;
        } catch (SQLException e) {
            BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
        return null;
    }

    public void asyncQuery(Consumer<MySQLResult> resultConsumer, String sql, Object... replacements) {
        this.executorService.execute(() -> {
            try {
                MySQLResult query = this.query(sql, replacements);
                resultConsumer.accept(query);
            } catch (Exception e) {
                BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
                resultConsumer.accept(null);
            }
        });
    }

    public void update(String sql, Object... replacements) {
        if (!this.openable.get()) {
            throw new IllegalStateException("Can not open any database connection!");
        }
        try {
            Connection connection = this.getConnection();
            if (connection == null) {
                throw new IllegalStateException("Can not open the connection to the database!");
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < replacements.length; i++) {
                preparedStatement.setObject(i+1, replacements[i]);
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            this.closeConnection(connection);
        } catch (SQLException e) {
            BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
    }

    public void asyncUpdate(String sql, Object... replacements) {
        this.executorService.execute(() -> {
            try {
                this.update(sql, replacements);
            } catch (Exception e) {
                BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
            }
        });
    }

    public void shutdown() {
        this.openable.set(false);
        this.closeConnections();
    }

    private Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    String.format(URL, this.data.getHostname(), this.data.getPort(), this.data.getDatabase()),
                    this.data.getUsername(),
                    this.data.getPassword()
            );
            if (connection != null) {
                this.pendingConnections.add(connection);
                return connection;
            }
        } catch (SQLException e) {
            BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
        return null;
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                this.pendingConnections.remove(connection);
            } catch (SQLException e) {
                BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
            }
        }
    }

    private void closeConnections() {
        for (Connection pendingConnection : this.pendingConnections) {
            try {
                pendingConnection.close();
            } catch (SQLException e) {
                BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
            }
        }
        this.pendingConnections.clear();
    }

}
