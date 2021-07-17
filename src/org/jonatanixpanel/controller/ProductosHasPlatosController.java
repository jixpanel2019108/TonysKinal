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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.Plato;
import org.jonatanixpanel.bean.Producto;
import org.jonatanixpanel.bean.ProductosHasPlatos;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;

public class ProductosHasPlatosController implements Initializable {
    
    @Override
    public void initialize (URL url,ResourceBundle rb){
        cargarDatos();
        cmbCodigoProducto.setItems(getProducto());
        cmbCodigoPlato.setItems(getPlato());

    }
    
//////////////////////////////    VARIABLES     ////////////////////////////////
    
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, AGREGAR, GUARDAR, ELIMINAR, CANCELAR, EDITAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<ProductosHasPlatos> listaProductosHasPlatos;
    ObservableList<Producto> listaProducto;
    ObservableList<Plato> listaPlato;
    
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblProductosHasPlatos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnEliminar;
    
/////////////////////////////   METODOS INTERNOS     ///////////////////////////
    
    public ObservableList<ProductosHasPlatos> getProductosHasPlatos(){
        ArrayList<ProductosHasPlatos> lista = new ArrayList<ProductosHasPlatos>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_Platos}");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new ProductosHasPlatos(resultado.getInt("codigoProducto"),
                                                     resultado.getInt("codigoPlato")));
                }
            }catch(Exception e){
            
            }
        return listaProductosHasPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new Producto(resultado.getInt("codigoProducto"),
                                            resultado.getString("nombreProducto"),
                                            resultado.getInt("cantidad") ));   
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                                        resultado.getInt("cantidad"),
                                        resultado.getString("nombrePlato"),
                                        resultado.getString("descripcionPlato"),
                                        resultado.getInt("precioPlato"),
                                        resultado.getInt("codigoTipoPlato") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getPlatos_codigoPlato()));
    }  
    
    
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),
                                            registro.getString("nombreProducto"),
                                            registro.getInt("cantidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                                        registro.getInt("cantidad"),
                                        registro.getString("nombrePlato"),
                                        registro.getString("descripcionPlato"),
                                        registro.getInt("precioPlato"),
                                        registro.getInt("codigoTipoPlato") );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void cargarDatos(){
        tblProductosHasPlatos.setItems(getProductosHasPlatos());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos,Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos,Integer>("Platos_codigoPlato"));
    }
    
    
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblProductosHasPlatos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro que quiere eliminar el registro?","Eliminar",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos_has_Platos(?)}");
                        procedimiento.setInt(1,((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto());
                        procedimiento.execute();
                        listaProductosHasPlatos.remove(tblProductosHasPlatos.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento primero"); 
            }
            break;    
        }
    }
    
    public void limpiarControles(){
        cmbCodigoProducto.getSelectionModel().clearSelection();
        cmbCodigoPlato.getSelectionModel().clearSelection();
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
    
    public void ventanaProductosHasPlatos(){
        escenarioPrincipal.ventanaProductosHasPlatos();
    }
    
}
