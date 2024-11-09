package wheresmymoney;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import wheresmymoney.category.CategoryData;
import wheresmymoney.category.CategoryTotalTracker;
import wheresmymoney.exception.WheresMyMoneyException;

class CategoryTotalTrackerTest {
    
    @Test
    void getCategoryDataOf_categoryInMap_returnsCategoryData() {
        CategoryTotalTracker tracker = new CategoryTotalTracker();
        try {
            tracker.increaseTotalBy("category", 0.00F);
            
            CategoryData actual = tracker.getTotalFor("category");
            CategoryData expected = new CategoryData(0.00F, 100.00F);
            
            assertEquals(expected.getCurrExpenditure(), actual.getCurrExpenditure());
            assertEquals(expected.getMaxExpenditure(), actual.getMaxExpenditure());
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when Category and Price are valid.");
        }
    }
    @Test
    void getCategoryDataOf_categoryNotInMap_throwsWheresMyMoneyException() {
        CategoryTotalTracker tracker = new CategoryTotalTracker();
        assertThrows(WheresMyMoneyException.class,
                () -> tracker.getTotalFor("category"));
    }
    
    @Test
    void increaseTotalByCurrExpenditure() {
        CategoryTotalTracker tracker = new CategoryTotalTracker();
        try {
            tracker.increaseTotalBy("category", 23.00F);
            
            assertEquals(1, tracker.size());
            tracker.increaseTotalBy("category", 46.00F);
            assertEquals(1, tracker.size());
            
            Float newCurrExpenditure = tracker.getTotalFor("category").getCurrExpenditure();
            assertEquals(69.00F, newCurrExpenditure);
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    @Test
    void increaseTotalByAddedToMap() {
        CategoryTotalTracker tracker = new CategoryTotalTracker();
        try {
            assertEquals(0, tracker.size());
            assertFalse(tracker.contains("category"));
            
            tracker.increaseTotalBy("category", 10.00F);
            
            assertEquals(1, tracker.size());
            assertTrue(tracker.contains("category"));
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    
    @Test
    void decreaseTotalByInMap_decrementsCurrExpenditure() {
        CategoryTotalTracker tracker = new CategoryTotalTracker();
        try {
            tracker.increaseTotalBy("category", 100.00F);
            
            assertEquals(1, tracker.size());
            tracker.decreaseTotalBy("category", 31.00F);
            assertEquals(1, tracker.size());
            
            Float newCurrExpenditure = tracker.getTotalFor("category").getCurrExpenditure();
            assertEquals(69.00F, newCurrExpenditure);
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    @Test
    void decreaseTotalByFromMap() {
        CategoryTotalTracker tracker = new CategoryTotalTracker();
        try {
            tracker.increaseTotalBy("category", 100.00F);
            
            assertEquals(1, tracker.size());
            assertTrue(tracker.contains("category"));
            
            tracker.decreaseTotalBy("category", 100.00F);
            
            assertEquals(0, tracker.size());
            assertFalse(tracker.contains("category"));
        } catch (WheresMyMoneyException e) {
            fail("Exception thrown when inputs are not null.");
        }
    }
    @Test
    void decreaseTotalByNotInMap_throwsWheresMyMoneyException() {
        CategoryTotalTracker tracker = new CategoryTotalTracker();
        assertThrows(WheresMyMoneyException.class,
                () -> tracker.decreaseTotalBy("category", 420.00F));
    }
    
}
