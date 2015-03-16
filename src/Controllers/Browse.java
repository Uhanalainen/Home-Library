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
import static GUI.Menu.browseTable;
import static GUI.Menu.filterAuthor;
import static GUI.Menu.sorter;
import java.util.ArrayList;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Juha
 */
public class Browse extends Menu {
    
    /*
    *   Method used to fill the browse table. First, it fetches all books in the database.
    *   Then, it sets the row count for the "mod" table model to 0 (this is a way of clearing
    *   the table of old information). After that, I use a for-each loop which loops through
    *   every book in the arraylist, adding the authors and categories to it, then adding
    *   all this information row by row to the table. At last, I've set some parameters for
    *   table column widths, and centered some of the table column names.
    */
    public static void fillTable() {
        ArrayList<Book> books = Book.getBooks();
        mod.setRowCount(0);

        for (Book b : books) {
            String aName = "";
            b.getAuthors();
            int aCount = 1;
            for (Author a : b.author) {
                aName += a.getLastName() + ", " + a.getName();
                aName += aCount < b.author.size() ? " - " : "";
                aCount++;
            }

            String cat = "";
            b.getCategories();
            int bCount = 1;
            for (Category c : b.category) {
                cat += c.getName();
                cat += bCount < b.category.size() ? " - " : "";
                bCount++;
            }
            mod.addRow(new Object[]{b.getId(), aName, b.getName(), b.getPubYear(), cat, b.getOnLoan(), b.getLoaner(), b.getOrigName()});
        }

        browseTable.getColumnModel().getColumn(0).setMaxWidth(40);
        browseTable.getColumnModel().getColumn(1).setMinWidth(220);
        browseTable.getColumnModel().getColumn(2).setMinWidth(200);
        browseTable.getColumnModel().getColumn(2).setMaxWidth(200);
        browseTable.getColumnModel().getColumn(3).setMinWidth(90);
        browseTable.getColumnModel().getColumn(3).setMaxWidth(90);
        browseTable.getColumnModel().getColumn(4).setMinWidth(160);

        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        browseTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        browseTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        browseTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }
    
    /*
    * A simple search filter that enables on-the-fly searching in the
    * browse table. Makes searching really easy, as it checks every cell
    * and filters the table based on user search input. I chose this instead
    * of separate searching possibilities (search by author, book name, year etc)
    * as I thought this was neater and easier to develop.
    */
    public static void newFilter() {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter("(?i)" + filterAuthor.getText());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
}
