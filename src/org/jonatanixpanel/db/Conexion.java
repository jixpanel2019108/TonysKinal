package org.jonatanixpanel.db;

import java.sql.Connection;
import com.mysql.jdbc.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    //Tenemos que crear una variable de tipo connection
    private Connection conexion;
    //Tenemos aue crear una variable estatica para poder usar un metodo estatico
    private static Conexion instancia; //Lo que hara es instanciar el objeto de conexion
    
    //Tenemos que crear un metodo para crear nuestra linea de conexion
    public Conexion(){
        try{// puede que nuestar clase tenga errores de distintos tipo por eso hacemos muchos try catchs    
            Class.forName("com.mysql.jdbc.Driver").newInstance();//El paquete de la clase que usare y crear una intancia para crear un espacio en memoria
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBTonysKinal2019108?useSSL=false","root","admin");//para mi conexion el driver manager / get connection me pide cual es ese url
            
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(InstantiationException e){//Que no exista la instancia (en workBench)
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(SQLException e){          //Que no pueda utilizar algo de sql
            e.printStackTrace();
        }
        
    }
        
        public static Conexion getInstance(){
            if (instancia == null){
                instancia = new Conexion();
            }
            return  instancia;  
        }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

        
        
        
        
    
    
    
    
    
    
}
