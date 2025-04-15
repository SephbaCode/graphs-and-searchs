package weight_graph_v2;

import java.util.ArrayList;
import java.util.Map;

public class EuristicSearchs {

    private EuristicSearchs(){}

    public static void BusquedaGradiente(GrafoPonderado grafo, String idOrigen, String idDestino, String max_or_min) {
        NodoPonderado actual = (NodoPonderado) grafo.obtenerNodo(idOrigen);
        ArrayList<String> camino = new ArrayList<>();
        int costoRealTotal = 0;

        while (actual != null && !camino.contains(actual.getId())) {

            camino.add(actual.getId());
            System.out.println("Visitando nodo: " + actual.getId());

            // Verificar si llegó al destino (si es que se proporcionó uno)
            if (idDestino != null && !idDestino.isEmpty() && actual.getId().equals(idDestino)) {
                System.out.println("Destino alcanzado: " + actual.getId());
                System.out.println("El camino encontrado es: " + camino);
                System.out.println("Costo real total del camino: " + costoRealTotal);
                return;
            }

            // Buscar siguiente nodo según heurística + peso
            NodoPonderado siguiente = null;
            int valorSeleccionado = -1;
            int costoAristaSeleccionada = 0;

            for (Map.Entry<String, Integer> entry : actual.obtenerValoresConexiones().entrySet()) {
                String idVecino = entry.getKey();
                int arista = entry.getValue();
                int vecinoValorEuristico = grafo.getValorEuristico(idVecino) + arista;

                if (max_or_min.equals("max")) {
                    if (vecinoValorEuristico > valorSeleccionado) {
                        valorSeleccionado = vecinoValorEuristico;
                        siguiente = (NodoPonderado) grafo.obtenerNodo(idVecino);
                        costoAristaSeleccionada = arista;
                    }
                } else if (max_or_min.equals("min")) {
                    if (valorSeleccionado == -1 || vecinoValorEuristico < valorSeleccionado) {
                        valorSeleccionado = vecinoValorEuristico;
                        siguiente = (NodoPonderado) grafo.obtenerNodo(idVecino);
                        costoAristaSeleccionada = arista;
                    }
                }
            }

            // Si encontró siguiente, sumar su costo real (sin heurística)
            if (siguiente != null) {
                costoRealTotal += costoAristaSeleccionada;
            }

            actual = siguiente;
        }

        // Mostrar resultado final
        System.out.println("Camino recorrido: " + camino);
        System.out.println("Costo real total del camino: " + costoRealTotal);

        if (idDestino != null && !idDestino.isEmpty()) {
            System.out.println("No se encontró un camino hacia el destino: " + idDestino);
        } else {
            System.out.println("Recorrido finalizado sin destino específico.");
        }
    }

}
