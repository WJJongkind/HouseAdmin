package util;

import java.util.List;
import java.util.Random;

/**
 * This class is used to generate random ID strings. It also provides functionality
 * to make the generated String unique in a String-set or in a database table.
 * @author Wessel Jongkind
 * @version 27-06-2017
 */
public class RandomIDGenerator
{
    /**
     * Generates a random ID-String.
     * @return The randomly generated ID-String.
     */
    public static String generateID()
    {
        Random random = new Random();
        return random.nextLong() + "" + random.nextLong();
    }
    
    /**
     * Generates a random ID-String which is unique in the given set of IDs.
     * @param idset A list containing all the IDs that the newly generated ID should be unique in.
     * @return The randomly generated unique ID-String.
     */
    public static String generateUniqueID(List<String> idset)
    {
        String id = generateID();
        if(idset.contains(id))
            return generateUniqueID(idset);
        else
            return id;
    }
}
