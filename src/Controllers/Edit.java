/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Entities.Author;
import Entities.Book;
import Entities.Category;
import GUI.Menu;
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

    /**
     *  Empties all text fields and resets checkbox selections on the edit
     *  panel.
     *
     *  <p>Also sets every combobox selection to show up as empty selection,
     *  and uses <code>setTableProperties</code> to empty the
     *  <code>editBookTable</code>.
     */
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
        checkEditLoan.setSelected(false);
        setTableProperties();
    }

    /**
     *  Set the <code>editBookTable</code> row and column properties.
     *
     *  <p>Sets row count to zero, then adds six new rows. Fixes first column
     *  width.
     */
    public static void setTableProperties() {
        model.setRowCount(0);
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        model.addRow(new Object[]{"", ""});
        editBookTable.getColumnModel().getColumn(0).setMinWidth(205);
        editBookTable.getColumnModel().getColumn(0).setMaxWidth(205);
    }

    /**
     *  Fills the <code>editBookTable</code> if a book is selected.
     *
     *  <p>Calls <code>setTableProperties</code> to make six empty rows in
     *  the <code>editBookTable</code>. Unless no book is selected, enters all
     *  book data in the table.
     */
    public static void fillEditBookTable() {
        setTableProperties();

        if (id != 0) {
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

    /**
     *  Edit selected author.
     *
     *  <p>Stores user input two separate strings, then splits combobox
     *  selection and removes anything that is not the name. Then compares the
     *  new author name with the old one. Also ensures that the author is not
     *  already stored in the database.
     *
     *  <p>Notifies user of all possible errors:
     *  <ul>
     *  <li>Author already exists</li>
     *  <li>First or last name is missing</li>
     *  </ul>
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

    /**
     *  Edit selected category.
     *
     *  <p>Stores user input and combobox selection in two separate strings,
     *  then compares the new category name with the old one. Also ensures that
     *  there is no blank spaces in the new name, and that the attempted
     *  category name is not already stored in the database.
     *
     *  <p>Notifies user of all possible errors:
     *  <ul>
     *  <li>Category already exists</li>
     *  <li>Name contains spaces</li>
     *  <li>No category name was given</li>
     *  </ul>
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
                    txtEditCategory.setText("");
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

    /**
     *  Deletes selected category from the <code>editBookTable</code>.
     *
     *  <p>A category must be selected by clicking the desired row in the
     *  table. If no category has been selected, notifies user with a popup.
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

    /**
     *  Deletes selected author from the <code>editBookTable</code>.
     *
     *  <p>An author must be selected by clicking the desired row in the
     *  <code>browseTable</code>. If no author has been selected, notifies
     *  user with a popup.
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

    /**
     *  Deletes the selected book.
     *
     *  <p>A book must be selected by clicking the desired row in the
     *  <code>browseTable</code>. If no book is selected, notifies the user
     *  with a popup.
     */
    public static void deleteBook() {
        if (id != 0) {
            Book b = new Book();
            b.deleteBook(id);
            clearEditBoxes();
        } else {
            JOptionPane.showMessageDialog(null, "Valitse ensin poistettava kirja selaus-taulukosta", "Virhe poistettaessa", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *  Update an existing book with new information.
     *
     *  <p>Creates a new Book object, and fetches the existing data by book id.
     *  Then stores all new data into variables, and creates empty arraylists
     *  for authors and categories. Then proceeds to set all new data to the
     *  book object. If the user has checked the <code>checkEditLoan</code>
     *  checkbox, also sets the loaner name. Otherwise, resets the loaner name
     *  to empty.
     *
     *  <p>Clears the original authors and categories from the book object,
     *  then checks the <code>editBookTable</code> for new data, entering all
     *  new authors and categories to respective lists. Then checks whether the
     *  given publication year fits the standard. If it does, proceeds to check
     *  that the new book name is not an empty string. If not, it loops through
     *  all books by all authors associated with this new book, and checks
     *  that none of the authors already has the book. Upon completion, clears
     *  all text fields, resets the checkbox and the
     *  <code>editBookTable</code>.
     *
     *  <p>Possible errors:
     *  <ul>
     *  <li>Book already exists</li>
     *  <li>Given book name was empty</li>
     *  </ul>
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

    /**
     *  Add selected author to the <code>editBookTable</code>.
     *
     *  <p>A book must be selected in the <code>browseTable</code>. The
     *  <code>editBookTable</code> is then populated with the author(s) and
     *  categories associated with it.
     *
     *  <p>Notifies user of possible errors:
     *  <ul>
     *  <li>Author is already in the table</li>
     *  <li>No book has been selected from the <code>browseTable</code></li>
     *  <li>No author has been selected from the combobox</li>
     *  </ul>
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

    /**
     *  Add selected category to <code>editBookTable</code>.
     *
     *  <p>A book must be selected in the <code>browseTable</code>. The
     *  <code>editBookTable</code> will be populated with the author(s) and
     *  categories associated with it.
     *
     *  <p>Notifies user of possible errors:
     *  <ul>
     *  <li>Category is already in the table</li>
     *  <li>No book has been selected in the <code>browseTable</code></li>
     *  <li>No category has been selected from the combobox</li>
     *  </ul>
     */
    public static void addCategoryToEditTable() {
        try {
            String category = cBoxEditBookCategory.getSelectedItem().toString();
            if (editBookTable.getRowCount() > 1) {
                for (int i = 0; i < 6; i++) {
                    String value = editBookTable.getValueAt(i, 1).toString();
                    if (value.equalsIgnoreCase(category)) {
                        JOptionPane.showMessageDialog(null, "Kategoria löytyy jo taulukosta", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
                        i = 6;
                    } else if (value.length() < 1) {
                        editBookTable.setValueAt(category, i, 1);
                        i = 6;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Valitse ensin selausvalikosta muokattava kirja", "Virhe", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valitse pudotusvalikosta lisättävä kategoria", "Virhe lisätessä", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *  Fill author name text fields for easy editing.
     *
     *  <p>Stores chosen combobox information in a string, splits it and
     *  removes spaces and non-alphabetic characters, then puts the names in
     *  the text fields to allow easy editing.
     */
    public static void getEditAuthor() {
        try {
            String author = cBoxEditAuthor.getSelectedItem().toString();
            String[] authName = author.split(", ");
            String lName = authName[0];
            String fName = authName[1];
            txtEditAuthorLastName.setText(lName);
            txtEditAuthorName.setText(fName);
        } catch (Exception e) {}
    }

    /**
     *  Fill category text field for easy editing.
     *
     *  <p>Stores chosen combobox information in a string and puts it in the
     *  category text field to allow easy editing.
     */
    public static void getEditCategory() {
        try {
            String category = cBoxEditCategory.getSelectedItem().toString();
            txtEditCategory.setText(category);
        } catch (Exception e) {}
    }
}