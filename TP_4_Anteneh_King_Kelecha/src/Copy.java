import java.time.LocalDateTime;
import java.util.ArrayList;

public class Copy
{
	private String copyID;
	private String title;
	private Patron outTo;
	private LocalDateTime dueDate;
	protected ArrayList<Hold> holdTobeAdded = new ArrayList<Hold>();

	public Copy(String copyID, String title)
	{
		// finish this
		this.copyID = copyID;
		this.title = title;
		this.dueDate = LocalDateTime.now();

	}

	public Copy()
	{

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

	public String toString(Patron patron)
	{
		return "Copy Title: " + this.getTitle() + " & Copy ID: ";
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

	protected ArrayList<Hold> getHoldTobeAdded()
	{
		return this.holdTobeAdded;
	}

	protected void setHoldTobeAdded(Hold newHold)
	{
		this.holdTobeAdded.add(newHold);
	}
}
