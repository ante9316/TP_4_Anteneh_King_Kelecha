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
		CheckInOutController controller = new CheckInOutController();
		TRLApp.validatePatron(controller);

	}

	@Test
	public void testValidCopy()
	{
		CheckInOutController controller = new CheckInOutController();
		TRLApp.validateCopies(controller);
	}

}
