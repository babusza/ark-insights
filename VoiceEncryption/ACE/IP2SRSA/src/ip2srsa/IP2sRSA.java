/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip2srsa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author babusza
 */
public class IP2sRSA {

    /* private final String inputPath;
     private final String outputPath;
     private final String publickeyPath;
     private final String privatekeyPath;
     */
    private KeyGenerator keygen;
    private DataOutputStream out;
    private SecretKey secretKey;
    private Key key;
    //  private String ori;
    private InputStream fileOriginal;
    private Cipher cipher;
    private final SecureRandom random;
    //  private byte[] bytes;

    public IP2sRSA() throws FileNotFoundException, IOException {
        /*
         Properties prop = new Properties();
         InputStream input = new FileInputStream("config.properties");
         prop.load(input);
         inputPath = prop.getProperty("inputPath");
         outputPath = prop.getProperty("outputPath");
         publickeyPath = prop.getProperty("publickeyPath");
         privatekeyPath = prop.getProperty("privatekeyPath");
         */
        random = new SecureRandom();
    }
    /*
     public String getInputPath() {
     return inputPath;
     }

     public String getOutputPath() {
     return outputPath;
     }

     public String getPublicKeyPath() {
     return publickeyPath;
     }

     public String getPrivateKeyPath() {
     return privatekeyPath;
     }
     */

    public SecureRandom getSecureRandom() {
        return random;
    }

    public KeyGenerator setKeyGenerator() throws NoSuchAlgorithmException {
        return keygen = KeyGenerator.getInstance("AES");
    }

    public SecretKey setSecretKey() throws NoSuchAlgorithmException {
        keygen.init(random);
        secretKey = keygen.generateKey();
        return secretKey;
    }

    public Key getPublicKey() throws Exception {
        InputStream in = getClass().getResourceAsStream("public.txt");
        ObjectInputStream keyIn = new ObjectInputStream(in);
        key = (Key) keyIn.readObject();

        return key;
    }

    public Key getPrivateKey() throws Exception {
        // System.out.println(getClass().getResource("private.txt"));
        InputStream in = getClass().getResourceAsStream("private.txt");
        ObjectInputStream keyIn = new ObjectInputStream(in);
        key = (Key) keyIn.readObject();
        return key;
    }

    public void setDataOutputEncrypt(String fileName) throws FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, this.key);
        byte[] wrappedKey = cipher.wrap(this.secretKey);
        out = new DataOutputStream(new FileOutputStream(fileName));
        out.writeInt(wrappedKey.length);
        out.write(wrappedKey);
    }

    public void setDataOutputDecrypt(String fileName) throws FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException {
        //    System.out.println("key:" + this.key);
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, this.key);
        out = new DataOutputStream(new FileOutputStream(fileName));
    }

    public InputStream setFileOriginal(String fileName) throws FileNotFoundException {
        fileOriginal = new FileInputStream(fileName);
        return fileOriginal;
    }

    public boolean encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, Exception {
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        crypt(fileOriginal, out, cipher);
        fileOriginal.close();
        out.close();
        return true;
    }

    public boolean decrypt(String fileEncrypt) throws Exception {
        DataInputStream in = new DataInputStream(new FileInputStream(fileEncrypt));
        int length = in.readInt();
        byte[] wrappedKey = new byte[length];
        in.read(wrappedKey, 0, length);
        key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        crypt(in, out, cipher);
        out.close();
        in.close();
        return true;
    }

    public static void crypt(InputStream in, OutputStream out, Cipher cipher) throws Exception {
        System.out.println("algorithm "+cipher.getAlgorithm());
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];
        int inLength = 0;
        boolean more = true;
        while (more) {
            inLength = in.read(inBytes);            
            if (inLength == blockSize) {
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outLength);
            } else {
                more = false;
            }
        }
        if (inLength > 0) {
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        } else {
            outBytes = cipher.doFinal();
        }
        out.write(outBytes);
    }

    public boolean deleteOriginal(String fileName) throws IOException {
        File file = new File(fileName);
        file.delete();
        return true;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                throw new Exception("invalid parameter");
            }
            if (args[0].equals("D")) {
                // option input output
                IP2sRSA rsa = new IP2sRSA();
                rsa.setKeyGenerator();
                rsa.setSecretKey();
                rsa.getPrivateKey();
                rsa.setDataOutputDecrypt(args[2]);
                rsa.decrypt(args[1]);
            } else if (args[0].equals("E")) {
                // option input output                
                IP2sRSA rsa = new IP2sRSA();
                rsa.setKeyGenerator();
                rsa.setSecretKey();
                rsa.getPublicKey();
                InputStream out = rsa.setFileOriginal(args[1]);
                rsa.setDataOutputEncrypt(args[2]);
                rsa.encrypt();
            } else if (args[0].equals("G")) {/*
                 KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
                 SecureRandom random = new SecureRandom();
                 pairgen.initialize(512, random);
                 KeyPair keyPair = pairgen.generateKeyPair();
                 ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[1]));
                 out.writeObject(keyPair.getPublic());
                 out.close();
                 out = new ObjectOutputStream(new FileOutputStream(args[2]));
                 out.writeObject(keyPair.getPrivate());
                 out.close();*/

            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
