//NOT FULLY implemented
public class Hold
{

	protected String reasonOfHold;
	protected String holdType;

	protected static String typeOfHold[] =
	{ "Overdue", "Book Dumping", "Damage" };

	public Hold(String reason, String typeOfHold)
	{
		reasonOfHold = reason;
		holdType = typeOfHold;

	}

	protected String getReasonofHold()
	{
		return this.reasonOfHold;
	}

	protected void setReasonOfHolds(String reasonOfHolds)
	{
		this.reasonOfHold = reasonOfHolds;
	}

	protected String getHoldType()
	{
		return holdType;
	}

	protected void setHoldType(String holdType)
	{
		this.holdType = holdType;
	}

}
