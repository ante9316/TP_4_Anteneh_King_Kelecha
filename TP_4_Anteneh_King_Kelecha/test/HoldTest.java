import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HoldTest
{

	@Test
	public void testNewHold()
	{
		Hold h1 = new Hold("Testing", "invalid");
		Hold h2 = new Hold("Testing", "book dumping");
		Hold h3 = new Hold("blah", "OVERDUE");
		Hold h4 = new Hold("abc123", "Damage");
		assertTrue(h1.getReasonofHold() == "Testing");
		assertTrue(h1.getHoldType().equals("OTHER"));
		assertTrue(h2.getReasonofHold() == "Testing");
		assertTrue(h2.getHoldType().equals("BOOK DUMPING"));
		assertTrue(h3.getReasonofHold() == "blah");
		assertTrue(h3.getHoldType().equals("OVERDUE"));
		assertTrue(h4.getReasonofHold() == "abc123");
		assertTrue(h4.getHoldType().equals("DAMAGE"));

	}

	@Test
	public void testHoldReason()
	{
		Hold h1 = new Hold("Testing", "other");
		h1.setReasonOfHolds("Updating this");
		assertTrue(h1.getReasonofHold() == "Updating this");
		assertTrue(h1.getHoldType().equals("OTHER"));
	}

	@Test
	public void testHoldType()
	{
		Hold h1 = new Hold("Testing", "other");
		h1.setHoldType("book dumping");
		assertTrue(h1.getReasonofHold() == "Testing");
		assertTrue(h1.getHoldType().equals("BOOK DUMPING"));
	}

}
