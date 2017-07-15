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
		FakeDB.fineStore.put(this.patronWithFee, this);

		// StdOut.println("FEE= " +
		// FakeDB.fineStore.get(this.patronWithFee).fee);
	}

	public void applyDumpingFee(Patron activePatron)
	{
		this.fee = DUMPING_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this);

		// StdOut.println("FEE= " +
		// FakeDB.fineStore.get(this.patronWithFee).fee);
	}

	public void applyOverDueFee(Patron activePatron)
	{
		this.fee = OVERDUE_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, this);

		// StdOut.println("FEE= " +
		// FakeDB.fineStore.get(this.patronWithFee).fee);
	}

	public double getFines()
	{
		double totalFines = 0;

		return totalFines;
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
