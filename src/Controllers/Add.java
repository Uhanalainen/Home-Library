/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import GUI.Menu;
import Entities.Author;
import Entities.Book;
import Entities.Category;
import static GUI.Menu.addBookTable;
import static GUI.Menu.checkLoan;
import static GUI.Menu.txtBookName;
import static GUI.Menu.txtLoaner;
import static GUI.Menu.txtOriginalName;
import static GUI.Menu.txtPubYear;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Juha
 */
public class Add extends Menu {
    
    /**
     *  Refresh the combobox after database addition.
     *  
     *  <p>Fetches a list of all authors, then removes all items from all
     *  author-related comboboxes and repopulates them. Resets selected author
     *  to empty selection.
     */
    public static void comboAuthorRefresh() {
        ArrayList<Author> authors = Author.getAuthors();
        cBoxAuthor.removeAllItems();
        cBoxEditAuthor.removeAllItems();
        cBoxEditBookAuthor.removeAllItems();
        for (Author a : authors) {
            cBoxAuthor.addItem(a.getLastName() + ", " + a.getName());
            cBoxEditAuthor.addItem(a.getLastName() + ", " + a.getName());
            cBoxEditBookAuthor.addItem(a.getLastName() + ", " + a.getName());
        }
        cBoxAuthor.setSelectedIndex(-1);
        cBoxEditAuthor.setSelectedIndex(-1);
        cBoxEditBookAuthor.setSelectedIndex(-1);
    }

    /**
     *  Refresh the combobox after database addition.
     * 
     *  <p>
     *  Fetches a list of all categories, then removes all items from all
     *  category-related comboboxes and repopulates them. Resets selected
     *  category to empty selection.
     *  </p>
     */
    public static void comboCatRefresh() {
        ArrayList<Category> categories = Category.getCategories();
        cBoxCategory.removeAllItems();
        cBoxEditCategory.removeAllItems();
        cBoxEditBookCategory.removeAllItems();
        for (Category c : categories) {
            cBoxCategory.addItem(c.getName());
            cBoxEditCategory.addItem(c.getName());
            cBoxEditBookCategory.addItem(c.getName());
        }
        cBoxCategory.setSelectedIndex(-1);
        cBoxEditCategory.setSelectedIndex(-1);
        cBoxEditBookCategory.setSelectedIndex(-1);
    }
    
    /**
     *  Empties all text fields and resets checkbox selections on the add
     *  panel.
     * 
     *  <p>Also calls <code>setAddBookTableProperties</code> to empty the
     *  <code>addBookTable</code>.
     */
    public static void clearBoxes() {
        txtAddAuthorFirstName.setText("");
        txtAddAuthorLastName.setText("");
        txtAddCategory.setText("");
        txtBookName.setText("");
        txtLoaner.setText("");
        txtPubYear.setText("");
        txtOriginalName.setText("");
        checkLoan.setSelected(false);
        setAddBookTableProperties();
    }
    
    /**
     *  Set the <code>addBookTable</code> row and column properties.
     *
     *  <p>Sets row count to zero, then adds six new rows. Fixes first column
     *  width.
     */
    public static void setAddBookTableProperties() {
        dtm.setRowCount(0);
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        addBookTable.getColumnModel().getColumn(0).setMinWidth(205);
        addBookTable.getColumnModel().getColumn(0).setMaxWidth(205);
    }
    
