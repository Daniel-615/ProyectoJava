/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto.backend;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author suyan
 */
public class Empleados {
    String primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,nit;
    int usuario_id,codigo;
    float salario;
    
    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }
    
    public String getPrimer_nombre() {
        return primer_nombre;
    }

    public void setPrimer_nombre(String primer_nombre) {
        this.primer_nombre = primer_nombre;
    }

    public String getSegundo_nombre() {
        return segundo_nombre;
    }

    public void setSegundo_nombre(String segundo_nombre) {
        this.segundo_nombre = segundo_nombre;
    }

    public String getPrimer_apellido() {
        return primer_apellido;
    }

    public void setPrimer_apellido(String primer_apellido) {
        this.primer_apellido = primer_apellido;
    }

    public String getSegundo_apellido() {
        return segundo_apellido;
    }

    public void setSegundo_apellido(String segundo_apellido) {
        this.segundo_apellido = segundo_apellido;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

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
                                    CREATE TABLE IF NOT EXISTS empleado (
                                        id_empleado serial PRIMARY KEY,
                                        primer_nombre VARCHAR(100),
                                        segundo_nombre VARCHAR(100),
                                        primer_apellido VARCHAR(100),
                                        segundo_apellido VARCHAR(100),
                                        nit VARCHAR(10),
                                        salario double precision,
                                        status BOOLEAN,
                                        usuario_id INT UNIQUE,  
                                        FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
                                    );""";
            CallableStatement cs=objetoConexion.establecerConexion().prepareCall(createTableSQL);
            cs.execute();
            System.out.println("Tabla creada correctamente");
           } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
            }
    }
     public void createProcedureEmpleados(){
        Connection connection = null;
        try {
            DBConnection objetoConexion = new DBConnection();
            String insert = """
                            CREATE OR REPLACE PROCEDURE INSERT_EMPLEADO(
                                                \tIN e_primer_nombre varchar(100),
                                                \tIN e_segundo_nombre VARCHAR(100),
                                                \tIN e_primer_apellido varchar(100),
                                                \tIN e_segundo_apellido VARCHAR(100),           
                                                \tIN e_nit VARCHAR(10),
                                                \tIN e_salario double precision,
                                                \tIN e_usuario_id int
                                                )
                                                LANGUAGE plpgsql
                                                AS $$
                                                    BEGIN 
                                                  INSERT INTO empleado (primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, nit,salario,status,usuario_id)
                                                  VALUES (e_primer_nombre, e_segundo_nombre, e_primer_apellido, e_segundo_apellido, e_nit,e_salario,TRUE,e_usuario_id);
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
    public void insertEmpleado(JTextField primerNombre, JTextField segundoNombre,JTextField primerApellido,JTextField segundoApellido, JTextField nit,JTextField Salario,JTextField usuario){
       Connection connection = null;
       if(primerNombre.getText().isEmpty() || segundoNombre.getText().isEmpty () ||primerApellido.getText().isEmpty() || segundoApellido.getText().isEmpty() || Salario.getText().isEmpty() || usuario.getText().isEmpty()){
           JOptionPane.showMessageDialog(null,"Debes llenar todos los campos");
       }else{
            setPrimer_nombre(primerNombre.getText());
            setSegundo_nombre(segundoNombre.getText());
            setPrimer_apellido(primerApellido.getText());
            setSegundo_apellido(segundoApellido.getText());
            setNit(nit.getText());
            setSalario(Float.parseFloat(Salario.getText()));
            setUsuario_id(Integer.parseInt(usuario.getText()));
            DBConnection objetoConexion = new DBConnection();
            try{
                String insert="CALL INSERT_EMPLEADO(?,?,?,?,?,?,?)";
                connection = objetoConexion.establecerConexion();
                try(CallableStatement cs = connection.prepareCall(insert)){
                    cs.setString(1,getPrimer_nombre());
                    cs.setString(2, getSegundo_nombre());
                    cs.setString(3,getPrimer_apellido());
                    cs.setString(4,getSegundo_apellido());
                    cs.setString(5, getNit());
                    cs.setDouble(6,getSalario());
                    cs.setInt(7, getUsuario_id());
                    System.out.println("Empleado creado correctamente");
                }
            }catch(SQLException e){
            System.out.println("Error al guardar: "+e);
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
    };
    public void updateEmpleado(){
        
    };
    public void deleteEmpleado(){
        
    };
}
