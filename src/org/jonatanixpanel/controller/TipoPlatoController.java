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
import org.jonatanixpanel.bean.TipoPlato;
import org.jonatanixpanel.db.Conexion;
import org.jonatanixpanel.sistema.Principal;

    
public class TipoPlatoController implements Initializable{
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        cargarDatos();
        btnNuevo.getStylesheets().add("/org/jonatanixpanel/resource/botonVerde.css");
        btnEliminar.getStylesheets().add("/org/jonatanixpanel/resource/botonRojo.css");
        btnEditar.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
        btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
    }
    
///////////////////////////// VARIABLES ////////////////////////////////////////
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;
    private ImageView imgPlatoUno = new ImageView("/org/jonatanixpanel/img/platoUno.png");
    private ImageView imgPlatoDos = new ImageView("/org/jonatanixpanel/img/platoDos.png");
    
    @FXML private Label lblCodigoTipoPlato;
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipoPlato;
    @FXML private TableView tblTipoPlatos;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipoPlato;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgLogoPlato;
    
    
/////////////////////////////   METODOS INTERNOS     ///////////////////////////
    
    ObservableList<TipoPlato> getTipoPlato(){
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
    
    public void cargarDatos(){
    tblTipoPlatos.setItems(getTipoPlato());
    colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
    colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,String>("descripcionTipoPlato"));
    }
    
    public void seleccionarElemento(){
    txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    txtDescripcionTipoPlato.setText(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcionTipoPlato());
    }
    
////////////////////////////       METODOS BOTONES     /////////////////////////
    
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        registro.setDescripcionTipoPlato(txtDescripcionTipoPlato.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato(?)");
            procedimiento.setString(1,registro.getDescripcionTipoPlato());
            procedimiento.execute();
            listaTipoPlato.add(registro);
                }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void borrar(){
        if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
            int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro que quiere eliminar este elemento?"
                    + "Si otro elemento de PLATOS  esta ligado a este tambien se eliminara","Eliminar Tipo Plato",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                        procedimiento.setInt(1,((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                        procedimiento.execute();
                        listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getFocusedIndex());
                        tblTipoPlatos.getSelectionModel().clearSelection();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }else{
            JOptionPane.showMessageDialog(null,"Debe de seleccionar un elemento");
        }
    }
    
    public void actualizar(){
        if(tblTipoPlatos.getSelectionModel().getSelectedItem() != null){
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoPlato(?,?)}");
                TipoPlato registro = (TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem();
                registro.setDescripcionTipoPlato(txtDescripcionTipoPlato.getText());
                
                
                procedimiento.setInt(1,registro.getCodigoTipoPlato());
                procedimiento.setString(2,registro.getDescripcionTipoPlato());
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
                txtDescripcionTipoPlato.setEditable(true);
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
                btnReporte.getStylesheets().clear();
                btnReporte.getStylesheets().add("/org/jonatanixpanel/resource/botonAzul.css");
                normalidad();
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
///////////////////////////    CONTROL DE BOTONES     //////////////////////////
    public void normalidad(){
        lblCodigoTipoPlato.setVisible(true);
        txtCodigoTipoPlato.setVisible(true);
        
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);
        
        txtCodigoTipoPlato.setText("");
        txtDescripcionTipoPlato.setText("");
        
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
        lblCodigoTipoPlato.setVisible(false);
        txtCodigoTipoPlato.setVisible(false);
    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(true);
        txtDescripcionTipoPlato.setEditable(true);
        
        txtCodigoTipoPlato.setText("");
        txtDescripcionTipoPlato.setText("");
    }
    
    public void logoInPlato(){
        imgLogoPlato.setImage(imgPlatoDos.getImage());
    }
    
    public void logoOutPlato(){
        imgLogoPlato.setImage(imgPlatoUno.getImage());
    }
    
/////////////////////////// GETTERS AND SETTERS ////////////////////////////////

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
   public void menuPrincipal(){
       escenarioPrincipal.menuPrincipal();
   }
    
   public void ventanaPlato(){
       escenarioPrincipal.ventanaPlato();
   }
    
}




