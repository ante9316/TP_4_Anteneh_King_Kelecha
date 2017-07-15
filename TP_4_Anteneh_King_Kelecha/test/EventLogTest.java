import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

public class EventLogTest
{

	@Test
	public void testNew()
	{
		EventLog e1 = new EventLog();
		assertTrue(e1.getEventLogs().size() == 0);
	}

	@Test
	public void testCheckInOut()
	{
		EventLog e1 = new EventLog();

		// everything good
		Copy c1 = new Copy("C1", "Testing");
		Patron p1 = new Patron("P1", "John");
		c1.setOutTo(p1);
		LocalDateTime timeOut = LocalDateTime.now();
		c1.setDueDate(timeOut.plusDays(120));

		e1.createCheckOutLog(c1);
		assertTrue(e1.getEventLogs().size() == 1);
		e1.createCheckInLog(c1);
		assertTrue(e1.getEventLogs().size() == 2);

		// Copy without due date
		Copy c2 = new Copy("C2", "Testing 2");
		c2.setOutTo(p1);

		e1.createCheckOutLog(c2);
		assertTrue(e1.getEventLogs().size() == 3);
		e1.createCheckInLog(c2);
		assertTrue(e1.getEventLogs().size() == 4);

		// Copy without patron
		Copy c3 = new Copy("C3", "Testing 3");
		c3.setDueDate(timeOut.plusDays(120));
		e1.createCheckOutLog(c3);
		assertTrue(e1.getEventLogs().size() == 5);
		e1.createCheckInLog(c3);
		assertTrue(e1.getEventLogs().size() == 6);

	}

	@Test
	public void testNewHolds()
	{
		// All valid Info
		EventLog e1 = new EventLog();
		Copy c1 = new Copy("C1", "Testing");
		Patron p1 = new Patron("P1", "Tom");
		c1.setOutTo(p1);
		Hold h1 = new Hold("Testing", "Other");
		e1.createMarkHoldLog(c1, h1, p1);

		// With patron that did not check out copy
		Copy c2 = new Copy("C2", "Testing 2");
		Hold h2 = new Hold("bad book", "Damage");
		e1.createMarkHoldLog(c2, h2, p1);

		// With null values
		e1.createMarkHoldLog(c2, h2, null);
		e1.createMarkHoldLog(c2, null, p1);
		e1.createMarkHoldLog(null, h2, p1);
	}

	@Test
	public void testRemoveHold()
	{
		EventLog e1 = new EventLog();
		Copy c1 = new Copy("C1", "Testing");

		// check valid
		e1.createRemoveHoldLog(c1);
		// check null
		e1.createRemoveHoldLog(null);
	}
}
