package com.mycompany.proyecto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
    Connection connection = null; // Declarar la conexión fuera del try para poder cerrarla en el bloque finally

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
            JOptionPane.showMessageDialog(null, "User table has been created successfully");
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

    public void createProcedure(){
        Connection connection = null; // Declarar la conexión fuera del try para poder cerrarla en el bloque finally

        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                      CREATE OR REPLACE PROCEDURE INSERT_USER(
                        IN username VARCHAR(100),
                      	IN contrasena_usuario VARCHAR(100),
                        IN status_usuario BOOLEAN
                          )
                      LANGUAGE plpgsql
                      AS $$
                       	BEGIN 
                          INSERT INTO USUARIO (USUARIO,CONTRASENA,FECHA_INGRESO,FECHA_ULTIMA_MODIFICACION,STATUS)
                          VALUES (username,contrasena_usuario,NOW(),NOW(),1);
                          END;
                      $$;
                      """;

            connection = objetoConexion.establecerConexion(); 

        try (CallableStatement cs = connection.prepareCall(insert)) {
            cs.execute();
            JOptionPane.showMessageDialog(null, "Procedure has been created successfully");
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
    public void createUser(JTextField paramUsername,JTextField paramPassword){
        setUsername(paramUsername.getText());
        setPassword(paramPassword.getText());
        DBConnection objetoConexion= new DBConnection();
        try{
             String call="CALL INSERT_USER(?,?);";
             CallableStatement cs=objetoConexion.establecerConexion().prepareCall(call);
             cs.setString(1, getUsername());
             cs.setString(2,getPassword());
             cs.execute();
             JOptionPane.showMessageDialog(null, "has been inserted sucessfully ");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            
        }
    }
    public void findUser(JTextField paramUsername){
        setUsername(paramUsername.getText());
        DBConnection objetoConexion= new DBConnection();
        try{
             String find="SELECT usuario from usuario where id_usuario=? AND status=1;";
             CallableStatement cs=objetoConexion.establecerConexion().prepareCall(find);
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
        }
    }
    public void updatePassword(){
        DBConnection objetoConexion= new DBConnection(); 
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
             CallableStatement cs=objetoConexion.establecerConexion().prepareCall(insert);
             cs.execute();
             JOptionPane.showMessageDialog(null, "Procedure has been created successfully");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            
        }
    }
    public void resetPassword(String username,JTextField paramPassword,JTextField paramConfirmPassword){
        setPassword(paramPassword.getText());
        setConfirm_password(paramConfirmPassword.getText());
        setUsername(username);
        if(getPassword().equals(getConfirm_password())){
            DBConnection objetoConexion= new DBConnection();
            try{
                String call="CALL UPDATE_PASSWORD(?,?);";
                CallableStatement cs=objetoConexion.establecerConexion().prepareCall(call);
                cs.setString(1,getUsername());
                cs.setString(2,getPassword());
                cs.execute();
                JOptionPane.showMessageDialog(null, "Password has been changed successfully"); 
                Signin obj=new Signin();
                obj.setVisible(true);
            }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            ResetPassword obj=new ResetPassword();
            obj.setVisible(true);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Error: " + "Password do not match");
            ResetPassword obj=new ResetPassword();
            obj.setVisible(true);
        }
    }
    public void procedure_deleteUser(){
        DBConnection objetoConexion= new DBConnection(); 
        String insert="""
                      CREATE OR REPLACE PROCEDURE DELETE_USER(
                      	IN username VARCHAR(100)
                          )
                      LANGUAGE plpgsql
                      AS $$
                       	BEGIN 
                          UPDATE USUARIO
                            SET STATUS=0,
                                FECHA_ULTIMA_MODIFICACION=NOW()
                            WHERE USUARIO=username;
                          END;
                      $$;
                      """;
        try{
             CallableStatement cs=objetoConexion.establecerConexion().prepareCall(insert);
             cs.execute();
             JOptionPane.showMessageDialog(null, "Procedure has been created successfully");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            
        }
    }
    
    public void deleteUser(JTextField User){
        setUsername(User.getText());
        DBConnection objetoConexion= new DBConnection(); 
        String call="CALL DELETE_USER(?)";
        try{
             CallableStatement cs=objetoConexion.establecerConexion().prepareCall(call);
             cs.setString(1,getUsername());
             cs.execute();
             JOptionPane.showMessageDialog(null, "Procedure has been called successfully");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            
        }
    }    
}
