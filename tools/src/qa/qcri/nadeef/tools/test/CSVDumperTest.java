/*
 * Copyright (C) Qatar Computing Research Institute, 2013.
 * All rights reserved.
 */

package qa.qcri.nadeef.tools.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.jooq.SQLDialect;

import qa.qcri.nadeef.core.util.DBConnectionFactory;
import qa.qcri.nadeef.tools.CSVDumper;

/**
 * CSV Dumper test.
 */
public class CSVDumperTest {
    private static final String url = "jdbc:postgresql://localhost/unittest";
    private static final String userName = "tester";
    private static final String password = "tester";
    private static final String inputFileName =
            "tools\\src\\qa\\qcri\\nadeef\\tools\\test\\input\\dumptest.csv";

    private static String tableName;
    private static Connection conn;

    @BeforeClass
    public static void setUp() {
        try {
           conn = DBConnectionFactory.getConnection(SQLDialect.POSTGRES, url, userName, password);
           conn.setAutoCommit(false);
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @AfterClass
    public static void tearDown() {
        if (conn != null) {
            try {
                Statement stat = conn.createStatement();
                stat.execute("DROP TABLE IF EXISTS public." + tableName);
                conn.commit();
                conn.close();
            } catch(Exception ignore) {}
        }
    }

    @Test
    public void goodDumpTest() {
        try {
            tableName = CSVDumper.dump(conn, inputFileName);
            Assert.assertNotNull("tableName cannot be null", tableName);

            BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount ++;
            }
            reader.close();

            PreparedStatement stat =
                    conn.prepareStatement("SELECT COUNT(*) FROM " + "public." + tableName);
            ResultSet resultSet = stat.executeQuery();
            int rowCount = -1;
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1);
            }

            resultSet.close();
            Assert.assertEquals("Row number is not correct", lineCount - 1, rowCount);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail(ex.getMessage());
        }
    }
}
