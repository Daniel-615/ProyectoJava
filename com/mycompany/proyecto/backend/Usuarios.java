package com.mycompany.proyecto.backend;

import com.mycompany.proyecto.frontend.ForgetPassword;
import com.mycompany.proyecto.frontend.ResetPassword;
import com.mycompany.proyecto.frontend.Signin;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.mycompany.proyecto.frontend.EntradaForm;  
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
 
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author suyan
 */
public class Usuarios {
    int codigo;
    String username,password,confirm_password;
    
    public String getConfirm_password() {
        return confirm_password;
    }
    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void createTable() {
    Connection connection = null; 

    try {
        String createTableSQL = """
                                CREATE TABLE IF NOT EXISTS usuario (
                                    id_usuario serial PRIMARY KEY,
                                    usuario VARCHAR(100),
                                    contrasena VARCHAR(100),
                                    fecha_ingreso DATE,
                                    fecha_ultima_modificacion DATE,
                                    status BOOLEAN
                                );""";

        DBConnection objetoConexion = new DBConnection();
        connection = objetoConexion.establecerConexion(); 

        try (CallableStatement cs = connection.prepareCall(createTableSQL)) {
            cs.execute();
            System.out.println("Tabla creada correctamente");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
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

    public void createProcedureShow(){
        Connection connection = null;
        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                      CREATE OR REPLACE PROCEDURE SELECT_USER(
                          )
                      LANGUAGE plpgsql
                      AS $$
                       	BEGIN 
                            SELECT USUARIO FROM USUARIO;
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
            
    public void createProcedure(){
        Connection connection = null;
        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                      CREATE OR REPLACE PROCEDURE INSERT_USER(
                        IN username VARCHAR(100),
                      	IN contrasena_usuario VARCHAR(100)
                          )
                      LANGUAGE plpgsql
                      AS $$
                       	BEGIN 
                          INSERT INTO USUARIO (USUARIO,CONTRASENA,FECHA_INGRESO,FECHA_ULTIMA_MODIFICACION,STATUS)
                          VALUES (username,contrasena_usuario,NOW(),NOW(),TRUE);
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
    
    public void showUser(JTable TablaUsuarios){
       DBConnection objetoConexion = new DBConnection();
        DefaultTableModel modelo = new DefaultTableModel();
        String sql = "SELECT usuario from usuario where status=TRUE;";
        modelo.addColumn("usuario");
        TablaUsuarios.setModel(modelo);
    
        Statement stmt = null;
        Connection connection = null;

        try {
            connection = objetoConexion.establecerConexion();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        
            while (rs.next()) {
            setUsername( rs.getString("usuario"));
                        
            // Crea un nuevo array de objetos para representar una fila de datos
            Object[] datos = { getUsername()};
            modelo.addRow(datos);
        }
        TablaUsuarios.setModel(modelo);
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
    };
    
    public void createUser(JTextField paramUsername,JTextField paramPassword){
        Connection connection = null;
        setUsername(paramUsername.getText());
        setPassword(paramPassword.getText());
        DBConnection objetoConexion= new DBConnection();
        connection = objetoConexion.establecerConexion(); 
        try{
             String call="CALL INSERT_USER(?,?);";
             CallableStatement cs=connection.prepareCall(call);
             cs.setString(1, getUsername());
             cs.setString(2,getPassword());
             cs.execute();
             JOptionPane.showMessageDialog(null, "has been inserted sucessfully ");
             EntradaForm obj=new EntradaForm(getUsername());
             obj.setVisible(true);
             
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            
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
    public void createProcedureLogin(){
        Connection connection = null;
        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                            CREATE OR REPLACE FUNCTION ACCESO_USUARIO(
                                IN p_username VARCHAR(100),
                                IN p_contrasena VARCHAR(100)
                            )
                            RETURNS BOOLEAN
                            LANGUAGE plpgsql
                            AS $$
                            DECLARE
                                v_count INTEGER;
                            BEGIN
                                -- Verificar si el nombre de usuario y la contraseña coinciden en la base de datos
                                SELECT COUNT(*)
                                INTO v_count
                                FROM USUARIO
                                WHERE USUARIO = p_username
                                  AND CONTRASENA = p_contrasena;

                                -- Si se encontró una coincidencia, devolver verdadero (true), de lo contrario, devolver falso (false)
                                IF v_count > 0 THEN
                                    RETURN TRUE;
                                ELSE
                                    RETURN FALSE;
                                END IF;
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
   public void Login(JTextField Username, JTextField Password,Signin signin) {
    Connection connection = null;
    setUsername(Username.getText());
    setPassword(Password.getText());
    DBConnection objetoConexion = new DBConnection();
    connection = objetoConexion.establecerConexion();
    try {
        String query = "SELECT ACCESO_USUARIO(?, ?)";
        try (CallableStatement cs = connection.prepareCall(query)) {
            cs.setString(1, getUsername());
            cs.setString(2, getPassword());

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next() && rs.getBoolean(1)) {
                    EntradaForm entradaForm = new EntradaForm(getUsername());
                    entradaForm.setVisible(true);
                    signin.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Acceso denegado");
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage());
    }
}

    public void findUser(JTextField paramUsername){
        Connection connection = null;
        setUsername(paramUsername.getText());
        DBConnection objetoConexion= new DBConnection();
        connection = objetoConexion.establecerConexion();
        try{
             String find="SELECT usuario from usuario where usuario=? AND status=TRUE;";
             CallableStatement cs=connection.prepareCall(find);
             cs.setString(1, getUsername());
             ResultSet rs=cs.executeQuery();
             if(rs.next()){
                JOptionPane.showMessageDialog(null, "User found");
                String user=rs.getString("usuario");
                ResetPassword obj=new ResetPassword(user);
                obj.setVisible(true);
             }else{
                JOptionPane.showMessageDialog(null, "User not found");
                ForgetPassword obj=new ForgetPassword();
                obj.setVisible(true); 
             }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            ForgetPassword obj=new ForgetPassword();
            obj.setVisible(true); 
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
    public void updatePassword(){
        Connection connection = null;
        DBConnection objetoConexion= new DBConnection(); 
        connection = objetoConexion.establecerConexion();
        String insert="""
                      CREATE OR REPLACE PROCEDURE UPDATE_PASSWORD(
                      IN username VARCHAR(100),	
                      IN contrasena_usuario VARCHAR(100)
                          )
                      LANGUAGE plpgsql
                      AS $$
                       	BEGIN 
                          UPDATE USUARIO
                          SET CONTRASENA=CONTRASENA_USUARIO,FECHA_ULTIMA_MODIFICACION=NOW()
                          WHERE USUARIO=USERNAME;
                          END;
                      $$;
                      """;
        try{
             CallableStatement cs=connection.prepareCall(insert);
             cs.execute();
              System.out.println("Procedure has been created successfully");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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
    public void resetPassword(String username, JTextField paramPassword, JTextField paramConfirmPassword) {
        Connection connection = null;
        try {
            setPassword(paramPassword.getText());
            setConfirm_password(paramConfirmPassword.getText());
            setUsername(username);
        
        if (getPassword().equals(getConfirm_password())) {
            DBConnection objetoConexion = new DBConnection();
            connection = objetoConexion.establecerConexion();
            
            String call = "CALL UPDATE_PASSWORD(?, ?);";
            CallableStatement cs = connection.prepareCall(call);
            cs.setString(1, getUsername());
            cs.setString(2, getPassword());
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Password has been changed successfully");
            Signin obj = new Signin();
            obj.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Passwords do not match");
            ResetPassword obj = new ResetPassword();
            obj.setVisible(true);
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            ResetPassword obj = new ResetPassword();
            obj.setVisible(true);
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

    public void procedure_deleteUser(){
        Connection connection = null;
        DBConnection objetoConexion= new DBConnection(); 
        connection = objetoConexion.establecerConexion();
        String insert="""
                      CREATE OR REPLACE PROCEDURE DELETE_USER(
                      	IN username VARCHAR(100)
                          )
                      LANGUAGE plpgsql
                      AS $$
                       	BEGIN 
                          UPDATE USUARIO
                            SET STATUS=FALSE,
                                FECHA_ULTIMA_MODIFICACION=NOW()
                            WHERE USUARIO=username;
                          END;
                      $$;
                      """;
        try{
             CallableStatement cs=connection.prepareCall(insert);
             cs.execute();
             System.out.println("Procedure has been created successfully");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            
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
    
    public void deleteUser(JTextField User){
        Connection connection = null;
        setUsername(User.getText());
        DBConnection objetoConexion= new DBConnection(); 
        connection = objetoConexion.establecerConexion();
        String call="CALL DELETE_USER(?)";
        try{
             CallableStatement cs=connection.prepareCall(call);
             cs.setString(1,getUsername());
             cs.execute();
             JOptionPane.showMessageDialog(null, "Procedure has been called successfully");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            
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
