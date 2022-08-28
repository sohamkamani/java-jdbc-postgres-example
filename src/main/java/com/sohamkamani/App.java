package com.sohamkamani;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.util.PSQLException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {

        DataSource dataSource = createDataSource();
        Connection conn = dataSource.getConnection();

        getAllBirds(conn);
        getFilteredBirds(conn);
        try {
            insertBird(conn);
        } catch (SQLException e) {
            String errorCode = e.getSQLState();
            // 08000 - connection_exception
            if (errorCode == "08000") {
                // retry query after re-establishing connection
            }
            // 42601 - syntax error
            else if (errorCode == "42601") {
                // throw error so that we can see the failure
                throw e;
            } else {
                // log a warning, or do some other action based on the error code
                System.out.printf("SQL failed with error code: %s%n", errorCode);
            }

        }
        updateBird(conn);
        deleteBird(conn);

    }

    private static DataSource createDataSource() {
        final String url =
                "jdbc:postgresql://localhost:5432/bird_encyclopedia?user=dbadmin&password=my-secret-password";
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    // Getting all entries from database
    private static void getAllBirds(Connection conn) throws SQLException {
        // get a connection from the datasource
        // Create a new statement on the connection
        PreparedStatement stmt = conn.prepareStatement("select * from birds");
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = stmt.executeQuery();
        // We run a loop to process the results.
        // The rs.next() method moves the result pointer to the next result row, and returns
        // true if a row is present, and false otherwise
        // note that initially the result pointer points before the first row, so we have to call
        // rs.next() the first time
        while (rs.next()) {
            // Now that `rs` points to a valid row (rs.next() is true), we can use the `getString`
            // and `getLong` methods to return each column value of the row as a string and long
            // respectively, and print it to the console
            System.out.printf("id:%d bird:%s description:%s%n", rs.getLong("id"),
                    rs.getString("bird"), rs.getString("description"));
        }
    }

    // Getting filtered entries from database using query params
    private static void getFilteredBirds(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from birds where bird=?");
        stmt.setString(1, "eagle");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.printf("id:%d bird:%s description:%s%n", rs.getLong("id"),
                    rs.getString("bird"), rs.getString("description"));
        }
    }

    // add a new bird to the table
    private static void insertBird(Connection conn) throws SQLException {
        PreparedStatement insertStmt =
                conn.prepareStatement("INSERT INTO birds(bird, description) VALUES (?, ?)");
        insertStmt.setString(1, "rooster");
        insertStmt.setString(2, "wakes you up in the morning");
        int insertedRows = insertStmt.executeUpdate();
        System.out.printf("inserted %s bird(s)%n", insertedRows);
    }

    // add a new bird to the table
    private static void updateBird(Connection conn) throws SQLException {
        // Similar to the previous example, we can use query params to fill in the condition
        // as well as the value to update
        PreparedStatement updateStmt =
                conn.prepareStatement("UPDATE birds SET description = ? WHERE bird = ?");
        updateStmt.setString(1, "has a red crown");
        updateStmt.setString(2, "rooster");
        int updatedRows = updateStmt.executeUpdate();
        System.out.printf("updated %s bird(s)%n", updatedRows);
    }

    // add a new bird to the table
    private static void deleteBird(Connection conn) throws SQLException {
        // Similar to the previous example, we can use query params to fill in the delete condition
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM birds WHERE bird = ?");
        deleteStmt.setString(1, "rooster");
        int deletedRows = deleteStmt.executeUpdate();
        System.out.printf("deleted %s bird(s)%n", deletedRows);
    }

}
