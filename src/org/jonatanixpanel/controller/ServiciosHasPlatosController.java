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
import org.jonatanixpanel.bean.Plato;
import org.jonatanixpanel.bean.Servicio;
import org.jonatanixpanel.bean.ServiciosHasPlatos;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;


public class ServiciosHasPlatosController implements Initializable{
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarDatos();
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoPlato.setItems(getPlato());
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
    }
    
//////////////////////////////    VARIABLES     ////////////////////////////////   
    private Principal escenarioPrincipal;

    private enum operaciones{NINGUNO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, ELIMINAR, AGREGAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<ServiciosHasPlatos> listaServiciosHasPlatos;
    ObservableList<Servicio> listaServicio;
    ObservableList<Plato> listaPlato;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblServiciosHasPlatos;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnEliminar;
    
/////////////////////////// METODOS INTERNOS /////////////////////////////////// 
    
    public ObservableList<ServiciosHasPlatos> getServiciosHasPlatos(){
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServiciosHasPlatos(resultado.getInt("codigoServicio"),
                                                 resultado.getInt("codigoPlato") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaServiciosHasPlatos = FXCollections.observableArrayList(lista);
    }
    
     public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoServicio"),
                                        resultado.getString("horaServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
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
    
        public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getInt("codigoServicio"),
                                            registro.getDate("fechaServicio"),
                                            registro.getString("tipoServicio"),
                                            registro.getString("horaServicio"),
                                            registro.getString("lugarServicio"),
                                            registro.getString("telefonoContacto"),
                                            registro.getInt("codigoEmpresa") );
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
        tblServiciosHasPlatos.setItems(getServiciosHasPlatos());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos,Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos,Integer>("Platos_codigoPlato"));
    }
    
    public void seleccionarElemento(){
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getPlatos_codigoPlato()));
    } 
    
////////////////////////// GETTERS AND SETTERS  ////////////////////////////////
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaServicioHasPlatos(){
        escenarioPrincipal.ventanaServiciosHasPlatos();
    }
    
    
    
}
