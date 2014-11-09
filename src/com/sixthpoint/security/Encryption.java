package com.sixthpoint.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Brandon
 */
public class Encryption {

    // A user chosen password to be used for the PBEKeySpec (This is completely random). Used for encoding / decoding (DO NOT CHANGE)
    private static final char[] PBEKeyPassword = "nviowefjklaasdlkjweklnvq".toCharArray();

    // Private salt for PBE algorithm (randomly changes) for encoding
    private static final byte[] PBESALT = {(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12};

    /**
     * Used in encryption for how many cycles (DO NOT CHANGE)
     */
    public static final int PBKDF2_ITERATIONS = 1000;

    /**
     * Typical test case to assure that Encoding / Decoding / Encryption work
     * properly
     *
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("1. Encryption Test");

        String password = "changeit";

        int i = 0;
        while (i < 1000) {

            try {
                // Create has in the form of (salt:hash)
                String hash = createHash(password);

                String[] params = hash.split(":");
                byte[] salt = fromHex(params[0]);
                byte[] hash5 = fromHex(params[1]);

                if (validatePassword(password, hash5, salt)) {

                    System.out.println("valid password!");
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                System.out.println("ERROR: " + ex);
            }

            i++;
        }

        System.out.println("2. Encoding / Decoding Test");

        i = 0;
        while (i < 1000) {
            try {
                String originalPassword = "mysecret";
                System.out.println("Original password: " + originalPassword);
                String encryptedPassword = encode(originalPassword);
                System.out.println("Encoded password: " + encryptedPassword);
                String decryptedPassword = decode(encryptedPassword);
                System.out.println("Decoded password: " + decryptedPassword);

            } catch (GeneralSecurityException | IOException ex) {

                System.out.println("Encoding / Decoding error: " + ex.getMessage());
            }
            i++;
        }
    }

    /**
     * Takes in a string which is then salted with a given password used for the
     * 2 way encryption
     *
     * @param data
     * @return
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String encode(String data) throws GeneralSecurityException, UnsupportedEncodingException {

        // Get algorithm to use
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

        // Generates a secretKey object from the provided key specification
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PBEKeyPassword));

        // Returns a cipher object that implements the specified transformation
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initializes cipher with public key from given certificate using encrypt mode
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(PBESALT, 20));

        // Returns the cipher encoded to a 64bit string (aka base64 encoded). getBytes() forms a new byte array
        return DatatypeConverter.printBase64Binary(pbeCipher.doFinal(data.getBytes("UTF-8")));
    }

    /**
     * Takes in encrypted data and uses the salt to decrypt it to its original
     * state
     *
     * @param data
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decode(String data) throws GeneralSecurityException, IOException {

        // Get algorithm to use
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

        // Generates a secretKey object from the provided key specification
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PBEKeyPassword));

        // Returns a cipher object that implements the specified transformation
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initializes cipher with public key from given certificate using decrypt mode
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(PBESALT, 20));

        // Returns a string of the orignally encrypted data
        return new String(pbeCipher.doFinal(DatatypeConverter.parseBase64Binary((data))), "UTF-8");
    }

    /**
     * Validates a password using a hash.
     *
     * @param password the password to check
     * @param hash
     * @param salt
     * @return true if the password is correct, false if not
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static boolean validatePassword(char[] password, byte[] hash, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // Compute the hash of the provided password, using the same salt, iteration count, and hash length
        byte[] testHash = pbkdf2(password, salt, PBKDF2_ITERATIONS, hash.length);

        // Compare the hashes in constant time. The password is correct if both hashes match.
        return slowEquals(hash, testHash);
    }

    /**
     * Validates a password using a hash.
     *
     * @param password the password to check
     * @param hash
     * @param salt
     * @return true if the password is correct, false if not
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static boolean validatePassword(String password, byte[] hash, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        return validatePassword(password.toCharArray(), hash, salt);
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password the password to hash.
     * @param salt the salt
     * @param iterations the iteration count (slowness factor)
     * @param bytes the length of the hash to compute in bytes
     * @return the PBDKF2 hash of the password
     */
    private static byte[] pbkdf2(final char[] password, final byte[] salt, final int iterationCount, final int keyLength) {

        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(password, salt, iterationCount, keyLength * 8)).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return createHash(password.toCharArray());
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password in the form of SALT:HASH
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    public static String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {

        // Generate a random salt
        byte[] salt = getSalt().getBytes();

        // Hash the password
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, 24);

        // Format salt:hash
        return toHex(salt) + ":" + toHex(hash);
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Arrays.toString(salt);
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param array the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private static String toHex(byte[] array) {

        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
    public static byte[] fromHex(String hex) {

        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {

        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

}
