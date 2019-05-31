/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.*;
import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.websocket.OnError;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;


/**
 *
 * @author stephen
 */
@ServerEndpoint("/server")
public class Server {
    private boolean pass;
    
    private boolean firstPass = true;
    private static ArrayList<Passwords> passes = new ArrayList<Passwords>();
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<Message> messages = new ArrayList<Message>();
    private User user;
    public static ArrayList<User> getUsers(){
        System.out.println(users.get(0));
        return users;
    }
    @OnOpen
    public void onOpen(Session session){
      System.out.println("Connection Opened");
      try{
          session.getBasicRemote().sendText("Connected");
      } catch(IOException e){
          e.printStackTrace();
      }
    }
    @OnMessage
    public void onMessage(String message, Session session){
     //   try{
   //     System.out.println(message);
        message = message.toLowerCase();
        if(message.contains(":")){
            String m = message.substring(0, message.indexOf(":"));
             if(m.equals("password")){
                 message = message.substring(message.indexOf(":")+2);
                 System.out.println("password");
                 System.out.println(message);
                 if(firstPassword()){
                     System.out.println("first password");
                   try{
                       byte[] salt = generateSalt();
                       byte[] encryptPass = getEncryptedPassword(message, salt);
                       passes.add(new Passwords(salt, encryptPass));
                       System.out.println("Added password");
                       firstPass = false;
                       pass = true;
                       session.getBasicRemote().sendText("confirmed");
                   } catch(NoSuchAlgorithmException e){
                       e.printStackTrace();
                   } catch(InvalidKeySpecException e){
                       e.printStackTrace();
                   } catch (IOException e){
                       e.printStackTrace();
                   }
                   
                 }
                 else{
                     System.out.println("second password");
                     try{
                         byte[] salt = passes.get(0).getSalt();
                         System.out.println(salt[0]);
                         byte[] encryptPass = getEncryptedPassword(message, salt);
                         for(int i = 0; i < encryptPass.length; i++){
                             if(encryptPass[i] != passes.get(0).getEncryptPass()[i]){
                                 System.out.println("failed");
                             }
                         }
                        
                             System.out.println("confirmed");
                             session.getBasicRemote().sendText("confirmed");
//                             if(user.isAdmin()){
//                                 for(int i = 0; i < messages.size(); i++){
//                                     if(messages.get(i).user().equals(user)){
//                                         session.getBasicRemote().sendText("Approve User: " + messages.get(i).message());
//                                     }
//                                 }
//                             }                 
                         
                            
                     } catch (NoSuchAlgorithmException e){
                         e.printStackTrace();
                     } catch (InvalidKeySpecException e){
                         e.printStackTrace();
                     } catch(IOException e){
                         e.printStackTrace();
                     }
                 }
            }
             else if(m.equals("login password")){
                 try{
                        firstPass = true;
                         byte[] salt = passes.get(0).getSalt();
                         System.out.println(salt[0]);
                         byte[] encryptPass = getEncryptedPassword(message, salt);
                         
                             if(auth(message, encryptPass, salt)){
                                 System.out.println("confirmed");
                                 session.getBasicRemote().sendText("confirmed");
                             }
                             else{
                                 System.out.println("failed");
                                 
                             }                   
//                             System.out.println("confirmed");
//                             session.getBasicRemote().sendText("confirmed");
//                             if(user.isAdmin()){
//                                 for(int i = 0; i < messages.size(); i++){
//                                     if(messages.get(i).user().equals(user)){
//                                         session.getBasicRemote().sendText("Approve User: " + messages.get(i).message());
//                                     }
//                                 }
//                             }                 
                         
                            
                     } catch (NoSuchAlgorithmException e){
                         e.printStackTrace();
                     } catch (InvalidKeySpecException e){
                         e.printStackTrace();
                     } catch(IOException e){
                         e.printStackTrace();
                     }
             }
             else if(m.equals("login user")){
                 message.substring(message.indexOf(":"));
                 
                     for(int i = 0; i < users.size(); i++){
                         if(users.get(i).equals(message)){
                             user = users.get(i);
                             return;
                         }                        
                     }
                      try{
                             session.getBasicRemote().sendText("Username not found");
                      } catch(IOException e){
                           e.printStackTrace();
                      }
                 
             }
             else if(m.equals("user")){
                 message.substring(message.indexOf(":"));
                 if(users.isEmpty()){
                     user = new User(message, true);
                     users.add(user);
                 }
                 else{
                     user = new User(message, false);
                     users.add(user);
                     Message mess = new Message(users.get(0).getUsername(), "Requesting admin privaleges");
                 }
             }
        }
        else{
            try{
        switch(message){
                case "lock":                    
             //          Runtime.getRuntime().exec("sudo neunfhpupvc");
                       Runtime rt = Runtime.getRuntime();
                       Process pr = rt.exec("sudo -S python run.py lock");
          //            File file = new File("run.py");
            //           System.out.println(file.getAbsoluteFile());
                      System.out.println("Yay");
                  //      File file = new File("run.py");
                    //    System.out.println(file.canExecute());
                     //   file.setExecutable(true);
                       // System.out.println(file.canExecute());
                        //System.out.println(file.getAbsolutePath());
                  //      Runtime.getRuntime().
                    //    System.out.println("Locking...");   
                        break;
                case "unlock":
                        System.out.println("Run");
                        Runtime.getRuntime().exec("sudo -S python run.py unlock");
                        //System.out.println("Unlocking...");
                        break;
        }            
            
          } catch (IOException e){
            e.printStackTrace();
          }                
        }
    }
    @OnClose
    public void onClose(Session session){
        System.out.println("Connection closed");
        try{
         //   session.close();
            session.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public boolean firstPassword(){
        return firstPass;
    }
    public boolean isPassworded(){
        return pass;
    }
    public boolean auth(String attemptedPassword, byte[] encryptedPassword, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }
    public byte[] getEncryptedPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException{
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 2000;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
        return f.generateSecret(spec).getEncoded();
    }
    public byte[] generateSalt() throws NoSuchAlgorithmException{
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return salt;
    }
    
    @OnError
    public void onError(Throwable t) {
    }
}
class Passwords{
    private byte[] salt;
    private byte[] encryptPass;
    public Passwords(byte[] salt, byte[] encryptPass){
        this.salt = salt;
        this.encryptPass = encryptPass;
    }
    public byte[] getSalt(){
        return salt;
    }
    public byte[] getEncryptPass(){
        return encryptPass;
    }
}

class User{
    private String username;
    private boolean admin;
    public User(String username, boolean admin){
        this.username = username;
        this.admin = admin;
    }
    public String getUsername(){
        return username;
    }
    public boolean isAdmin(){
        return admin;
    }
    public void setAdmin(boolean admin){
        this.admin = admin;
    }
}
class Message{
    private String user;
    private String message;
    public Message(String user, String message){
        this.user = user;
        this.message = message;
    }
    public Message(String user){
        this.user = user;
    }
    public String user(){
        return user;
    }
    public String message(){
        return message;
    }
}
