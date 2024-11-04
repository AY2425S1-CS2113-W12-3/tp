package wheresmymoney.category;

import java.time.LocalDate;
import java.util.HashMap;

public class CategoryManager {
    private HashMap<LocalDate, CategoryTracker> trackerManager;
    
    // wrapper to Facade?
    
    public CategoryManager() {
        this.trackerManager = new HashMap<>();
    }
    public HashMap<LocalDate, CategoryTracker> getTrackerManager() {
        return trackerManager;
    }
    public int size() {
        return trackerManager.size();
    }
    public boolean isEmpty() {
        return trackerManager.isEmpty();
    }
    public boolean containsKey(LocalDate localDate) {
        return trackerManager.containsKey(localDate);
    }
    
    public CategoryTracker getTracker(LocalDate localDate) {
        return trackerManager.get(localDate);
    }
    public void insertIntoTracker(LocalDate localDate) {
        trackerManager.put(localDate, new CategoryTracker());
    }
    public void removeFromTracker(LocalDate localDate) {
        trackerManager.remove(localDate);
    }
    
}
