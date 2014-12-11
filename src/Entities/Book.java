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
public class Book extends Master {
    
    private String origName;
    private String onLoan;
    private String loaner;
    private int pubYear;
    public ArrayList<Author> author = new ArrayList();
    public ArrayList<Category> category = new ArrayList();
    
    public Book() {
        
    }

    public Book(int id, String name, int pubYear, String onLoan, String loaner, String origName) {
        this.id = id;
        this.name = name;
        this.pubYear = pubYear;
        this.onLoan = onLoan;
        this.loaner = loaner;
        this.origName = origName;
    }
    
    public Book(String name, String origName, int pubYear, String onLoan, String loaner) {
        this.name = name;
        this.origName = origName;
        this.pubYear = pubYear;
        this.onLoan = onLoan;
        this.loaner = loaner;
    }
    
    public void getCategories() {
        this.setCategory((ArrayList<Category>) new ArrayList());
        String sql = "SELECT categoryId FROM bookCategories WHERE bookID = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, this.getId());
            try (ResultSet rs = ps.executeQuery(); ) {
                while(rs.next()) {
                    Category cat = new Category();
                    cat.getCategory(rs.getInt("categoryId"));
                    this.getCategory().add(cat);
                }
            }
        } catch (SQLException e) {
            System.out.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        
    }

    public ArrayList<Author> getAuthors() {

        String sql = "SELECT authorId FROM bookAuthors WHERE bookId = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, this.id);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    Author a = new Author();
                    a.getAuthor(rs.getInt("authorId"));
                    this.author.add(a);
                }
            }
        } catch (SQLException e) {
            System.out.println("Tapahtui virhe: " + e);
        } catch (NullPointerException npe) {
            System.out.println("saasf" + npe);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return this.author;
    }

    public static ArrayList<Book> getBooks() {

        String sql = "SELECT * FROM books";
        Connection conn = DbConn.getConnection();
        PreparedStatement ps = null;

        ArrayList<Book> books = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    books.add(new Book(rs.getInt("id"), rs.getString("name"), rs.getInt("pubYear"),
                            (rs.getBoolean("onLoan") == true ? "Kyllä" : "Ei"), rs.getString("loaner"), rs.getString("origName")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong" + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return books;
    }

    public static ArrayList<Book> findBooks(String name) {
        
        String sql = "SELECT id, name, pubYear, onLoan, loaner, origName FROM books WHERE LOWER(name) LIKE LOWER(?)";
        Connection conn = DbConn.getConnection();
        PreparedStatement ps = null;

        ArrayList<Book> books = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    books.add(new Book(rs.getInt("id"), rs.getString("name"), rs.getInt("pubYear"),
                            (rs.getBoolean("onLoan") == true ? "Kyllä" : "Ei"), rs.getString("loaner"), rs.getString("origName")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong" + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return books;
    }

    public void getBook(int id) {
        
        String sql = "SELECT book FROM books WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    this.setId(id);
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

    public void addBook() {
        
        String sql = "INSERT INTO books (name, origName, pubYear, onLoan, loaner) VALUES (?, ?, ?, ?, ?)";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setString(2, this.getOrigName());
            ps.setInt(3, this.getPubYear());
            ps.setBoolean(4, this.getOnLoan().equals("Kyllä") ? true : false);
            ps.setString(5, this.getLoaner());
            int n1 = ps.executeUpdate();
            if(n1 > 0) {
                JOptionPane.showMessageDialog(null, this.name + " on lisätty tietokantaan", "", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void updateBook() {
        
        String sql = "UPDATE books SET name = ?, pubYear = ?, onLoan = ?, loaner = ?, origName = ? WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(6, this.getId());
            ps.setString(1, this.name);
            ps.setInt(2, this.getPubYear());
            ps.setString(3, this.getOnLoan());
            ps.setString(4, this.getLoaner());
            ps.setString(5, this.getOrigName());
            ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }
    
    public void deleteBook() {
        
    }
    
    public boolean doesItExist() {

        String sql = "SELECT count(id) FROM books WHERE LOWER(name) = LOWER(?) AND pubYear = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        int i = 0;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setInt(2, this.pubYear);
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
     * @return the origName
     */
    public String getOrigName() {
        return origName;
    }

    /**
     * @param origName the origName to set
     */
    public void setOrigName(String origName) {
        this.origName = origName;
    }

    /**
     * @return the onLoan
     */
    public String getOnLoan() {
        return onLoan;
    }

    /**
     * @param onLoan the onLoan to set
     */
    public void setOnLoan(String onLoan) {
        this.onLoan = onLoan;
    }

    /**
     * @return the loaner
     */
    public String getLoaner() {
        return loaner;
    }

    /**
     * @param loaner the loaner to set
     */
    public void setLoaner(String loaner) {
        this.loaner = loaner;
    }

    /**
     * @return the pubYear
     */
    public int getPubYear() {
        return pubYear;
    }

    /**
     * @param pubYear the pubYear to set
     */
    public void setPubYear(int pubYear) {
        this.pubYear = pubYear;
    }

    /**
     * @return the author
     */
    public ArrayList<Author> getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(ArrayList<Author> author) {
        this.author = author;
    }

    /**
     * @return the category
     */
    public ArrayList<Category> getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
}
