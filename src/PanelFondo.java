import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PanelFondo extends JPanel {
    private Image fondo;
    private ArrayList<BotonImagen> botones;

    public PanelFondo(String rutaImagen) {
        this.fondo = new ImageIcon(rutaImagen).getImage();
        this.botones = new ArrayList<>();
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (BotonImagen boton : botones) {
                    if (boton.contiene(e.getX(), e.getY())) {
                        boton.ejecutarAccion();
                        break; 
                    }
                }
            }
        });
    }

    public void agregarBoton(BotonImagen boton) {
        this.botones.add(boton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
        for (BotonImagen boton : botones) {
            boton.dibujar(g);
        }
    }
    
    private void centrarBotones() {
        int anchoPanel = getWidth();
        int altoPanel = getHeight();
        int totalAltura = 0;
        for (BotonImagen b : botones) totalAltura += b.height + 20;
        int startY = (altoPanel - totalAltura + 20) / 2;

        for (BotonImagen b : botones) {
            int x = (anchoPanel - b.width) / 2;
            b.setPos(x, startY);
            startY += b.height + 20;
        }
    }
}