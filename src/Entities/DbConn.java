/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Juha
 */
public class DbConn {

    private Connection conn;

    public static Connection getConnection() {
        DbConn conn = new DbConn();

        return conn.conn;
    }

    public DbConn() {
        if (conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                this.conn = DriverManager.getConnection("jdbc:sqlite:src/Database/Library2.sqlite");
            } catch (Exception e) {
                System.out.println("Virhe");
            }
        }
    }
}