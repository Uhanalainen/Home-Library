/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Entities;

import java.sql.Connection;

/**
 *
 * @author Juha
 */
public abstract class Master {
    protected int id;
    protected String name;
    protected Connection conn;
}
