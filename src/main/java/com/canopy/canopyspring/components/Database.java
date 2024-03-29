package com.canopy.canopyspring.components;

import java.sql.*;

public class Database {

    public static Connection getDBConnection() throws Exception {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "**OMITTED**";
            String username = "**OMITTED**";
            String password = "**OMITTED**";
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch(Exception e){
            //System.out.println(e);
        }
        return null;
    }

    public static void commitToDB(String statement) {
        try {
            Connection conn = getDBConnection();
            assert conn != null;
            PreparedStatement posted = conn.prepareStatement(statement);
            posted.executeUpdate();
        } catch(Exception e){
            //System.out.println(e);
        }
    }

    public static ResultSet pullFromDB(String statement) {
        try {
            Connection conn = getDBConnection();
            assert conn != null;
            PreparedStatement posted = conn.prepareStatement(statement);
            return posted.executeQuery();
        } catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static void clearTable(String table) {
        String statement = "DELETE FROM " + table;
        Database.commitToDB(statement);
    }

    public static void buildSimDatabase() throws SQLException {
        clearTable("simruns");
        clearTable("simrescues");
        clearTable("simbids");
        String lastDate = "";
        String pullDateStatement = "SELECT date FROM canopy.rawdata GROUP BY date ORDER BY date DESC";
        ResultSet pullDateResult = pullFromDB(pullDateStatement);
        while (true) {
            assert pullDateResult != null;
            if (!pullDateResult.next()) break;
            lastDate = pullDateResult.getString("date");
        }
        String pullStatement = "SELECT * FROM rundeltas WHERE date = '" + lastDate +"' AND time = '17:00'";
        ResultSet result = pullFromDB(pullStatement);
        while (true) {
            assert result != null;
            if (!result.next()) break;
            String pushStatement = "INSERT INTO simruns (simrunid, routeid, driverid, routecode, driverinitials, stopsleft, packagesleft, currentcompletion, targetcompletion, delta) VALUES " +
                    "(NULL, " + result.getInt("routeid") + ", " + result.getInt("driverid") + ", '" + result.getString("routecode") +
                    "', '" + result.getString("driverinitials") + "', " + result.getInt("stopsleft") + ", " + result.getInt("packagesleft") +
                    ", " + result.getDouble("currentcompletion") + ", " + result.getDouble("targetcompletion") + ", " + result.getDouble("delta") + ")";
            commitToDB(pushStatement);
        }
    }

}
