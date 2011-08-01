package test.order;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.database.Database;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.order.ChargeLine;
import org.salespointframework.core.order.OrderLine;
import org.salespointframework.core.product.SerialNumber;

import test.inventory.KeksInventory;

public class OrderLineTest {
	
	private EntityManagerFactory emf = Database.INSTANCE
	.getEntityManagerFactory();
	private EntityManager em;
	private long timeStamp;
	SerialNumber sn1, sn2, sn3;
	
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		
	}
	
	@Before 
	public void setUp() {
		
		em = emf.createEntityManager();
		timeStamp = new DateTime().getMillis();
		Set<SerialNumber> serialNumbers = new TreeSet<SerialNumber>();
		sn1 = new SerialNumber();
		sn2 = new SerialNumber();
		sn3 = new SerialNumber();
		
		KeksInventory inv = new KeksInventory(em);
		serialNumbers.add(sn2);
		serialNumbers.add(sn3);
		
		OrderLine ol1 = new OrderLine(sn1, inv, "testdescription1", "testcomment1", new Money(1), new DateTime(timeStamp+1));
		OrderLine ol2 = new OrderLine(serialNumbers, inv, "testdescription2", "testcomment2", new Money(2), new DateTime(timeStamp+2));
		
		ChargeLine cl1 = new ChargeLine(new Money(1), "cl1description", "cl1comment");
		ChargeLine cl2 = new ChargeLine(new Money(2), "cl2description", "cl2comment");
		
		ol1.addChargeLine(cl1);
		ol2.addChargeLine(cl2);
		
		em.getTransaction().begin();
		em.persist(ol1);
		em.persist(ol2);
		em.getTransaction().commit();
	}
	
	@After 
	public void tearDown() {
		
		List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		em.getTransaction().begin();
		for (OrderLine current : list) {
			em.remove(current);
		}
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void persistOrderLine() {
		
		final List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		assertEquals(2, list.size());
		
		for (OrderLine current : list) {
            final String description = current.getDescription();
            final String comment = current.getComment();
            final int numberOrdered = current.getNumberOrdered();
            final Money unitPrice = current.getUnitPrice();
            final DateTime expectedDeliveryDate = current.getExpectedDeliveryDate();
                    
 
            assertTrue(description.equals("testdescription1")
                    || description.equals("testdescription2"));
            assertTrue(comment.equals("testcomment1")
                    || comment.equals("testcomment2"));
            assertTrue(numberOrdered==1
                    || numberOrdered==2);
            assertTrue(unitPrice.getAmount().intValue()==1
                    || unitPrice.getAmount().intValue()==2);
            assertTrue(expectedDeliveryDate.equals(new DateTime(timeStamp+1))
                    || expectedDeliveryDate.equals(new DateTime(timeStamp+2)));
            
            Iterator<ChargeLine> it = current.getChargeLines().iterator();
            int chargeLineCount = 0;
            
            while(it.hasNext()) {
            	chargeLineCount++;
            	it.next();
            }
            
            assertEquals(1, chargeLineCount);
            
            it = current.getChargeLines().iterator();
            ChargeLine cl = it.next();
            
            assertTrue(cl.getAmount().getAmount().intValue()==1
                    || cl.getAmount().getAmount().intValue()==2);
            assertTrue(cl.getDescription().equals("cl1description")
                    || cl.getDescription().equals("cl2description"));
            assertTrue(cl.getComment().equals("cl1comment")
                    || cl.getComment().equals("cl2comment"));
        }
	}
	
	@Test
	public void calculateAmounts() {
		
		final List<OrderLine> list = em.createQuery("SELECT o FROM OrderLine o",
				OrderLine.class).getResultList();
		
		assertEquals(2, list.size());
		
		Money m1 = new Money(2);
		Money m2 = new Money(6);
		
		for (OrderLine current : list) {
            assertTrue(current.getOrderLinePrice().equals(m1)
                    || current.getOrderLinePrice().equals(m2));
        }
	}

}
