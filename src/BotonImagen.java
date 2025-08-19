import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class BotonImagen {
    private Image imagen;
    private int x, y;
	int width;
	int height;
    private ActionListener accion;

    public BotonImagen(String rutaImagen, int x, int y, int width, int height, ActionListener accion) {
        this.imagen = new ImageIcon(rutaImagen).getImage();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.accion = accion;
    }

    public void dibujar(Graphics g) {
        g.drawImage(imagen, x, y, width, height, null);
    }

    public boolean contiene(int clickX, int clickY) {
        return clickX >= x && clickX <= x + width && clickY >= y && clickY <= y + height;
    }

    public void ejecutarAccion() {
        if (accion != null) {
            accion.actionPerformed(null);
        }
    }

	public void setPos(int x2, int startY) {
		
		
	}
}