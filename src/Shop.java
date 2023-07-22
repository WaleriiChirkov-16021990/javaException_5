import MyException.IllegalCountException;
import MyException.NoBuyerFoundException;
import MyException.NoItemFoundException;
import MyException.TooMuchSaleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Класс описывает магазин с примитивным функционалом регистрации купли-продажи товаров (без финансовых транзакций)
 */
public class Shop {
	public static int idOrders = 0;
	final ArrayList<Item> stock;
	final ArrayList<Buyer> customers;
	private HashMap<Integer, ArrayList<Order>> orders;
	private ArrayList<Order> tempOrders;
	private HashMap<Item.Category, Discount> mySales;
	private static final int limitSale = 50;
	private static final int innerSale = 20; //20%
	
	/**
	 * Конструктор магазина
	 *
	 * @param stock     - БД товаров магазина
	 * @param customers - БД покупателей магазина
	 */
	public Shop(ArrayList<Item> stock, ArrayList<Buyer> customers) {
		this.stock = stock;
		this.customers = customers;
		this.tempOrders = new ArrayList<>();
		this.orders = new HashMap<>();
		this.mySales = new HashMap<>();
		startedSale();
	}
	
	/**
	 * Метод стартово заполняет таблицу скидок на все катогории товаров , скидка по умолчанию = 0
	 */
	public void startedSale() {
		for (Item.Category f :
				Item.Category.values()) {
			this.mySales.put(f, Discount.sale0);
		}
	}
	
	/**
	 * Метод проверяет коректность ввода количества товаров в сделке и наличие товара в БД магазина,
	 * в случае неуспеха проверок кидает ошибки
	 *
	 * @param tempBuyer - юзер
	 * @param item      Название товара
	 * @param count     - целочисленное количество
	 * @return Ордер сделки по определенному товару, в дальнейшем в чеке каждая строка это ордера сделок по конкретным товарам
	 * @throws NoItemFoundException  - неудачный поиск товара по названию в БД магазина
	 * @throws IllegalCountException - не корректный ввод количества товаров в ордер
	 */
	public Order deal(Buyer tempBuyer, String item, int count) throws NoItemFoundException, IllegalCountException {
		
		if (count <= 0) {
			throw new IllegalCountException("Count is: " + count + "\nExpected > 0");
		}
		Item tempItem = null;
		
		for (int i = 0; i < stock.size(); i++) {
			if (item.equalsIgnoreCase(stock.get(i).getName().toLowerCase())) {
				tempItem = stock.get(i);
				break;
			}
		}
		if (tempItem == null) {
			throw new NoItemFoundException("Shop doesn't have " + item);
		}
		generatedRandomDiscountForItemCategory(tempItem.getCategory());
		return new Order(tempBuyer, tempItem, count, this.mySales);
	}
	
