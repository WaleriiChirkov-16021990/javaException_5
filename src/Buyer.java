/**
 * Класс покупателя
 */
public class Buyer {

   private String name;
   private int money;


    public Buyer(String name, int money) {
        this.name = name;
        this.money = money;
    }

    
    public String getName() {
        return name;
    }
}
