public class Fine
{
	protected double fee;
	protected Patron patronWithFee;
	protected static final double DAMAGE_FEE = 1;
	protected static final double OVERDUE_FEE = 2.5;
	protected static final double DUMPING_FEE = 1.5;

	public void applyDamageFee(Patron activePatron)
	{
		this.fee = DAMAGE_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this.fee);
	}

	public void applyDumpingFee(Patron activePatron)
	{
		this.fee = DUMPING_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this.fee);
	}

	public void applyOverDueFee(Patron activePatron)
	{
		this.fee = OVERDUE_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this.fee);
	}

	public boolean finePaid()
	{

		return true;
	}

	public void payment()
	{

		// OUT OF SCOPE
	}
}
