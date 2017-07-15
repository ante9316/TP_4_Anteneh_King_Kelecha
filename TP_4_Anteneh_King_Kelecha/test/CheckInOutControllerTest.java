import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;

public class CheckInOutControllerTest
{
	@Test
	public void testCheckInOutMechanics()
	{
		// event log is not independent
		CheckInOutController controller = new CheckInOutController();

		Patron p1 = new Patron("P1", "John");
		Patron p2 = new Patron("P2", "Tom");
		Copy c1 = new Copy("C1", "Testing");
		Copy c2 = new Copy("C2", "Testing 2");
		LocalDateTime t1 = LocalDateTime.now().plusDays(120);

		// check out 1
		controller.checkOut(p1, c1);
		assertTrue(c1.getOutTo() == p1);
		assertTrue(p1.getCopiesStillOut().get(0) == c1);
		assertTrue(c1.getDueDate().isEqual(t1));

		// check out 2
		controller.checkOut(p1, c2);
		assertTrue(c2.getOutTo() == p1);
		assertTrue(p1.getCopiesStillOut().get(1) == c2);
		assertTrue(p1.getCopiesStillOut().size() == 2);
		assertTrue(c2.getDueDate().isEqual(t1));

		// check out copy already checked out (same patron)
		controller.checkOut(p1, c2);

		// check out copy already checked out (different patron)
		controller.checkOut(p2, c1);

		// return c1
		controller.checkIn(p1, c1);
		assertTrue(c2.getOutTo() == p1);
		assertTrue(c1.getOutTo() == null);
		assertTrue(p1.getCopiesStillOut().get(0) == c2);
		assertTrue(p1.getCopiesStillOut().size() == 1);
		assertTrue(c1.getDueDate().isEqual(LocalDateTime.MIN));

		// return c2
		controller.checkIn(p1, c2);
		assertTrue(c2.getOutTo() == null);
		assertTrue(p1.getCopiesStillOut() == null);
		assertTrue(c1.getDueDate().isEqual(LocalDateTime.MIN));

		// return copy not checked out
		controller.checkIn(p1, c1);
	}

	@Test
	public void testEventPrint()
	{
		Patron p1 = new Patron("P1", "John");
		Copy c1 = new Copy("C1", "Testing");

		CheckInOutController controller = new CheckInOutController();

		System.out.println(controller.printEventLog());
		assertTrue(controller.printEventLog().isEmpty());

		controller.checkOut(p1, c1);
		assertTrue(controller.printEventLog().size() == 1);
	}

	@Test
	public void testPatronPrint()
	{
		CheckInOutController controller = new CheckInOutController();
		assertNotNull(controller.printAllPatrons().size());
	}

	@Test
	public void testCopiesPrint()
	{
		CheckInOutController controller = new CheckInOutController();
		assertNotNull(controller.printAllCopies().size());
	}

	@Test
	public void testMarkHold()
	{
		CheckInOutController controller = new CheckInOutController();
		Patron p1 = FakeDB.getPatron("P1");
		Patron p2 = FakeDB.getPatron("P2");
		Copy c1 = FakeDB.getCopy("C1");
		Copy c2 = FakeDB.getCopy("C2");
		Copy c3 = FakeDB.getCopy("C3");

		// test patron without any books checked out
		controller.markHold(c1, p1, "Overdue", "Test");
		assertTrue(FakeDB.getHoldStore().isEmpty());

		controller.checkOut(p1, c1);
		// test hold on book that is not checked out
		controller.markHold(c2, p1, "Other", "Testing");
		assertTrue(FakeDB.getHoldStore().isEmpty());

		controller.checkOut(p2, c2);
		// test hold on copy checked out by another patron
		controller.markHold(c2, p1, "Overdue", "Because");
		assertTrue(FakeDB.getHoldStore().isEmpty());

		controller.checkIn(p2, c2);
		// test hold related to a copy that is checked out
		controller.markHold(c1, p1, "Overdue", "Because");
		controller.markHold(c2, p1, "Overdue", "Because");
		controller.markHold(c3, p1, "Overdue", "Because");
		assertTrue(FakeDB.getHoldStore().size() == 3);

		// test hold when has null values
		controller.markHold(c1, p1, "Overdue", null); // this one should be okay
		assertTrue(FakeDB.getHoldStore().size() == 4);
		controller.markHold(null, p1, "Overdue", "Because");
		assertTrue(FakeDB.getHoldStore().size() == 5);
		controller.markHold(c1, null, "Overdue", "Because");
		assertTrue(FakeDB.getHoldStore().size() == 6);
		controller.markHold(c1, p1, null, "Because");
		assertTrue(FakeDB.getHoldStore().size() == 7);
	}

