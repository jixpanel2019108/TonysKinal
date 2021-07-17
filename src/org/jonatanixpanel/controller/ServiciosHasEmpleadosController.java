package org.jonatanixpanel.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.Empleado;
import org.jonatanixpanel.bean.Servicio;
import org.jonatanixpanel.bean.ServiciosHasEmpleados;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;


public class ServiciosHasEmpleadosController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarDatos();
        
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpFechaEvento.add(fecha, 1, 2);
        fecha.getStylesheets().add("/org/jonatanixpanel/resource/DatePicker.css");
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
//////////////////////////////    VARIABLES     //////////////////////////////// 
    private Principal escenarioPrinicpal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosHasEmpleados> listaServicioHasEmpleados;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private DatePicker fecha;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private TableView tblServiciosHasEmpleados;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private GridPane grpFechaEvento;
    
/////////////////////////////   METODOS INTERNOS     ///////////////////////////

    public ObservableList<ServiciosHasEmpleados> getServiciosHasPlatos(){
        ArrayList<ServiciosHasEmpleados> lista = new ArrayList<ServiciosHasEmpleados>();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados}");
                ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new ServiciosHasEmpleados(resultado.getInt("codigoServiciosHasEmpleados"),
                                                        resultado.getInt("codigoServicio"),
                                                        resultado.getInt("codigoEmpleado"),
                                                        resultado.getDate("fechaEvento"),
                                                        resultado.getString("horaEvento"),
                                                        resultado.getString("lugarEvento")));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return listaServicioHasEmpleados = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList lista = new ArrayList();
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
                ResultSet resultado = procedimiento.executeQuery();
                    while(resultado.next()){
                        lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
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
    
    public Servicio buscarServicio(int codigoServicio){
        Servicio registro = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
                procedimiento.setInt(1,codigoServicio);
                ResultSet resultado = procedimiento.executeQuery();
                    while(resultado.next()){
                        registro = new Servicio(resultado.getInt("codigoServicio"),
                                                resultado.getDate("fechaServicio"),
                                                resultado.getString("tipoServicio"),
                                                resultado.getString("horaServicio"),
                                                resultado.getString("lugarServicio"),
                                                resultado.getString("telefonoContacto"),
                                                resultado.getInt("codigoEmpresa"));
                    }
            }catch(Exception e){
                e.printStackTrace();
            }
        return registro;
    }
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado registro = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1,codigoEmpleado);
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){}
                    registro = new Empleado(resultado.getInt("codigoEmpleado"),
                                                resultado.getInt("numeroEmpleado"),
                                                resultado.getString("apellidosEmpleado"),
                                                resultado.getString("nombresEmpleado"),
                                                resultado.getString("direccionEmpleado"),
                                                resultado.getString("telefonoContacto"),
                                                resultado.getString("gradoCocinero"),
                                                resultado.getInt("codigoTipoEmpleado"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return registro; 
    }
    
    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServiciosHasPlatos());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados,Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados,Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados,Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados,String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados,String>("lugarEvento"));     
    }
    
    public void seleccionarElemento(){
        if (tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
        cmbCodigoServicio.getSelectionModel().select(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio());
        cmbCodigoEmpleado.getSelectionModel().select(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        fecha.selectedDateProperty().set(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        txtHoraEvento.setText(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento());
        txtLugarEvento.setText(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
        }else{
        
        }
    }
    
////////////////////////////       METODOS BOTONES     ////////////////////////////

    public void guardar(){
        ServiciosHasEmpleados registro = new ServiciosHasEmpleados();
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio()); 
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleados(?,?,?,?,?)}");
                procedimiento.setInt(1,registro.getCodigoServicio());
                procedimiento.setInt(2,registro.getCodigoEmpleado());
                procedimiento.setDate(3,new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setString(4,registro.getHoraEvento());
                procedimiento.setString(5,registro.getLugarEvento());
                procedimiento.execute();
                listaServicioHasEmpleados.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    public void borrar(){
        if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar este elemento? si esta ligado a otros datos en otras tablas tambien se eliminaran esos registros","Eliminar Servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleados(?)}");
                        procedimiento.setInt(1,((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServiciosHasEmpleados());
                        listaServicioHasEmpleados.remove(tblServiciosHasEmpleados.getSelectionModel().getFocusedIndex());
                        tblServiciosHasEmpleados.getSelectionModel().clearSelection();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }else{
            JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
        }
    }
    
    public void actualizar(){
        if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null){
            try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarServicios_has_Empleados(?,?,?,?,?,?)}");
               ServiciosHasEmpleados registro = (ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem();
               registro.setCodigoServicio(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio());
               registro.setCodigoEmpleado(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
               registro.setFechaEvento(fecha.getSelectedDate());
               registro.setHoraEvento(txtHoraEvento.getText());
               registro.setLugarEvento(txtLugarEvento.getText());
               
               procedimiento.setInt(1,registro.getCodigoServicio());
               procedimiento.setInt(2,registro.getCodigoEmpleado());
               procedimiento.setDate(3,new java.sql.Date(registro.getFechaEvento().getTime()));
               procedimiento.setString(4,registro.getHoraEvento());
               procedimiento.setString(5,registro.getLugarEvento());
               procedimiento.setInt(6,registro.getCodigoServiciosHasEmpleados());
               procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
        }
    }

///////////////////////////////    BOTONES     /////////////////////////////////
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                cargarDatos();
                normalidad();
                tipoOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
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
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
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
            default:
            break;
        }
    }

///////////////////////////    METODOS BOTONES     /////////////////////////////

    public void activarControles(){
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
    }
    
    public void limpiarControles(){
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
    }
    
    public void normalidad(){
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
        
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
        
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnReporte.setText("Reporte");
        
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);
    }
    
    
//////////////////////    GETERS Y SETTERS ESCENARIOS     //////////////////////

    public Principal getEscenarioPrinicpal() {
        return escenarioPrinicpal;
    }

    public void setEscenarioPrinicpal(Principal escenarioPrinicpal) {
        this.escenarioPrinicpal = escenarioPrinicpal;
    }

        public void menuPrincipal(){
        escenarioPrinicpal.menuPrincipal();
    }
    
    
}
