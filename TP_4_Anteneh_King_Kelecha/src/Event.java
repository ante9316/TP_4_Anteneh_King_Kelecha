import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Event
{
	private static Map<Object, Object> eventLogs;
	private static ArrayList<String> checkOutHistory = new ArrayList<String>();
	static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	static
	{
		eventLogs = new HashMap<Object, Object>();

	}

	/*
	 * public Event(Copy copyToCheckOut) { this.eventLogs.put(copyToCheckOut,
	 * this.createCheckOutLog(copyToCheckOut)); //
	 * this.createEventLog(this.events.get(activePatron)); }
	 */

	public static void createCheckOutLog(Copy currentCopy, LocalDateTime now)
	{

		String tempLog = "\nA copy titled: " + currentCopy.getTitle() + " ( " + currentCopy.getCopyID()
				+ " ) has been checked out to a patron: " + currentCopy.getOutTo().getName() + " ( "
				+ currentCopy.getOutTo().getPatronID() + " )." + " at : " + dateFormat.format(now)
				+ " with a DUE DATE of 120 days: " + dateFormat.format(currentCopy.getDueDate()) + "\n";

		checkOutHistory.add(tempLog);

		eventLogs.put(currentCopy, tempLog);
	}

	public static void createCheckInLog(Copy currentCopy)
	{
		LocalDateTime now = LocalDateTime.now();
		String tempLog = "\nA copy titled: " + currentCopy.getTitle() + " ( " + currentCopy.getCopyID()
				+ " ) has been checked in by: " + currentCopy.getOutTo().getName() + " ( "
				+ currentCopy.getOutTo().getPatronID() + " )." + " at : " + dateFormat.format(now) + "\n";

		checkOutHistory.add(tempLog);

		eventLogs.put(currentCopy, tempLog);

	}

	/*
	 * public static void createMarkHoldLog(Copy activeCopy, ArrayList<Hold>
	 * newHold) { LocalDateTime now = LocalDateTime.now(); String tempLog =
	 * "\nA hold has been marked against a copy titled: " +
	 * activeCopy.getTitle() + " ( " + activeCopy.getCopyID() +
	 * " ) has been checked in by: " + currentCopy.getOutTo().getName() + " ( "
	 * + currentCopy.getOutTo().getPatronID() + " )." + " at : " +
	 * dateFormat.format(now) + "\n";
	 * 
	 * checkOutHistory.add(tempLog);
	 * 
	 * eventLogs.put(currentCopy, tempLog); }
	 */
	public static ArrayList<String> getEventLogs()
	{
		return checkOutHistory;
	}

}
