package wheresmymoney.visualizer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

import wheresmymoney.DateUtils;
import wheresmymoney.Expense;
import wheresmymoney.exception.WheresMyMoneyException;

import javax.swing.JFrame;


public class Visualizer {
    private static final int DAY_VISUALIZER_THRESHOLD = 32;
    private static final int MONTH_VISUALIZER_THRESHOLD = 1080;
    private ArrayList<Expense> expenses;
    private LocalDate beginDate;
    private LocalDate endDate;
    private int dateRange;
    private List<String> timeSeries = new ArrayList<>();
    private List<Float> valueSeries = new ArrayList<>();


    public Visualizer(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    private void getBeginDate() {
        beginDate = expenses.get(0).getDateAdded();
        for (Expense expense : expenses) {
            if (expense.getDateAdded().isBefore(beginDate)) {
                beginDate = expense.getDateAdded();
            }
        }
    }

    private void getEndDate(){
        endDate = expenses.get(0).getDateAdded();
        for (Expense expense : expenses) {
            if (expense.getDateAdded().isAfter(endDate)) {
                endDate = expense.getDateAdded();
            }
        }
    }

    private void getTimeRange() {
        assert (beginDate != null && endDate != null);
        dateRange = (int) ChronoUnit.DAYS.between(beginDate, endDate);
    }

    private List<String> createDateList () {
        List<LocalDate> localDateList = beginDate.datesUntil(endDate.plusDays(1)).toList();
        timeSeries = localDateList.stream()
                .map(DateUtils::dateFormatToString).toList();
        for (Expense e : expenses) {
            assert (timeSeries.contains(DateUtils.dateFormatToString(e.getDateAdded())));
        }
        return timeSeries;
    }

    private List<String> createMonthList () { // Fix implementation!!
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

        LocalDate current = beginDate.withDayOfMonth(1);
        LocalDate end = endDate.withDayOfMonth(1);

        while (!current.isAfter(end)) {
            timeSeries.add(current.format(formatter));
            current = current.plusMonths(1);
        }
        return timeSeries;
    }

    private HashMap<String, Float> groupPriceByDay () {
        HashMap<String, Float> dateToExpenseMap = new HashMap<>();

        // Initialize to give all dates a starting expenditure of 0
        for (String date : timeSeries) {
            dateToExpenseMap.put(date, 0.0f);
        }

        for (Expense e : expenses) {
            String dateKey = DateUtils.dateFormatToString(e.getDateAdded());

            // Increment the spending
            dateToExpenseMap.put(dateKey, dateToExpenseMap.getOrDefault(dateKey, 0.0f) + e.getPrice());
        }
        return dateToExpenseMap;
    }

    private HashMap<String, Float> groupPriceByMonth () {
        HashMap<String, Float> dateToExpenseMap = new HashMap<>();

        for (String month : timeSeries) {
            dateToExpenseMap.put(month, 0.0f);
        }
        for (Expense e : expenses) {
            String dateKey = e.getDateAdded().getMonthValue() + "-" + e.getDateAdded().getYear();
            if (e.getDateAdded().getMonthValue() < 10) { // One-digit month
                dateKey = "0" + dateKey;
            }

            dateToExpenseMap.put(dateKey, dateToExpenseMap.getOrDefault(dateKey, 0.0f) + e.getPrice());
        }
        return dateToExpenseMap;
    }

    public void processChartData() throws WheresMyMoneyException {
        getBeginDate();
        getEndDate();
        getTimeRange();

        if (dateRange > MONTH_VISUALIZER_THRESHOLD) {
            throw new WheresMyMoneyException("This date range is too large to display!");
        }

        // Group expenses by day or month based on time range
        Map<String, Float> dateToExpenseMap;

        if (dateRange <= DAY_VISUALIZER_THRESHOLD) {  // For short durations (less than ~one month)
            timeSeries = createDateList();
            dateToExpenseMap = groupPriceByDay();
        } else {
            timeSeries = createMonthList();
            dateToExpenseMap = groupPriceByMonth();
        }

        // Sort by date for clear plotting
        valueSeries = timeSeries.stream().map(dateToExpenseMap::get).collect(Collectors.toList());
    }

    public void drawChart() {
        // Create chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(900)
                .height(650)
                .title("Spending Chart")
                .xAxisTitle(dateRange <= DAY_VISUALIZER_THRESHOLD ? "Day" : "Month")
                .yAxisTitle("Total Spending")
                .build();

        // Customize chart
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setPlotGridLinesVisible(true);
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler().setXAxisTickMarkSpacingHint(150);

        // Add data to chart
        chart.addSeries("Spending", timeSeries, valueSeries);

        // Display chart
        SwingWrapper<CategoryChart> swingWrapper = new SwingWrapper<>(chart);
        JFrame chartFrame = swingWrapper.displayChart();
        chartFrame.setTitle("WheresMyMoney Visualizer");

        // Set the default close operation to HIDE instead of EXIT
        chartFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void visualize() throws WheresMyMoneyException {
        processChartData();
        drawChart();
    }
}
