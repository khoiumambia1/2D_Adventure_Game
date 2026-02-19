package khouim;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnowflakeConnector {

    private static String jdbcUrl;
    private static String user;
    private static String password;
    private static String playerName;
    private static boolean configured = false;

    // Single background thread for DB writes so the game loop is never blocked
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("snowflake_config.properties")) {
            props.load(fis);

            String account = props.getProperty("snowflake.account");
            String database = props.getProperty("snowflake.database");
            String schema = props.getProperty("snowflake.schema");
            String warehouse = props.getProperty("snowflake.warehouse");

            jdbcUrl = "jdbc:snowflake://" + account + ".snowflakecomputing.com/"
                    + "?db=" + database
                    + "&schema=" + schema
                    + "&warehouse=" + warehouse;

            user = props.getProperty("snowflake.user");
            password = props.getProperty("snowflake.password");
            playerName = props.getProperty("snowflake.player_name", "Unknown");
            configured = true;

            System.out.println("[Snowflake] Configuration loaded successfully.");
        } catch (IOException e) {
            System.err.println("[Snowflake] Could not load snowflake_config.properties - database logging disabled.");
            System.err.println("[Snowflake] Create snowflake_config.properties in the project root to enable it.");
            configured = false;
        }
    }

    /**
     * Save a game event to Snowflake asynchronously (does not block the game thread).
     */
    public static void saveEvent(String eventName, double timeSeconds) {
        if (!configured) {
            return;
        }

        // Run the DB insert on a background thread
        executor.submit(() -> {
            String sql = "INSERT INTO GAME_METRICS (PLAYER_NAME, EVENT_NAME, TIME_SECONDS) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, playerName);
                pstmt.setString(2, eventName);
                pstmt.setDouble(3, timeSeconds);
                pstmt.executeUpdate();

                System.out.println("[Snowflake] Saved event: " + eventName + " = " + timeSeconds + "s");

            } catch (SQLException e) {
                System.err.println("[Snowflake] Failed to save event '" + eventName + "': " + e.getMessage());
            }
        });
    }

    /**
     * Gracefully shut down the background executor (call when game exits).
     */
    public static void shutdown() {
        executor.shutdown();
    }
}