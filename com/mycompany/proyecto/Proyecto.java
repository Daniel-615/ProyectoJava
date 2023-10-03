/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto;

import com.mycompany.proyecto.frontend.WelcomeForm;
import com.mycompany.proyecto.backend.Usuarios;
import com.mycompany.proyecto.backend.Productos;
import com.mycompany.proyecto.backend.Empleados;
import com.mycompany.proyecto.backend.Clientes;

/**
 *
 * @author suyan
 */
public class Proyecto {

    public static void main(String[] args) {
        //Creacion de tablas
        Usuarios objU=new Usuarios();
        objU.createTable();
        Clientes objC=new Clientes();
        objC.createTable();
        Empleados objE=new Empleados();
        objE.createTable();
        Productos objp=new Productos();
        objp.createTable();
        
        //Procedures
        Usuarios objP=new Usuarios();
        objP.createProcedure();
        objP.updatePassword();
        objP.procedure_deleteUser();
        objP.createProcedureLogin();
        //Forms
        
        WelcomeForm obj=new WelcomeForm();
        obj.setVisible(true);  
    }
}
