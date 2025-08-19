import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    public static JFrame frame;
    public static Jugador jugador;
    public static PanelJuego panelJuego;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::mostrarMenuInicial);
    }
    

    public static void mostrarMenuInicial() {
        frame = new JFrame("Arkanoid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        PanelFondo menuPanel = new PanelFondo("res/menu_fondo.png");
        menuPanel.setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = screenSize.width;
        int frameHeight = screenSize.height;
        
       
        int botonWidth = (int) (frameWidth * 0.15); 
        int botonHeight = (int) (frameHeight * 0.05); 
       
        int xPos = (frameWidth - botonWidth) / 2;
        int yPosJugar = (int) (frameHeight * 0.6); 
        int yPosSalir = yPosJugar + botonHeight + 20;

        BotonImagen botonJugar = new BotonImagen("res/jugar.jpg", xPos, yPosJugar, botonWidth, botonHeight, e -> {
            frame.dispose();
            iniciarJuego();
        });
        
        BotonImagen botonSalir = new BotonImagen("res/salir.jpg", xPos, yPosSalir, botonWidth, botonHeight, e -> System.exit(0));

        menuPanel.agregarBoton(botonJugar);
        menuPanel.agregarBoton(botonSalir);

        frame.add(menuPanel);
        frame.setVisible(true);
    }

    public static void iniciarJuego() {
        frame = new JFrame("Arkanoid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setUndecorated(false);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        jugador = new Jugador();
        panelJuego = new PanelJuego(jugador);
        
        frame.add(panelJuego);
        frame.setVisible(true);
    }

    public static void mostrarGameOver() {
        frame.getContentPane().removeAll();
        
        PanelFondo gameOverPanel = new PanelFondo("res/gameover_fondo.png");
        gameOverPanel.setLayout(null);

     
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = screenSize.width;
        int frameHeight = screenSize.height;
        
     
        int botonWidth = (int) (frameWidth * 0.15);
        int botonHeight = (int) (frameHeight * 0.05);
        

        int xPos = (frameWidth - botonWidth) / 2;
        int yPosReiniciar = (int) (frameHeight * 0.6);
        int yPosSalir = yPosReiniciar + botonHeight + 20;

        BotonImagen botonReiniciar = new BotonImagen("res/reiniciar.jpg", xPos, yPosReiniciar, botonWidth, botonHeight, e -> {
            frame.dispose();
            iniciarJuego();
        });

        BotonImagen botonSalir = new BotonImagen("res/salir.jpg", xPos, yPosSalir, botonWidth, botonHeight, e -> System.exit(0));

        gameOverPanel.agregarBoton(botonReiniciar);
        gameOverPanel.agregarBoton(botonSalir);
        
        frame.setContentPane(gameOverPanel);
        frame.revalidate();
        frame.repaint();
    }
}