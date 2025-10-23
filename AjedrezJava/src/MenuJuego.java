import javax.swing.*;
import java.awt.*;

public class MenuJuego extends JFrame {

    public MenuJuego() {
        setTitle("Menú Principal - Ajedrez");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titulo = new JLabel("Menú Principal");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton btnIniciar = new JButton("Iniciar Juego 1 vs 1");
        btnIniciar.setFont(new Font("Arial", Font.PLAIN, 18));
        btnIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciar.addActionListener(e -> {
            dispose();
            new TableroAjedrez(false).setVisible(true); 
        });


        JButton btnJugarBot = new JButton("Jugar vs Bot");
        btnJugarBot.setFont(new Font("Arial", Font.PLAIN, 18));
        btnJugarBot.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugarBot.addActionListener(e -> {
            dispose();
            new TableroAjedrez(true).setVisible(true); 
        });


        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 18));
        btnSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalir.addActionListener(e -> System.exit(0));

  
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnIniciar);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnJugarBot);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnSalir);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuJuego().setVisible(true));
    }
}
