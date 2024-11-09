package wheresmymoney;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import wheresmymoney.category.CategoryData;
import wheresmymoney.category.CategoryTotal;
import wheresmymoney.exception.WheresMyMoneyException;

class CategoryTotalTest {
    
    @Test
    void getCategoryDataOf_categoryInMap_returnsCategoryData() {
        CategoryTotal tracker = new CategoryTotal();
        try {
            tracker.addCategory("category", 0.00F);
            
            CategoryData actual = tracker.getCategoryDataOf("category");
            CategoryData expected = new CategoryData(0.00F, 100.00F);
            
            assertEquals(expected.getCurrExpenditure(), actual.getCurrExpenditure());
            assertEquals(expected.getMaxExpenditure(), actual.getMaxExpenditure());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when Category and Price are valid.");
        }
    }
    @Test
    void getCategoryDataOf_categoryNotInMap_throwsWheresMyMoneyException() {
        CategoryTotal tracker = new CategoryTotal();
        assertThrows(WheresMyMoneyException.class,
                () -> tracker.getCategoryDataOf("category"));
    }
    
    @Test
    void addCategory_categoryInMap_incrementsThatCategoryCurrExpenditure() {
        CategoryTotal tracker = new CategoryTotal();
        try {
            tracker.addCategory("category", 23.00F);
            
            assertEquals(1, tracker.size());
            tracker.addCategory("category", 46.00F);
            assertEquals(1, tracker.size());
            
            Float newCurrExpenditure = tracker.getCategoryDataOf("category").getCurrExpenditure();
            assertEquals(69.00F, newCurrExpenditure);
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    @Test
    void addCategory_categoryNotInMap_categoryAddedToMap() {
        CategoryTotal tracker = new CategoryTotal();
        try {
            assertEquals(0, tracker.size());
            assertFalse(tracker.contains("category"));
            
            tracker.addCategory("category", 10.00F);
            
            assertEquals(1, tracker.size());
            assertTrue(tracker.contains("category"));
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    
    @Test
    void deleteCategory_categoryInMap_decrementsCurrExpenditure() {
        CategoryTotal tracker = new CategoryTotal();
        try {
            tracker.addCategory("category", 100.00F);
            
            assertEquals(1, tracker.size());
            tracker.deleteCategory("category", 31.00F);
            assertEquals(1, tracker.size());
            
            Float newCurrExpenditure = tracker.getCategoryDataOf("category").getCurrExpenditure();
            assertEquals(69.00F, newCurrExpenditure);
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    @Test
    void deleteCategory_categoryInMap_removesCategoryFromMap() {
        CategoryTotal tracker = new CategoryTotal();
        try {
            tracker.addCategory("category", 100.00F);
            
            assertEquals(1, tracker.size());
            assertTrue(tracker.contains("category"));
            
            tracker.deleteCategory("category", 100.00F);
            
            assertEquals(0, tracker.size());
            assertFalse(tracker.contains("category"));
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    @Test
    void deleteCategory_categoryNotInMap_throwsWheresMyMoneyException() {
        CategoryTotal tracker = new CategoryTotal();
        assertThrows(WheresMyMoneyException.class,
                () -> tracker.deleteCategory("category", 420.00F));
    }
    
}
