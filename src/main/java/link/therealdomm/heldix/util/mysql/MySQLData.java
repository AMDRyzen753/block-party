package link.therealdomm.heldix.util.mysql;

import lombok.Data;

/**
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Data
public class MySQLData {

    private String hostname = "localhost";
    private String database = "database";
    private String username = "username";
    private String password = "password";
    private int port = 3306;

}
