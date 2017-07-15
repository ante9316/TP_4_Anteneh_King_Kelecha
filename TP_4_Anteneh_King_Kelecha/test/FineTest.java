import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FineTest
{

	@Test

	public void testGetFine()
	{
		double fine = Fine.getFine("damage");

		assertTrue(fine == 1);

	}

}
