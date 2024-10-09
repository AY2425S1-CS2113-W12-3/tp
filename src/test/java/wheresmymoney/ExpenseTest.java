package wheresmymoney;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class ExpenseTest {
    private Expense initNullExpense() {
        return new Expense(null, null, null);
    }
    @Test
    public void getPrice_nullInput_priceIsNull() {
        Expense expense = initNullExpense();
        assertNull(expense.getPrice());
    }
    @Test
    public void getDescription_nullInput_descIsNull() {
        Expense expense = initNullExpense();
        assertNull(expense.getDescription());
    }
    @Test
    public void getCategory_nullInput_categoryIsNull() {
        Expense expense = initNullExpense();
        assertNull(expense.getCategory());
    }
    @Test
    public void setPrice_nullInput_priceIsNull() {
        Expense expense = new Expense(0.0F, "desc", "category");
        expense.setPrice(null);
        assertNull(expense.getPrice());
    }
    @Test
    public void setDescription_nullInput_descIsNull() {
        Expense expense = new Expense(0.0F, "desc", "category");
        expense.setDescription(null);
        assertNull(expense.getDescription());
    }
    @Test
    public void setCategory_nullInput_categoryIsNull() {
        Expense expense = new Expense(0.0F, "desc", "category");
        expense.setCategory(null);
        assertNull(expense.getCategory());
    }
}
