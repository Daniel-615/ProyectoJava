package com.mycompany.proyecto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WelcomeForm extends JFrame {

    public WelcomeForm() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Welcome Form");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setUndecorated(true); // Para eliminar la barra de título

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibuja una imagen de fondo
                ImageIcon image = new ImageIcon("src\\main\\java\\com\\mycompany\\proyecto\\icon\\background.jpg"); // Reemplaza con la ubicación de tu imagen
                g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };

        getContentPane().add(panel);

        // Crear un botón "Iniciar" en la parte inferior del formulario
        JButton btnStart = new JButton("Iniciar");
        btnStart.setFont(new Font("Arial", Font.BOLD, 18));
        btnStart.setForeground(Color.WHITE);
        btnStart.setBackground(new Color(25, 118, 211));
        btnStart.setBorderPainted(false);
        btnStart.setFocusPainted(false);
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Signin obj=new Signin();
                obj.setVisible(true);
                dispose(); // Cierra el formulario de bienvenida
            }
        });

        panel.setLayout(new BorderLayout());
        panel.add(btnStart, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomeForm().setVisible(true);
            }
        });
    }
}
