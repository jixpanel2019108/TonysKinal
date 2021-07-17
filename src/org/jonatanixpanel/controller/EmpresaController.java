package org.jonatanixpanel.controller;

import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.Empresa;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.report.GenerarReporte;
import org.jonatanixpanel.sistema.Principal;


public class EmpresaController implements Initializable {
    //Creamos la variable que tendra estas diferentes opciones para los botones
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    //Seteamos una variable de este tipo este objeto
    private operaciones tipoDeOperacion = operaciones.NINGUNO; //ponemos la variable tipo operaciones default ninguno
    // Va a ser del modelo de datos tal en este caso es empresa pensando en los atributos que tiene
    private ObservableList<Empresa> listaEmpresa;//Hacemos una referencia a una ObsList que no es mas que el modelo de datos
    /*private final String Paquete_Vista = "/org/jonatanixpanel/view/";
    private Scene escena;*/
    private ImageView imgPresupuestoUno = new ImageView("/org/jonatanixpanel/img/presupuestoUno.png");
    private ImageView imgPresupuestoDos = new ImageView("/org/jonatanixpanel/img/presupuestoDos.png");
    
    private ImageView imgServiciosUno = new ImageView("/org/jonatanixpanel/img/ServiciosUno.png");
    private ImageView imgServiciosDos = new ImageView("/org/jonatanixpanel/img/ServiciosDos.png");
    
    private boolean vacio = false;
    
//Table  view funciona con dos objetos en si, el contenedor y el otro la columna
    //Como funciona parecido a excel temos que decirle de donde viene y el tipo de datos <Modelo,String>
    //Y cuando ya sabe esto el programa tenemos que decirle el nombre <Modelo,String>("nombreEmpresa")
    //Tiene que estar el nombre igual que en mi base de datos para qye haya coincidencia
    
    //Como nuestros objetos estan en JavaFX ponemos FXML
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Label lblCodigoEmpresa;
    @FXML private ImageView imgLogoPresupuesto;
    @FXML private ImageView imgLogoServicios;
    
