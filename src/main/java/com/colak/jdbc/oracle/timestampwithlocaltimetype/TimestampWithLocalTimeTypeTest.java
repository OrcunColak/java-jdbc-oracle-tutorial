package com.colak.jdbc.oracle.timestampwithlocaltimetype;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

// Insert OffsetDateTime into TIMESTAMP WITH LOCAL TIME ZONE column type as setObject() and read it back
@Slf4j
class TimestampWithLocalTimeTypeTest {

    private static final String TABLE_NAME = "datetime_types_table";

    public static void main() {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:FREE";
        String username = "sa";
        String password = "My1passw";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            clearTable(connection);
            insertAsOffsetDateTime(connection);
            // offsetDateTime : 2022-02-06T13:01:05.123457+03:00
            selectAsOffsetDateTime(connection);

        } catch (Exception exception) {
            log.error("Exception ", exception);
        }

        setJvmTimeZone();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Different result
            // offsetDateTime : 2022-02-06T15:31:05.123457+05:30
            selectAsOffsetDateTime(connection);

        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }

    private static void setJvmTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        TimeZone.setDefault(timeZone);
    }

    // This has no effect
    private static void setSessionTimeZone(Connection connection, String timeZone) throws SQLException {

        try (Statement statement = connection.createStatement()) {
            // Set the session time zone using ALTER SESSION
            String alterSessionSQL = "ALTER SESSION SET TIME_ZONE = '" + timeZone + "'";
            statement.execute(alterSessionSQL);
        }
    }

    private static void clearTable(Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM " + TABLE_NAME;
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void insertAsOffsetDateTime(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (timestamp_with_local_time_zone_column) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            // Use Zone offset (UTC+3)
            OffsetDateTime offsetDateTime = OffsetDateTime.of(2022, 2, 6, 13, 1, 5, 123_456_789,
                    ZoneOffset.ofHours(3));


            preparedStatement.setObject(1, offsetDateTime);

            // Executing the INSERT operation
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                log.info("Insert successful!");
            } else {
                log.info("Insert failed.");
            }
        }
    }

    private static void selectAsOffsetDateTime(Connection connection) throws SQLException {
        String insertQuery = "SELECT * FROM " + TABLE_NAME;
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.next();
            OffsetDateTime offsetDateTime = resultSet.getObject("timestamp_with_local_time_zone_column", OffsetDateTime.class);

            log.info("offsetDateTime : " + offsetDateTime);
        }
    }

}
