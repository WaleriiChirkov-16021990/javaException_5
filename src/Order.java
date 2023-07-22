import java.util.HashMap;

/*
Задача: Эмуляция интернет-магазина
написать классы покупатель, товар и заказ;
создать массив покупателей, массив товаров и массив заказов;
создать статический метод “совершить покупку” со строковыми параметрами, соответствующими полям объекта заказа. Метод должен вернуть объект заказа
Если в метод передан несуществующий покупатель, товар или отрицательное количество, метод должен выбросить соответствующее исключение;
Вызвать метод совершения покупки несколько раз таким образом, чтобы заполнить массив покупок возвращаемыми значениями. Обработать исключения.
Вывести в консоль итоговое количество совершённых покупок после выполнения приложения.

 */

/**
 * Класс сделки
 */
public class Order {
	private Buyer buyer;
	private Item item;
	private int countItems;
	private int totalPrice;
	private int totalAmountOfGoods;
	private int count;
	
	private HashMap<Item.Category, Shop.Discount> sale;
	private static int counter;
	
	/**
	 * Конструктор, некоторые поля сделки разчитываются автоматически из переданных данных.
	 * @param buyer - покупатель
	 * @param item - товар
	 * @param countItem - количество указанного товара
	 * @param mySales - текущие дейтсвующие скидки магазина.
	 */
	public Order(Buyer buyer, Item item, int countItem, HashMap<Item.Category, Shop.Discount> mySales) {
		this.buyer = buyer;
		this.item = item;
		this.count = countItem;
		this.setTotalPrice(item.getPrice() * countItem);
		this.setTotalAmountOfGoods((item.getPrice() * countItem) - (item.getPrice() * countItem * mySales.get(item.getCategory()).getValue()) / 100);
		this.sale = mySales;
		counter++;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getTotalAmountOfGoods() {
		return totalAmountOfGoods;
	}
	
	private void setTotalAmountOfGoods(int totalAmountOfGoods) {
		this.totalAmountOfGoods = totalAmountOfGoods;
	}
	
	public HashMap<Item.Category, Shop.Discount> getSale() {
		return sale;
	}
	
	@Override
	public String toString() {
		return "Order{" +
				"buyer=" + buyer +
				", item=" + item +
				", countItems=" + countItems +
				", totalPrice=" + totalPrice +
				", totalAmountOfGoods=" + totalAmountOfGoods +
				", count=" + count +
				", sale=" + sale +
				'}';
	}
}
