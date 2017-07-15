import java.time.LocalDateTime;
import java.util.ArrayList;

public class Copy
{
	private String copyID;
	private String title;
	private Patron outTo;
	private LocalDateTime dueDate;
	protected ArrayList<Hold> copyHolds = new ArrayList<Hold>();

	public Copy(String copyID, String title)
	{
		// finish this
		this.copyID = copyID;
		this.title = title;
		this.dueDate = LocalDateTime.now();

	}

	protected LocalDateTime getDueDate()
	{
		return this.dueDate;
	}

	protected void setDueDate(LocalDateTime dueDate)
	{
		this.dueDate = dueDate;
	}

	public String toString()
	{
		// finish this

		return "Copy Title: " + this.getTitle() + " & Copy ID: " + this.getCopyID();

	}
	// generate getters and setters using Eclipse Source menu

	public String getCopyID()
	{
		return this.copyID;
	}

	public void setCopyID(String copyID)
	{
		this.copyID = copyID;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Patron getOutTo()
	{
		return this.outTo;
	}

	public void setOutTo(Patron outTo)
	{
		this.outTo = outTo;
	}

	protected ArrayList<Hold> getCopyHolds()
	{
		return this.copyHolds;
	}

	protected void setHoldTobeAdded(Hold newHold)
	{
		this.copyHolds.add(newHold);
	}

	protected void setHoldTobeRemoved(Hold removeHold)
	{
		this.copyHolds.remove(removeHold);
	}
}
