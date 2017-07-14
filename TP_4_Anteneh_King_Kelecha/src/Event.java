import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Event
{

	private Map<Copy, String> eventByCopy;
	private Map<Patron, String> eventByPatron;
	private Map<Hold, String> eventByHold;
	private ArrayList<String> alleventDetail;

	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	public Event()
	{
		this.eventByCopy = new HashMap<Copy, String>();
		this.eventByPatron = new HashMap<Patron, String>();
		this.eventByHold = new HashMap<Hold, String>();
		this.alleventDetail = new ArrayList<String>();
	}

	public void createCheckOutLog(Copy copyCheckedOut, LocalDateTime now)
	{

		String tempLog = "\nA copy titled: " + copyCheckedOut.getTitle() + " ( " + copyCheckedOut.getCopyID()
				+ " ) has been checked out to a patron: " + copyCheckedOut.getOutTo().getName() + " ( "
				+ copyCheckedOut.getOutTo().getPatronID() + " )." + " at : " + dateFormat.format(now)
				+ " with a DUE DATE of 120 days: " + dateFormat.format(copyCheckedOut.getDueDate()) + "\n";

		alleventDetail.add(tempLog);

		eventByCopy.put(copyCheckedOut, tempLog);
		eventByPatron.put(copyCheckedOut.getOutTo(), tempLog);

	}

	public void createCheckInLog(Copy copyCheckedIn)
	{
		LocalDateTime now = LocalDateTime.now();
		String tempLog = "\nA copy titled: " + copyCheckedIn.getTitle() + " ( " + copyCheckedIn.getCopyID()
				+ " ) has been checked in by: " + copyCheckedIn.getOutTo().getName() + " ( "
				+ copyCheckedIn.getOutTo().getPatronID() + " )." + " at : " + dateFormat.format(now) + "\n";

		alleventDetail.add(tempLog);
		eventByPatron.put(copyCheckedIn.getOutTo(), tempLog);
		eventByCopy.put(copyCheckedIn, tempLog);

	}

	public void createMarkHoldLog(Copy copyOnHold, Hold newHold, Patron activePatron)
	{
		LocalDateTime now = LocalDateTime.now();
		String tempLog = "\nA hold has been marked against a copy titled: " + copyOnHold.getTitle() + " ( "
				+ copyOnHold.getCopyID() + " ) and was checked out by: " + activePatron.getName() + " ( "
				+ activePatron.getPatronID() + " )." + " at : " + dateFormat.format(now) + "\n";

		alleventDetail.add(tempLog);

		eventByCopy.put(copyOnHold, tempLog);
		eventByPatron.put(copyOnHold.getOutTo(), tempLog);
		eventByHold.put(newHold, tempLog);
	}

	public ArrayList<String> getEventLogs()
	{
		return alleventDetail;
	}

	public void createRemoveHoldLog(Copy activeCopy)
	{
		LocalDateTime now = LocalDateTime.now();
		String tempLog = "\nA hold has been removed against a copy titled: " + activeCopy.getTitle() + " ( "
				+ activeCopy.getCopyID() + " ) " + " at : " + dateFormat.format(now) + "\n";

		alleventDetail.add(tempLog);

	}

}
