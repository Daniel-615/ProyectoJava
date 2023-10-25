/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.backend;

import com.mycompany.proyecto.frontend.ProductoView;
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author suyan
 */
public class Productos {
    String nombre,descripcion;
    int stock,stock_min,id;
    double precio_unitario;
    public int getId() {
        return id;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public void setId(int id) {
        this.id = id;
    }
    

    //Constructor vacio 
    public Productos(){  
    };
    
    //metodos

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
                                        precio_unitario double precision
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
                            CREATE OR REPLACE PROCEDURE INSERT_PRODUCTO(
                                                \tIN p_nombre varchar(30),
                                                \tIN p_descripcion VARCHAR(100),
                                                \tIN p_stock INTEGER,
                                                \tIN p_stock_minimo INTEGER,
                                                \tIN p_precio_unitario double precision
                                                )
                                                LANGUAGE plpgsql
                                                AS $$
                                                    BEGIN 
                                                  INSERT INTO producto (nombre, descripcion, stock, stock_minimo, precio_unitario)
                                                  VALUES (p_nombre, p_descripcion, p_stock, p_stock_minimo, p_precio_unitario);
                                                  END;
                                                  $$;
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

    if (paramNombre.getText().isEmpty() || paramDescripcion.getText().isEmpty() || paramStock.getText().isEmpty() || paramStock_min.getText().isEmpty() || paramPrecio.getText().isEmpty()) {
        productoView.setTxtNombreBackgroundColor(Color.RED);
        productoView.setTxtDescripcionBackgroundColor(Color.RED);
        productoView.setTxtStockBackgroundColor(Color.RED);
        productoView.setTxtStock_minBackgroundColor(Color.RED);
        productoView.setTxtPrecioBackgroundColor(Color.RED);
        JOptionPane.showMessageDialog(null, "Debes llenar todos los campos");
    } else {
        setNombre(paramNombre.getText().trim());
        setDescripcion(paramDescripcion.getText().trim());
        setStock(Integer.parseInt(paramStock.getText().trim()));
        setStock_min(Integer.parseInt(paramStock_min.getText().trim()));
        setPrecio_unitario(Float.parseFloat(paramPrecio.getText().trim()));

        DBConnection objetoConexion = new DBConnection();
        try {
            String insert = "CALL insert_producto(?, ?, ?, ?, ?);";
            connection = objetoConexion.establecerConexion();
            try (CallableStatement cs = connection.prepareCall(insert)) {
                cs.setString(1, getNombre());
                cs.setString(2, getDescripcion());
                cs.setInt(3, getStock());
                cs.setInt(4, getStock_min());
                cs.setDouble(5, getPrecio_unitario());
                cs.execute();
                limpiar(productoView);
                productoView.setTxtNombreBackgroundColor(Color.green);
                productoView.setTxtDescripcionBackgroundColor(Color.green);
                productoView.setTxtStockBackgroundColor(Color.green);
                productoView.setTxtStock_minBackgroundColor(Color.green);
                productoView.setTxtPrecioBackgroundColor(Color.green);
                System.out.println("Producto creado exitosamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
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
}

    public void limpiar(ProductoView productoView){
        productoView.setNombreText("");
        productoView.setDescripcionText("");
        productoView.setStockText("");
        productoView.setStockMinText("");
        productoView.setPrecioText("");
    }
    //Tabla
    public void mostrarProductos(JTable TablaTotalProductos) {
        DBConnection objetoConexion = new DBConnection();
        DefaultTableModel modelo = new DefaultTableModel();
        String sql = "SELECT * FROM producto;";
        modelo.addColumn("nombre");
        modelo.addColumn("descripcion");
        modelo.addColumn("stock");
        modelo.addColumn("stock_minimo");
        modelo.addColumn("precio_unitario");
        TablaTotalProductos.setModel(modelo);
    
        Statement stmt = null;
        Connection connection = null;

        try {
            connection = objetoConexion.establecerConexion();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        
            while (rs.next()) {
            setNombre( rs.getString("nombre"));
            setDescripcion(rs.getString("descripcion"));
            setStock(rs.getInt("stock"));
            setStock_min(rs.getInt("stock_minimo"));
            setPrecio_unitario(rs.getDouble("precio_unitario"));
          
            // Crea un nuevo array de objetos para representar una fila de datos
            Object[] datos = { getNombre(), getDescripcion(), getStock(), getStock_min(), getPrecio_unitario()};
            modelo.addRow(datos);
        }
        TablaTotalProductos.setModel(modelo);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } finally {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Error: "+e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion: "+e);
            }
        }
    }
}
    //ProductoConsulta
    public void mostrarProducto(JTextField id, ProductoView productoView) {
        Connection connection = null;
        DBConnection objetoConexion = new DBConnection();

        try {
            String consulta = "SELECT nombre, descripcion, stock, stock_minimo, precio_unitario, imagen FROM producto WHERE id_producto = ?";
            connection = objetoConexion.establecerConexion();
            setId(Integer.parseInt(id.getText()));
            try (PreparedStatement preparedStatement = connection.prepareStatement(consulta)) {
                preparedStatement.setInt(1, getId());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        setNombre(resultSet.getString("nombre"));
                        setDescripcion(resultSet.getString("descripcion"));
                        setStock(resultSet.getInt("stock"));
                        setStock_min(resultSet.getInt("stock_minimo"));
                        setPrecio_unitario(resultSet.getFloat("precio_unitario"));
                        productoView.setNombreText(getNombre());
                        productoView.setDescripcionText(getDescripcion());
                        productoView.setStockText(String.valueOf(getStock()));
                        productoView.setStockMinText(String.valueOf(getStock_min()));
                        productoView.setPrecioText(String.valueOf(getPrecio_unitario()));
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "El producto con ID " + id + " no se encontró.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al recuperar el producto: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    };
    public void actualizarProcedure(){
       Connection connection = null;
        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                           CREATE OR REPLACE PROCEDURE actualizar_producto(
                                p_id_producto INT,
                                p_nombre VARCHAR(30),
                                p_descripcion VARCHAR(100),
                                p_stock INTEGER,
                                p_stock_minimo INTEGER,
                                p_precio_unitario double precision
                            ) AS $$
                            BEGIN
                                UPDATE producto
                            SET 
                                nombre = p_nombre,
                                descripcion = p_descripcion,
                                stock = p_stock,
                                stock_minimo = p_stock_minimo,
                                precio_unitario = p_precio_unitario
                            WHERE
                                id_producto = p_id_producto;
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
    public void actualizarProducto(JTextField id, JTextField Nombre, JTextField Descripcion, JTextField Stock, JTextField Stock_min, JTextField precio) {
    setId(Integer.parseInt(id.getText()));
    setNombre(Nombre.getText());
    setDescripcion(Descripcion.getText());
    setStock(Integer.parseInt(Stock.getText()));
    setStock_min(Integer.parseInt(Stock_min.getText()));
    setPrecio_unitario(Float.parseFloat(precio.getText()));

    Connection connection = null;
    DBConnection objetoConexion = new DBConnection();
    connection = objetoConexion.establecerConexion();

    // Llama al procedimiento almacenado utilizando la sintaxis correcta
    String update = "CALL actualizar_producto(?,?,?,?,?,?)";

    try (CallableStatement cs = connection.prepareCall(update)) {
        cs.setInt(1, getId());
        cs.setString(2, getNombre());
        cs.setString(3, getDescripcion());
        cs.setInt(4, getStock());
        cs.setInt(5, getStock_min());
        cs.setDouble(6, getPrecio_unitario());
        cs.execute();
        System.out.println("Producto actualizado correctamente.");
    } catch (Exception e) {
        System.out.println("Eror:"+e.getMessage());
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
    public void deleteProdProcedure(){
        Connection connection = null;
        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                          CREATE OR REPLACE PROCEDURE delete_producto(
                            IN p_nombre VARCHAR(30)
                            )
                            LANGUAGE plpgsql
                            AS $$
                            BEGIN
                                DELETE FROM producto
                                WHERE nombre = p_nombre;
                            END;
                            $$;
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
    };
    public void SeleccionarProducto(JTable TablaProductos,JTextField paramCodigo){
        try{
           int fila=TablaProductos.getSelectedRow();
           if(fila>=0){
               paramCodigo.setText(TablaProductos.getValueAt(fila,0).toString());
              
           }else{
               JOptionPane.showMessageDialog(null, "fila no seleccionada");
           }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    public void deleteProducto(JTextField nombre) {
    Connection connection = null;
    DBConnection objetoConexion = new DBConnection();
    setNombre(nombre.getText());
    try {
        String deleteQuery = "CALL delete_producto(?)";
        connection = objetoConexion.establecerConexion();

        try (CallableStatement cs = connection.prepareCall(deleteQuery)) {
            cs.setString(1, getNombre());
            int rowCount = cs.executeUpdate();

            if (rowCount > 0) {
                System.out.println("Producto con nombre " + getNombre() + " eliminado correctamente.");
            } else {
                System.out.println("Producto con nombre " + getNombre() + " no encontrado.");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al eliminar el producto: " + e.getMessage());
    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}

}
