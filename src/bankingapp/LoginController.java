/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author josej
 */
public class LoginController implements Initializable {
    
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Text error;
    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException, Exception {  
//        System.out.println(username.getText());
        String uname = username.getText();
        String pswd =  password.getText();
        System.out.println("Inputed username: "+uname);
        System.out.println("Inputted password: "+pswd);
        if(uname == null || pswd == null || uname.trim().isEmpty() || pswd.trim().isEmpty() || uname.contains(" ")
                || pswd.contains(" ")){
           error.setText("No spaces or empty fields!"); 
           username.setText("");
           password.setText("");
           return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        User user = getInstance();
        if(user.login(uname, pswd)){
            Parent root = loader.load();
            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();          
        }     
        username.setText("");
        password.setText("");
        error.setText("Invalid username or password!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
