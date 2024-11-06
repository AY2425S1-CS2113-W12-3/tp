package wheresmymoney;

import org.junit.jupiter.api.Test;
import wheresmymoney.exception.WheresMyMoneyException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ExpenseListTest {

    @Test
    public void getList_notNullInput_expenseExpenseListIsNotNull() {
        ExpenseList expenseList = new ExpenseList();
        assertNotNull(expenseList);
    }
    @Test
    public void getTotal_expenseList_correctTotal() {
        try {
            ExpenseList expenseList = new ExpenseList();
            assertEquals(0, expenseList.getTotal());
            expenseList.addExpense(0.0F, "desc", "category", "25-10-2024");
            assertEquals(1, expenseList.getTotal());
            expenseList.deleteExpense(0);
            assertEquals(0, expenseList.getTotal());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when Expense parameters and list index are valid.");
        }
    }
    @Test
    public void isEmpty_expenseList_correctBoolean() {
        try {
            ExpenseList expenseList = new ExpenseList();
            assertTrue(expenseList.isEmpty());
            expenseList.addExpense(0.0F, "desc", "category", "25-10-2024");
            assertFalse(expenseList.isEmpty());
            expenseList.deleteExpense(0);
            assertTrue(expenseList.isEmpty());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when Expense parameters and list index are valid.");
        }
    }

    @Test
    public void getExpenseAtIndex_indexIsOutOfBounds_throwsWheresMyMoneyException() {
        ExpenseList expenseList = new ExpenseList();
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.getExpenseAtIndex(-1));
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.getExpenseAtIndex(0));
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.getExpenseAtIndex(1));
    }
    @Test
    public void getIndexOf_expenseNotInList_throwsWheresMyMoneyException() {
        ExpenseList expenseList = new ExpenseList();
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.getIndexOf(
                        new Expense(0.0F, "desc", "category", "25-10-2024")));
    }

    @Test
    public void addExpense_validExpenseWithDateSpecified_success() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc", "cat", "25-10-2024");
            Expense expense = expenseList.getExpenseAtIndex(0);
            assertEquals(0.01F, expense.getPrice());
            assertEquals("desc", expense.getDescription());
            assertEquals("cat", expense.getCategory());
            assertEquals("25-10-2024", DateUtils.dateFormatToString(expense.getDateAdded()));
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when all expense parameters are valid.");
        }
    }
    @Test
    public void addExpense_validExpenseWithDateUnspecified_success() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc", "cat");
            Expense expense = expenseList.getExpenseAtIndex(0);
            assertEquals(0.01F, expense.getPrice());
            assertEquals("desc", expense.getDescription());
            assertEquals("cat", expense.getCategory());
            assertEquals(DateUtils.getCurrentDate(), expense.getDateAdded());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when all expense parameters are valid.");
        }
    }
    @Test
    public void addExpense_invalidExpenseParameters_throwsWheresMyMoneyException() {
        ExpenseList expenseList = new ExpenseList();
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.addExpense(null, "desc", "category", "25-10-2024"));
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.addExpense(0.0F, null, "category", "25-10-2024"));
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.addExpense(0.0F, "desc", null, "25-10-2024"));
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.addExpense(0.0F, "desc", "category", null));
    }

    @Test
    public void editExpense_editPriceOnly_success() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc", "cat", "01-01-2024");
            expenseList.editExpense(0, 1.00F, null, null, null);
            Expense expense = expenseList.getExpenseAtIndex(0);
            assertEquals(1.00F, expense.getPrice());
            assertEquals("desc", expense.getDescription());
            assertEquals("cat", expense.getCategory());
            assertEquals(DateUtils.stringToDate("01-01-2024"), expense.getDateAdded());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when only price was edited.");
        }
    }

    @Test
    public void editExpense_editDescriptionOnly_success() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc", "cat", "01-01-2024");
            expenseList.editExpense(0, null, "desc1", null, null);
            Expense expense = expenseList.getExpenseAtIndex(0);
            assertEquals(0.01F, expense.getPrice());
            assertEquals("desc1", expense.getDescription());
            assertEquals("cat", expense.getCategory());
            assertEquals(DateUtils.stringToDate("01-01-2024"), expense.getDateAdded());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when only description was edited.");
        }
    }

    @Test
    public void editExpense_editCategoryOnly_success() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc", "cat", "01-01-2024");
            expenseList.editExpense(0, null, null, "cat1", null);
            Expense expense = expenseList.getExpenseAtIndex(0);
            assertEquals(0.01F, expense.getPrice());
            assertEquals("desc", expense.getDescription());
            assertEquals("cat1", expense.getCategory());
            assertEquals(DateUtils.stringToDate("01-01-2024"), expense.getDateAdded());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when only category was edited.");
        }
    }

    @Test
    public void editExpense_editDateAddedOnly_success() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc", "cat", "01-01-2024");
            expenseList.editExpense(0, null, null, null, "15-10-2024");
            Expense expense = expenseList.getExpenseAtIndex(0);
            assertEquals(0.01F, expense.getPrice());
            assertEquals("desc", expense.getDescription());
            assertEquals("cat", expense.getCategory());
            assertEquals(DateUtils.stringToDate("15-10-2024"), expense.getDateAdded());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when only description was edited.");
        }
    }

    @Test
    public void deleteExpense_nonemptyList_reduceListSize() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc", "cat");
            assertEquals(1,  expenseList.getTotal());
            expenseList.deleteExpense(0);
            assertEquals(0, expenseList.getTotal());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when Expense parameters and list index are valid.");
        }
    }

    /**
     * Tests the invalid deletion of an expense from an empty expense list.
     */
    @Test
    public void deleteExpense_emptyList_throwsWheresMyMoneyException() {
        ExpenseList expenseList = new ExpenseList();
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.deleteExpense(1));
    }

    @Test
    public void listByCategory_validListByCategory_success() {
        ExpenseList expenseList = new ExpenseList();
        try {
            expenseList.addExpense(0.01F, "desc1", "cat1");
            expenseList.addExpense(0.02F, "desc2", "cat2");
            assertEquals(1, expenseList.listByCategory("cat1").size());
            Expense expenseToFindInList = new Expense(0.03F, "desc1", "cat1");
            Expense expenseOfSameCategory = expenseList.listByCategory("cat1").get(0);
            assertEquals(expenseToFindInList.getDescription(), expenseOfSameCategory.getDescription());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when Expenses' parameters are valid.");
        }
    }
    @Test
    public void listByCategory_emptyListByCategory_success() {
        ExpenseList expenseList = new ExpenseList();
        ArrayList<Expense> testArrayList = new ArrayList<>();
        assertEquals(testArrayList, expenseList.listByCategory("cat"));
    }
    @Test
    public void listByCategory_noMatchForCategory_success() {
        ExpenseList expenseList = new ExpenseList();
        ArrayList<Expense> testArrayList = new ArrayList<>();
        try {
            expenseList.addExpense(0.01F, "desc1", "cat1");
            expenseList.addExpense(0.02F, "desc2", "cat2");
            assertEquals(testArrayList, expenseList.listByCategory("cat"));
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when Expenses' parameters are valid.");
        }
    }

}
