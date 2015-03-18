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

    /**
     *  Connects to the database.
     *
     * @return Connection
     */
    public static Connection getConnection() {
        DbConn conn = new DbConn();

        return conn.conn;
    }

    /**
     *  Attempts to load database driver and connect to the database.
     */
    public DbConn() {
        if (conn == null) {
            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:Library2.sqlite");
            } catch (Exception e) {
                throw new RuntimeException("Something is wrong", e);
            }
        }
    }
}