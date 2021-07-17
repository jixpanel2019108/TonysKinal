package org.jonatanixpanel.sistema;

import java.io.InputStream;//
import javafx.application.Application;//
import javafx.fxml.FXMLLoader;//
import javafx.fxml.Initializable;//
import javafx.fxml.JavaFXBuilderFactory;//
import javafx.scene.Scene;//
import javafx.scene.image.Image;//
import javafx.scene.layout.AnchorPane;//
import javafx.stage.Stage;//
import org.jonatanixpanel.controller.*;

public class Principal extends Application {
    private final String Paquete_Vista = "/org/jonatanixpanel/view/";
    private Stage escenarioPrincipal; //
    private Scene escena;
    
    public static void main(String[] args) {
        launch(args);
    }
    // Sobreescibrimos el metodo start
    @Override
        public void start(Stage escenarioPrincipal) throws Exception{
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal App");
        escenarioPrincipal.getIcons().add(new Image("/org/jonatanixpanel/img/logo.png"));
        menuPrincipal();
        escenarioPrincipal.setResizable(false);
        escenarioPrincipal.show();
    }   
    
    //Me perimitira cargar cualquier archivo fxml
    //Como cada initializabel necesita una excepcion le ponemos throws exeption
    public Initializable cambiarEscena(String fxml, int ancho, int alto)throws Exception{
    Initializable resultado = null; // resultado contiene la ruta
    FXMLLoader cargadorFXML = new FXMLLoader();//Inicializa un objeto tipo loader que puede levantar un archivo FXML
    InputStream archivo = Principal.class.getResourceAsStream(Paquete_Vista+fxml); //archivo sera la ruta viendolo desde paquetes dependiendo d ela clase indicamor ruta
    cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());// Me hace un nuevo objeto un archivo fxml, constructor de archivos fxml 
    cargadorFXML.setLocation(Principal.class.getResource(Paquete_Vista+fxml));
    escena = new Scene((AnchorPane)cargadorFXML.load(archivo));//Que me cargue en un achor pane la ventana que elijamos
    escenarioPrincipal.setScene(escena); // Asignamos al escenario Principal la escena que mandé
    escenarioPrincipal.sizeToScene();//El escenario principal se ajuste a el tamaño de la ventana
    resultado = (Initializable)cargadorFXML.getController();//Traer el archivo FXML y lo castea a un tipo un initializable  
    
    return resultado;//Devuelve el archivo FXML
    }

    // metodo me puede levantar el menu princupal
    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml",747,719);// Hacemos un casting a tipo MenuPrincipalController 
            menuPrincipal.setEscenarioPrincipal(this);//Varoabñe tipo controller establecemos el escenario principal con el setter que programamos en el controller
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
    
    public void ventanaDatosProgramador(){
        try{
            DatosProgramadorController DatosProgramador = (DatosProgramadorController) cambiarEscena ("DatosView.fxml",600,400);
            DatosProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try{
            EmpresaController empresa = (EmpresaController) cambiarEscena ("EmpresaView.fxml", 672 , 478);
            empresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena ("PresupuestoView.fxml",600,400);
            presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena ("TipoEmpleadoView.fxml",675,430);
            tipoEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaEmpleado(){
        try{
            EmpleadoController empleado = (EmpleadoController) cambiarEscena ("EmpleadoView.fxml",700,550);
            empleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void ventanaServicio() {
        try{
            ServiciosController servicio = (ServiciosController) cambiarEscena ("ServiciosView.fxml",720,490);
            servicio.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto(){
        try{
            ProductoController producto = (ProductoController) cambiarEscena("ProductosView.fxml",700,550);
            producto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
        TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",720,479);
        tipoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try{
            PlatosController plato = (PlatosController) cambiarEscena("PlatosView.fxml",719,490);
            plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosHasEmpleados(){
        try{
            ServiciosHasEmpleadosController serviciosHasEmpleados = (ServiciosHasEmpleadosController) cambiarEscena ("ServiciosHasEmpleadosView.fxml",700,502);
            serviciosHasEmpleados.setEscenarioPrinicpal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosHasPlatos(){
        try{
            ServiciosHasPlatosController serviciosHasPlatos = (ServiciosHasPlatosController) cambiarEscena("ServiciosHasPlatosView.fxml",609,502);
            serviciosHasPlatos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductosHasPlatos(){
        try{
            ProductosHasPlatosController productosHasPlatos = (ProductosHasPlatosController) cambiarEscena("ProductosHasPlatosView.fxml",609,424);
            productosHasPlatos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

