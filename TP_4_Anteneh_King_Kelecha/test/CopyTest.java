import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

public class CopyTest
{

	@Test
	public void testNew()
	{
		Copy c1 = new Copy("C1", "Testing");
		assertTrue(c1.getCopyID() == "C1");
		assertTrue(c1.getTitle() == "Testing");
		assertTrue(c1.getDueDate() == LocalDateTime.MIN);
		assertTrue(c1.getOutTo() == null);
		assertTrue(c1.getCopyHolds().size() == 0);
	}

	@Test
	public void testCopyID()
	{
		Copy c1 = new Copy("C1", "Testing");
		c1.setCopyID("C3");
		assertTrue(c1.getCopyID() == "C3");
		assertTrue(c1.getTitle() == "Testing");
		assertTrue(c1.getDueDate() == LocalDateTime.MIN);
		assertTrue(c1.getOutTo() == null);
		assertTrue(c1.getCopyHolds().size() == 0);
	}

	@Test
	public void testTitle()
	{
		Copy c1 = new Copy("C1", "Testing");
		c1.setTitle("Bob the Builder");
		assertTrue(c1.getCopyID() == "C1");
		assertTrue(c1.getTitle() == "Bob the Builder");
		assertTrue(c1.getDueDate() == LocalDateTime.MIN);
		assertTrue(c1.getOutTo() == null);
		assertTrue(c1.getCopyHolds().size() == 0);
	}

	@Test
	public void testDueDate()
	{
		Copy c1 = new Copy("C1", "Testing");
		LocalDateTime dueDate = LocalDateTime.now().plusYears(1);
		c1.setDueDate(dueDate);
		assertTrue(c1.getCopyID() == "C1");
		assertTrue(c1.getTitle() == "Testing");
		assertTrue(c1.getDueDate() == dueDate);
		assertTrue(c1.getOutTo() == null);
		assertTrue(c1.getCopyHolds().size() == 0);
	}

	@Test
	public void testOutTo()
	{
		Copy c1 = new Copy("C1", "Testing");
		Patron p1 = new Patron("P1", "Bob");
		c1.setOutTo(p1);
		assertTrue(c1.getCopyID() == "C1");
		assertTrue(c1.getTitle() == "Testing");
		assertTrue(c1.getDueDate() == LocalDateTime.MIN);
		assertTrue(c1.getOutTo() == p1);
		assertTrue(c1.getCopyHolds().size() == 0);

		c1.setOutTo(null);
		assertTrue(c1.getOutTo() == null);
	}

	@Test
	public void testHolds()
	{
		Copy c1 = new Copy("C1", "Testing");
		Hold h1 = new Hold("Testing", "Book Dumping");
		Hold h2 = new Hold("Testing", "Overdue");

		c1.setHoldTobeAdded(h1);
		assertTrue(c1.getCopyHolds().get(0) == h1);

		c1.setHoldTobeAdded(h2);
		assertTrue(c1.getCopyHolds().get(1) == h2);

		c1.setHoldTobeRemoved(h1);
		assertTrue(c1.getCopyHolds().size() == 1);
		assertTrue(c1.getCopyHolds().get(0) == h2);
	}

	@Test
	public void testPrint()
	{
		Copy c1 = new Copy("C1", "Testing");
		assertTrue(c1.toString().equals("Copy Title: Testing & Copy ID: C1"));

	}

}
