import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

public class PatronTest
{

	@Test
	public void testGetPatronID()
	{
		Patron testPatron = createPatron();
		assertEquals("P1", testPatron.getPatronID());

	}

	@Test
	public void testGetPatronName()
	{
		Patron testPatron = createPatron();
		assertEquals("Eric", testPatron.getName());

	}

	@Test
	public void testPrintAllPatronRecords()
	{
		CheckInOutController newTransaction = createTransaction();
		assertEquals(FakeDB.getPatronStore(), newTransaction.printAllPatrons());

	}

	@Test
	public void testPrintAllCopyRecords()
	{
		CheckInOutController newTransaction = createTransaction();
		assertEquals(FakeDB.getCopyStore(), newTransaction.printAllCopies());

	}

	@Test
	public void testCheckOut()
	{
		Patron testPatron = createPatron();
		Copy testCopy = createCopy();
		CheckInOutController newTransaction = createTransaction();
		newTransaction.checkOut(testPatron, testCopy);
		assertTrue(testPatron.getCopiesStillOut().contains(testCopy));
	}

	@Test
	public void testCheckIn()
	{
		Patron testPatron = createPatron();
		Copy testCopy = createCopy();
		CheckInOutController newTransaction = createTransaction();
		newTransaction.checkOut(testPatron, testCopy);
		assertTrue(!testPatron.getCopiesStillOut().contains(testPatron));
	}

	@Test
	public void testHold()
	{
		Patron testPatron = createPatron();
		Copy testCopy = createCopy();
		CheckInOutController newTransaction = createTransaction();
		newTransaction.checkOut(testPatron, testCopy);

		testCopy.setDueDate(LocalDateTime.now().minusDays(2));

		newTransaction.markHoldOnOverDueCopies();
		assertTrue(FakeDB.getHoldStore().containsKey(testCopy));
	}

	private CheckInOutController createTransaction()
	{
		return new CheckInOutController();
	}

	private Copy createCopy()
	{
		return FakeDB.getCopy("C1");
	}

	protected Patron createPatron()
	{
		return FakeDB.getPatron("P1");

	}

}
