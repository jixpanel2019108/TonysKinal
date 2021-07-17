package org.jonatanixpanel.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jonatanixpanel.bean.Empresa;
import org.jonatanixpanel.bean.Presupuesto;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.report.GenerarReporte;

import org.jonatanixpanel.sistema.Principal;


//imlementamos implemets initializabel la interfaz
public class PresupuestoController implements Initializable{//1
    private Principal escenarioPrincipal;//2
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO; //Variable tipo Operaciones que le ponemos UN default
    //Voy a tener una observable list de tipo Presupuesto
    //y voy a necesitar el codigoEmpresa necesito una lista observable de empresa 
    //entonces tendre 2 observablelist
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Presupuesto> listaPresupuesto;
    private DatePicker fecha;
    private boolean vacio = false;
    
    //INYECCION DE OBJETOS
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFechaSolicitud;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Label lblCodigoPresupuesto;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/jonatanixpanel/resource/DatePicker.css");
        grpFechaSolicitud.add(fecha,0,0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
    
    
    //INYECCION DE OBJETOS
    //TENEMOS QUE CARGAR DATOS  Y PONERLO EN EL initialize
    /////////////////////////   INTERNO     ////////////////////////////////////
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));//Seteamos en la celda
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto,Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElemento(){
        if (tblPresupuestos.getSelectionModel().getSelectedItem() != null){
        //es un entero que quiero pasar a string
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
         }else{
            tblPresupuestos.getSelectionModel().select(-1); 
        }
    }
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        //IR A TRAR LOS DATOS DEL MODELO DE DATOS
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuestos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                          resultado.getDate("fechaSolicitud"),
                                          resultado.getDouble("cantidadPresupuesto"),
                                          resultado.getInt("codigoEmpresa")));
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
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
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro  = procedimiento.executeQuery();
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
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(2,registro.getCantidadPresupuesto());
            procedimiento.setInt(3,registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
                }
    }
    
    public void eliminarMetodo(){
        if(tblPresupuestos.getSelectionModel().getSelectedItem() != null ){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                        procedimiento.setInt(1,((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                        procedimiento.execute();
                        if(procedimiento.execute()== false){
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
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
            JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemnto");
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarPresupuesto(?,?,?,?)}");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            
            procedimiento.setInt(1,registro.getCodigoPresupuesto());
            procedimiento.setDate(2,new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setDouble(3,registro.getCantidadPresupuesto());
            procedimiento.setInt(4,registro.getCodigoEmpresa());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("reportePresupuestoFinal.jasper", "Reporte de Presupuesto",parametros);
    }
    
    
    
    ///////////////////////////    BOTONES     /////////////////////////////////
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                desactivarBotones();
                activarControles();
                desactivarID();
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCantidadPresupuesto.setEditable(true);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                limpiarControles();
                tipoDeOperaciones = operaciones.GUARDAR;
            break;
            case GUARDAR:
                
                activarBotones();
                desactivarControles();
                activarID();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                guardar();
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                
            break;
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                activarBotones();
                desactivarControles();
                limpiarControles();
                activarID();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                tipoDeOperaciones = operaciones.NINGUNO;
            break;
            default:
                eliminarMetodo();
            break;
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    desactivarBotones();
                    activarControles();
                    desactivarID();
                    btnEditar.setVisible(true);
                    btnReporte.setVisible(true);
                    btnEditar.setText("Guardar");
                    btnReporte.setText("Cancelar");
                    btnReporte.getStylesheets().clear();
                    btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null,"Debe Seleccionar un elemento");
                }
            break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                activarBotones();
                activarID();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void reporte(){
        switch(tipoDeOperaciones){
            case ACTUALIZAR:
                desactivarControles();
                activarBotones();
                activarID();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                tipoDeOperaciones = operaciones.NINGUNO;
            break;
            default:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                imprimirReporte();
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar algun elemento");
                }
            break;
        }
    }
    
    ///////////////////////    METODOS BOTONES     /////////////////////////////
    public void desactivarID(){
        lblCodigoPresupuesto.setVisible(false);
        txtCodigoPresupuesto.setVisible(false);
    }
    
    public void activarID(){
        lblCodigoPresupuesto.setVisible(true);
        txtCodigoPresupuesto.setVisible(true);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(true);
        txtCantidadPresupuesto.setEditable(true);
        grpFechaSolicitud.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(false);
        grpFechaSolicitud.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
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
    
    public void limpiarControles(){
        txtCodigoPresupuesto.setText("");
        txtCantidadPresupuesto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
    }
    
    
    //getters y setteers de escenario principal 3
    //////////////////    GETERS Y SETTERS ESCENARIOS     //////////////////////
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
}

//pra limiar existe un index