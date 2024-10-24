package wheresmymoney;

import org.junit.jupiter.api.Test;
import wheresmymoney.exception.WheresMyMoneyException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void setPrice_nullInput_throwsWheresMyMoneyException() {
        Expense expense = new Expense(0.0F, "desc", "category");
        assertThrows(WheresMyMoneyException.class,
                () -> expense.setPrice(null));
    }

    @Test
    public void setDescription_nullInput_throwsWheresMyMoneyException() {
        Expense expense = new Expense(0.0F, "desc", "category");
        assertThrows(WheresMyMoneyException.class,
                () -> expense.setDescription(null));
    }

    @Test
    public void setCategory_nullInput_throwsWheresMyMoneyException() {
        Expense expense = new Expense(0.0F, "desc", "category");
        assertThrows(WheresMyMoneyException.class,
                () -> expense.setCategory(null));
    }
}
