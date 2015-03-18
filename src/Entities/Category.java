/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Juha
 */
public class Category extends Master {

    public ArrayList<Book> books = new ArrayList();

    /**
     *  Creates a new, empty Category object.
     */
    public Category() {

    }

    /**
     *  Creates a new Category object with an id and a name.
     * 
     * @param id the id of the category
     * @param name the name of the category
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     *  Creates a new Category object with name only.
     * 
     * @param name the name of the category
     */
    public Category (String name) {
        this.name = name;
    }

    /**
     *  Gets a list of all categories in the database.
     * 
     * <p>Ordered by name for easy use.
     * 
     * @return a list of all categories
     */
    public static ArrayList<Category> getCategories() {
        String sql = "SELECT id, name FROM categories ORDER BY name";
        Connection conn = DbConn.getConnection();
        PreparedStatement ps = null;

        ArrayList<Category> categories = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    categories.add(new Category(rs.getInt("id"), rs.getString("name")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return categories;
    }
  
    /**
     *  Finds category id when category name is known.
     *
     * @param name the name of the category
     */
    public void getCategory(String name) {
        String sql = "SELECT id FROM categories WHERE name = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            try {
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    this.id = rs.getInt(1);
                    this.name = name;
                }
            } catch (Exception e) {
                System.out.println("Virhe" + e);
            }
        } catch (SQLException e) {
            System.out.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    /**
     *  Finds category name when category id is known.
     *
     * @param id the id of the category
     */
    public void getCategory(int id) {

        String sql = "SELECT name FROM categories WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    this.id = id;
                    this.name = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    /**
     *  Adds a category to the database.
     */
    public void addCategory() {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            int n1 = ps.executeUpdate();
            if (n1 > 0) {
                JOptionPane.showMessageDialog(null, this.name + " on lisätty tietokantaan", "", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    /**
     *  Updates a category.
     */
    public void updateCategory() {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setInt(2, this.id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Kategorian tiedot on päivitetty", "", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }
    
    /**
     *  Checks if given category already exists in the database.
     * 
     *  <p>To prevent duplicate entries of the same category, this method
     *  checks whether there's already a category with the same name in the
     *  database.
     *  
     * @return boolean
     */
    public boolean doesItExist() {

        String sql = "SELECT count(id) FROM categories WHERE LOWER(name) = LOWER(?)";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        int i = 0;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.executeQuery();
            try (ResultSet rs = ps.executeQuery();) {
                i = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return i > 0;
    }

    /**
     * @return the books
     */
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * @param books the books to set
     */
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }
}
