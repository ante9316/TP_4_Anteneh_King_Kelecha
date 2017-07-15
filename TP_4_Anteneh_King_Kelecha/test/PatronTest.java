import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PatronTest
{
	@Test
	public void testNew()
	{
		Patron p1 = new Patron("P5", "AFBHERRA");
		assertTrue(p1.getPatronID() == "P5");
		assertTrue(p1.getName() == "AFBHERRA");
		assertTrue(p1.getCopiesStillOut() == null);

	}

	@Test
	public void testSetName()
	{
		Patron p1 = new Patron("P5", "AFBHERRA");
		p1.setName("Bob");
		assertTrue(p1.getName() == "Bob");
	}

	@Test
	public void testSetID()
	{
		Patron p1 = new Patron("P5", "AFBHERRA");
		p1.setPatronID("P23573");
		assertTrue(p1.getPatronID() == "P23573");
	}

	@Test
	public void testCopiesOut()
	{
		Patron p1 = new Patron("P1", "Bob");
		Copy c1 = new Copy("C1", "Morning");
		Copy c2 = new Copy("C2", "Night");

		p1.setCopiesStillOut(p1, c1);
		assertTrue(p1.getCopiesStillOut().get(0) == c1);

		p1.setCopiesStillOut(p1, c2);
		assertTrue(p1.getCopiesStillOut().size() == 2);

		// check that cannot do copies that are already checked out

	}
}
