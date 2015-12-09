package com.imi.rest.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DbConnectorUtil {

    private static final Logger LOGGER = Logger
            .getLogger(DbConnectorUtil.class);

    private static final String paramValueColumnName = "param_value";
    private static final String paramNameColumnName = "param_name";
    private static Connection conn = null;
    private static Map<String, String> constantsMap = new HashMap<String, String>();

    public static Map<String, String> getConstantsMap() {
        return constantsMap;
    }

    public static void getDbParamDetails(String driver, String url,
            String username, String password, String query, String moduleName) {
        PreparedStatement stmt = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.prepareStatement(query);
            stmt.setString(1, moduleName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                constantsMap.put(rs.getString(paramNameColumnName),
                        rs.getString(paramValueColumnName));
            }
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            LOGGER.error(se.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static final void closeConnection() {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
    }

}
