package generic;

public class Arista<T> {
    private Nodo<T> destino;
    private int peso;

    public Arista(Nodo<T> destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    public Nodo<T> getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "Arista{" +
                "destino=" + destino.getId() +
                ", peso=" + peso +
                '}';
    }
}
