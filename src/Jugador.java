public class Jugador {
    private int vidas;
    private int puntaje;
    private int nivel;

    public Jugador() {
        vidas = 3;
        puntaje = 0;
        nivel = 1;
    }

    public int getVidas() { return vidas; }
    public int getPuntaje() { return puntaje; }
    public int getNivel() { return nivel; }

    public void perderVida() { vidas--; }
    public void sumarPuntaje(int puntos) { puntaje += puntos; }
    public void subirNivel() { nivel++; }
    public void setVidas(int v) { vidas = v; }
    public void setPuntaje(int p) { puntaje = p; }
    public void setNivel(int n) { nivel = n; }
}