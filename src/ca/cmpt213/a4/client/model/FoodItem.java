package ca.cmpt213.a4.client.model;

import java.time.LocalDateTime;

/**
 * Subclass of Consumable object. Represents a food item (excludes drinks).
 * Stores unique food item attributes like weight. Customized toString that shows
 * it is a food item and displays the weight of the food item.
 * @author Rio Samson
 */
public class FoodItem extends Consumable {
    private double weight;

    /**
     * Constructor for the food item
     * @param name (String) name of the food item
     * @param notes (double) notes of the food item
     * @param price (double) price of the food item
     * @param expirationDate (LocalDateTime) expiration date of the food item
     * @param weight (double) weight of the food item
     */
    public FoodItem(String name, String notes, double price, LocalDateTime expirationDate, double weight) {
        super(name, notes, price, expirationDate);
        this.weight = weight;
    }

    /**
     * toString: contains a long string the has all the basic information about the food including
     * expiration date, notes, price, name
     * @return String the has all the information about the food
     */
    @Override
    public String toString() {
        long daysToExpire = this.daysTillExpire();
        StringBuilder result = new StringBuilder("");

        //append all the necessary information to the string builder
        result.append("Name: ").append(super.getName()).append("\nNotes: ").append(super.getNotes()).append("\n");
        result.append("Price: ").append(super.getDECIMAL_FORMAT().format(super.getPrice())).append("\n");
        result.append("Weight: ").append(super.getDECIMAL_FORMAT().format(weight)).append("\n");
        result.append("Expiry date: ").append(super.getDATE_FORMATTER().format(super.getExpirationDate())).append('\n');

        //display different strings for different foods
        if (daysToExpire == 0) {
            result.append("This food item expires today.");
        } else if (daysToExpire > 0) {
            result.append("This food item will expire in ").append(daysToExpire).append(" day(s).");
        } else {
            result.append("This food item is expired for ").append(daysToExpire * (-1)).append(" day(s).");
        }
        return result.toString();
    }
}
