package ca.cmpt213.a4.client.model;

import java.time.LocalDateTime;

/**
 * This is the factory class that decides which Consumable subclass to create.
 * The logic is based on the first parameter foodType. 1 as put in by the user
 * is a food item, 2 as put in by the user is drink item.
 * @author Rio Samson
 */
public class ConsumableFactory {
    private final int FOOD_ITEM = 1;

    /**
     * This method makes the instance of the food or drink item as specified by the user input.
     * @param foodType (int) type of consumable the user wants to input: 1=food, 2=drink
     * @param name (String) name of the consumable
     * @param notes (double) notes of the consumable
     * @param price (double) price of the consumable
     * @param expirationDate (LocalDateTime) expiration date of the consumable
     * @param quantity (double) quantity of the consumable
     * @return (FoodItem or DrinkItem) with appropriate attributes
     */
    public Consumable getInstance(int foodType, String name, String notes, double price,
                                  LocalDateTime expirationDate, double quantity) {

        if (foodType == FOOD_ITEM) {
            return new FoodItem(name, notes, price, expirationDate, quantity);
        }
        else {
            return new DrinkItem(name, notes, price, expirationDate, quantity);
        }
    }
}
