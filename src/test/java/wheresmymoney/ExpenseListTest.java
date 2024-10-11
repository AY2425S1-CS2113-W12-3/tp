package wheresmymoney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class ExpenseListTest {

    @Test
    public void getList_notNullInput_expenseListIsNotNull() {
        ExpenseList expenseList = new ExpenseList();
        assertNotNull(expenseList);
    }
    @Test
    public void getTotal_emptyExpenseList_totalIsZero() {
        ExpenseList expenseList = new ExpenseList();
        assertEquals(0, expenseList.getTotal());
    }
    @Test
    public void isEmpty_emptyExpenseList_returnTrue() {
        ExpenseList expenseList = new ExpenseList();
        assertEquals(true, expenseList.isEmpty());
    }
    @Test
    public void getExpenseAtIndex_indexIsOutOfBounds_throwsIndexOutOfBoundsException() {
        ExpenseList expenseList = new ExpenseList();
        assertThrows(IndexOutOfBoundsException.class,
                () -> expenseList.getExpenseAtIndex(-1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> expenseList.getExpenseAtIndex(0));
        assertThrows(IndexOutOfBoundsException.class,
                () -> expenseList.getExpenseAtIndex(1));
    }
    @Test
    public void getIndexOf_expenseNotInList_indexIsMinusOne() {
        ExpenseList expenseList = new ExpenseList();
        Expense expense = new Expense(0F, "", "");
        assertEquals(-1, expenseList.getIndexOf(expense));
    }

    @Test
    public void deleteExpense_normalList_reduceListSize() {
        ExpenseList expenseList = new ExpenseList();
        expenseList.addExpense(1.00f, "Ice Cream", "Food");
        assertEquals(1,  expenseList.getTotal());
        try {
            expenseList.deleteExpense(0);
        } catch (WheresMyMoneyException e) {
            fail();
        }
        assertEquals(0,  expenseList.getTotal());
    }

    @Test
    public void deleteExpense_emptyList_throwsWheresMyMoneyException() {
        ExpenseList expenseList = new ExpenseList();
        assertThrows(WheresMyMoneyException.class,
                () -> expenseList.deleteExpense(1));
    }

}
