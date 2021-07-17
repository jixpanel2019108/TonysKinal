        package org.jonatanixpanel.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.Empleado;
import org.jonatanixpanel.bean.Presupuesto;
import org.jonatanixpanel.bean.TipoEmpleado;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;

public class EmpleadoController implements Initializable{

    
    @Override
    public void initialize(URL url,ResourceBundle rb){
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
///////////////////////////////    VARIABLES     ////////////////////////////////    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private ObservableList<Empleado> listaEmpleado;
    
    @FXML private Label lblCodigoEmpleado;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtGradoCocinero;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
/////////////////////////////   METODOS INTERNOS     ///////////////////////////
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro  = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(
                                registro.getInt("codigoTipoEmpleado"),
                                registro.getString("descripcion"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado (resultado.getInt("codigoEmpleado"),
                                       resultado.getInt("numeroEmpleado"),
                                       resultado.getString("apellidosEmpleado"),
                                       resultado.getString("nombresEmpleado"),
                                       resultado.getString("direccionEmpleado"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getString("gradoCocinero"),
                                       resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
        public ObservableList<TipoEmpleado> getTipoEmpleado(){
            ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleados()}");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                                resultado.getString("descripcion")));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return listaTipoEmpleado = FXCollections.observableArrayList(lista);
        }
    
    public void cargarDatos(){
        
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("numeroEmpleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }
    
    public void seleccionarElemento(){
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null){
        txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtNombres.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        txtApellidos.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtDireccion.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtGradoCocinero.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
        txtTelefono.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        }else{
        
        }
    }
    

////////////////////////////    METODOS BOTONES     ////////////////////////////    
    public void guardar(){
        Empleado registro = new Empleado();
        //registro.setCodigoEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidos.getText());
        registro.setNombresEmpleado(txtNombres.getText());
        registro.setDireccionEmpleado(txtDireccion.getText());
        registro.setTelefonoContacto(txtTelefono.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2,registro.getApellidosEmpleado());
            procedimiento.setString(3,registro.getNombresEmpleado());
            procedimiento.setString(4,registro.getDireccionEmpleado());
            procedimiento.setString(5,registro.getTelefonoContacto());
            procedimiento.setString(6,registro.getGradoCocinero());
            procedimiento.setInt(7,registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
                }
    }
    
    public void eliminarMetodo(){
        if(tblEmpleados.getSelectionModel().getSelectedItem() != null ){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar el registro?","Eliminar Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                        procedimiento.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                        procedimiento.execute();
                        listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }else{
            JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemnto");
        }
    }
    
    public void actualizar(){
        try{
            
                        
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleado(?,?,?,?,?,?,?,?)}");
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidos.getText());
            registro.setNombresEmpleado(txtNombres.getText());
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            //registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            
            procedimiento.setInt(1,registro.getCodigoEmpleado());
            procedimiento.setInt(2,registro.getNumeroEmpleado());
            procedimiento.setString(3,registro.getApellidosEmpleado());
            procedimiento.setString(4,registro.getNombresEmpleado());
            procedimiento.setString(5,registro.getDireccionEmpleado());
            procedimiento.setString(6,registro.getTelefonoContacto());
            procedimiento.setString(7,registro.getGradoCocinero());
            procedimiento.setInt(8,registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
///////////////////////////////    BOTONES     /////////////////////////////////
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                desactivarID();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                cargarDatos();
                limpiarControles();
                normalidad();
                tipoOperacion = operaciones.NINGUNO;

            break;
        
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                normalidad();
                limpiarControles();
                tipoOperacion = operaciones.NINGUNO;
            break;
            default: 
                eliminarMetodo();
            break;
        
        }
    }
    
    public void editar(){
        switch (tipoOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    desactivarID();
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnEditar.setText("Guardar");
                    btnReporte.setText("Cancelar");
                    btnReporte.getStylesheets().clear();
                    btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                }
            break;
            case ACTUALIZAR:
                actualizar();
                normalidad();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                cargarDatos();
            break;
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                normalidad();
                limpiarControles();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                tipoOperacion = operaciones.NINGUNO;
            break;
            
        }
    }
    
    
///////////////////////////    METODOS BOTONES     /////////////////////////////
    public void desactivarID(){
        lblCodigoEmpleado.setVisible(false);
        txtCodigoEmpleado.setVisible(false);
    }
    

    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(true);
        txtNumeroEmpleado.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtDireccion.setEditable(true);
        txtGradoCocinero.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtDireccion.setEditable(false);
        txtGradoCocinero.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void normalidad(){
        lblCodigoEmpleado.setVisible(true);
        txtCodigoEmpleado.setVisible(true);
        
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtDireccion.setEditable(false);
        txtGradoCocinero.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(false);
        
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);

        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);  
        
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnReporte.setText("Reporte");
    }
    
    public void desactivarBotones(){
        btnNuevo.setVisible(false);
        btnEliminar.setVisible(false);
        btnEditar.setVisible(false);
        btnReporte.setVisible(false);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDireccion.setText("");
        txtGradoCocinero.setText("");
        txtTelefono.setText("");
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
    }
    
    
//////////////////////    GETERS Y SETTERS ESCENARIOS     //////////////////////    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
   
}
