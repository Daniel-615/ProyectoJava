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
public class Empleados {
    public void createTable() {
        try {
            DBConnection objetoConexion = new DBConnection();
            String createTableSQL = """
                                    CREATE TABLE IF NOT EXISTS empleado (
                                        id_empleado serial PRIMARY KEY,
                                        primer_nombre VARCHAR(100),
                                        segundo_nombre VARCHAR(100),
                                        primer_apellido VARCHAR(100),
                                        segundo_apellido VARCHAR(100),
                                        nit VARCHAR(10),
                                        salario DECIMAL(10, 2),
                                        status BOOLEAN
                                    );""";
            CallableStatement cs=objetoConexion.establecerConexion().prepareCall(createTableSQL);
            cs.execute();
            JOptionPane.showMessageDialog(null, "Tabla Empleados creada correctamente");
           } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
            }
    }
}
