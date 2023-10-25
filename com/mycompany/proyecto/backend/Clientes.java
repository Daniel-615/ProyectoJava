/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.backend;

import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author suyan
 */
public class Clientes {
    int codigo;
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    public void createTable() {
        try {
            DBConnection objetoConexion = new DBConnection();
            String createTableSQL = """
                                    CREATE TABLE IF NOT EXISTS Cliente (
                                        id_cliente serial PRIMARY KEY,
                                        nombre VARCHAR(100),
                                        apellido VARCHAR(100),
                                        razon_social VARCHAR(100),
                                        nit VARCHAR(10),
                                        direccion VARCHAR(100),
                                        telefono VARCHAR(100),
                                        email VARCHAR(50),
                                        fecha_ingreso DATE,
                                        status BOOLEAN
                                    );""";
            CallableStatement cs=objetoConexion.establecerConexion().prepareCall(createTableSQL);
            cs.execute();
            System.out.println("Tabla creada correctamente");
           } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
            }
    }
    public void DeleteClientes(JTextField paramCodigo) {
        setCodigo(Integer.parseInt(paramCodigo.getText()));
       
        DBConnection objetoConexion= new DBConnection();
        String delete="""
                      CREATE OR REPLACE PROCEDURE DELETE_CLIENTE(
                      \tIN ID INT
                      )
                       LANGUAGE plpgsql
                       AS $$
                       BEGIN 
                       \tDELETE FROM CLIENTE
                      \tWHERE CODIGO=ID;
                       END;
                       $$;""";
        try{
            CallableStatement cs=objetoConexion.establecerConexion().prepareCall(delete);
            cs.execute();
            System.out.println("Se ha crado el procedimiento correctamente");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        String call="CALL DELETE_CLIENTE(?);";
        try{
            CallableStatement cs=objetoConexion.establecerConexion().prepareCall(call);
            cs.setInt(1,getCodigo());
            cs.execute();
            JOptionPane.showMessageDialog(null, "Se ha llamado el procedimiento correctamente");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}

