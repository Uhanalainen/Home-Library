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
    
    /*
    *   Fetch an arraylist of all authors
    *   Remove all items from all author-related comboboxes used
    *   For each author in the 'authors' -list...
    *   ...add author name to every author-related combobox
    */
    private static void comboAuthorRefresh() {
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
    
    /*  
    *   Clears all textfields in add-panel
    *   Deletes all rows in dtm table model, then creates six empty ones
    *   Resets authorCounter and categoryCounter to 0
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
        dtm.setRowCount(0);
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
    }
    
    /*
    *   Fetch an arraylist of all categories in the database
    *   Remove all items from all category-related comboboxes
    *   For each category in 'categories'-list...
    *   ...add category to all category-related comboboxes
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
    
    /*  Method used to create six empty rows in the small table located in
    *   add book panel.
    */
    public static void fillAddManyTable() {
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        dtm.addRow(new Object[]{"", ""});
        addBookTable.getColumnModel().getColumn(0).setMinWidth(205);
        addBookTable.getColumnModel().getColumn(0).setMaxWidth(205);
    }
    
    /*
    *   First, saves user input as two strings from the text boxes. Then, checks
    *   that neither of the boxes were empty. If not, proceeds to create a new Author
    *   object, with which it can then check that the given author doesn't already
    *   exist in the database (a.doIExist). If it doesn't, proceeds to add the author
    *   to the database. Otherwise, tell the user what went wrong (that either name was
    *   missing, or that the author has already been added. Last but not least,
    *   the method empties both text boxes and calls for comboAuthorRefresh(), which,
    *   as the name implies, refreshes author combobox contents.
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
    
    /*
    *   Stores the given category name in variable.
    *   Checks that given name is not empty
    *   Checks that the category name doesn't contain blank spaces
    *   If all is well, create a new Category object with given name
    *   Check that the category doesn't already exist in the database
    *   Gives different error popups based on what went wrong
    *   Empties text box and refreshes category combobox
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
    
    /*
    *   Store all user input from text boxes and checkboxes, and the addManyTable
    *   Create new arraylists for authors and categories
    *   Go through the table, adding every author to the authors list
    *   Do same thing as above for categories
    *   Check that neither list is empty, if either one is, display error, prompting
    *   user to add at least one author and category
    *   Proceed to check publication year and name
    *   Then, for each author, go through his/her books, so that we don't accidentaly
    *   add duplicate books if any of them has the same book already entered in
    *   the database (in which case the user should rather edit that book and add the
    *   other authors to it).
    *   If all goes right, makes a Book object containing all given information
    *   Adds the categories and authors to the book
    *   Adds the book to the database, and empties all text fields and the table
    */
    public static void storeBook() {
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

        boolean addBook = true;
            if(authors.isEmpty() || categories.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kirja vaatii vähintään yhden kirjailija ja kategorian", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                addBook = false;
            }

        if(addBook) {
            if (checkPubYear(pubYear)) {
                if (!name.isEmpty()) {
                    for (Author a : authors) {
                        for (Book bo : a.books) {
                            if (name.equalsIgnoreCase(bo.getName()) && Integer.parseInt(pubYear) == bo.getPubYear()) {
                                JOptionPane.showMessageDialog(null, "Kirja löytyy jo tietokannasta", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                                addBook = false;
                            }
                        }
                    }
                    if (addBook) {
                        Book b = new Book(name, origName, Integer.parseInt(pubYear), onLoan, loaner);
                        for (Category c : categories) {
                            b.category.add(c);
                        }
                        for (Author a : authors) {
                            b.author.add(a);
                        }
                        b.addBook();
                        checkLoan.setSelected(false);
                        Add.clearBoxes();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Anna kirjalle nimi", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /*
    * A method for checking if the year of publication submitted by the user
    * is valid. The number is submitted as a String, because it's easier to verify the
    * length of a string than it is to check the amount of numbers in an integer.
    * 1: Check if the box is empty - if it is, notify user that he needs to add publication year
    * 2: Check the number submitted by the user - if it is anything but 4 letters long, inform user
    *    that he needs to fix his input to fit our database standard
    * 3: If, and only if, the number is 4 letters long, try to convert it into an integer.
    *    This tells us if there's something else than numbers submitted by the user. If there is,
    *    again, notify the user that he has to correct his input to numbers only.
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
    
    /*
    *   Saves combobox selection as string variable
    *   Checks that category the user wants to add to the table isn't already added
    *   If it isn't, add category to the table, otherwise tell user what went wrong
    *   Possible errors: trying to add more than 6 categories, or the category was
    *   already added to the table
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
    
    /*
    *   Saves combobox selection as a string variable
    *   Checks that the author the user wants to add to the table isn't already added
    *   If it isn't, add author to table, otherwise tell user what went wrong
    *   Also makes sure that there's not more than 6 authors added
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
