package wheresmymoney.category;

import java.util.HashMap;

import wheresmymoney.exception.WheresMyMoneyException;

public class CategoryLimitTracker {
    public static final Float DEFAULT_LIMIT = 100F;
    private HashMap<String, Float> categoryLimits;
    
    public CategoryLimitTracker() {
        categoryLimits = new HashMap<>();
    }
    public HashMap<String, Float> getLimits() {
        return categoryLimits;
    }
    public boolean isEmpty() {
        return categoryLimits.isEmpty();
    }
    public int size() {
        return categoryLimits.size();
    }
    public boolean contains(String category) {
        return categoryLimits.containsKey(category);
    }
    
    public Float getLimitFor(String category) {
        if (!categoryLimits.containsKey(category)) {
            throw new WheresMyMoneyException("Limit cannot be found as no such category exists.");
        }
        return categoryLimits.get(category);
    }
    public void setDefaultLimitFor(String category) {
        categoryLimits.put(category, DEFAULT_LIMIT);
    }
    public void setCustomLimitFor(String category, Float spendingLimit) {
        if (spendingLimit < 0) {
            throw new WheresMyMoneyException("Limit should not be negative.");
        }
        categoryLimits.put(category, spendingLimit);
    }
    public void removeLimitFor(String category) {
        categoryLimits.remove(category);
    }
}
