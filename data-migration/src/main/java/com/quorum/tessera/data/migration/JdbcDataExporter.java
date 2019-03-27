package com.quorum.tessera.data.migration;


import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class JdbcDataExporter implements DataExporter {

    private final String jdbcUrl;

    private final String insertRow;

    private final List<String> createTables;

    public JdbcDataExporter(String jdbcUrl, String insertRow, URL ddl) {
        this.jdbcUrl = jdbcUrl;
        this.insertRow = insertRow;
        this.createTables = UriCallback.execute(() -> Files.lines(Paths.get(ddl.toURI()))
            .collect(Collectors.toList()));

    }
    @Override
    public void export(Map<byte[], byte[]> data, Path output, String username, String password) throws SQLException {

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {

            try (Statement stmt = conn.createStatement()) {
                for(String createTable : createTables) {
                    stmt.executeUpdate(createTable);
                }
            }

            try (PreparedStatement insertStatement = conn.prepareStatement(insertRow)) {
                for (Entry<byte[], byte[]> values : data.entrySet()) {
                    insertStatement.setBytes(1, values.getKey());
                    insertStatement.setBytes(2, values.getValue());
                    insertStatement.execute();
                }
            }

        }
    }

}
