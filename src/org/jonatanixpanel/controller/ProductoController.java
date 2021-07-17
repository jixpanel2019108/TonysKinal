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
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.Producto;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;

public class ProductoController implements Initializable{
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarDatos();
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
    
///////////////////////////////VARIABLES ///////////////////////////////////////
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    ObservableList<Producto> listaProductos;
    
    @FXML private Label lblCodigoProducto;
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidadProducto;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
//////////////////////////   METODOS INTERNOS     //////////////////////////////
    
    ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto,String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto,String>("cantidad"));
    }
    
    public void seleccionarElemento(){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidadProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
    }

    
/////////////////////////       METODOS BOTONES     ////////////////////////////
    public void guardar(){
        Producto registro = new Producto();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?,?)}");
            procedimiento.setString(1,txtNombreProducto.getText());
            procedimiento.setInt(2,Integer.parseInt(txtCantidadProducto.getText()));
            procedimiento.execute();
            listaProductos.add(registro);        
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void borrar(){
        if(tblProductos.getSelectionModel().getSelectedItem() != null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta Seguro que quiere eliminar el mensaje?","Eliminar Producto",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                procedimiento.setInt(1,((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                procedimiento.execute();
                listaProductos.remove(tblProductos.getSelectionModel().getFocusedIndex());
                tblProductos.getSelectionModel().clearSelection();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                
            }
        }else{
            JOptionPane.showMessageDialog(null,"Debe de seleccionar un elemento");
        }
    }
    
    public void actualizar(){
        if(tblProductos.getSelectionModel().getSelectedItem() != null){
            try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProducto(?,?,?)}");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
            
            procedimiento.setInt(1,registro.getCodigoProducto());
            procedimiento.setString(2,registro.getNombreProducto());
            procedimiento.setInt(3,registro.getCantidad());
            procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null,"Debe de seleccionar un elemento");
        }
    }
    
    
///////////////////////////////    BOTONES     /////////////////////////////////
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                desactivarID();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                normalidad();
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                normalidad();
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                borrar();
                normalidad();
            break;
        }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                desactivarID();
                txtNombreProducto.setEditable(true);
                txtCantidadProducto.setEditable(true);
                btnNuevo.setVisible(false);
                btnEliminar.setVisible(false);
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
                tipoOperacion = operaciones.ACTUALIZAR;
            break;
            case ACTUALIZAR:
                actualizar();
                cargarDatos();
                normalidad();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                normalidad();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                
            break;
        }
    }
    
///////////////////////////    METODOS BOTONES     /////////////////////////////
    public void normalidad(){
        lblCodigoProducto.setVisible(true);
        txtCodigoProducto.setVisible(true);
        
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
        
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
        
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnReporte.setText("Reporte");
        
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);
    }
    
    public void desactivarID(){
        lblCodigoProducto.setVisible(false);
        txtCodigoProducto.setVisible(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
        
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidadProducto.setText("");
    }
    
//////////////////////////GETTER AND SETTERS ///////////////////////////////////
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
