package com.colak.jdbc.oracle.databasemetadata;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

@Slf4j
class DatabaseMetadataTest {

    public static void main() {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:FREE";
        String username = "sa";
        String password = "my1passw";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

            DatabaseMetaData metaData = connection.getMetaData();
            boolean storesUpperCase = metaData.storesUpperCaseIdentifiers();
            log.info("Database stores identifiers in uppercase: {}", storesUpperCase);

            // Check if the database stores identifiers in lowercase
            boolean storesLowerCase = metaData.storesLowerCaseIdentifiers();
            log.info("Database stores identifiers in lowercase: {}", storesLowerCase);

            // Specify the table name for which you want to retrieve metadata
            String tableName = "all_char_types_table";
            String tableSeparator = "---------- {} ----------";
            log.info(tableSeparator, tableName);
            printTable(metaData, tableName);

            tableName = "boolean_types_table";
            log.info(tableSeparator, tableName);
            printTable(metaData, tableName);

            tableName = "all_clob_types_table";
            log.info(tableSeparator, tableName);
            printTable(metaData, tableName);

            tableName = "all_blob_types_table";
            log.info(tableSeparator, tableName);
            printTable(metaData, tableName);

            tableName = "bit_types_table";
            log.info(tableSeparator, tableName);
            printTable(metaData, tableName);

            tableName = "integer_types_table";
            log.info(tableSeparator, tableName);
            printTable(metaData, tableName);

            tableName = "all_numeric_types_table";
            log.info(tableSeparator, tableName);
            printTable(metaData, tableName);

        } catch (Exception exception) {
            log.error("Exception ", exception);
        }
    }

    private static void printTable(DatabaseMetaData metaData, String tableName) throws SQLException {
        boolean storesUpperCase = metaData.storesUpperCaseIdentifiers();

        if (storesUpperCase) {
            tableName = tableName.toUpperCase(Locale.ROOT);
        }

        // Same as "SELECT * FROM all_tab_columns t where t.table_name = 'TEST1';"
        // Get columns metadata
        try (ResultSet resultSet = metaData.getColumns(null, null, tableName, null)) {
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String dataType = resultSet.getString("TYPE_NAME");
                int columnSize = resultSet.getInt("COLUMN_SIZE");
                int scale = resultSet.getInt("DECIMAL_DIGITS");

                log.info("Column Name {} ", columnName);
                log.info("Data Type: {}", dataType);
                log.info("Column Size: {}", columnSize);
                log.info("Decimal Digits: {}", scale);
                log.info("----------");
            }
        }
    }
}