    /**
     *  Add a new author to the database.
     * 
     *  <p>Stores given author name in two string variables, creates a new
     *  author object, and adds the author to the database.
     * 
     *  <p>Possible errors:
     *  <ul>
     *  <li>Author already exists in the database</li>
     *  <li>First or last name is missing</li>
     *  </ul>
     */
    public static void addAuthor() {
        String name = txtAddAuthorFirstName.getText();
        String lastName = txtAddAuthorLastName.getText();

        if (!"".equalsIgnoreCase(name) && !"".equalsIgnoreCase(lastName)) {
            Author a = new Author(name, lastName);
            if (!a.doIExist()) {
                a.addAuthor();
            } else {
                JOptionPane.showMessageDialog(null, "Kirjailija löytyy jo tietokannasta.", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
            }
        } else if ("".equalsIgnoreCase(name)) {
            JOptionPane.showMessageDialog(null, "Syötä myös etunimi!", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Syötä myös sukunimi!", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
        txtAddAuthorFirstName.setText("");
        txtAddAuthorLastName.setText("");
        comboAuthorRefresh();
    }
    
    /**
     *  Add a new category to the database.
     * 
     *  <p>Stores given category name in a string variable, creates a new
     *  category object, and adds the category to the database.
     * 
     *  <p>Possible errors:
     *  <ul>
     *  <li>Category already exists in the database</li>
     *  <li>Category name contains blank spaces</li>
     *  <li>Given category name is empty</li>
     *  </ul>
     */
    public static void addCategory() {
        String name = txtAddCategory.getText();

        if (!"".equalsIgnoreCase(name)) {
            if (!name.contains(" ")) {
                Category c = new Category(name);
                if (!c.doesItExist()) {
                    c.addCategory();
                } else {
                    JOptionPane.showMessageDialog(null, "Kategoria löytyy jo tietokannasta.", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Kategorian nimessä ei voi olla välilyöntiä", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Anna kategorian nimi", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
        txtAddCategory.setText("");
        comboCatRefresh();
    }
    
    /**
     *  Deletes selected author from the <code>addBookTable</code>.
     *
     *  <p>An author must be selected by clicking the desired row in the table.
     *  If no category has been selected, notifies user with a popup.
     */
    public static void deleteAuthorFromTable() {
        try {
            int row = addBookTable.getSelectedRow();
            addBookTable.setValueAt("", row, 0);
            for(int i = row+1; i < 6; i++) {
                String value = addBookTable.getValueAt(i, 0).toString();
                if(!value.equalsIgnoreCase("")) {
                    addBookTable.setValueAt(value, i-1, 0);
                    addBookTable.setValueAt("", i, 0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse poistettava kirjailija klikkaamalla haluttua riviä taulukossa", "Virhe poistettaessa", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     *  Deletes selected category from the <code>addBookTable</code>.
     *
     *  <p>A category must be selected by clicking the desired row in the
     *  table. If no category has been selected, notifies user with a popup.
     */
    public static void deleteCategoryFromTable() {
        try {
            int row = addBookTable.getSelectedRow();
            addBookTable.setValueAt("", row, 1);
            for(int i = row+1; i < 6; i++) {
                String value = addBookTable.getValueAt(i, 1).toString();
                if(!value.equalsIgnoreCase("")) {
                    addBookTable.setValueAt(value, i-1, 1);
                    addBookTable.setValueAt("", i, 1);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse poistettava kategoria klikkaamalla haluttua riviä taulukossa", "Virhe poistettaessa", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *  Add a new book the the database.
     * 
     *  <p>Stores user input from textfields, the <code>addBookTable</code> and
     *  the checkbox. Creates empty arraylists for authors and categories.
     *  Loops through the <code>addBookTable</code> and stores all authors and
     *  categories in respective lists. Creates new book object with given
     *  parameters and adds it to the database.
     * 
     *  <p>Notifies user of possible errors:
     *  <ul>
     *  <li>No authors or categories has been added to the table</li>
     *  <li>The book already exists in the database</li>
     *  <li>No book name was given</li>
     *  <li>Publication year was erroneous or empty</li>
     *  </ul>
     */    
    public static void storeBook() {
        boolean addBook = true;
        String name = txtBookName.getText();
        String origName = txtOriginalName.getText();
        String pubYear = txtPubYear.getText();
        String loaner = txtLoaner.getText();
        boolean onLoan = checkLoan.isSelected();
        ArrayList<Author> authors = new ArrayList();
        ArrayList<Category> categories = new ArrayList();

        for (int i = 0; i < 6; i++) {
            String val = addBookTable.getValueAt(i, 0).toString();
            if(!val.isEmpty()) {
                Author a = new Author();
                String[] authName = val.split(", ");
                String lName = authName[0];
                String fName = authName[1];
                a.getAuthor(fName, lName);
                a.getBooks();
                authors.add(a);
            } else {
                i = 6;
            }
        }

        for (int i = 0; i < 6; i++) {
            String cat = addBookTable.getValueAt(i, 1).toString();
            if(!cat.isEmpty()) {
                Category c = new Category();
                c.getCategory(cat);
                categories.add(c);
            } else {
                i = 6;
            }
        }

        if(authors.isEmpty() || categories.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Kirja vaatii vähintään yhden kirjailija ja kategorian", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
            addBook = false;
        }

        while(addBook) {
            if (checkPubYear(pubYear)) {
                if (!name.isEmpty()) {
                    for (Author a : authors) {
                        for (Book bo : a.books) {
                            if (name.equalsIgnoreCase(bo.getName()) && Integer.parseInt(pubYear) == bo.getPubYear()) {
                                JOptionPane.showMessageDialog(null, "Kirja löytyy jo tietokannasta", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                                addBook = false;
                                break;
                            } else {
                                Book b = new Book(name, origName, Integer.parseInt(pubYear), onLoan, loaner);
                                for (Category c : categories) {
                                    b.category.add(c);
                                }
                                for (Author aa : authors) {
                                    b.author.add(aa);
                                }
                                b.addBook();
                                checkLoan.setSelected(false);
                                Add.clearBoxes();
                                addBook = false;
                                break;
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Anna kirjalle nimi", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
        }
    }

    /**
     *  Checks if publication year given by the user is valid.
     * 
     *  <p>Gets pubYear as String parameter. First, check that it is not empty.
     *  Then, ensure that the length is four digits. If it is, proceed to try
     *  and parse it to an integer. If that succeeds, return true.
     *  
     *  <p>Notifies user of possible errors:
     *  <ul>
     *  <li>Given input contains anything else than numbers</li>
     *  <li>Given input is not four digits</li>
     *  <li>No input was given</li>
     *  </ul>
     * 
     *  @param pubYear the year to be checked
     *  @return boolean yearOk
     */
    public static boolean checkPubYear(String pubYear) {

        boolean yearOk = false;
        if (!pubYear.isEmpty()) {
            if (pubYear.length() == 4) {
                try {
                    int pYear = Integer.parseInt(pubYear);
                    yearOk = true;
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(null, "Syötä neljänumeroinen vuosiluku, esim '1994'", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                    yearOk = false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Syötä neljänumeroinen vuosiluku, esim '1994'", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Julkaisuvuosi oli tyhjä", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
        return yearOk;
    }
    
    /**
     *  Add selected category to the <code>addBookTable</code>.
     * 
     *  <p>Stores selected category name to a string variable and puts it in
     *  the <code>addBookTable</code>.
     *  
     *  <p>Notifies user of possible errors:
     *  <ul>
     *  <li>Category is already in the table</li>
     *  <li>No category has been selected from the combobox</li>
     *  </ul>
     */
    public static void addCategoryToTable() {
        try {
            String category = cBoxCategory.getSelectedItem().toString();
            for (int i = 0; i < 6; i++) {
                String value = addBookTable.getValueAt(i, 1).toString();
                if (category != null && category.length() != 0 ) {
                    if (value.equalsIgnoreCase(category)) {
                        JOptionPane.showMessageDialog(null, "Kategoria löytyy jo taulukosta", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                        i = 6;
                    } else if (value.length() < 1) {
                        addBookTable.setValueAt(category, i, 1);
                        i = 6;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse pudotusvalikosta lisättävä kategoria", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     *  Add selected author to the <code>addBookTable</code>.
     *
     *  <p>Stores selected author name to a string variable and puts it in the
     *  <code>addBookTable</code>.
     *
     *  <p>Notifies user of possible errors:
     *  <ul>
     *  <li>Author is already in the table</li>
     *  <li>No author has been selected from the combobox</li>
     *  </ul>
     */
    public static void addAuthorToTable() {
        try {
            String author = cBoxAuthor.getSelectedItem().toString();
            for (int i = 0; i < 6; i++) {
                String val = addBookTable.getValueAt(i, 0).toString();
                if (val.equalsIgnoreCase(author)) {
                    JOptionPane.showMessageDialog(null, "Kirjailija löytyy jo taulukosta", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                    i = 6;
                } else if (val.length() < 1) {
                    addBookTable.setValueAt(author, i, 0);
                    i = 6;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse pudotusvalikosta lisättävä kirjailija", "Virhe", JOptionPane.ERROR_MESSAGE);
        }
    }
}
