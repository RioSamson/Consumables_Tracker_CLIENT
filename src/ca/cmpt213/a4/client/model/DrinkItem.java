package ca.cmpt213.a4.client.model;

import java.time.LocalDateTime;

/**
 * Subclass of Consumable object. Represents a drink item.
 * Stores unique drink item attributes like volume. Customized toString that shows
 * it is a drink item and displays the volume of the drink item.
 * @author Rio Samson
 */
public class DrinkItem extends Consumable {
    private double volume;

    /**
     * Constructor for the drink item class
     * @param name (String) name of the drink
     * @param notes (double) notes of the drink
     * @param price (double) price of the drink
     * @param expirationDate (LocalDateTime) epiration date of the drink
     * @param volume (double) volume of the drink
     */
    public DrinkItem(String name, String notes, double price, LocalDateTime expirationDate, double volume) {
        super(name, notes, price, expirationDate);
        this.volume = volume;
    }

    /**
     * toString: contains a long string the has all the basic information about the drink including
     * expiration date, notes, price, name
     * @return String the has all the information about the drink
     */
    @Override
    public String toString() {
        long daysToExpire = this.daysTillExpire();
        StringBuilder result = new StringBuilder("");

        //append all the necessary information to the string builder
        result.append("Name: ").append(super.getName()).append("\nNotes: ").append(super.getNotes()).append("\n");
        result.append("Price: ").append(super.getDECIMAL_FORMAT().format(super.getPrice())).append("\n");
        result.append("Volume: ").append(super.getDECIMAL_FORMAT().format(volume)).append("\n");
        result.append("Expiry date: ").append(super.getDATE_FORMATTER().format(super.getExpirationDate())).append('\n');

        //display different strings for different drink
        if (daysToExpire == 0) {
            result.append("This drink item expires today.");
        } else if (daysToExpire > 0) {
            result.append("This drink item will expire in ").append(daysToExpire).append(" day(s).");
        } else {
            result.append("This drink item is expired for ").append(daysToExpire * (-1)).append(" day(s).");
        }
        return result.toString();
    }
}