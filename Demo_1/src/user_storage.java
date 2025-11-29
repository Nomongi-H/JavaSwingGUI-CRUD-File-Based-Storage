
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author NomongiHlatshwayo
 * Class to handle saving, reading, and managing user data in a text file
 */

public class user_storage {
    // Constant for the filename where user data will be stored
  private static final String FILE = "users.txt";

    // Method to hash a password using SHA-256 for secure storage
    public static String hashPassword(String password) {
        try {
             // Create a SHA-256 message digest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
             // Convert the password into bytes and hash it
            byte[] hash = digest.digest(password.getBytes());
             // Convert the byte array into a readable hexadecimal string
            StringBuilder sb = new StringBuilder();
            // Return the final hashed string
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            // Wrap and rethrow any errors as a runtime exception
            throw new RuntimeException(e);
        }
    }

    // Method to read all users stored in the text file
    public static List<User> readAll() {
        File file = new File(FILE);
         // If the file doesn't exist, return an empty list
        if (!file.exists()) return new ArrayList<>();
        try {
             // Read each line, convert it into a User object, filter out nulls, and collect into a list
            return Files.lines(file.toPath()) 
                        .map(User::fromString)  // Convert text line to User object
                        .filter(Objects::nonNull) // Exclude any invalid lines
                        .collect(Collectors.toList());  // Collect users into a list
        } catch (IOException e) {
              // Print the error if something goes wrong during reading
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Helper method to overwrite the file with the entire user list
    private static boolean saveAll(List<User> users) {
         // Create a temporary file to avoid corruption if saving fails
        File temp = new File(FILE + ".tmp");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
            // Write each user as a line in the temporary file
            for (User u : users) {
                bw.write(u.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            // Print error and return false if saving fails
            e.printStackTrace();
            return false;
        }
         // Replace the original file with the temporary file
        File orig = new File(FILE);
        orig.delete();  // Delete the old file
        return temp.renameTo(orig);  // Rename temp file to the original filename
    }

    // CREATE = Method to add a new user to storage
    public static boolean addUser(User user) {
        List<User> users = readAll(); // Read existing users
           // Check if a user with the same username already exists
        boolean exists = users.stream() //.stream() turns that list into a stream
                              .anyMatch(u -> u.getUsername().equalsIgnoreCase(user.getUsername()));
        if (exists) 
            return false; // If username exists, do not add
        users.add(user); // Add the new user
        return saveAll(users);  // Save the updated list

    }

    // UPDATE = Method to update an existing user's details
    public static boolean updateUser(User updated) {
        List<User> users = readAll();
        boolean found = false;
        // Loop through the list to find the matching username
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(updated.getUsername())) {
                users.set(i, updated); // Replace old user with updated one
                found = true;
                break;
            }
        }
        // Only save if the user was found and updated
        return found && saveAll(users);
    }

    // DELETE = Method to delete a user by username
    public static boolean deleteUser(String username) {
        List<User> users = readAll();
        // Remove the user that matches the username (case-insensitive)
        boolean removed = users.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
        // Save the updated list only if a user was removed
        return removed && saveAll(users);
    }

    // LOGIN = Method to check if username and password match a stored user
    public static boolean authenticate(String username, String password) {
         // Hash the provided password to compare with stored hash
        String hashed = hashPassword(password);
        // Check if any user matches both username and password hash
        return readAll().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username) &&
                            u.getPasswordHash().equals(hashed));
    }
}