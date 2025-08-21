import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class PanelJuego extends JPanel implements ActionListener, KeyListener {
    // Bola
    private double bolaX, bolaY;
    private double bolaDX, bolaDY;
    private double bolaVelocidad; 

    // Paleta
    private double paletaX;
    private final double velocidadPaleta = 30;


    // Ladrillos
    private int[][] ladrillos;
    private int columnas = 10;
    private int filas = 5;

    // Jugador
    private Jugador jugador;

    // Timer
    private Timer timer;

    // Texturas
    private Image texturaFondo;
    private Image texturaBola;
    private Image texturaPaleta;
    private Image texturaLadrilloNormal;
    private Image texturaLadrilloDoble;
    private Image texturaLadrilloIndestructible;

    private boolean moverIzq = false;
    private boolean moverDer = false;


    private boolean inicioListo = false;

    public PanelJuego(Jugador j) {
        this.jugador = j;
        setFocusable(true);
        addKeyListener(this);
        cargarTexturas();

        timer = new Timer(16, this);

        timer.start();
    }
    
    private void cargarTexturas() {
        texturaFondo = new ImageIcon("res/fondo.jpg").getImage();
        texturaBola = new ImageIcon("res/bola.png").getImage();
        texturaPaleta = new ImageIcon("res/paleta.png").getImage();
        texturaLadrilloNormal = new ImageIcon("res/ladrillo_normal.png").getImage();
        texturaLadrilloDoble = new ImageIcon("res/ladrillo_doble.png").getImage();
        texturaLadrilloIndestructible = new ImageIcon("res/ladrillo_indestructible.png").getImage();
    }

    private void initJuego() {
        bolaVelocidad = 20;
        generarLadrillosPorNivel(jugador.getNivel());
        reiniciarBola(); 
    }
    
    public void reiniciarJuego() {
        jugador.setVidas(3);
        jugador.setPuntaje(0);
        jugador.setNivel(1);
        initJuego();
        timer.start();
    }

    private void generarLadrillosPorNivel(int nivel) {
        ladrillos = new int[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                switch (nivel) {
                    case 1 -> ladrillos[i][j] = 1;
                    case 2 -> ladrillos[i][j] = (i + j) % 2 == 0 ? 1 : 0;
                    case 3 -> ladrillos[i][j] = (j >= filas - i - 1 && j <= columnas - 1 - (filas - i - 1)) ? 2 : 0;
                    default -> {
                        double rand = Math.random();
                        if (rand < 0.6) ladrillos[i][j] = 1;
                        else if (rand < 0.8) ladrillos[i][j] = 2;
                        else ladrillos[i][j] = 3;
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!inicioListo) {
            initJuego(); 
            inicioListo = true;
        }

        Graphics2D g2d = (Graphics2D) g;
        int anchoPanel = getWidth();
        int altoPanel = getHeight();

        // Fondo
        g2d.drawImage(texturaFondo, 0, 0, anchoPanel, altoPanel, this);

        int bolaRadio = anchoPanel / 70;
        int paletaWidth = anchoPanel / 9;
        int paletaHeight = altoPanel / 40;

        int margenLateral = anchoPanel / 10;
        int margenSuperior = altoPanel / 5;
        int anchoDisponible = anchoPanel - 2 * margenLateral;
        int ladrilloWidth = anchoDisponible / columnas;
        int ladrilloHeight = altoPanel / 20;

        // Bola
        g2d.drawImage(texturaBola, (int) bolaX, (int) bolaY, bolaRadio * 2, bolaRadio * 2, this);

        // Paleta
        g2d.drawImage(texturaPaleta, (int) paletaX, altoPanel - paletaHeight * 4, paletaWidth, paletaHeight, this);

        // Ladrillos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int tipo = ladrillos[i][j];
                if (tipo != 0) {
                    Image texturaActual = switch (tipo) {
                        case 1 -> texturaLadrilloNormal;
                        case 2 -> texturaLadrilloDoble;
                        case 3 -> texturaLadrilloIndestructible;
                        default -> null;
                    };
                    if (texturaActual != null) {
                        int x = margenLateral + j * ladrilloWidth;
                        int y = margenSuperior + i * ladrilloHeight;
                        g2d.drawImage(texturaActual, x, y, ladrilloWidth - 5, ladrilloHeight - 5, this);
                    }
                }
            }
        }

        // Textos
        g2d.setColor(Color.WHITE);
        int fontSize = anchoPanel / 50;
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));

        FontMetrics fm = g2d.getFontMetrics();
        int espacioLineas = fontSize + 10;
        int textoY = altoPanel / 50 + espacioLineas;

        String vidasStr = "Vidas: " + jugador.getVidas();
        String puntajeStr = "Puntaje: " + jugador.getPuntaje();
        String nivelStr = "Nivel: " + jugador.getNivel();

        int vidasX = (anchoPanel - fm.stringWidth(vidasStr)) / 2;
        int puntajeX = (anchoPanel - fm.stringWidth(puntajeStr)) / 2;
        int nivelX = (anchoPanel - fm.stringWidth(nivelStr)) / 2;

        g2d.drawString(vidasStr, vidasX, textoY);
        g2d.drawString(puntajeStr, puntajeX, textoY + espacioLineas);
        g2d.drawString(nivelStr, nivelX, textoY + espacioLineas * 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	moverPaleta(); 
    	moverBola();    
    	chequearColisiones();
    	repaint();

    }

    private void moverBola() {
        double factor = 16.0 / 1000.0; 

        bolaX += bolaDX * factor * 60;
        bolaY += bolaDY * factor * 60;

        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int bolaRadio = anchoPanel / 70;

        if (bolaX < 0 || bolaX > anchoPanel - bolaRadio * 2) bolaDX = -bolaDX;
        if (bolaY < 0) bolaDY = -bolaDY;

        if (bolaY > altoPanel) {
            jugador.perderVida();
            if (jugador.getVidas() <= 0) {
                timer.stop();
                Main.mostrarGameOver();
            } else {
                reiniciarBola();
            }
        }
    }


    private void reiniciarBola() {
        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int paletaWidth = anchoPanel / 9;
        int paletaHeight = altoPanel / 40;
        int bolaRadio = anchoPanel / 70;

        paletaX = anchoPanel / 2.0 - paletaWidth / 2.0;

        bolaX = paletaX + paletaWidth / 2.0 - bolaRadio;
        bolaY = altoPanel - paletaHeight * 4 - bolaRadio * 2;

       
        double angulo;
        do {
            angulo = Math.toRadians((Math.random() * 120) - 60);
        } while (Math.abs(Math.sin(angulo)) < 0.2); 

        double velocidad = bolaVelocidad;
        bolaDX = velocidad * Math.sin(angulo);
        bolaDY = -Math.abs(velocidad * Math.cos(angulo)); 
    }



    private void chequearColisiones() {
        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int bolaRadio = anchoPanel / 70;
        int paletaWidth = anchoPanel / 9;
        int paletaHeight = altoPanel / 40;
        int filas = ladrillos.length;
        int margenLateral = anchoPanel / 10;
        int margenSuperior = altoPanel / 5;

        int anchoDisponible = anchoPanel - 2 * margenLateral;
        int ladrilloWidth = anchoDisponible / columnas;
        int ladrilloHeight = altoPanel / 20;

        Rectangle2D bolaRect = new Rectangle2D.Double(bolaX, bolaY, bolaRadio * 2, bolaRadio * 2);
        Rectangle2D paletaRect = new Rectangle2D.Double(paletaX, altoPanel - paletaHeight * 4, paletaWidth, paletaHeight);

        if (bolaRect.intersects(paletaRect)) {
            bolaY = paletaRect.getY() - bolaRect.getHeight();

            double paletaCentro = paletaX + paletaWidth / 2;
            double distancia = (bolaX + bolaRadio) - paletaCentro;
            double porcentaje = distancia / (paletaWidth / 2);
            double anguloMax = Math.toRadians(75);
            double velocidad = Math.sqrt(bolaDX * bolaDX + bolaDY * bolaDY);

            bolaDX = velocidad * Math.sin(anguloMax * porcentaje);
            bolaDY = -Math.abs(velocidad * Math.cos(anguloMax * porcentaje));
        }

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int tipo = ladrillos[i][j];
                if (tipo != 0) {
                    Rectangle ladrilloRect = new Rectangle(margenLateral + j * ladrilloWidth, margenSuperior + i * ladrilloHeight, ladrilloWidth - 5, ladrilloHeight - 5);
                    
                    if (bolaRect.intersects(ladrilloRect)) {
                        switch (tipo) {
                            case 1:
                                ladrillos[i][j] = 0;
                                jugador.sumarPuntaje(10);
                                break;
                            case 2:
                                ladrillos[i][j] = 1;
                                jugador.sumarPuntaje(10);
                                break;
                            case 3:
                                break;
                        }
                        
                        double prevBolaX = bolaX - bolaDX;
                        double prevBolaY = bolaY - bolaDY;

                        boolean colisionHorizontal = (prevBolaX + bolaRect.getWidth() <= ladrilloRect.x || prevBolaX >= ladrilloRect.x + ladrilloRect.width);
                        boolean colisionVertical = (prevBolaY + bolaRect.getHeight() <= ladrilloRect.y || prevBolaY >= ladrilloRect.y + ladrilloRect.height);

                        if (colisionVertical && colisionHorizontal) {
                            bolaDX = -bolaDX;
                            bolaDY = -bolaDY;
                        } else if (colisionVertical) {
                            bolaDY = -bolaDY;
                        } else if (colisionHorizontal) {
                            bolaDX = -bolaDX;
                        }

                        chequearNivelCompletado();
                        return;
                    }
                }
            }
        }
    }

    private void chequearNivelCompletado() {
        boolean todosDestruidos = true;
        for (int[] fila : ladrillos) {
            for (int b : fila) {
                if (b != 0 && b != 3) {
                    todosDestruidos = false;
                    break;
                }
            }
            if (!todosDestruidos) break;
        }

        if (todosDestruidos) {
            if (jugador.getNivel() < 10) {
                jugador.subirNivel();
                jugador.setVidas(3);
                
                bolaVelocidad += 0.8; 
                
                JOptionPane.showMessageDialog(this, "¡Felicidades, pasaste al Nivel " + jugador.getNivel() + "!", "Level Up!", JOptionPane.INFORMATION_MESSAGE);
                
                generarLadrillosPorNivel(jugador.getNivel());
                reiniciarBola();
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(this, "¡Ganaste el juego!\nPuntaje: " + jugador.getPuntaje());
                System.exit(0);
            }
        }
    }

    
   
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moverIzq = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moverDer = true;

        if (e.getKeyCode() == KeyEvent.VK_N) {
            jugador.subirNivel();
            jugador.setVidas(3); 
            bolaVelocidad += 0.8;
            generarLadrillosPorNivel(jugador.getNivel());
            reiniciarBola();
            JOptionPane.showMessageDialog(this, "¡Nivel cambiado! Ahora estás en Nivel " + jugador.getNivel());
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            jugador.setVidas(3);
            generarLadrillosPorNivel(jugador.getNivel());
            reiniciarBola();
           
        }
    }



    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moverIzq = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moverDer = false;
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    
    private void moverPaleta() {
        int anchoPanel = getWidth();
        int paletaWidth = anchoPanel / 9;

        if (moverIzq) {
            paletaX -= velocidadPaleta;
            if (paletaX < 0) paletaX = 0;
        }
        if (moverDer) {
            paletaX += velocidadPaleta;
            if (paletaX > anchoPanel - paletaWidth) paletaX = anchoPanel - paletaWidth;
        }
    }


}