	@Test
	public void testApplyFine()
	{
		CheckInOutController controller = new CheckInOutController();
		Hold h1 = new Hold("Damage", "Testing");
		Hold h2 = new Hold("Overdue", "Testing 2");
		Hold h3 = new Hold("Book Dumping", "Testing 3");
		Hold h4 = new Hold("Other", "Testing 4");
		Patron p1 = new Patron("P1", "John");

		controller.applyFine(p1, h1);
		controller.applyFine(p1, h2);
		controller.applyFine(p1, h3);
		controller.applyFine(p1, h4);
	}

	@Test
	public void testHoldStore()
	{
		CheckInOutController controller = new CheckInOutController();
		Patron p1 = FakeDB.getPatron("P1");
		Map<Copy, ArrayList<Hold>> list = controller.checkHoldsRecord(p1);

		assertTrue(list == null);
		// TO DO: need built in with a hold on their record
	}

	@Test
	public void testCheckPatron()
	{
		CheckInOutController controller = new CheckInOutController();
		String id1 = "P1";
		String id2 = "P257";

		assertNotNull(controller.checkPatronExist(id1));
		assertNull(controller.checkPatronExist(id2));

	}

	@Test
	public void testCheckCopy()
	{
		CheckInOutController controller = new CheckInOutController();
		String id1 = "C1";
		String id2 = "C257";

		assertNotNull(controller.checkCopyExist(id1));
		assertNull(controller.checkCopyExist(id2));

	}

	@Test
	public void testPrintRecord()
	{
		CheckInOutController controller = new CheckInOutController();
		Patron p1 = new Patron("P1", "John");
		controller.printPatronRecord(p1);
		controller.printPatronRecord(null);
	}

	@Test
	public void testIsCheckedOut()
	{
		CheckInOutController controller = new CheckInOutController();
		Patron p1 = new Patron("P1", "John");
		String patronName;
		Copy c1 = new Copy("C1", "Testing");

		assertNull(controller.isCopyCheckedOut(c1));
		controller.checkOut(p1, c1);
		patronName = controller.isCopyCheckedOut(c1);
		assertTrue(patronName == "John");
	}

	@Test
	public void testPrintCopies()
	{
		CheckInOutController controller = new CheckInOutController();
		Patron p1 = new Patron("P1", "John");
		Copy c1 = new Copy("C1", "Testing");
		Copy c2 = new Copy("C2", "Testing 2");

		assertTrue(controller.printCheckedOutCopies(p1) == null);
		controller.checkOut(p1, c1);
		assertNotNull(controller.printCheckedOutCopies(p1));
		controller.checkOut(p1, c2);
		assertNotNull(controller.printCheckedOutCopies(p1));
	}

	@Test
	public void testTypesofHold()
	{
		CheckInOutController controller = new CheckInOutController();
		assertNotNull(controller.getTypeOfHold());
	}

	@Test
	public void testOverDue()
	{
		CheckInOutController controller = new CheckInOutController();
		Patron p1 = FakeDB.getPatron("P1");
		Copy c1 = FakeDB.getCopy("C1");
		Copy c2 = FakeDB.getCopy("C2");
		Copy c3 = FakeDB.getCopy("C3");
		controller.checkOut(p1, c1);
		controller.checkOut(p1, c2);
		controller.checkOut(p1, c3);
		c1.setDueDate(LocalDateTime.now().minusDays(120));
		c2.setDueDate(LocalDateTime.MIN);
		c3.setDueDate(LocalDateTime.now());

		controller.markHold();
		controller.printNotice();
		controller.removeHold(c1, "Overdue");

	}

	@Test
	public void testPrintNotices()
	{
		CheckInOutController controller = new CheckInOutController();
		// nothing to print
		controller.printNotice();
		Patron p1 = FakeDB.getPatron("P1");
		Copy c1 = FakeDB.getCopy("C1");
		Copy c2 = FakeDB.getCopy("C2");
		Copy c3 = FakeDB.getCopy("C3");
		controller.checkOut(p1, c1);
		controller.checkOut(p1, c2);
		controller.checkOut(p1, c3);
		c1.setDueDate(LocalDateTime.now().minusDays(120));
		c2.setDueDate(LocalDateTime.MIN);
		c3.setDueDate(LocalDateTime.now());

		controller.markHold();
		controller.printNotice();
	}

}
