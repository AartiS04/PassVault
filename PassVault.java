import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

public class PassVault {

    static final String MASTER_FILE = "master.txt";
    static final String VAULT_FILE = "vault.txt";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        File masterFile = new File(MASTER_FILE);

        // Setting of the master password
        if (!masterFile.exists()) {
            System.out.print("Set master password: ");
            String masterPass = sc.nextLine();
            FileWriter fw = new FileWriter(MASTER_FILE);
            fw.write(masterPass);
            fw.close();
            System.out.println("Master password set.");
        }

        // Master password login
        BufferedReader br = new BufferedReader(new FileReader(MASTER_FILE));
        String savedPass = br.readLine();
        System.out.print("Enter master password: ");
        String inputPass = sc.nextLine();

        if (!inputPass.equals(savedPass)) {
            System.out.println("Wrong password. Exiting...");
            return;
        }

        // Menu
        while (true) {
            System.out.println("\n--- PassVault ---");
            System.out.println("1. Add Credential");
            System.out.println("2. View Credentials");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addCredential(savedPass);
                case 2 -> viewCredentials(savedPass);
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Add credentials (encrypting and saving)
    static void addCredential(String key) throws Exception {
        System.out.print("Enter website: ");
        String site = sc.nextLine();
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        String entry = site + "," + user + "," + pass;
        String encrypted = encrypt(entry, key);

        FileWriter fw = new FileWriter(VAULT_FILE, true);
        fw.write(encrypted + "\n");
        fw.close();

        System.out.println("Credential saved (encrypted).");
    }

    // View decrypted credentials
    static void viewCredentials(String key) throws Exception {
        File file = new File(VAULT_FILE);
        if (!file.exists()) {
            System.out.println("No credentials saved yet.");
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(VAULT_FILE));
        String line;
        System.out.println("\n--- Decrypted Credentials ---");
        while ((line = br.readLine()) != null) {
            try {
                String decrypted = decrypt(line, key);
                String[] parts = decrypted.split(",");
                System.out.println("Site: " + parts[0] + " | User: " + parts[1] + " | Pass: " + parts[2]);
            } catch (Exception e) {
                System.out.println("Error decrypting entry.");
            }
        }
        br.close();
    }

    // AES Encryption
    static String encrypt(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(fixKey(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encBytes);
    }

    // AES Decryption
    static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(fixKey(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decBytes = cipher.doFinal(decoded);
        return new String(decBytes);
    }

    // Fix key to 16 bytes (AES-128)
    static byte[] fixKey(String key) {
        byte[] bytes = new byte[16];
        byte[] inputBytes = key.getBytes();
        System.arraycopy(inputBytes, 0, bytes, 0, Math.min(inputBytes.length, bytes.length));
        return bytes;
    }
}
