package org.salespointframework.core.product.later;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.Product;
import org.salespointframework.core.quantity.Quantity;

/**
 * This is an interface which provides basic methods to handle
 * MeasuredProductInstances.
 * 
 */
public interface MeasuredProduct extends Product
{
	/**
	 * Returns the Quantity
	 * 
	 * @return the quantity of this MeasuredProductInstance
	 */
	public Quantity getQuantity();
	/**
	 * Returns the Price of this MeasuredProductInstance
	 * 
	 * @return the price of this MeasuredProductInstance
	 */
	@Override
	public Money getPrice();
}