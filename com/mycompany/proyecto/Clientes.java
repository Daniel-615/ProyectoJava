/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto;

import java.sql.CallableStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author suyan
 */
public class Clientes {
   
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
}
