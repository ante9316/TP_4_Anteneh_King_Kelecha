import org.junit.Test;

public class TRLAppTest
{
	@Test
	public void testSelHoldType()
	{

	}

	@Test
	public void testValidatePatron()
	{
		StdOut.println("Valid Patron can be used:-  P1, P2, P3");

		CheckInOutController controller = new CheckInOutController();

		TRLApp.validatePatron(controller);

	}

	@Test
	public void testValidCopy()
	{
		StdOut.println("Valid Copy can be used:-  C1, C2,C3");
		CheckInOutController controller = new CheckInOutController();
		TRLApp.validateCopies(controller);
	}

}
