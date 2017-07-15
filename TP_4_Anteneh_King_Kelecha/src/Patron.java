
import java.util.ArrayList;

public class Patron
{
	private String name;
	private String patronID;
	private ArrayList<Copy> copiesStillOut;
	// private Hold patronOnHold;

	public Patron(String id, String name)
	{
		// finish this
		this.patronID = id;
		this.name = name;
		this.copiesStillOut = new ArrayList<Copy>();

	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPatronID()
	{
		return patronID;
	}

	public void setPatronID(String patronID)
	{
		this.patronID = patronID;
	}

	public ArrayList<Copy> getCopiesStillOut()
	{
		if (this.copiesStillOut.size() != 0)
		{
			return this.copiesStillOut;
		}
		return null;
	}

	public void setCopiesStillOut(Patron activePatron, Copy copyToCheckOut)
	{
		copiesStillOut.add(copyToCheckOut);
	}

}