	/**
	 * Метод купли-продажи в нашем магазине (без учета финансового оборота, нет в задании)
	 *
	 * @param buyerName - переданное имя пользователя (используется для поиска в БД пользователей магазина).
	 * @throws NoBuyerFoundException - ошибка поиска юзера, юзер не найден в БД
	 * @throws IllegalCountException - ошибка с нулевым или отрицательным значение количества товара в сделке
	 * @throws NoItemFoundException  - ошибка поиска товара в БД магазина (не найденный товар не продать).
	 */
	public void paymentByReceipt(String buyerName) throws NoBuyerFoundException {
		Buyer tempBuyer = null;
		this.tempOrders = new ArrayList<>();
		
		for (int i = 0; i < customers.size(); i++) {
			if (buyerName.equals(customers.get(i).getName())) {
				tempBuyer = customers.get(i);
				break;
			}
		}
		if (tempBuyer == null) {
			throw new NoBuyerFoundException("No such Buyer found: " + buyerName);
		}
		
		//После успешных проверок пользователю начинают сканировать товары и заносить их количество для дальнейшего завершения сделки
		try {
			addOrder(deal(tempBuyer, "Porsche", 2));
			addOrder(deal(tempBuyer, "Shampoo", 1));
			addOrder(deal(tempBuyer, "bmw", 1));
			addOrder(deal(tempBuyer, "Banana", 1));
		} catch (IllegalCountException e) {
			e.printStackTrace();
		} catch (NoItemFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		boolean totalSale = false;
		try {
			totalSale = checkTotalSaleOrder(tempBuyer);
		} catch (TooMuchSaleException e) {
			e.printStackTrace();
		}
		if (totalSale) {
			Shop.idOrders += 1;
			this.orders.put(Shop.idOrders, this.tempOrders);
			int sumNoSale = summarizeAmount(2);
			int sum = summarizeAmount(1);
			System.out.println("покупатель: " + buyerName);
			summarizeAmount(3);
			System.out.println("Скидки применились");
			System.out.println("итог. цена без скидки = " + sumNoSale);
			System.out.println("итоговая цена = " + sum + "\n");
		} else {
			Shop.idOrders += 1;
			int sum = summarizeAmount(2);
			System.out.println("покупатель: " + buyerName);
			summarizeAmount(3);
			System.out.println("Ваши скидки не применились, общая применяемая скидка = 20%");
			System.out.println("итог. цена без скидки = " + sum);
			System.out.println("итоговая цена = " + sum * (100 - innerSale) / 100 + "\n");
		}
	}
	
	/**
	 * Метод проверяет что добавляемый ордер не ссылается на пустой объект
	 *
	 * @param order - переданный ордер на проверку
	 */
	private void addOrder(Order order) throws NullPointerException {
		if (order == null) {
			throw new NullPointerException("Ордер пуст");
		}
		this.tempOrders.add(order);
	}
	
	/**
	 * Метод принимает числовой код для разного суммирования разных значений цены сделки
	 *
	 * @param code - числовой код
	 * @return - возвращает сумму
	 */
	private int summarizeAmount(int code) {
		int sumAmount = 0;
		if (code == 1) { // totalSale orders <= 50%
			
			for (Order o :
					this.tempOrders) {
				sumAmount += o.getTotalAmountOfGoods();
			}
			return sumAmount;
		} else if (code == 2) { //в случае если totalSale orders> 50%
			
			for (Order o :
					this.tempOrders) {
				sumAmount += o.getTotalPrice();
			}
			return sumAmount;
		} else if (code == 3) { //для печати цены до и после применения случайно скидки к категориям товаров
			for (Order o :
					this.tempOrders) {
				System.out.println("Товар: " + o.getItem().getName() + " count: " + o.getCount() + " price no discount: " + o.getTotalPrice());
				System.out.println("Товар: " + o.getItem().getName() + " count: " + o.getCount() + " current price: " + o.getTotalAmountOfGoods());
			}
			return sumAmount;
		} else {
			throw new RuntimeException("не правильно сосчитана сумма заказа, ошибка кода вызова функции");
		}
	}
	
	/**
	 * Метод проверяет сумму скидок по товарам в корзине покупателя
	 *
	 * @param buyer -  покупатель
	 * @return true => sum <= 50%
	 * @throws TooMuchSaleException - ошибка при sum > 50%
	 */
	private boolean checkTotalSaleOrder(Buyer buyer) throws TooMuchSaleException {
		int currentAmountDiscount = 0;
		for (Order sale :
				this.tempOrders) {
			currentAmountDiscount += sale.getSale().get(sale.getItem().getCategory()).getValue();
		}
		if (currentAmountDiscount > Shop.limitSale) {
			throw new TooMuchSaleException("Для покупателя : " + buyer.getName() + " => limit скидки превышен");
		}
		return true;
	}
	
	/**
	 * Метод генерирует случайные скидки для переданной категории товаров из созданного пеерчисления возможных значений скидок
	 *
	 * @param category - переданная категория из перечисления категорий товаров
	 */
	public void generatedRandomDiscountForItemCategory(Item.Category category) {
		Random random = new Random();
		if (!this.mySales.containsKey(category)) {
			this.mySales.put(category, Discount.values()[random.nextInt(Discount.values().length)]);
		} else {
			this.mySales.replace(category, Discount.values()[random.nextInt(Discount.values().length)]);
		}
	}
	
	/**
	 * Перечисление возможных скидок
	 */
	enum Discount {
		sale0(0), sale5(5), sale10(10), sale15(15), sale20(20);
		private final int value;
		
		Discount(int i) {
			this.value = i;
		}
		
		/**
		 * метод получения целочисленного значения скидки
		 *
		 * @return целое число = значение скидки
		 */
		public int getValue() {
			return value;
		}
	}
	
}
