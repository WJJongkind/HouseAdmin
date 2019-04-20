package util;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

/**
 * Objects of this class can be used to hash a password either with a given
 * hash or a newly generated hash.
 * @author Wessel Jongkind
 * @version 21-06-2017
 */
public class PasswordHasher
{
    public static void main(String[] args) throws Exception {
        PasswordHasher hasher = new PasswordHasher();
        hasher.setPassword("cayoesquelito1");
        hasher.hash();
        System.out.println(hasher.hash);
        System.out.println(hasher.salt);
    }
    
    /**
     * The password that is to be hashed.
     */
    private String password;
    
    /**
     * The hash that is generated from the password and the salt.
     */
    private String hash;
    
    /**
     * The salt used to hash the password.
     */
    private String salt;
    
    /**
     * This method hashes the password with the given salt. 
     * @param salt The salt used to hash the password.
     * @throws Exception When no password has been set to be hashed.
     * @see #setPassword(java.lang.String) setPassword(String password)
     * @see #hash() hash()
     */
    public void hash(String salt) throws Exception
    {
        if(password == null)
            throw new Exception("No password has been set to be hashed.");
        String salted = password + salt;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(salted.getBytes());
        
        /*
            The String needs to  be encoded to  Base64. If we simply
            use String(byte[]) to create the String and then store this
            in a database, the byte-values might be altered during storage. When
            then later on comparing two hashes, they might not be equal
            on a byte-array level even though their representation is the same.
        */
        hash = Base64.getEncoder().encodeToString(messageDigest.digest());
    }
    
    /**
     * This method hashes the password with a randomly generated salt.
     * @throws Exception When no password has been specified.
     * @see #setPassword(java.lang.String) setPassword(String password)
     * @see #hash(java.lang.String) hash(String salt)
     */
    public void hash() throws Exception
    {
        Random random = new Random();
        salt = random.nextLong() + "" + random.nextLong();
        hash(salt);
    }
    
    /**
     * This method can be used to set the password that is to be hashed.
     * @param password The password that is to be hashed.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * This method can be used to obtain the hash that is the product of hashing
     * the password with the given salt.
     * @return The hash produced by hashing the password with the given salt.
     */
    public String getHash()
    {
        return hash;
    }
    
    /**
     * This method can be used to obtain the salt used to hash the password.
     * @return The salt that is used to hash the password.
     */
    public String getSalt()
    {
        return salt;
    }
    
    /**
     * This method can be used to obtain the password that is set to be hashed.
     * @return The password that is set to be hashed.
     */
    public String getPassword()
    {
        return password;
    }
}
