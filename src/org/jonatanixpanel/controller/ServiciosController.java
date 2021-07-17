package org.jonatanixpanel.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.Empresa;
import org.jonatanixpanel.bean.Servicio;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.report.GenerarReporte;
import org.jonatanixpanel.sistema.Principal;

public class ServiciosController implements Initializable{
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpFechaServicios.add(fecha,1,1);
        fecha.getStylesheets().add("/org/jonatanixpanel/resource/DatePicker.css");
        cmbCodigoEmpresa.setItems(getEmpresa());
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
//////////////////////////////    VARIABLES     //////////////////////////////// 
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    private boolean vacio = false;
    
    @FXML private Label lblCodigoPresupuesto;
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtFechaServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn  colCodigoServicio;
    @FXML private TableColumn  colFechaServicio;
    @FXML private TableColumn  colTipoServicio;
    @FXML private TableColumn  colHoraServicio;
    @FXML private TableColumn  colLugarServicio;
    @FXML private TableColumn  colTelefonoContacto;
    @FXML private TableColumn  colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgHome;
    @FXML private GridPane grpFechaServicios;
    
    
/////////////////////////////   METODOS INTERNOS     ///////////////////////////

    public void cargarDatos(){
        
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio,String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElemento(){
        if (tblServicios.getSelectionModel().getSelectedItem() != null){
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set((((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio()));
        txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
        txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
        txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }else{
        
        }
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
    
    private ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(  resultado.getInt("codigoEmpresa"),
                                        resultado.getString("nombreEmpresa"),
                                        resultado.getString("direccion"),
                                        resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
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
                                registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
////////////////////////////       METODOS BOTONES     ////////////////////////////

    public void guardar(){
        Servicio registro = new Servicio();
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
                procedimiento.setDate(1,new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(2,registro.getTipoServicio());
                procedimiento.setString(3,registro.getHoraServicio());
                procedimiento.setString(4,registro.getLugarServicio());
                procedimiento.setString(5,registro.getTelefonoContacto());
                procedimiento.setInt(6,registro.getCodigoEmpresa());
                procedimiento.execute();
                listaServicio.add(registro);
            }catch(Exception e){    
                e.printStackTrace();
            }
    }
    
    public void borrar(){
        if(tblServicios.getSelectionModel().getSelectedItem() != null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar este elemento?","Eliminar Servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                        procedimiento.setInt(1,((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                        procedimiento.execute();
                        listaServicio.remove(tblServicios.getSelectionModel().getFocusedIndex());
                        tblServicios.getSelectionModel().clearSelection();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    
                }
        
        }else{
            JOptionPane.showMessageDialog(null,"Tiene que seleccionar un elemento");
        }
    }
    
    public void actualizar(){
        if(tblServicios.getSelectionModel().getSelectedItem() != null){
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarServicio(?,?,?,?,?,?,?)}");
                Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
                registro.setFechaServicio(fecha.getSelectedDate());
                registro.setTipoServicio(txtTipoServicio.getText());
                registro.setHoraServicio(txtHoraServicio.getText());
                registro.setLugarServicio(txtLugarServicio.getText());
                registro.setTelefonoContacto(txtTelefonoContacto.getText());
                
                procedimiento.setInt(1,registro.getCodigoServicio());
                procedimiento.setDate(2,new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(3,registro.getTipoServicio());
                procedimiento.setString(4,registro.getHoraServicio());
                procedimiento.setString(5,registro.getLugarServicio());
                procedimiento.setString(6,registro.getTelefonoContacto());
                procedimiento.setInt(7,registro.getCodigoEmpresa());
                procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe de Seleccionar un elemento");
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codServicio = Integer.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
        parametros.put("codServicio", codServicio);
        GenerarReporte.mostrarReporte("ReporteServicios.jasper", "Reporte de Servicios",parametros);
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
            if(tblServicios.getSelectionModel().getSelectedItem() != null){
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
                limpiarControles();
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
            default:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
            break;
        }    
    }


///////////////////////////    METODOS BOTONES     /////////////////////////////
public void desactivarID(){
        lblCodigoPresupuesto.setVisible(false);
        txtCodigoServicio.setVisible(false);
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(true);
        //txtFechaServicio.setEditable(true);
        txtTipoServicio.setEditable(true);
        txtHoraServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void normalidad(){
        lblCodigoPresupuesto.setVisible(true);
        txtCodigoServicio.setVisible(true);
        
        txtCodigoServicio.setEditable(false);
        //txtFechaServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtHoraServicio.setEditable(false);
        txtLugarServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setDisable(false);
        
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
        txtCodigoServicio.setText("");
        //txtFechaServicio.setText("");
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
    }

    
//////////////////////    GETERS Y SETTERS ESCENARIOS     //////////////////////     
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    
    
}
