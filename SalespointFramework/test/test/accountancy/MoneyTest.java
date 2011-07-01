package test.accountancy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.database.Database;

public class MoneyTest {
	private EntityManagerFactory emf = Database.INSTANCE
	.getEntityManagerFactory();
	
	
	@BeforeClass
	public static void classSetup() {
		Database.INSTANCE.initializeEntityManagerFactory("SalespointFramework");
		
	}

	@Test
	public void persistMoney() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(new Money(1));
		em.getTransaction().commit();
	}
}
