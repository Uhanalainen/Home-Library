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
    private boolean onLoan;
    private String loaner;
    private int pubYear;
    public ArrayList<Author> author = new ArrayList();
    public ArrayList<Category> category = new ArrayList();
    
    /**
     *  Creates an empty book object without parameters.
     */
    public Book() {
        
    }
    
    /**
     *  Creates an empty book object with id only.
     * @param id
     */
    public Book(int id) {
        this.id = id;
    }

    /**
     *  Creates an empty Book object with id, name, publication year, if it is
     *  on loan, who has loaned it and its original name.
     * 
     * @param id
     * @param name
     * @param pubYear
     * @param onLoan
     * @param loaner
     * @param origName
     */
    public Book(int id, String name, int pubYear, boolean onLoan, String loaner, String origName) {
        this.id = id;
        this.name = name;
        this.pubYear = pubYear;
        this.onLoan = onLoan;
        this.loaner = loaner;
        this.origName = origName;
    }
    
    /**
     *  Creates an empty Book object with name, publication year, if it is
     *  on loan, who has loaned it and its original name.
     *
     * @param name
     * @param origName
     * @param pubYear
     * @param onLoan
     * @param loaner
     */
    public Book(String name, String origName, int pubYear, boolean onLoan, String loaner) {
        this.name = name;
        this.origName = origName;
        this.pubYear = pubYear;
        this.onLoan = onLoan;
        this.loaner = loaner;
    }
    
    /**
     *  Get all categories associated with one specific book.
     * 
     *  <p>If book id is known, this method can get all associated categories
     *  from the database and store them in a list.
     *
     *  @return category arraylist
     */
    public ArrayList<Category> getCategories() {

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
                    this.category.add(cat);
                }
            }
        } catch (SQLException e) {
            System.out.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return this.category;
    }
    
    /**
     *  Get all authors associated with a specific book.
     * 
     *  <p>If book id is known, this method gets all authors associated with
     *  that book from the database and stores them in an arraylist.
     *
     * @return a list of all authors in the database
     */
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
            System.out.println("Something went wrong" + npe);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return this.author;
    }

    /**
     *  Return a list of all books in the database.
     *  
     * @return a list of all books
     */
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
                            rs.getBoolean("onLoan"), rs.getString("loaner"), rs.getString("origName")));
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

    /**
     *  Get all information of a a specific book when book id is known.
     * 
     *  <p>Fetches book name, publication year, if it is on loan, who has
     *  loaned it and the books original name.
     *
     * @param id the id of the book to get
     */
    public void getBook(int id) {
        
        String sql = "SELECT name, pubYear, onLoan, loaner, origName FROM books WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    this.id = id;
                    this.name = rs.getString("name");
                    this.pubYear = rs.getInt("pubYear");
                    this.onLoan = rs.getBoolean("onLoan");
                    this.loaner = rs.getString("loaner");
                    this.origName = rs.getString("origName");
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
     *  Adds a book to the database.
     * 
     *  <p>This method is broken into three parts: first, insert all book
     *  information into the books-table. Second, insert book and author id
     *  into the bookAuthors-table. Lastly, insert book and category id into
     *  the bookCategories-table. These last two are used so that a book can
     *  have multiple authors and categories.
     *
     */
    public void addBook() {
        
        String sql1 = "INSERT INTO books (name, origName, pubYear, onLoan, loaner) VALUES (?, ?, ?, ?, ?)";
        String sql2 = "INSERT INTO bookAuthors (bookId, authorId) VALUES (?, ?)";
        String sql3 = "INSERT INTO bookCategories (bookId, categoryId) VALUES (?, ?)";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql1);
            ps.setString(1, this.name);
            ps.setString(2, this.getOrigName());
            ps.setInt(3, this.getPubYear());
            ps.setBoolean(4, this.getOnLoan());
            ps.setString(5, this.getLoaner());
            int n1 = ps.executeUpdate();
            if(n1 > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                JOptionPane.showMessageDialog(null, this.name + " on lisätty tietokantaan", "", JOptionPane.PLAIN_MESSAGE);
                for (Author a : this.author) {
                    PreparedStatement ps2;
                    ps2 = conn.prepareStatement(sql2);
                    ps2.setInt(1, rs.getInt(1));
                    ps2.setInt(2, a.getId());
                    ps2.executeUpdate();
                }
                for(Category c : this.category) {
                    PreparedStatement ps3;
                    ps3 = conn.prepareStatement(sql3);
                    ps3.setInt(1, rs.getInt(1));
                    ps3.setInt(2, c.getId());
                    ps3.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    /**
     *  Update a book already stored in the database.
     * 
     *  <p>Updates all book data. Then deletes all data from bookAuthors and
     *  bookCategories-tables, and inserts new data. This ensures that the
     *  references to book in bookAuthors and bookCategories are up to date.
     */
    public void updateBook() {
        
        String sql = "UPDATE books SET name = ?, pubYear = ?, onLoan = ?, loaner = ?, origName = ? WHERE id = ?";
        String sql2 = "DELETE FROM bookAuthors WHERE bookId = ?";
        String sql3 = "INSERT INTO bookAuthors (bookId, authorId) VALUES (?, ?)";
        String sql4 = "DELETE FROM bookCategories WHERE bookId = ?";
        String sql5 = "INSERT INTO bookCategories (bookId, categoryId) VALUES (?, ?)";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(6, this.getId());
            ps.setString(1, this.name);
            ps.setInt(2, this.getPubYear());
            ps.setBoolean(3, this.getOnLoan());
            ps.setString(4, this.getLoaner());
            ps.setString(5, this.getOrigName());
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Kirjan muokkaus on tallennettu", "", JOptionPane.PLAIN_MESSAGE);
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, this.getId());
            ps2.executeUpdate();
            for(Author a : this.author) {
                PreparedStatement ps3 = conn.prepareStatement(sql3);
                ps3.setInt(1, this.getId());
                ps3.setInt(2, a.getId());
                ps3.executeUpdate();
            }
            PreparedStatement ps4 = conn.prepareStatement(sql4);
            ps4.setInt(1, this.getId());
            ps4.executeUpdate();
            for(Category c : this.category) {
                PreparedStatement ps5 = conn.prepareStatement(sql5);
                ps5.setInt(1, this.getId());
                ps5.setInt(2, c.getId());
                ps5.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }
    
    /**
     *  Deletes book from the database.
     *
     * @param id the id of the book to be deleted
     */
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        this.conn = DbConn.getConnection();
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Kirja on poistettu", "", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            System.err.println("Tapahtui virhe: " + e);
        } finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }
    
    /**
     *  Checks if the given book already exists in the database.
     *
     *  <p>To prevent duplicate entries of the same book, this method checks
     *  whether there's already a book with the same name, released in the same
     *  year in the database already.
     * 
     *  @return boolean
     */
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
    public boolean getOnLoan() {
        return onLoan;
    }

    /**
     * @param onLoan the onLoan to set
     */
    public void setOnLoan(boolean onLoan) {
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
}
