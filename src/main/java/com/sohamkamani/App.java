package com.sohamkamani;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

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
        insertBird(conn);

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
        PreparedStatement stmt = conn.prepareStatement("select * from birds");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
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


}
