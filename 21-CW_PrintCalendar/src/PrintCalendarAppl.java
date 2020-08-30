import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class PrintCalendarAppl {

	private static Locale locale = Locale.US;
	private static int columnWidth = 4;
static private int firstWeekDay;
	public static void main(String[] args) {
		int monthYear[];
		try {
			monthYear = getMonthYear(args);
			firstWeekDay=args.length>2?getFirstDay(args[2]):1;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		printCalendar(monthYear[0], monthYear[1]);
		
		
	}
	private static int getFirstDay(String firstDayStr ) throws Exception {
		try {
			int res=DayOfWeek.valueOf(firstDayStr.toUpperCase()).getValue();
			return res;
		} catch (Exception e) {
			throw new Exception(firstDayStr + " is wrong week day");
		}
	}
	private static void printCalendar(int month, int year) {
		printTitle(month, year); //printing title, e.g August, 2020
		printWeekDays();
		printDates(month, year);
		
	}

	private static void printDates(int month, int year) {
		int firstColumn = getFirstColumn(month, year);
		printDatesFromFirstColumn(firstColumn, month, year);
		
	}

	private static void printDatesFromFirstColumn(int firstColumn, int month, int year) {
		printOffset(firstColumn);
		int lastDayOfMonth = getLastDayOfMonth(month, year);
		for (int day = 1; day <= lastDayOfMonth; day++) {
			System.out.printf("%" + columnWidth + "d", day);
			firstColumn++;
			if(firstColumn == 8) {
				firstColumn = 1;
				System.out.println();
			}
		}
		
	}

	private static int getLastDayOfMonth(int month, int year) {
		YearMonth yearMonth = YearMonth.of(year, month);
		return yearMonth.lengthOfMonth();
	}

	private static void printOffset(int firstColumn) {
		int limit = (firstColumn - 1) * columnWidth ;
		//for JDK 11+
		System.out.print(" ".repeat(limit));
		//for JDK 10-
		/* 
		for(int i = 0; i < limit; i++) {
			System.out.print(" ");
		}*/
		
	}

	private static int getFirstColumn(int month, int year) {
		LocalDate firstDayOfMonth = 
				LocalDate.of(year, month, 1);
		DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
		int res = firstDayOfWeek.getValue();
		if(firstWeekDay!=1) {
			int delta=res-firstWeekDay;
			res=delta>=0?delta+1:delta+8;
		}
		return res;
	}

	private static void printWeekDays() {
		System.out.printf("%" + columnWidth / 2 + "s", " ");
		DayOfWeek[]daysOfWeek = DayOfWeek.values();
		for(int i = firstWeekDay-1; i < daysOfWeek.length; i++) {
			printDayOfWeekName(daysOfWeek[i]);
			
		}
		int limit = firstWeekDay -1;
		for(int i = 0; i < limit; i++) {
			printDayOfWeekName(daysOfWeek[i]);
		}
		
		System.out.println();
		
	}
	private static void printDayOfWeekName(DayOfWeek dayOfWeek) {
		System.out.print(dayOfWeek.getDisplayName
				(TextStyle.SHORT, locale)+" ");
		
	}

	private static void printTitle(int month, int year) {
		Month monthName = Month.of(month);
		System.out.printf("\t%s, %d\n", monthName.getDisplayName(TextStyle.FULL, locale ), year);
		
	}

	private static int[] getMonthYear(String[] args) throws Exception {
		
		
		return args.length == 0 ? currentMonthYear() : monthYearByArgs(args);
	}

	private static int[] monthYearByArgs(String[] args) throws Exception {
		int month;
		try {
			month = Integer.parseInt(args[0]);
			if (month < 1 || month > 12) {
				throw new Exception(String.format("you have entered %d but month should be in range [1-12]", month));
			}
		} catch (NumberFormatException e) {
			throw new Exception(String.format("you have entered %s but month value should be number", args[0]));
		}
		int year;
		try {
			year = args.length > 1 ? Integer.parseInt(args[1]) : LocalDate.now().getYear();
			if (year < 0) {
				throw new Exception("year should be only positive value");
			}
		} catch (NumberFormatException e) {
			throw new Exception(String.format("you have entered %s but year value should be a positive number", args[1]));
		}
		return new int[] {month, year};
	}

	private static int[] currentMonthYear() {
		LocalDate current = LocalDate.now();
		return new int[] {current.getMonthValue(), current.getYear()};
	}

}
