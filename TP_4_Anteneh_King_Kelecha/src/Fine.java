public class Fine
{
	protected double fee;
	protected Patron patronWithFee;
	protected static final double DAMAGE_FEE = 1;
	protected static final double OVERDUE_FEE = 2.5;
	protected static final double DUMPING_FEE = 1.5;

	public void applyDamageFee(Patron activePatron, Fine newFine)
	{
		this.fee = DAMAGE_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, newFine);

		StdOut.println("FEE= " + FakeDB.fineStore.get(this.patronWithFee).fee);
	}

	public void applyDumpingFee(Patron activePatron, Fine newFine)
	{
		this.fee = DUMPING_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, newFine);

		StdOut.println("FEE= " + FakeDB.fineStore.get(this.patronWithFee).fee);
	}

	public void applyOverDueFee(Patron activePatron, Fine newFine)
	{
		this.fee = OVERDUE_FEE;
		this.patronWithFee = activePatron;
		FakeDB.fineStore.put(this.patronWithFee, newFine);

		StdOut.println("FEE= " + FakeDB.fineStore.get(this.patronWithFee).fee);
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
