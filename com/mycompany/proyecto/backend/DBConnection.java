/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author suyan
 */
public class DBConnection {
     Connection cn = null;

    public Connection establecerConexion() {
        try {
            Properties propiedades = new Properties();
            String rutaArchivo = "src\\main\\java\\com\\mycompany\\proyecto\\backend\\config.properties";

            try (FileInputStream fis = new FileInputStream(rutaArchivo)) {
                propiedades.load(fis);

                String dbUser = propiedades.getProperty("dbUser");
                String dbPassword = propiedades.getProperty("dbPassword");
                String dbUrl = propiedades.getProperty("dbUrl");

                Class.forName("org.postgresql.Driver");
                cn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                System.out.println("Conexión establecida");
            } catch (IOException | ClassNotFoundException | SQLException e) {
                System.out.println("Error de Conexión: "+e.getMessage());
            }
        } catch (Exception e) {
             System.out.println("Error de Conexión: "+e.getMessage());
        }
        return cn;
    }
    public void cerrarConexion(Connection conexion) {
    if (conexion != null) {
        try {
            conexion.close();
            JOptionPane.showMessageDialog(null, "Conexión cerrada");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

}
