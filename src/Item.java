/**
 * Класс товара
 */
public class Item {
	
	private String name;
	
	private int price;
	
	private Category category;
	
	public Item(String name, int price, Category category) {
		this.name = name;
		this.price = price;
		this.category = category;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public Category getCategory() {
		return category;
	}
	
	/**
	 * Перечисление категорий товаров магазина
	 */
	public enum Category {HYGIENE_PRODUCTS, HOUSEHOLD_GOODS, CARS}
}
