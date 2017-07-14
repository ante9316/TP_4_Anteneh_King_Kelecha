import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Event
{
	private static Map<Object, Object> eventBYCopy;
	private static ArrayList<String> alleventDetail = new ArrayList<String>();
	static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	static
	{
		eventBYCopy = new HashMap<Object, Object>();

	}

	public static void createCheckOutLog(Copy currentCopy, LocalDateTime now)
	{

		String tempLog = "\nA copy titled: " + currentCopy.getTitle() + " ( " + currentCopy.getCopyID()
				+ " ) has been checked out to a patron: " + currentCopy.getOutTo().getName() + " ( "
				+ currentCopy.getOutTo().getPatronID() + " )." + " at : " + dateFormat.format(now)
				+ " with a DUE DATE of 120 days: " + dateFormat.format(currentCopy.getDueDate()) + "\n";

		alleventDetail.add(tempLog);

		eventBYCopy.put(currentCopy, tempLog);
	}

	public static void createCheckInLog(Copy currentCopy)
	{
		LocalDateTime now = LocalDateTime.now();
		String tempLog = "\nA copy titled: " + currentCopy.getTitle() + " ( " + currentCopy.getCopyID()
				+ " ) has been checked in by: " + currentCopy.getOutTo().getName() + " ( "
				+ currentCopy.getOutTo().getPatronID() + " )." + " at : " + dateFormat.format(now) + "\n";

		alleventDetail.add(tempLog);

		eventBYCopy.put(currentCopy, tempLog);

	}

	public static void createMarkHoldLog(Copy copyOnHold, Hold newHold)
	{
		LocalDateTime now = LocalDateTime.now();
		String tempLog = "\nA hold has been marked against a copy titled: " + copyOnHold.getTitle() + " ( "
				+ copyOnHold.getCopyID() + " ) and was checked out by: " + copyOnHold.getOutTo().getName() + " ( "
				+ copyOnHold.getOutTo().getPatronID() + " )." + " at : " + dateFormat.format(now) + "\n";

		alleventDetail.add(tempLog);

		eventBYCopy.put(copyOnHold, tempLog);
	}

	public static ArrayList<String> getEventLogs()
	{
		return alleventDetail;
	}

}
