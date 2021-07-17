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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.TipoEmpleado;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;


public class TipoEmpleadoController implements Initializable{
    
///////////////////////////////    VARIABLES     ////////////////////////////////
    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoOperaciones = operaciones.NINGUNO;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    private ImageView imgEmpleadosUno = new ImageView("/org/jonatanixpanel/img/EmpleadoUno.png");
    private ImageView imgEmpleadosDos = new ImageView("/org/jonatanixpanel/img/EmpleadoDos.png");
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Label lblCodigoTipoEmpleado;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleados;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private ImageView imgLogoEmpleados;

    
    /////////////////////////   METODOS INTERNOS     ///////////////////////////
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarDatos();
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleados()}");
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
        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,String>("descripcion"));
    }
    
    public void seleccionarElemento(){
        txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtDescripcion.setText(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcion());
    }
    
    
    ////////////////////////    METODOS BOTONES     ////////////////////////////
    
    public void guardar(){
        TipoEmpleado registro = new TipoEmpleado();
        registro.setDescripcion(txtDescripcion.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
            procedimiento.setString(1,registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void borrar(){
        if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar el registro?"
                    + " Si un EMPLEADO esta ligado a este tipo tambien se eliminara","Eliminar Empleado",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            procedimiento.setInt(1,((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedIndex());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoEmpleado(?,?)}");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1,registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    ///////////////////////////    BOTONES     /////////////////////////////////
    
    public void nuevo(){
        switch(tipoOperaciones){
            case NINGUNO:
                limpiarControles();
                activarControles();
                desactivarID();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                tipoOperaciones = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                normalidad();
                limpiarControles();
                cargarDatos();
                tipoOperaciones = operaciones.NINGUNO;
            break;
        }
    }
    
    public void eliminar(){
        switch (tipoOperaciones){
            case GUARDAR:
                normalidad();
                limpiarControles();
                tipoOperaciones = operaciones.NINGUNO;
            break;
            default:
                borrar();
                
            break;
        }
    
    }
    
    public void editar(){
        switch(tipoOperaciones){
            case NINGUNO:
                if (tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnEditar.setText("Guardar");
                    btnReporte.setText("Cancelar");
                    btnReporte.getStylesheets().clear();
                    btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
                    tipoOperaciones = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Debe de seleccionar un elemento");
                }
            break;
            case ACTUALIZAR:
                actualizar();
                normalidad();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                tipoOperaciones = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
            break;
        }
    }
    
    public void reporte(){
        switch(tipoOperaciones){
            case ACTUALIZAR:
                normalidad();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                limpiarControles();
            break;    
        
        }
        
    
    }
    
    ///////////////////////    METODOS BOTONES     /////////////////////////////
    
    public void desactivarID(){
        lblCodigoTipoEmpleado.setVisible(false);
        txtCodigoTipoEmpleado.setVisible(false);
    }
    
    public void activarControles(){
        txtCodigoTipoEmpleado.setEditable(true);
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoEmpleado.setText("");
        txtDescripcion.setText("");
    }
    
    public void normalidad(){
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);
        lblCodigoTipoEmpleado.setVisible(true);
        txtCodigoTipoEmpleado.setVisible(true);
        
        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
        txtDescripcion.setEditable(false);
        txtCodigoTipoEmpleado.setEditable(false);
        
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnReporte.setText("Reporte");
    }
    
    public void logoIn(){
        imgLogoEmpleados.setImage(imgEmpleadosDos.getImage());
    }
    
    public void logoOut(){
        imgLogoEmpleados.setImage(imgEmpleadosUno.getImage());
    }
    
    //////////////////    GETERS Y SETTERS ESCENARIOS     //////////////////////
    
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
//    public void ventanaEmpleado(){
//        escenarioPrincipal.ventanaEmpleado();
//    }
}
