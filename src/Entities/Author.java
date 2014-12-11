/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Juha
 */
public class Author extends Master {

    public ArrayList<Book> books = new ArrayList();
    private String lastName;

    public Author(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Author() {

    }

    public Author(int id, String name, String lastName) {
        
        this.id = id;
        this.name = name;
        this.lastName = lastName;

    }

    public static ArrayList<Author> getAuthors() {

        String sql = "SELECT id, firstName, lastName FROM authors ORDER BY lastName";
        Connection conn = DbConn.getConnection();
        PreparedStatement ps = null;

        ArrayList<Author> authors = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    authors.add(new Author(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return authors;
    }
    
    public static ArrayList<Author> findAuthors(String lastName) {

        String sql = "SELECT id, firstName, lastName FROM authors WHERE LOWER(lastName) LIKE LOWER(?)";
        Connection conn = DbConn.getConnection();
        PreparedStatement ps = null;

        ArrayList<Author> authors = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, lastName);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    authors.add(new Author(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return authors;
    }
    
    public void getAuthor(String firstName, String lastName) {
        
        String sql = "SELECT id FROM authors WHERE firstName = ? AND lastName = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            try {
                ResultSet rs = ps.executeQuery();
                this.id = rs.getInt(1);
                this.name = firstName;
                this.lastName = lastName;
            } catch (Exception e) {
                System.out.println("Virhe: " + e);
            }
        }catch (SQLException e) {
            System.err.println("Error: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void getAuthor(int id) {

        String sql = "SELECT firstName, lastName FROM authors WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    this.id = id;
                    this.name = rs.getString("firstName");
                    this.lastName = rs.getString("lastName");
                }
            }
        } catch (SQLException e) {
            System.out.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }

    }

    public void addAuthor() {

        String sql = "INSERT INTO authors (firstName, lastName) VALUES (?, ?)";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setString(2, this.getLastName());
            int n1 = ps.executeUpdate();
            if (n1 > 0) {
                JOptionPane.showMessageDialog(null, this.name + " " + this.getLastName() + " on lisätty tietokantaan", "", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void updateAuthor() {

        String sql = "UPDATE authors SET (firstName = ?, lastName = ?) WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setString(2, this.getLastName());
            ps.setInt(3, this.id);
            ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void deleteAuthor() {

    }
    
    public ArrayList<Book> getBooks() {
        
        String sql = "SELECT bookId FROM bookAuthors WHERE authorId = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, this.id);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    Book b = new Book();
                    b.getBook(rs.getInt("bookId"));
                    this.books.add(b);
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
        return this.books;
    }

    public boolean doIExist() {

        String sql = "SELECT count(id) FROM authors WHERE LOWER(firstName) = LOWER(?) and LOWER(lastName) = LOWER(?)";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        int i = 0;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.name);
            ps.setString(2, this.getLastName());
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
     * @param books the books to set
     */
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
    
    public int getId(){
        return this.id;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getName() {
        return name;
    }
}
