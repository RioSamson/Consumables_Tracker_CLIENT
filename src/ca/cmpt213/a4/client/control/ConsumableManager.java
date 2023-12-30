package ca.cmpt213.a4.client.control;

import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumableFactory;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the food objects and operations pertaining to organization of these objects
 * Most logic done in this class. Contains a list of food objects
 * methods to add, delete the list etc. according to the menu options
 * @author Rio SamsonC
 */
public class ConsumableManager implements Iterable<Consumable>{
    private List<Consumable> foods = new ArrayList<>();
    private List<Integer> foodTypes = new ArrayList<>();
    private List<Double> quantities = new ArrayList<>();
    private ConsumableFactory factory = new ConsumableFactory();

    /**
     * basic empty constructor
     */
    public ConsumableManager() {
    }

    /**
     * Adds a new food item to the foods ArrayList in the right position: oldest to newest
     * @param food - the object food that is being added
     */
    public void add(Consumable food, int foodType, double quantity) {
        if (foods.isEmpty()) {
            foods.add(food);
            quantities.add(quantity);
            foodTypes.add(foodType);
        } else {
            int i = 0;
            //while loop to go through the list and add food to valid index
            //compares expiration dates
            boolean isDone = false;
            while(!isDone) {
                int compare = food.getExpirationDate().compareTo(foods.get(i).getExpirationDate());
                if (compare <= 0) {
                    foods.add(i, food);
                    quantities.add(i, quantity);
                    foodTypes.add(i, foodType);
                    isDone = true;
                } else if (i + 1 == foods.size()) {
                    foods.add(food);
                    quantities.add(quantity);
                    foodTypes.add(foodType);
                    isDone = true;
                }
                i++;
            }
        }
    }

    /**
     * @return true if foods list is empty, false otherwise
     */
    public boolean isEmpty() {
        return foods.isEmpty();
    }

    /**
     * @return size of list containing food
     */
    public int size() {
        return foods.size();
    }

    /**
     * Deletes the food object from foods array list
     * @param index of which food item to remove
     */
    public ArrayList<Consumable> delete(int index) {
        return getServerList("curl -X POST localhost:8080/removeItem/" + index,
                "curl -X GET localhost:8080/getQuantities",
                "curl -X GET localhost:8080/getTypes");
    }

    /**
     * Returns the name of the food item requested from list
     * calls Food method to get its name
     * @param index of the food item in the list
     * @return string name of the food item
     */
    public String getName(int index) {
        return foods.get(index).getName();
    }

    /**
     * Makes an ArrayList of all the food oldest to newest
     * @return ArrayList of all foods
     */
    public ArrayList<Consumable> allList() {
        ArrayList<Consumable> loadedFoods = getServerList("curl -X GET localhost:8080/listAll",
                "curl -X GET localhost:8080/getQuantities",
                "curl -X GET localhost:8080/getTypes");
        foods = loadedFoods;
        return loadedFoods;
    }

    /**
     * Makes an ArrayList of all the food that has already expired excluding today. oldest to newest
     * calls food method to check if the food is expired
     * @return ArrayList of the expired foods
     */
    public ArrayList<Consumable> expiredList() {
        return getServerList("curl -X GET localhost:8080/listExpired",
                "curl -X GET localhost:8080/getExpQuantities",
                "curl -X GET localhost:8080/getExpTypes");
    }

    /**
     * Makes an ArrayList of all the foods that are not already expired including today. Oldest to newest
     * calls food method to check if the food is expired
     * @return ArrayList of all the nonExpired food
     */
    public ArrayList<Consumable> nonExpiredList() {
        return getServerList("curl -X GET localhost:8080/listNonExpired",
                    "curl -X GET localhost:8080/getNonExpQuantities",
                    "curl -X GET localhost:8080/getNonExpTypes");
    }

    /**
     * Makes an ArrayList of all the foods that will expire in the next 7 days inclusive.
     * calls food method to check how many days for food to expire
     * @return ArrayList of foods that expire in 7 days
     */
    public ArrayList<Consumable> expireInWeek() {
        return getServerList("curl -X GET localhost:8080/listExpiringIn7Days",
                    "curl -X GET localhost:8080/getWeekExpQuantities",
                    "curl -X GET localhost:8080/geWeekExpTypes");
    }

    /**
     * Gets the appropriate list of consumables from the server using https.
     * It also uses helper methods to get needed information like the item types and quantities.
     * The extra information just to remake the consumable items again for to-string display of child class
     * @param listCommand the command that the server takes to return the needed list
     * @param quantitiesCommand the command that the server takes to return the needed quantities list
     * @param typesCommand the command that the server takes to return the needed item type list
     * @return return the appropriate list of consumables needed re-made to properly display foodItem and
     * drink item to-strings
     */
    private ArrayList<Consumable> getServerList(String listCommand, String quantitiesCommand, String typesCommand) {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
        Process process;
        String command = listCommand;
        String text;
        ArrayList<Consumable> list = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Type type = new TypeToken<ArrayList<Consumable>>(){}.getType();
            List<Consumable> object = myGson.fromJson(text, type);
            ArrayList<Double> quantities = getListOfQuantities(quantitiesCommand);
            ArrayList<Integer> types = getListOfTypes(typesCommand);
            int i = 0;
            if(object != null) {
                for(Consumable items: object) {
                    Consumable consumable = factory.getInstance(types.get(i), items.getName(),
                            items.getNotes(), items.getPrice(), items.getExpirationDate(), quantities.get(i));
                    list.add(consumable);
                    i++;
                }
            }
            return list;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Helper method to get the list of quantities for the items already gotten from the server
     * @param commandString the command used to get the list
     * @return returns the list of double quantities
     */
    private ArrayList<Double> getListOfQuantities(String commandString) {
        List<Double> list;
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
        Process process;
        String command = commandString;
        String text;
        ArrayList<Double> returnList = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Type type = new TypeToken<ArrayList<Double>>(){}.getType();
            list = myGson.fromJson(text, type);
            if( list != null) {
                for(Double value : list) {
                    returnList.add(value);
                }
            }
            return returnList;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return returnList;
    }

    /**
     * Helper method to get the list of item types for the consumables already gotten from the server
     * @param commandString the command used to get the list
     * @return returns the list of integer types
     */
    private ArrayList<Integer> getListOfTypes(String commandString) {
        List<Integer> list = new ArrayList<>();
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
        Process process;
        String command = commandString;
        String text;
        ArrayList<Integer> returnList = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            Type type = new TypeToken<ArrayList<Integer>>(){}.getType();
            list = myGson.fromJson(text, type);
            if( list != null) {
                for(Integer value : list) {
                    returnList.add(value);
                }
            }
            return returnList;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return returnList;
    }

    /**
     * Iterator to go through the food objects in the foods list
     * @return an iterator for the list
     */
    @Override
    public Iterator<Consumable> iterator() {
        return foods.iterator();
    }
}