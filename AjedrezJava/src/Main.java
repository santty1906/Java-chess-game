import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Presentación del Proyecto");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con los logos
        JPanel panelLogos = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        panelLogos.setBackground(Color.WHITE);

        // Sustituye los nombres por tus archivos reales de imagen
        ImageIcon iconoUTP = new ImageIcon("resources/logo_utp.png");
        Image imagenUTP = iconoUTP.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Cambia el tamaño aquí
        JLabel logoUTP = new JLabel(new ImageIcon(imagenUTP));
        ImageIcon iconoFacultad = new ImageIcon("resources/logo_facultad.png");
        Image imagenFacultad = iconoFacultad.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Ajusta según lo que necesites
        JLabel logoFacultad = new JLabel(new ImageIcon(imagenFacultad));
        panelLogos.add(logoUTP);
        panelLogos.add(logoFacultad);

        // Panel del contenido textual
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setBackground(Color.WHITE);

        // Texto estilizado
        String[] lineas = {
            "UNIVERSIDAD TECNOLÓGICA DE PANAMÁ",
            "Facultad de Ingeniería en Sistemas Computacionales",
            "Carrera: Ingeniería en Software",
            "Grupo: 1SF125",
            "Integrantes:",
            " - Gabriel Ortega – 8-1025-286",
            " - Antonio Gonzalez – 6-727-2002",
            " - Diego Perez - 8-1030-939",
            " - Santiago Lopez - 20-70-7965",
            "Profesor: Rodrigo Yangüez",
            "Fecha de entrega: 25 de julio de 2025"
        };

        for (String linea : lineas) {
            JLabel etiqueta = new JLabel(linea);
            etiqueta.setFont(new Font("Arial", Font.PLAIN, 18));
            etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelTexto.add(etiqueta);
            panelTexto.add(Box.createVerticalStrut(10)); // Espaciado
        }

        // Botón continuar
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContinuar.addActionListener(e -> {
            dispose(); // Cierra esta ventana
            new MenuJuego().setVisible(true); // Abre la siguiente
        });
        panelTexto.add(btnContinuar);

        // Añadir al panel principal
        panelPrincipal.add(panelLogos, BorderLayout.NORTH);
        panelPrincipal.add(panelTexto, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main Main = new Main();
            Main.setVisible(true);
        });
    }
}
