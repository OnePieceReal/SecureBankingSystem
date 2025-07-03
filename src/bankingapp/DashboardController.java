
package bankingapp;

import bankingapp.server.User;
import static bankingapp.server.User.getInstance;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class DashboardController implements Initializable {
   
    
    @FXML
    Text balance;
    @FXML
    TextField withdrawAmount;
    @FXML
    TextField depositAmount;
    @FXML
    Text error;
    
    @FXML
    private void handleDepositButtonAction() throws IOException, Exception { 
        User user = getInstance();
        System.out.println("you clicked the deposit button and the deposit amount is:"+depositAmount.getText());
        String command = "deposit "+depositAmount.getText();
        String result =user.action(command);
        if(!result.equals("failed")){
            balance.setText(result);
            error.setText("");
        }
        else{
           error.setText("Amount deposited exceeds limit!"); 
        }
        depositAmount.setText("");
    }
     @FXML
    private void handleWithdrawButtonAction() throws IOException, Exception { 
        User user =getInstance();
        System.out.println("you clicked the withdraw button:"+withdrawAmount.getText());
        String command = "withdraw "+withdrawAmount.getText();
        String result =user.action(command);
        if(!result.equals("failed")){
            balance.setText(result);
            error.setText("");
        }
        else{
             error.setText("Insufficient funds!"); 
        }
        withdrawAmount.setText("");  
    }
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) throws IOException, Exception { 
        System.out.println("you clicked the logout button");
        User user = getInstance();
        user.action("exit");
        user.logout();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        // Get the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Set the new scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();  
    }  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            User user = getInstance();
            try {
                balance.setText(user.action("balance"));
            } catch (Exception ex) {
                Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
