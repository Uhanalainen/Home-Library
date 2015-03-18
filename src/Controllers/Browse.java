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

    /**
     *  Fill browse-panel table with data on all books and authors.
     * 
     *  <p>Fetches an arraylist of all books, then resets the row count to
     *  zero. Gets all authors and all categories for every book and then
     *  stores the information in the <code>browseTable</code>.
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
        
        setBrowseTableProperties();        
    }
    
    /**
     *  Sets the column widths and centers some cell header texts.
     * 
     *  <p>Used for setting minimum and maximum column width to ensure that all
     *  data is visible at all times even when the window has been resized.
     *  Also centers some of the smaller cell header texts.
     */
    public static void setBrowseTableProperties() {
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
    
    /**
     *  Search filter that enables on-the-fly filtering of the data in the
     *  browse table.
     * 
     *  <p>Simplifies searching by monitoring every cell and filtering the
     *  table based on user search input.
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
