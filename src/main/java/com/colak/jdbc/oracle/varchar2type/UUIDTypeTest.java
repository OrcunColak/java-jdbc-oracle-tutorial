package com.colak.jdbc.oracle.varchar2type;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

// See https://medium.com/@thecodingdon/oracle-db-save-uuid-as-string-in-database-with-jpa-hibernate-version-6-ad15b90ceecc
@Slf4j
class UUIDTypeTest {

    private static final UUID RANDOM_UUID = UUID.randomUUID(); // Replace with the actual UUID you are looking for

    public static void main() {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:FREE";
        String username = "sa";
        String password = "my1passw";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            writeUUID(connection);
            readUUID(connection);
        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }

    // Here we are not using "WHERE ACTIVE = 1;" or "WHERE ACTIVE = TRUE;" but it returns only true values
    private static void writeUUID(Connection connection) {
        String sql = "INSERT INTO uuid_table (id, name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, RANDOM_UUID.toString()); // Set UUID as String
            preparedStatement.setString(2, "Example Name");
            preparedStatement.executeUpdate();
            log.info("Inserted UUID: {}", RANDOM_UUID);
        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }

    // Here we are using "WHERE ACTIVE = 1;" or "WHERE ACTIVE = TRUE;"
    private static void readUUID(Connection connection) {
        String sql = "SELECT id, name FROM uuid_table WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, RANDOM_UUID.toString());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    log.info("Retrieved UUID: {}", id);
                    log.info("Name: {}", name);

                    // Optionally convert back to UUID object
                    UUID retrievedUUID = UUID.fromString(id);
                    log.info("UUID Object: {}", retrievedUUID);
                }
            }
        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }
}
