package wheresmymoney.category;

import java.util.HashMap;

public class CategorySpendingLimit {
    private HashMap<String, Float> categoryLimits;
    
    public CategorySpendingLimit() {
        categoryLimits = new HashMap<>();
    }
    
    public boolean isEmpty() {
        return categoryLimits.isEmpty();
    }
    public int size() {
        return categoryLimits.size();
    }
    
    public Float getLimitFor(String category) {
        return categoryLimits.get(category);
    }
    public void setLimitFor(String category, Float spendingLimit) {
        categoryLimits.put(category, spendingLimit);
    }
}
