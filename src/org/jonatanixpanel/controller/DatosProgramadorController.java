package org.jonatanixpanel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.jonatanixpanel.sistema.Principal;

public class DatosProgramadorController implements Initializable {
    private Principal escenarioPrincipal;
    @FXML private Button btnProgramador;
    @FXML private Button btnCorporacion;
    @FXML private Button btnApp;
    @FXML private Label lblTextoUno;
    @FXML private Label lblTextoDos;    
    @FXML private Label lblTextoTres;
    @FXML private Label lblTextoCuatro;
    @FXML private Label lblDatoUno;
    @FXML private Label lblDatoDos;   
    @FXML private Label lblDatoTres;
    @FXML private Label lblDatoCuatro;
    @FXML private ImageView imgJava;
    @FXML private ImageView imgKinal;
    @FXML private ImageView imgProgramador;
    @FXML private ImageView imgTK;
    @FXML private ImageView imgHome;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
   @FXML 
   private void opciones (ActionEvent event){
       if(event.getSource() == btnProgramador){
           lblTextoUno.setText("NOMBRES:");
           lblTextoDos.setText("APELLIDO:");
           lblTextoTres.setText("CARNÉ:");
           lblTextoCuatro.setText("CÓDIGO:");
           lblDatoUno.setText("Jonatan Josue");
           lblDatoDos.setText("Ixpanel Panjoj");
           lblDatoTres.setText("2019108");
           lblDatoCuatro.setText("PE5DM");
           imgJava.setVisible(false);
           imgKinal.setVisible(false);
           imgTK.setVisible(false);
           imgProgramador.setVisible(true);
           
       }else if(event.getSource() == btnCorporacion){
           lblTextoCuatro.setText("CORPORACIÓN:");
           lblTextoTres.setText("TIPO:");
           lblTextoDos.setText("CARRERA:");
           lblTextoUno.setText("APP:");
           lblDatoCuatro.setText("Kinal");
           lblDatoTres.setText("Fundación");
           lblDatoDos.setText("Informática");
           lblDatoUno.setText("Tony's Kinal");
           imgJava.setVisible(false);
           imgKinal.setVisible(true);
           imgTK.setVisible(false);
           imgProgramador.setVisible(false);
           
       }else if(event.getSource() == btnApp){
           lblTextoUno.setText("TONY'S KINAL");
           lblTextoDos.setText("JAVA PROJECT ");
           lblTextoTres.setText("");
           lblTextoCuatro.setText("");
           lblDatoUno.setText("Ver.1.0");
           lblDatoDos.setText("");
           lblDatoTres.setText("");
           lblDatoCuatro.setText("");
           imgJava.setVisible(true);
           imgKinal.setVisible(false);
           imgTK.setVisible(true);
           imgProgramador.setVisible(false);
       }
    }
    
       public void imgMouseInLogoHome(){
        imgHome.setFitWidth(imgHome.getFitWidth()+5);
        imgHome.setFitHeight(imgHome.getFitHeight()+5);
        imgHome.setLayoutX(imgHome.getLayoutX()-2.5) ;
        imgHome.setLayoutY(imgHome.getLayoutY()-2.5);
    }
    
    public void imgMouseOutLogoHome(){
        imgHome.setFitWidth(imgHome.getFitWidth()-5);
        imgHome.setFitHeight(imgHome.getFitHeight()-5);
        imgHome.setLayoutX(imgHome.getLayoutX()+2.5) ;
        imgHome.setLayoutY(imgHome.getLayoutY()+2.5);
    }
    
}
