package com.sixthpoint.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

/**
 * MD5 based 2 way encryption
 *
 * @author Brandon
 */
public class MD5andDES {

    // A user chosen password used for the PEKeySpec. Once set do not change or all previously encoded data will be lost
    private static final char[] PBEKeyPassword = "nviowefjklaasdlkjweklnvq".toCharArray();

    // Private salt for PBE algorithm
    private static final byte[] PBESALT = {(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12};

    private static final Integer checkLoops = 10;

    private static final String password = "changeit";

    /**
     * Test case
     *
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("Encoding / Decoding Test\n");

        for (int i = 0; i <= checkLoops; i++) {

            try {

                System.out.println("Original password: " + password);
                String encryptedPassword = encode(password);
                System.out.println("Encoded password: " + encryptedPassword);
                String decryptedPassword = decode(encryptedPassword);
                System.out.println("Decoded password: " + decryptedPassword);

            } catch (GeneralSecurityException | IOException ex) {
                System.out.println("Encoding / Decoding error: " + ex.getMessage());
            }
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

        // Initializes cipher with public key
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

        // Initializes cipher with public key 
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(PBESALT, 20));

        // Returns a string of the orignally encrypted data
        return new String(pbeCipher.doFinal(DatatypeConverter.parseBase64Binary((data))), "UTF-8");
    }

}
