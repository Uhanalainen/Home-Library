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
import static GUI.Menu.checkEditLoan;
import static GUI.Menu.editBookTable;
import static GUI.Menu.model;
import static GUI.Menu.txtEditBookName;
import static GUI.Menu.txtEditLoaner;
import static GUI.Menu.txtEditOrigName;
import static GUI.Menu.txtEditPubYear;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Juha
 */
public class Edit extends Menu {

    //Clears all textfields and the table in the edit-panel
    public static void clearEditBoxes() {
        txtEditAuthorLastName.setText("");
        txtEditAuthorName.setText("");
        txtEditBookName.setText("");
        txtEditCategory.setText("");
        txtEditLoaner.setText("");
        txtEditOrigName.setText("");
        txtEditPubYear.setText("");
        cBoxEditAuthor.setSelectedIndex(-1);
        cBoxEditBookAuthor.setSelectedIndex(-1);
        cBoxEditBookCategory.setSelectedIndex(-1);
        cBoxEditCategory.setSelectedIndex(-1);
        model.setRowCount(0);
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        checkEditLoan.setSelected(false);
    }

    /*
     *   Much like it's neighbor, fillTable(), this is used to fill the small table
     *   in the edit panel. It only fills it if the id variable is something else than 0,
     *   though. Because it relies on the user clicking on a row in the browse table
     *   to get the information to fill in all known information about the book, such
     *   as authors, categories, book name, publication year etc. If the user hasn't
     *   selected any book, there's not much point in filling a empty table.
     */
    public static void fillEditBookTable() {
        if (id != 0) {
            model.setRowCount(0);
            model.addRow(new Object[]{"", ""});
            model.addRow(new Object[]{"", ""});
            model.addRow(new Object[]{"", ""});
            model.addRow(new Object[]{"", ""});
            model.addRow(new Object[]{"", ""});
            model.addRow(new Object[]{"", ""});
            editBookTable.getColumnModel().getColumn(0).setMinWidth(205);
            editBookTable.getColumnModel().getColumn(0).setMaxWidth(205);

            Book b = new Book();
            b.getBook(id);
            txtEditBookName.setText(b.getName());
            txtEditPubYear.setText(b.getPubYear() + "");
            txtEditLoaner.setText(b.getLoaner());
            txtEditOrigName.setText(b.getOrigName());
            if (b.getOnLoan()) {
                checkEditLoan.setSelected(true);
            } else {
                checkEditLoan.setSelected(false);
            }

            b.getAuthors();
            b.getCategories();

            String authorName = "";
            int authorCount = 0;
            for (Author a : b.author) {
                authorName = a.getLastName() + ", " + a.getName();
                editBookTable.setValueAt(authorName, authorCount, 0);
                authorCount++;
            }
            String categoryName = "";
            int categoryCount = 0;
            for (Category c : b.category) {
                categoryName = c.getName();
                editBookTable.setValueAt(categoryName, categoryCount, 1);
                categoryCount++;
            }
        }
    }

