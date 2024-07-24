import java.util.regex.*;

class InvalidDateException extends Exception {
    public InvalidDateException(String message) {
        super(message);
    }
}

public class DateFormatter {

    public static String normalizeDate(String date) {
        // Regular expression to match date-like numbers with dashes or periods
        Pattern pattern = Pattern.compile("^(\\d{4})[-.](\\d{2})[-.](\\d{2})$|^(\\d{2})[-.](\\d{2})[-.](\\d{4})$");
        Matcher matcher = pattern.matcher(date);
        
        if (!matcher.matches()) {
            return "";
        }
        
        String year, month, day;
        
        if (matcher.group(1) != null) {
            // yyyy-mm-dd or yyyy.mm.dd format
            year = matcher.group(1);
            month = matcher.group(2);
            day = matcher.group(3);
        } else {
            // dd-mm-yyyy or dd.mm.yyyy format
            day = matcher.group(4);
            month = matcher.group(5);
            year = matcher.group(6);
        }
        
        try {
            validateDate(year, month, day);
        } catch (InvalidDateException e) {
            return e.getMessage();
        }
        
        return String.format("%s-%s-%s", year, month, day);
    }

    private static void validateDate(String year, String month, String day) throws InvalidDateException {
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);
        
        if (m < 1 || m > 12) {
            throw new InvalidDateException("Not a valid month");
        }
        
        int[] daysInMonth = {31, (isLeapYear(y) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        
        if (d < 1 || d > daysInMonth[m - 1]) {
            throw new InvalidDateException("Not a valid day");
        }
    }

    private static boolean isLeapYear(int year) {
        if (year % 4 != 0) return false;
        if (year % 100 != 0) return true;
        return year % 400 == 0;
    }

    public static void main(String[] args) {
        String[] testDates = {
            "2021-12-16", "2023.01.15", "15-01-2023", "16.12.2021", "14.32.2021"
        };
        
        for (String date : testDates) {
            String normalized = normalizeDate(date);
            System.out.println(date + " -> " + normalized);
        }
    }
}
