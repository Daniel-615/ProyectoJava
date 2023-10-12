/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.backend;

import com.mycompany.proyecto.frontend.ProductoView;
import java.awt.Color;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author suyan
 */
public class Productos {
    String nombre,descripcion;
    int stock,stock_min;
    float precio_unitario;
    //obtener los bytes de la imagen
    private int longitudBytes;
    private FileInputStream fS;
    // Constructor que acepta la longitud de bytes como argumento
    public Productos(int longitudBytes,FileInputStream fS) {
        this.longitudBytes = longitudBytes;
        this.fS = fS;
    }
    public Productos(){  
    };
    public int getBytes(){
        return longitudBytes;
    }
    public FileInputStream getFS() {
        return fS;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock_min() {
        return stock_min;
    }

    public void setStock_min(int stock_min) {
        this.stock_min = stock_min;
    }

    public float getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(float precio_unitario) {
        this.precio_unitario = precio_unitario;
    }
    
    
    public void createTable() {
        try {
            DBConnection objetoConexion = new DBConnection();
            String createTableSQL = """
                                    CREATE TABLE IF NOT EXISTS producto (
                                        id_producto serial PRIMARY KEY,
                                        nombre varchar(30),
                                        descripcion VARCHAR(100),
                                        stock INTEGER,
                                        stock_minimo INTEGER,
                                        precio_unitario DECIMAL(10,2),
                                        imagen BYTEA
                                    );""";
            CallableStatement cs = objetoConexion.establecerConexion().prepareCall(createTableSQL);
            cs.execute();
            System.out.println("Tabla creada correctamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
        }
    }
    public void createProcedureProducto(){
        Connection connection = null;
        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                            CREATE OR REPLACE FUNCTION insertar_producto(
                                p_nombre varchar(30),
                                p_descripcion VARCHAR(100),
                                p_stock INTEGER,
                                p_stock_minimo INTEGER,
                                p_precio_unitario DECIMAL(10, 2),
                                p_imagen BYTEA
                            ) RETURNS void AS
                            $$
                            BEGIN
                                INSERT INTO producto (nombre, descripcion, stock, stock_minimo, precio_unitario, imagen)
                                VALUES (p_nombre, p_descripcion, p_stock, p_stock_minimo, p_precio_unitario, p_imagen);
                            END;
                            $$
                            LANGUAGE plpgsql;
                      """;

            connection = objetoConexion.establecerConexion(); 

        try (CallableStatement cs = connection.prepareCall(insert)) {
            cs.execute();
             System.out.println("Procedure has been created successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }
    public void createProducto(JTextField paramNombre, JTextField paramDescripcion, JTextField paramStock, JTextField paramStock_min, JTextField paramPrecio, ProductoView productoView) {
        Connection connection = null;
        if (paramNombre.getText().equals("")) {
            productoView.setTxtNombreBackgroundColor(Color.RED);
            productoView.setTxtDescripcionBackgroundColor(Color.RED);
            productoView.setTxtStockBackgroundColor(Color.RED);
            productoView.setTxtStock_minBackgroundColor(Color.RED);
            productoView.setTxtPrecioBackgroundColor(Color.RED);
            JOptionPane.showMessageDialog(null,"Debes llenar todos los campos");
        } else {
            setNombre(paramNombre.getText().trim());
            setDescripcion(paramDescripcion.getText().trim());
            setStock(Integer.parseInt(paramStock.getText().trim()));
            setStock_min(Integer.parseInt(paramStock_min.getText().trim()));
            setPrecio_unitario(Float.parseFloat(paramPrecio.getText().trim()));
            DBConnection objetoConexion = new DBConnection();
            try{
                String insert="CALL insertar_producto(?, ?, ?, ?, ?, ?)";
                connection = objetoConexion.establecerConexion();
                try(CallableStatement cs = connection.prepareCall(insert)){
                    cs.setString(1, getNombre());
                    cs.setString(2, getDescripcion());
                    cs.setInt(3,getStock());
                    cs.setInt(4,getStock_min());
                    cs.setFloat(5,getPrecio_unitario());
                    cs.setBlob(6,getFS(),getBytes());
                    cs.execute();
                    limpiar(productoView);
                    productoView.setTxtNombreBackgroundColor(Color.green);
                    productoView.setTxtDescripcionBackgroundColor(Color.green);
                    productoView.setTxtStockBackgroundColor(Color.green);
                    productoView.setTxtStock_minBackgroundColor(Color.green);
                    productoView.setTxtPrecioBackgroundColor(Color.green);
                    System.out.println("Product has been created successfully");
                }
            }catch(SQLException e){
                System.out.println("Error saving: "+e.getMessage());
            }finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage());
                    }
                }
            }   
        }
    }
    public void limpiar(ProductoView productoView){
        productoView.setNombreText("");
        productoView.setFotoText("Foto");
    }
}
