package link.therealdomm.heldix.mysql;

import com.google.gson.internal.Primitives;
import link.therealdomm.heldix.BlockPartyPlugin;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
public class MySQLResult {

    @Getter private final List<Result> results = new ArrayList<>();

    public static MySQLResult build(ResultSet resultSet) {
        if (resultSet == null) {
            throw new IllegalArgumentException("Could not build result because set was null!");
        }
        try {
            MySQLResult mySQLResult = new MySQLResult();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                Result result = new Result();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String name = metaData.getColumnName(i+1);
                    result.addData(name, resultSet.getObject(name));
                }
                mySQLResult.getResults().add(result);
            }
            resultSet.close();
            return mySQLResult;
        } catch (SQLException e) {
            BlockPartyPlugin.getInstance().getLogger().log(Level.SEVERE, "", e);
        }
        return null;
    }

    public static class Result {
        public static final Integer DEFAULT_INTEGER = -1;
        public static final String DEFAULT_STRING = "";

        @Getter private Map<String, Object> dataSet = new LinkedHashMap<>();

        public void addData(String name, Object data) {
            this.dataSet.put(name, data);
        }

        public boolean hasData(String name) {
            return this.dataSet.containsKey(name);
        }

        public <T> T getData(String name, Class<? extends T> type) {
            Object o = this.dataSet.get(name);
            if (o != null) {
                return Primitives.wrap(type).cast(o);
            }
            return null;
        }

        public Integer getInteger(String name) {
            Object o = this.dataSet.get(name);
            if (o != null && o instanceof Integer) {
                return (Integer) o;
            }
            return DEFAULT_INTEGER;
        }

        public String getString(String name) {
            Object o = this.dataSet.get(name);
            if (o != null && o instanceof String) {
                return (String) o;
            }
            return DEFAULT_STRING;
        }

        public UUID getUuid(String name) {
            Object o = this.dataSet.get(name);
            try {
                if (o instanceof String) {
                    return UUID.fromString((String) o);
                }
            } catch (IllegalArgumentException e) {}
            return null;
        }
    }

}
