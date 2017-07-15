public class Fine
{
	protected double fee;
	protected Patron patronWithFee;
	protected static final double DAMAGE_FEE = 1;
	protected static final double OVERDUE_FEE = 2.5;
	protected static final double DUMPING_FEE = 1.5;

	protected static double getFine(String holdType)
	{
		if (holdType.equalsIgnoreCase("damage"))
			return DAMAGE_FEE;

		else if (holdType.equalsIgnoreCase("dumping"))
		{
			return DUMPING_FEE;
		}
		else
		{
			return OVERDUE_FEE;
		}
	}

	public void applyDamageFee(Patron activePatron)
	{
		this.fee = DAMAGE_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this);

	}

	public void applyDumpingFee(Patron activePatron)
	{
		this.fee = DUMPING_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this);

	}

	public void applyOverDueFee(Patron activePatron)
	{
		this.fee = OVERDUE_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this);

	}

	public boolean finePaid()
	{
		return true;
	}

	public void payment(Patron activePatron)
	{
		FakeDB.getFineStore().get(activePatron).fee = 0.0;

		// OUT OF SCOPE , fee is paid for now
		// TODO: Log payment event
	}
}
