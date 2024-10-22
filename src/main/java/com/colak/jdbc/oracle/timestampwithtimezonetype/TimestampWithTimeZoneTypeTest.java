package com.colak.jdbc.oracle.timestampwithtimezonetype;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

// Shows that with "TIMESTAMP WITH TIME ZONE" time zone or offset is stored, displayed with original time zone.
// It is not affected from JVM time zone change
@Slf4j
class TimestampWithTimeZoneTypeTest {

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
            // No change
            // offsetDateTime : 2022-02-06T13:01:05.123457+03:00
            selectAsOffsetDateTime(connection);

        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }

    private static void setJvmTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        TimeZone.setDefault(timeZone);
    }

    private static void clearTable(Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM " + TABLE_NAME;
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void insertAsOffsetDateTime(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (timestamp_with_time_zone_column) VALUES (?)";
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
            OffsetDateTime offsetDateTime = resultSet.getObject("timestamp_with_time_zone_column", OffsetDateTime.class);

            // offsetDateTime : 2022-02-06T13:01:05.123457+03:00
            log.info("offsetDateTime : " + offsetDateTime);
        }
    }

}