    //Este es el primer metodo que se ejecuta cuando carga los objetos
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();  
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }    

    public void cargarDatos(){
        //en la tblEmpresas voy a setear un item
        tblEmpresas.setItems(getEmpresa());//El metodo getEmpresa va a obtener una observable list y lo transforma en la observlist que necesito
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,Integer>("codigoEmpresa"));//setamos dentro de la celda
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa,String>("telefono"));
    }
    
    //Esta observable list viene del modelo de datos que se llama empresa
    //Y como es una funcion tiene que retornarme la lista emopresa pero va a ser una coleccion de objetos que sera tipo
    //ObservableArraylist
    public ObservableList<Empresa> getEmpresa(){
        //Este arrayList tambien viene de un modelo de datos y se llama empresa
        ArrayList<Empresa> lista = new ArrayList<Empresa>();//Y este creara una instancia tipo arraylist de un modelo de datos que se llama empresa
        //Aqui tenemos que llamar a una clase para conectarnos con nuestra base de datos
        try{
        //En MySQL hay una opcion para que ejecute solo la linea en la que estoy
        //Y la clase que me prermite hacerlo se llama PreparedStatement
        //Tenemos que ejecutar nuestra clase conexion entonces //llamare a mi calse conexion//
        //Dentro de la clse conexion tengo un getInstance que //crea la instancia hacia esa conexion//
        //llamar a la conexion que es el getConexion//
        //Y depeus utilizare la clase para ejecutar mi procedimieto almacenado con el prepareCall
        //Lo que me pide es el resultado de un procedimiento almacenado
        //Pide un string "" pero como es un sp tengo que ponerlo entre llaves   
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
        //Como el sp me tira una respuesta resulset
        //Creamos una variable tipo resulset
        //Como esa instruccion me mandarea solo ese registro tenemos que ver si es la ultima metiendola en un bucle
        //Un while que verificaras si hay registros o no
        ResultSet resultado = procedimiento.executeQuery();
            //el next contendra una expresion booleana que verificara si hay registros
            while (resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),         //aqui como se llama en la db o en el modelo que son exactamente iguales
                                      resultado.getString("nombreEmpresa"),
                                      resultado.getString("direccion"),
                                      resultado.getString("telefono")
                ));//Le tengo que decir como estan formados los datos es decir codigoempresa etc       
            } 
        }catch(Exception e){
            e.printStackTrace();
        }
        //Va a transformar un observableArrayList a un ObservableList
        //Entre parentesis le tengo que decir a quien va a transformar
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper","Reporte de Empresa",parametros);
    }
    
    public boolean validacion(){
        boolean vacio = false;
            if(txtNombreEmpresa.getText() != null && txtDireccionEmpresa.getText() != null && txtTelefonoEmpresa.getText() != null){
                vacio = true;
            }else{
                vacio = false;
            }
        return vacio;
    }
    
    
    
    //////////////////////////CRUD//////////////////////////////////////////////
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                desactivarBotones();
                limpiarControles();
                btnNuevo.setVisible(true);
                btnNuevo.setText("Guardar");
                btnEliminar.setVisible(true);
                btnEliminar.setText("Cancelar");
                desactivarID();
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                if(validacion() == true){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(true);
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                activarID();
                tipoDeOperacion = operaciones.NINGUNO;
                activarBotones();
                cargarDatos();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos");
                }
                break;
        }
    }

    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                activarBotones();
                activarID();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar el registro? "
                            + "Si el registro esta ligado a uno de Presupuesto o servicio tambien sera eliminado","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    
                        if (respuesta == JOptionPane.YES_OPTION){
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresa(?)}");
                                procedimiento.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                                procedimiento.execute();
                                
                                if(procedimiento.execute()== false){
                                    listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                                    limpiarControles();
                                     JOptionPane.showMessageDialog(null, "Registro Eliminado");
                                }else{
                                    JOptionPane.showMessageDialog(null,"No se puede borrar el registro porque esta ligado a otro registro");
                                }
                                
                                
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }    
    
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    desactivarBotones();
                    btnEditar.setVisible(true);
                    btnReporte.setVisible(true);
                    /*btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);*/
                    activarControles();
                    btnReporte.getStylesheets().clear();
                    btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                }
            break;
            
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                /*btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);*/
                activarBotones();
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        
        }
    }
    
    public void generarReporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                
                break;
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                activarBotones();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    
    
    
    public void guardar(){
        Empresa registro = new Empresa();
        //Estoy seteando a el modelo empresa el valor que tiene codigoEmpresa en txtCodigoEmpresa que pero obtener el texto y castearlo a int
        //registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
        registro.setNombreEmpresa(txtNombreEmpresa.getText());// ya lo obtuve y lo tengo listo para enviar
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());
        
        //Puedo hacer la llamada ql poroceso
        try{
            //Procedimiento va a recibir conexion mi clase. para crear la instancia
            //GetConexion el get de conexion
            //. prepared call para poder ejecutar mi proedimiento almacenado de ahora agregar empersa
            //adentro agregare el procedimiento de agregar empresa 
            //Enviar parametros si es auto increment soolo los 3 
            //como son 3 son 3 ? separados por "," 
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresa(?,?,?)}");
            procedimiento.setString(1,registro.getNombreEmpresa());
            procedimiento.setString(2,registro.getDireccion());
            procedimiento.setString(3,registro.getTelefono  ());
            procedimiento.execute();
            ///Va ahora que ya tenemos esto hay que agregarlo a nuestra lista como la que esta arriba
            listaEmpresa.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //Revisar bases de datoooooos
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpresa(?,?,?,?)}");
            Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            //Para identificarlos en la primera linea
            procedimiento.setInt(1,registro.getCodigoEmpresa());
            procedimiento.setString(2,registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();    
            
        }catch(Exception e){
            e.printStackTrace();
        
        }
    }
    
    public void seleccionarElemento(){
        if (tblEmpresas.getSelectionModel().getSelectedItem() != null){
            
                //Seleccionamos un item
                //de la tabla empresa selecciono un item
                //obtengo codigo empresa con el get
                //Lo transformo a String con el stringvalieOf
                txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
                txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
                //Viene de TblEmpresa.metodoSeleccion.Obtenerlo   castearlo a tipo Empresa  . obtenemos los datos con getDireccion
                txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
                txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
            
        }else{
            tblEmpresas.getSelectionModel().select(-1); 
        }
    }
    
    public void deseleccionarElemento(){
        tblEmpresas.getSelectionModel().select(-1); 
    }
    
    
    
    //Procedimiento buscar para eliminar
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1,codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(
                                registro.getInt("codigoEmpresa"),
                                registro.getString("nombreEmpresa"),
                                registro.getString("direccion"),
                                registro.getString("telefono")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        return resultado;
    }
    
    
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoEmpresa.setText("");
        txtNombreEmpresa.setText("");
        txtDireccionEmpresa.setText("");
        txtTelefonoEmpresa.setText("");
    }
     
    public void activarBotones(){
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);

        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
    }
    
    public void desactivarBotones(){
        btnNuevo.setVisible(false);
        btnEliminar.setVisible(false);
        btnEditar.setVisible(false);
        btnReporte.setVisible(false);
    }
    
    public void activarID(){
        txtCodigoEmpresa.setVisible(true);
        lblCodigoEmpresa.setVisible(true);
    }
    
    public void desactivarID(){
        txtCodigoEmpresa.setVisible(false);
        lblCodigoEmpresa.setVisible(false);
    }
    
    
    
    public void logoInPresupuesto(){
        imgLogoPresupuesto.setImage(imgPresupuestoDos.getImage());
    }
    
    public void logoOutPresupuesto(){
        imgLogoPresupuesto.setImage(imgPresupuestoUno.getImage());
    }
    
    
    public void logoInServicio(){
        imgLogoServicios.setImage(imgServiciosDos.getImage());
    }
    
    public void logoOutServicio(){
        imgLogoServicios.setImage(imgServiciosUno.getImage());
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
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }

}
