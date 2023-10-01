/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto;

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
        
        
        //Procedures
        Usuarios objP=new Usuarios();
        objP.createProcedure();
        objP.updatePassword();
        objP.procedure_deleteUser();
        //Forms
        Signin objetoVista=new Signin();
        objetoVista.setVisible(true);
    }
}
