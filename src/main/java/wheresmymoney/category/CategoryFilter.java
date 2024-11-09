package wheresmymoney.category;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import wheresmymoney.exception.WheresMyMoneyException;

public class CategoryFilter {
    private CategoryFacade categoryFacade;
    private PriorityQueue<Map.Entry<String, CategoryData>> exceededCategories;
    private PriorityQueue<Map.Entry<String, CategoryData>> nearingCategories;
    
    public void setCategoryFacade(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }
    
    private Float[] getTotalAndLimit(String category) {
        Float currTotal = categoryFacade.getMonthlyTotalTracker().getCurrMonthTotalFor(category);
        Float spendingLimit = categoryFacade.getCategoryLimitTracker().getLimitFor(category);
        return new Float[] {currTotal, spendingLimit};
    }
    /**
     * Checks if the expenditure for this category is nearing its spending limit.
     * Nearing the limit is defined as the current total expenditure exceeding
     * 80% of its spending limit.
     *
     * @return {@code true} if the current total expenditure is at least 80% of the spending limit,
     *         {@code false} otherwise.
     */
    private boolean isNearingLimit(Float currTotal, Float spendingLimit) {
        return Float.compare(currTotal, 0.80F * spendingLimit) >= 0;
    }
    /**
     * Checks if the current total expenditure for this category has exceeded its spending limit.
     *
     * @return {@code true} if the current total expenditure exceeds the spending limit,
     *         {@code false} otherwise.
     */
    private boolean hasExceededLimit(Float currTotal, Float spendingLimit) {
        return Float.compare(currTotal, spendingLimit) > 0;
    }
    /**
     * Checks if the expenditure for a given category is nearing or exceeding the spending limit.
     * If the limit is exceeded, an alert message is printed.
     * If the spending is nearing the limit, a warning message is printed.
     *
     * @param category The name of the category to check.
     * @throws WheresMyMoneyException If the category does not exist in the tracker.
     */
    public void checkLimitFor(String category) throws WheresMyMoneyException {
        Float[] spendingData = getTotalAndLimit(category);
        Float currTotal = spendingData[0];
        Float spendingLimit = spendingData[1];
        boolean hasExceededLimit = hasExceededLimit(currTotal, spendingLimit);
        boolean isNearingLimit = isNearingLimit(currTotal, spendingLimit);
        if (hasExceededLimit) {
            System.out.println("Alert! You have exceeded the spending limit of " + spendingLimit +
                    " for the category of " + category + ", with a total expenditure of " + currTotal + ". ");
        } else if (isNearingLimit) {
            System.out.println("Warning! You are close to the spending limit of " + spendingLimit +
                    " for the category of " + category  + ", with a total expenditure of " + currTotal + ". ");
        }
    }
    
    /**
     * Filters categories from the provided {@code CategoryTracker} into two heaps.
     * <p>
     * The heaps are:
     * <li> For categories that have exceeded their spending limits. <li/>
     * For categories that are close to, but not exceeded, their spending limits.
     * </p>
     */
    public void getCategoriesFiltered() {
        HashMap<String, Float> totalTracker = categoryFacade.getMonthlyTotalTracker().getCurrMonthTotalFor();
        HashMap<String, Float> limitTracker = categoryFacade.getCategoryLimitTracker().getLimits();
        exceededCategories = new PriorityQueue<>((f1, f2) -> Float.compare(f2, f1) );
        nearingCategories = initMaxHeap(); // ?
        for (Map.Entry<String, CategoryData> entry : limitTracker.entrySet()) {
            CategoryData categoryData = entry.getValue();
            if (categoryData.hasExceededLimit()) {
                exceededCategories.add(entry);
            } else if (categoryData.isNearingLimit()) {
                nearingCategories.add(entry);
            }
        }
    }
    /**
     * Displays the categories in the provided category heap.
     * <p>
     * If the queue is not empty, it prints the provided {@code messageIfFound},
     * then a list of categories, each with its current and maximum expenditures.
     * If the queue is empty, it prints {@code messageIfNoneFound}.
     * </p>
     *
     * @param filtered the {@code PriorityQueue} containing the filtered categories to display.
     * @param messageIfFound the message to display if the queue contains categories.
     * @param messageIfNoneFound the message to display if the queue is empty.
     */
    private void displayFilteredCategories(PriorityQueue<Map.Entry<String, Float>> filtered,
                                          String messageIfFound, String messageIfNoneFound) {
        if (!filtered.isEmpty()) {
            System.out.println(messageIfFound);
            int index = 1;
            while (!filtered.isEmpty()) {
                Map.Entry<String, CategoryData> entry = filtered.poll();
                System.out.println(index + ". " + entry.getKey() + ", totalling to " +
                        entry.getValue().getCurrExpenditure() + ", exceeds " + entry.getValue().getMaxExpenditure());
                index++;
            }
        } else {
            System.out.println(messageIfNoneFound);
        }
    }
    /**
     * Displays the categories that have exceeded their spending limits.
     * If there are such categories, they are listed in descending order of current expenditure.
     * Otherwise, a message indicating that no categories have exceeded their limits is displayed.
     */
    public void displayExceededCategories() {
        displayFilteredCategories(exceededCategories,
                "These categories have exceeded their spending limits: ",
                "None of the categories have exceeded their respective spending limits.");

    }
    /**
     * Displays the categories that are nearing, but not exceeded, their spending limits.
     * If there are such categories, they are listed in descending order of current expenditure.
     * Otherwise, a message indicating that no categories are nearing their limits is displayed.
     */
    public void displayNearingCategories() {
        displayFilteredCategories(exceededCategories,
                "These categories are close to their spending limits: ",
                "None of the categories were close to their respective spending limits.");
    }
}