    /*
     *   Method used when user has edited an author and clicks the edit author-button
     *   
     *   Gets the old name of the author from the combobox selection, stores it to a variable
     *   Splits it into parts so that it gets first and lastname separate
     *   Stores the new names in different variables from the information in the text boxes
     *   Checks that neither textbox was empty
     *   If not, create a new Author object using the old names
     *   Use the created author a to get the author id (which is needed to save the new information)
     *   Use author.java setters to set new first and last name to Author object a
     *   Checks that this new author doesn't already exist (prevents duplicate authors)
     *   If not, use the updateAuthor method from author.java, which was why the id was
     *   needed
     *   Upon update, informs the user via popup that the update was done, refreshes authors combobox
     *   and empties both textfields
     *   Else informs what went wrong (Author already exists, first name was empty or last name was empty)
     */
    public static void editAuthor() {
        String author = cBoxEditAuthor.getSelectedItem().toString();
        String[] authName = author.split(", ");
        String oldName = authName[1];
        String oldLastName = authName[0];
        String name = txtEditAuthorName.getText();
        String lastName = txtEditAuthorLastName.getText();

        if (!"".equalsIgnoreCase(name) && !"".equalsIgnoreCase(lastName)) {
            Author a = new Author(oldName, oldLastName);
            a.getAuthor(oldName, oldLastName);
            a.setName(name);
            a.setLastName(lastName);
            if (!a.doIExist()) {
                a.updateAuthor();
                Add.comboAuthorRefresh();
                txtEditAuthorName.setText("");
                txtEditAuthorLastName.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Kirjailija löytyy jo tietokannasta.", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
            }
        } else if ("".equalsIgnoreCase(name)) {
            JOptionPane.showMessageDialog(null, "Syötä myös etunimi!", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Syötä myös sukunimi!", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*
     *   Method used when user has edited an category and clicks the edit category-button
     *   
     *   Gets the old name of the category from the combobox selection, stores it to a variable
     *   Stores the new name in different variable from the information in the text box
     *   Checks that textbox is not empty and that it doesn't contain blank spaces
     *   If not, create a new Category object using the old name
     *   Use the created category a to get the category id (which is needed to save the new information)
     *   Use category.java setter to set new name to Category object c
     *   Checks that this new category doesn't already exist (prevents duplicate categories)
     *   If not, use the updateCategory method from category.java, which was why the id was
     *   needed
     *   Upon update, informs the user via popup that the update was done, refreshes categories combobox
     *   and empties the textfield
     *   Else informs what went wrong (Category already exists, there was illegal characters in hte input, or name was empty
     */
    public static void editCategory() {
        String name = txtEditCategory.getText();
        String oldName = cBoxEditCategory.getSelectedItem().toString();

        if (!"".equalsIgnoreCase(name)) {
            if (!name.contains(" ")) {
                Category c = new Category(oldName);
                c.getCategory(oldName);
                c.setName(name);
                if (!c.doesItExist()) {
                    c.updateCategory();
                    Add.comboCatRefresh();
                    txtAddCategory.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Kategoria löytyy jo tietokannasta.", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Kategorian nimessä ei voi olla välilyöntiä", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Tekstilaatikko oli tyhjä!", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     *   Method used when user clicks delete category-button when editing a book
     *   User must choose which category to delete by selecting a row from the table
     *   The row number is stored in a variable
     *   The method deletes the text in the given cell
     *   Then moves all other categories upwards one row so no empty rows are left
     *   in the middle of the table
     *   Displays error popup if user has not selected a row in the table
     */
    public static void deleteCategory() {
        try {
            int row = editBookTable.getSelectedRow();
            editBookTable.setValueAt("", row, 1);
            for (int i = row + 1; i < 6; i++) {
                String value = editBookTable.getValueAt(i, 1).toString();
                if (!value.equalsIgnoreCase("")) {
                    editBookTable.setValueAt(value, i - 1, 1);
                    editBookTable.setValueAt("", i, 1);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse poistettava kategoria klikkaamalla haluttua riviä taulukossa", "Virhe poistettaessa", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     *   Method used when user clicks delete author-button when editing a book
     *   User must choose which author to delete by selecting a row from the table
     *   The row number is stored in a variable
     *   The method deletes the text in the given cell
     *   Then moves all other authors upwards one row so no empty rows are left
     *   in the middle of the table
     *   Displays error popup if user has not selected a row in the table
     */
    public static void deleteAuthor() {
        try {
            int row = editBookTable.getSelectedRow();
            editBookTable.setValueAt("", row, 0);
            for (int i = row + 1; i < 6; i++) {
                String value = editBookTable.getValueAt(i, 0).toString();
                if (!value.equalsIgnoreCase("")) {
                    editBookTable.setValueAt(value, i - 1, 0);
                    editBookTable.setValueAt("", i, 0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse poistettava kirjailija klikkaamalla haluttua riviä taulukossa", "Virhe poistettaessa", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     *   Simply used to delete a book, this method depends on the user choosing
     *   a book by clicking a row in the browse table in the browse panel.
     */
    public static void deleteBook() {
        if (id != 0) {
            Book b = new Book();
            b.deleteBook(id);
        } else {
            JOptionPane.showMessageDialog(null, "Valitse ensin poistettava kirja selaus-taulukosta", "Virhe poistettaessa", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     *   This method is used whenever the user has made some changes to a book and
     *   clicks the save changes-button. This is perhaps the most complicated method
     *   of all methods used.
     *
     *   First, create an empty Book object.
     *   Get the original book information, using the id (which we know since this
     *   relies on the user selecting a row in the browse table).
     *   Save all new information to variables
     *   Create empty arraylists for authors and categories
     *   Use book.java setter methods to set the new information to the book object
     *   If the user has checked that the book is on loan, also set loaner name,
     *   otherwise set loaner name as empty string
     *   Clear the original authors and categories from the book object, so that
     *   we can store new authors and categories
     *   As when adding a book, go through the editBookTable and add every author
     *   and category to the lists.
     *   Check publication year, and that the given new name is not empty
     *   Check that none of the authors has the new book associated with them already,
     *   based on name and publication year
     *   If ok, save changes to the book, adding the new associated authors and categories
     *   Else tell user what went wrong (one of the authors has the book associated already,
     *   the publication year was faulty, or the new given book name was empty)
     *   If the update is done, the method will also empty all text boxes, checkboxes and the table
     */
    public static void updateBook() {
        Book b = new Book();
        b.getBook(id);

        String newName = txtEditBookName.getText();
        String newPubYear = txtEditPubYear.getText();
        boolean newOnLoan = checkEditLoan.isSelected();
        String newLoaner = txtEditLoaner.getText();
        String newOrigName = txtEditOrigName.getText();

        ArrayList<Author> authors = new ArrayList();
        ArrayList<Category> categories = new ArrayList();

        b.setName(newName);
        b.setOnLoan(newOnLoan);
        b.setOrigName(newOrigName);
        if (newOnLoan) {
            b.setLoaner(newLoaner);
        } else {
            b.setLoaner("");
        }
        b.author.clear();
        b.category.clear();

        for (int i = 0; i < 6; i++) {
            String val = editBookTable.getValueAt(i, 0).toString();
            if (!val.isEmpty()) {
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
            String cat = editBookTable.getValueAt(i, 1).toString();
            if (!cat.isEmpty()) {
                Category c = new Category();
                c.getCategory(cat);
                categories.add(c);
            } else {
                i = 6;
            }
        }

        if (Add.checkPubYear(newPubYear)) {
            if (!newName.isEmpty()) {
                boolean bookExists = false;
                for (Author a : authors) {
                    for (Book bo : a.books) {
                        if (newName.equalsIgnoreCase(bo.getName()) && Integer.parseInt(newPubYear) == bo.getPubYear()
                                && bo.getId() != b.getId()) {
                            bookExists = true;
                        }
                    }
                }
                if (!bookExists) {
                    for (Category c : categories) {
                        b.category.add(c);
                    }
                    for (Author a : authors) {
                        b.author.add(a);
                    }
                    b.updateBook();
                    checkEditLoan.setSelected(false);
                    clearEditBoxes();
                } else {
                    JOptionPane.showMessageDialog(null, "Kirjan muokkaus epäonnistui. Tarkasta, ettei jollain kirjailijalla ole jo kyseistä kirjaa", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Kirjan nimi puuttuu", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     *   Save combobox selection to string variable
     *   Checks that the author the user wants to add isn't already in the table
     *   If it's not, enter the author information in the first empty row
     *   Display error to the user depending on what went wrong (author already in table,
     *   user has failed to select a book to edit from the browse table, or user
     *   has failed to choose which author to add from the author combobox
     */
    public static void addAuthorToEditTable() {
        try {
            String author = cBoxEditBookAuthor.getSelectedItem().toString();
            if (editBookTable.getRowCount() > 1) {
                for (int i = 0; i < 6; i++) {
                    String val = editBookTable.getValueAt(i, 0).toString();
                    if (val.equalsIgnoreCase(author)) {
                        JOptionPane.showMessageDialog(null, "Kirjailija löytyy jo taulukosta", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                        i = 6;
                    } else if (val.length() < 1) {
                        editBookTable.setValueAt(author, i, 0);
                        i = 6;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Valitse ensin selausvalikosta muokattava kirja", "Virhe", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse pudotusvalikosta lisättävä kirjailija", "Virhe", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     *   Save combobox selection to string variable
     *   Checks that the category the user wants to add isn't already in the table
     *   If it's not, enter the category information in the first empty row
     *   Display error to the user depending on what went wrong (category already in table,
     *   user has failed to select a book to edit from the browse table, or user
     *   has failed to choose which category to add from the category combobox
     */
    public static void addCategoryToEditTable() {
        try {
            String category = cBoxEditBookCategory.getSelectedItem().toString();
            if (editBookTable.getRowCount() > 1) {
                for (int i = 0; i < 6; i++) {
                    String value = editBookTable.getValueAt(i, 1).toString();
                    if (category != null && category.length() != 0) {
                        if (value.equalsIgnoreCase(category)) {
                            JOptionPane.showMessageDialog(null, "Kategoria löytyy jo taulukosta", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                            i = 6;
                        } else if (value.length() < 1) {
                            editBookTable.setValueAt(category, i, 1);
                            i = 6;
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Valitse ensin selausvalikosta muokattava kirja", "Virhe", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse pudotusvalikosta lisättävä kategoria", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     *   Method used to get information on what author user wants to edit
     *   Stores chosen combobox information in a string
     *   Splits the string into parts and removes anything that isn't first- or lastname
     *   Puts the names in correct text boxes for easy editing
     */
    public static void getEditAuthor() {
        try {
            String author = cBoxEditAuthor.getSelectedItem().toString();
            String[] authName = author.split(", ");
            String lName = authName[0];
            String fName = authName[1];
            txtEditAuthorLastName.setText(lName);
            txtEditAuthorName.setText(fName);
        } catch (Exception e) {
        }
    }

    /*
     *   Method used to get information on what category user wants to edit
     *   Get information from categories combobox, store which category was selected in a variable
     *   Stores chosen combobox information in a string
     *   Puts the name in correct text box for easy editing
     */
    public static void getEditCategory() {
        try {
            String category = cBoxEditCategory.getSelectedItem().toString();
            txtEditCategory.setText(category);
        } catch (Exception e) {
        }
    }
}
