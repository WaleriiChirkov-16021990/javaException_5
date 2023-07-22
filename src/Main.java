import MyException.IllegalCountException;
import MyException.NoBuyerFoundException;
import MyException.NoItemFoundException;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		
		ArrayList<Item> items = new ArrayList<>();
		// генерируем товары в БД магазина
		items.add(new Item("Shampoo", 10, Item.Category.HYGIENE_PRODUCTS));
		items.add(new Item("Toothpaste", 20, Item.Category.HYGIENE_PRODUCTS));
		items.add(new Item("Banana", 30, Item.Category.HOUSEHOLD_GOODS));
		items.add(new Item("Rope", 50, Item.Category.HOUSEHOLD_GOODS));
		items.add(new Item("Porsche", 100, Item.Category.CARS));
		items.add(new Item("Lada", 40, Item.Category.CARS));
		items.add(new Item("BMW", 60, Item.Category.CARS));
		
		ArrayList<Buyer> buyers = new ArrayList<>();
		// генерируем покупателей в БД магазина
		buyers.add(new Buyer("Ilia", 1000));
		buyers.add(new Buyer("Sabina", 5000));
		
		Shop shop = new Shop(items, buyers);
		try { //пытаемся продать товары покупателям
			shop.paymentByReceipt("Ilia");
			shop.paymentByReceipt("Sabina");
			shop.paymentByReceipt("Sabin");
		} catch (NoBuyerFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("-------------");
		System.out.println("Total deals: " + Shop.idOrders); // выводим число успешных продаж(оплаченных чеков) товаров.
	}
	
	
}