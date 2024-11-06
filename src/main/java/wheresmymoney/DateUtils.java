package wheresmymoney;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import wheresmymoney.exception.WheresMyMoneyException;

/**
 * Utility class for handling date formatting, validation, and conversion.
 * <p>
 * The {@code DateUtils} class provides static methods to work with dates in the
 * {@code dd-MM-yyyy} format. It includes methods for validating date strings,
 * converting between strings and {@code LocalDate} objects,
 * and retrieving the current date.
 * </p>
 */
public class DateUtils {
    
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    
    public static boolean isInDateFormat(String dateAsString) throws WheresMyMoneyException {
        if (dateAsString == null)  {
            throw new WheresMyMoneyException("Null date was provided.");
        }
        try {
            LocalDate.parse(dateAsString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDate stringToDate(String dateAsString) throws WheresMyMoneyException {
        try {
            return LocalDate.parse(dateAsString, formatter);
        } catch (DateTimeParseException e) {
            throw new WheresMyMoneyException("Date string is not in the correct format: " + DATE_FORMAT);
        }
    }

    public static String dateFormatToString(LocalDate date) {
        return date.format(formatter);
    }
    
    public static LocalDate dayMonthYearToYearMonth(LocalDate localDate) {
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }
}
