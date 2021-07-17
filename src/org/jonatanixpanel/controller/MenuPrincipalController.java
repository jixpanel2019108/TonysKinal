package org.jonatanixpanel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import org.jonatanixpanel.sistema.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal; // para poder pasar el escenario Principal
    
    @FXML private ImageView imgLogoEmpresa;
    @FXML private ImageView imgLogoTipoEmpleado;
    @FXML private ImageView imgLogoProducto;
    @FXML private ImageView imgLogoTipoPlato;
    @FXML private ImageView imgLogoCreditos;

    private ImageView imgEmpresaUno = new ImageView("/org/jonatanixpanel/img/empresaUno.png");
    private ImageView imgEmpresaDos = new ImageView("/org/jonatanixpanel/img/empresaDos.png");
    
    private ImageView imgTipoEmpleadoUno = new ImageView("/org/jonatanixpanel/img/tipoEmpleadoUno.png");
    private ImageView imgTipoEmpleadoDos = new ImageView("/org/jonatanixpanel/img/tipoEmpleadoDos.png");
    
    private ImageView imgProductosUno = new ImageView("/org/jonatanixpanel/img/productosUno.png");
    private ImageView imgProductosDos = new ImageView("/org/jonatanixpanel/img/productosDos.png");
    
    private ImageView imgTipoPlatoUno = new ImageView("/org/jonatanixpanel/img/tipoPlatoUno.png");
    private ImageView imgTipoPlatoDos = new ImageView("/org/jonatanixpanel/img/tipoPlatoDos.png");
    
    private ImageView imgCreditosUno = new ImageView("/org/jonatanixpanel/img/creditosUno.png");
    private ImageView imgCreditosDos = new ImageView("/org/jonatanixpanel/img/creditosDos.png");

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    // Encapsulacion
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaDatosProgramador(){
        escenarioPrincipal.ventanaDatosProgramador();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
    
    public void ventanaTipoPlato(){
       escenarioPrincipal.ventanaTipoPlato();
   }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    
    public void ventanaServiciosHasEmpleados(){
        escenarioPrincipal.ventanaServiciosHasEmpleados();
    }
    
    public void ventanaServicioHasPlatos(){
        escenarioPrincipal.ventanaServiciosHasPlatos();
    }
    
    public void ventanaProductosHasPlatos(){
        escenarioPrincipal.ventanaProductosHasPlatos();
    }
    
    
    
    //1
    public void empresaIn(){
        imgLogoEmpresa.setImage(imgEmpresaDos.getImage());
    }
    public void empresaOut(){
        imgLogoEmpresa.setImage(imgEmpresaUno.getImage());
    }
    //2
    public void tipoEmpleadoIn(){
        imgLogoTipoEmpleado.setImage(imgTipoEmpleadoDos.getImage());
    }
    public void tipoEmpleadoOut(){
        imgLogoTipoEmpleado.setImage(imgTipoEmpleadoUno.getImage());
    }
    //3
    public void productoIn(){
        imgLogoProducto.setImage(imgProductosDos.getImage());
    }
    public void productoOut(){
        imgLogoProducto.setImage(imgProductosUno.getImage());
    }
    //4
    public void tipoPlatoIn(){
        imgLogoTipoPlato.setImage(imgTipoPlatoDos.getImage());
    }
    public void tipoPlatoOut(){
        imgLogoTipoPlato.setImage(imgTipoPlatoUno.getImage());
    }
    //5
    public void creditosIn(){
        imgLogoCreditos.setImage(imgCreditosDos.getImage());
    }
    public void creditosOut(){
        imgLogoCreditos.setImage(imgCreditosUno.getImage());
    }
}
