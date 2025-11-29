/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author NomongiHlatshwayo
 */


// File: User.java
public class User {
    // FIELDS (Instance Variables)
    //These store information about each user
    private String username; // The unique username for the user
    private String passwordHash;  // The hashed version of the user's password (for security)
    private String email; // The user's email address
    private String role;  // The user's role (e.g., "admin", "user")

    //CONSTRUCTOR
    
    public User(String username, String passwordHash, String email, String role) {
        this.username = username; // Set the username
        this.passwordHash = passwordHash; // Set the hashed password
        this.email = email;  // Set the email
        this.role = role;   // Set the role
    }

    //Getter methods = These methods allow other classes to read user information
    public String getUsername(){ 
        return username;   // Return the username
    }
    
    public String getPasswordHash(){ 
        return passwordHash;  // Return the hashed password
    }
    
    public String getEmail(){ 
        return email;   // Return the email
    }
    
    public String getRole(){ 
        return role;  // Return the role
    }

    //Setters method = These methods allow other classes to modify user information (except username)
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash;  // Update the user's password hash
    }
    
    public void setEmail(String email){
        this.email = email;  // Update the user's email
    }
    
    public void setRole(String role){ 
        this.role = role;  // Update the user's role
    }

    // toString() Method = This method converts the user object into a single line of text.
    // The format is: username,passwordHash,email,role
    @Override
    public String toString() {
        return username + "," + passwordHash + "," + email + "," + role;
    }
    
    //fromString() method = This static method takes a line of text (from the file) and converts it back into a User object.
    public static User fromString(String line) {
        // Split the line by commas to get the different pieces of information
        String[] p = line.split(",");
        // If there are exactly 4 parts (username, passwordHash, email, role)
        if (p.length == 4) {
            // Create and return a new User object using those values
            return new User(p[0], p[1], p[2], p[3]);
        }
        // If the line is not in the correct format, return null
        return null;
    }
}