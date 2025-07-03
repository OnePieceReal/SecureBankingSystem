/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bankingapp;

import bankingapp.server.User;
import static bankingapp.server.User.getInstance;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author josej
 */
public class RegistrationController implements Initializable {
    @FXML
    TextField username;
    @FXML
    TextField password;
            
    @FXML
    private void handleRegistrationButtonAction(ActionEvent event) throws IOException, Exception { 
        
//        
//        
//        System.out.println("you clicked the logout button");
//        User user = getInstance();
//        user.action("exit");
//        user.logout();
        String uname = username.getText();
        String pswd = password.getText();
        User user = getInstance();
        user.register(uname,pswd);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        // Get the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Set the new scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();  
    } 
            
   
            
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
