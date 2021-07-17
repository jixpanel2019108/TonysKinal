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
import org.jonatanixpanel.bean.Plato;
import org.jonatanixpanel.bean.TipoPlato;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;


public class PlatosController implements Initializable{
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlato());
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
//////////////////////////////    VARIABLES     ////////////////////////////////
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private Label lblCodigoPlato;
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidadPlato;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidadPlato;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
/////////////////////////////   METODOS INTERNOS     ///////////////////////////
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista = new ArrayList<Plato>(); 
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
                ResultSet resultado = procedimiento.executeQuery();
                    while(resultado.next()){
                        lista.add(new Plato(resultado.getInt("codigoPlato"),
                                            resultado.getInt("cantidad"),
                                            resultado.getString("nombrePlato"),
                                            resultado.getString("descripcionPlato"),
                                            resultado.getInt("precioPlato"),
                                            resultado.getInt("codigoTipoPlato")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
                ResultSet resultado = procedimiento.executeQuery();
                    while(resultado.next()){
                        lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                                resultado.getString("descripcionTipoPlato")));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato registro = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
                procedimiento.setInt(1,codigoTipoPlato);
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    registro = new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                            resultado.getString("descripcionTipoPlato"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return registro;
    }
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("codigoPlato"));
        colCantidadPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato,String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato,String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato,Integer>("codigoTipoPlato"));
    }
    
    public void seleccionarElemento(){
        txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidadPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtDescripcionPlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
        txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    }
    
    
////////////////////////////       METODOS BOTONES     /////////////////////////
    
    public void guardar(){
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidadPlato.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Integer.parseInt(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
                procedimiento.setInt(1,registro.getCantidad());
                procedimiento.setString(2,registro.getNombrePlato());
                procedimiento.setString(3,registro.getDescripcionPlato());
                procedimiento.setInt(4,registro.getPrecioPlato());
                procedimiento.setInt(5,registro.getCodigoTipoPlato());
                procedimiento.execute();
                listaPlato.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void borrar(){
        if(tblPlatos.getSelectionModel().getSelectedItem() != null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar este elemento?","Eliminar Servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlato(?)}");
                        procedimiento.setInt(1,((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                        procedimiento.execute();
                        listaPlato.remove(tblPlatos.getSelectionModel().getFocusedIndex());
                        tblPlatos.getSelectionModel().clearSelection();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }else{
            JOptionPane.showMessageDialog(null,"Debe de seleccionar un elemento");
        }
    }
    
    public void actualizar(){
        if(tblPlatos.getSelectionModel().getSelectedItem() != null){
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarPlato(?,?,?,?,?,?)}");
                Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
                registro.setCantidad(Integer.parseInt(txtCantidadPlato.getText()));
                registro.setNombrePlato(txtNombrePlato.getText());
                registro.setNombrePlato(txtDescripcionPlato.getText());
                registro.setPrecioPlato(Integer.parseInt(txtPrecioPlato.getText()));
                
                procedimiento.setInt(1,registro.getCodigoPlato());
                procedimiento.setInt(2, registro.getCantidad());
                procedimiento.setString(3, registro.getNombrePlato());
                procedimiento.setString(4,registro.getDescripcionPlato());
                procedimiento.setInt(5,registro.getPrecioPlato());
                procedimiento.setInt(6,registro.getCodigoTipoPlato());
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
        switch (tipoOperacion){
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
        switch (tipoOperacion){
            case GUARDAR:
                
                limpiarControles();
                normalidad();
                tipoOperacion = operaciones.NINGUNO;
            break; 
            default:
                borrar();
                limpiarControles();
                normalidad();
            break;
        }
    }

    public void editar(){
        switch (tipoOperacion){
            case NINGUNO:
                activarControles();
                desactivarID();
                btnNuevo.setVisible(false);
                btnEliminar.setVisible(false);
                btnEditar.setText("Guardar");
                btnReporte.setText("Cancelar"); 
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
                tipoOperacion = operaciones.ACTUALIZAR;
            break;    
            case ACTUALIZAR:
                actualizar();
                normalidad();
                limpiarControles();
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
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
        lblCodigoPlato.setVisible(false);
        txtCodigoPlato.setVisible(false);
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(true);
        txtCantidadPlato.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoPlato.setText("");
        txtCantidadPlato.setText("");
        txtNombrePlato.setText("");
        txtDescripcionPlato.setText("");
        txtPrecioPlato.setText("");
    }
    
    public void normalidad(){
        lblCodigoPlato.setVisible(true);
        txtCodigoPlato.setVisible(true);
        
        txtCodigoPlato.setEditable(false);
        txtCantidadPlato.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);
        
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnReporte.setText("Reporte");
        
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
    
//////////////////////    GETERS Y SETTERS ESCENARIOS     ////////////////////// 

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    
    
}
