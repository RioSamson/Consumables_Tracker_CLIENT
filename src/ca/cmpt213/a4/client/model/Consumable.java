package ca.cmpt213.a4.client.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This is the superclass Consumables, which has subclasses FoodItem and DrinkItem.
 * This class stores the common attributes of the food types like name, notes, price, expiration date.
 * @author Rio Samson
 */
public class Consumable implements Comparable<Consumable> {
    private String name;
    private String notes;
    private double price;
    private LocalDateTime expirationDate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    /**
     * Constructor for the Consumables class.
     * @param name String name of the consumable item
     * @param notes String notes of the consumable item
     * @param price double price of the consumable item
     * @param expirationDate LocalDateTime expiry date of the consumable item
     */
    public Consumable(String name, String notes, double price, LocalDateTime expirationDate) {
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    /**
     * getter method for DateTimeFormatter
     * @return DATE_FORMATTER
     */
    public DateTimeFormatter getDATE_FORMATTER() {
        return DATE_FORMATTER;
    }

    /**
     * getter method for DecimalFormat
     * @return DECIMAL_FORMAT
     */
    public DecimalFormat getDECIMAL_FORMAT() {
        return DECIMAL_FORMAT;
    }

    /**
     * getter method for the notes
     * @return String notes of the consumable item
     */
    public String getNotes() {
        return notes;
    }

    /**
     * getter method for the price
     * @return double price of the consumable item
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gives the expiration date of the food iem
     * @return localDateTime date of the expiration
     */
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Give the name of the food
     * @return string name of the food
     */
    public String getName() {
        return name;
    }

    /**
     * calculates if the food is expired or not
     * @return boolean true if expired, false otherwise
     */
    public boolean isExpired() {
        LocalDateTime dateNow = LocalDateTime.now();
        int compare = this.expirationDate.compareTo(dateNow);
        return compare <= 0;
    }

    /**
     * calculates the number of days till expiration
     * @return long days till expiry
     */
    public long daysTillExpire() {
        LocalDateTime dateNow = LocalDateTime.now().withHour(23).withMinute(59).withSecond(0).withNano(0);
        long numDays;
        numDays = dateNow.until(this.expirationDate, DAYS);
        return numDays;
    }

    /**
     * This is an override comparable function to aid with comparing Consumable
     * objects. Such as in the case with sorting or any comparisons.
     * @param other - the object to compare with
     * @return positive int if bigger, 0 if same, negative int if smaller
     */
    @Override
    public int compareTo(Consumable other) {
        return expirationDate.compareTo(other.expirationDate);
    }
}
