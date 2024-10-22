package com.colak.jdbc.oracle.booleantype;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// See https://medium.com/oracledevs/the-new-boolean-data-type-in-oracle-database-23c-with-the-oracle-jdbc-drivers-23c-21c-jdbc-f3252b200838
@Slf4j
class BooleanTypeTest {

    public static void main() {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:FREE";
        String username = "sa";
        String password = "my1passw";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            selectWithoutWhereParameter(connection);
            selectWitWhereParameter(connection);
        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }

    // Here we are not using "WHERE ACTIVE = 1;" or "WHERE ACTIVE = TRUE;" but it returns only true values
    private static void selectWithoutWhereParameter(Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM boolean_types_table WHERE ACTIVE");
             ResultSet resultSet = stmt.executeQuery()) {

            StringBuilder stringBuilder = new StringBuilder();
            while (resultSet.next()) {
                stringBuilder.append(resultSet
                                .getInt(1)).append("|")
                        .append(resultSet.getBoolean(2)).append("\n");

            }
            System.out.println(stringBuilder);

        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }

    // Here we are using "WHERE ACTIVE = 1;" or "WHERE ACTIVE = TRUE;"
    private static void selectWitWhereParameter(Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM boolean_types_table WHERE ACTIVE = ?")) {
            stmt.setBoolean(1, true);

            try (ResultSet resultSet = stmt.executeQuery()) {
                StringBuilder stringBuilder = new StringBuilder();
                while (resultSet.next()) {
                    stringBuilder.append(resultSet
                                    .getInt(1)).append("|")
                            .append(resultSet.getBoolean(2)).append("\n");

                }
                System.out.println(stringBuilder);
            }

        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }
}
