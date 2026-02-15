package khouim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SnowflakeConnector {

    // 1️⃣ Change this to your Snowflake account info
    private static final String JDBC_URL = "jdbc:snowflake://BOWTVIV-JB80366.snowflakecomputing.com/?db=USERKHOIUMAMBIA&schema=PUBLIC&warehouse=COMPUTE_WH";
    private static final String USER = "KHOIUMAMBIA";           // your Snowflake username
    private static final String PASSWORD = "I aM kk*474747"; // <-- put your password here

    // 2️⃣ Save an event to Snowflake
    public static void saveEvent(String eventName, double timeSeconds) {
        String sql = "INSERT INTO GAME_METRICS (PLAYER_NAME, EVENT_NAME, TIME_SECONDS) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "Manzur"); // Player name
            pstmt.setString(2, eventName);
            pstmt.setDouble(3, timeSeconds);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
