package weight_graph_v2;

import java.util.*;
import java.util.stream.Collectors;

public class EuristicSearchs {

    private EuristicSearchs(){}

    public static void BusquedaGradiente(GrafoPonderado grafo, String idOrigen, String idDestino, String max_or_min) {
        long inicioTiempo = System.nanoTime(); // ⏱ Tiempo de inicio

        NodoPonderado actual = (NodoPonderado) grafo.obtenerNodo(idOrigen);
        ArrayList<String> camino = new ArrayList<>();
        int costoRealTotal = 0;
        int paso = 0;

        // 📊 Métricas
        int nodosAnalizados = 0;                      // 🧠
        int maximaMemoria = 0;                        // 📦

        System.out.printf("%-5s | %-15s | %s%n", "PASO", "NODO EXTRAÍDO", "COLA");
        System.out.printf("%-5d | %-15s | [%s]%n", paso, "", actual.getId());

        paso++;

        while (actual != null && !camino.contains(actual.getId())) {
            camino.add(actual.getId());
            nodosAnalizados++; // 🧠 contar nodo analizado

            // Preparar la cola de vecinos
            List<NodoPonderado> vecinos = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : actual.obtenerValoresConexiones().entrySet()) {
                NodoPonderado vecino = (NodoPonderado) grafo.obtenerNodo(entry.getKey());
                vecinos.add(vecino);
            }

            // Actualizar la métrica de memoria máxima
            maximaMemoria = Math.max(maximaMemoria, vecinos.size());

            // Ordenar según heurística
            vecinos.sort((a, b) -> {
                return max_or_min.equalsIgnoreCase("min")
                        ? Integer.compare(a.getValorEuristico(), b.getValorEuristico())
                        : Integer.compare(b.getValorEuristico(), a.getValorEuristico());
            });

            String colaTexto = vecinos.stream()
                    .map(n -> n.getId() + "(" + n.getValorEuristico() + ")")
                    .collect(Collectors.joining(", "));

            System.out.printf("%-5d | %-15s | [%s]%n", paso, actual.getId(), colaTexto);

            // Verificar si llegó al destino
            if (idDestino != null && !idDestino.isEmpty() && actual.getId().equals(idDestino)) {
                long tiempoTotal = System.nanoTime() - inicioTiempo; // ⏱ tiempo en nanosegundos
                imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);
                System.out.println("Destino alcanzado: " + actual.getId());
                System.out.println("El camino encontrado es: " + camino);
                System.out.println("Costo real total del camino: " + costoRealTotal);
                return;
            }

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

            if (siguiente != null) {
                costoRealTotal += costoAristaSeleccionada;
            }

            actual = siguiente;
            paso++;
        }

        long tiempoTotal = System.nanoTime() - inicioTiempo; // ⏱
        imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);

        System.out.println("Camino recorrido: " + camino);
        System.out.println("Costo real total del camino (suma de aristas): " + costoRealTotal);

        if (idDestino != null && !idDestino.isEmpty() && !camino.contains(idDestino)) {
            System.out.println("No se encontró un camino hacia el destino: " + idDestino);
        } else {
            System.out.println("Recorrido finalizado sin destino específico.");
        }
    }



    public static void BusquedaGradienteIgnoraCiclos(GrafoPonderado grafo, String idOrigen, String idDestino, String max_or_min) {
        NodoPonderado actual = (NodoPonderado) grafo.obtenerNodo(idOrigen);
        ArrayList<String> camino = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        int costoRealTotal = 0;
        int paso = 0;

        // Paso 0
        System.out.printf("%-5s | %-15s | %s%n", "PASO", "NODO EXTRAÍDO", "COLA");
        System.out.printf("%-5d | %-15s | [%s]%n", paso, "", actual.getId());

        paso++;

        while (actual != null) {

            // Marcar como visitado y añadir al camino
            visitados.add(actual.getId());
            camino.add(actual.getId());

            // Preparar la lista de vecinos no visitados
            List<NodoPonderado> vecinos = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : actual.obtenerValoresConexiones().entrySet()) {
                NodoPonderado vecino = (NodoPonderado) grafo.obtenerNodo(entry.getKey());
                if (!visitados.contains(vecino.getId())) {
                    vecinos.add(vecino);
                }
            }

            // Ordenar vecinos por heurística
            vecinos.sort((a, b) -> {
                return max_or_min.equalsIgnoreCase("min")
                        ? Integer.compare(a.getValorEuristico(), b.getValorEuristico())
                        : Integer.compare(b.getValorEuristico(), a.getValorEuristico());
            });

            // Mostrar la cola con heurística
            String colaTexto = vecinos.stream()
                    .map(n -> n.getId() + "(" + n.getValorEuristico() + ")")
                    .collect(Collectors.joining(", "));

            System.out.printf("%-5d | %-15s | [%s]%n", paso, actual.getId(), colaTexto);

            // Verificar si llegó al destino
            if (idDestino != null && !idDestino.isEmpty() && actual.getId().equals(idDestino)) {
                System.out.println("\nDestino alcanzado: " + actual.getId());
                System.out.println("El camino encontrado es: " + camino);
                System.out.println("Costo real total (suma de aristas): " + costoRealTotal);
                return;
            }

            // Seleccionar siguiente nodo ignorando los ya visitados
            NodoPonderado siguiente = null;
            int valorSeleccionado = -1;
            int costoAristaSeleccionada = 0;

            for (Map.Entry<String, Integer> entry : actual.obtenerValoresConexiones().entrySet()) {
                String idVecino = entry.getKey();
                int arista = entry.getValue();

                if (!visitados.contains(idVecino)) {
                    int vecinoValorEuristico = grafo.getValorEuristico(idVecino);

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
            }

            if (siguiente != null) {
                costoRealTotal += costoAristaSeleccionada;
            } else {
                // No hay vecinos no visitados, fin del recorrido
                break;
            }

            actual = siguiente;
            paso++;
        }

        // Resultado final
        System.out.println("\nCamino recorrido: " + camino);
        System.out.println("Costo real total (suma de aristas): " + costoRealTotal);

        if (idDestino != null && !idDestino.isEmpty() && !camino.contains(idDestino)) {
            System.out.println("No se encontró un camino hacia el destino: " + idDestino);
        } else {
            System.out.println("Recorrido finalizado sin destino específico.");
        }
    }

    public static void BusquedaPrimeroElMejor(GrafoPonderado grafo, String idOrigen, String idDestino, String max_or_min) {
        long inicioTiempo = System.nanoTime(); // ⏱ Tiempo de inicio

        PriorityQueue<NodoPonderado> frontera = new PriorityQueue<>((a, b) -> {
            return max_or_min.equalsIgnoreCase("min")
                    ? Integer.compare(a.getValorEuristico(), b.getValorEuristico())
                    : Integer.compare(b.getValorEuristico(), a.getValorEuristico());
        });

        Set<String> visitados = new HashSet<>();
        Map<String, String> padres = new HashMap<>();
        List<String> camino = new ArrayList<>();

        NodoPonderado actual = (NodoPonderado) grafo.obtenerNodo(idOrigen);
        frontera.add(actual);

        int paso = 0;

        // 📊 Métricas
        int nodosAnalizados = 0;             // 🧠
        int maximaMemoria = frontera.size(); // 📦

        System.out.printf("%-5s | %-15s | %s%n", "PASO", "NODO EXTRAÍDO", "COLA");

        while (!frontera.isEmpty()) {
            actual = frontera.poll();

            // Mostrar cola antes de expandir
            String colaTexto = frontera.stream()
                    .map(n -> n.getId() + "(" + n.getValorEuristico() + ")")
                    .collect(Collectors.joining(", "));

            System.out.printf("%-5d | %-15s | [%s]%n", paso, actual.getId(), colaTexto);
            paso++;

            if (visitados.contains(actual.getId())) continue;

            visitados.add(actual.getId());
            nodosAnalizados++; // 🧠 nodo analizado

            // Actualizar memoria máxima usada
            maximaMemoria = Math.max(maximaMemoria, frontera.size());

            if (actual.getId().equals(idDestino)) {
                // Reconstruir camino desde el mapa de padres
                String nodo = idDestino;
                while (nodo != null) {
                    camino.add(0, nodo);
                    nodo = padres.get(nodo);
                }

                long tiempoTotal = System.nanoTime() - inicioTiempo;
                imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);

                System.out.println("\nDestino alcanzado: " + idDestino);
                System.out.println("Camino encontrado: " + camino);
                return;
            }

            for (Map.Entry<String, Integer> entry : actual.obtenerValoresConexiones().entrySet()) {
                String idVecino = entry.getKey();
                if (!visitados.contains(idVecino)) {
                    NodoPonderado vecino = (NodoPonderado) grafo.obtenerNodo(idVecino);
                    frontera.add(vecino);
                    padres.put(idVecino, actual.getId());
                }
            }
        }

        long tiempoTotal = System.nanoTime() - inicioTiempo;
        imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);

        System.out.println("No se encontró un camino hacia el destino: " + idDestino);
    }


    public static void BusquedaAEstrella(GrafoPonderado grafo, String idOrigen, String idDestino) {

        long inicioTiempo = System.nanoTime(); // ⏱ Tiempo de inicio

        Set<String> visitados = new HashSet<>();
        Map<String, String> padres = new HashMap<>();
        Map<String, Integer> costos = new HashMap<>();

        PriorityQueue<NodoPonderado> frontera = new PriorityQueue<>((a, b) -> {
            int fA = costos.get(a.getId()) + a.getValorEuristico();
            int fB = costos.get(b.getId()) + b.getValorEuristico();
            return Integer.compare(fA, fB);
        });

        NodoPonderado origen = (NodoPonderado) grafo.obtenerNodo(idOrigen);
        frontera.add(origen);
        costos.put(idOrigen, 0);

        int paso = 0;

        // 📊 Métricas
        int nodosAnalizados = 0;             // 🧠
        int maximaMemoria = frontera.size(); // 📦

        System.out.printf("%-5s | %-15s | %s%n", "PASO", "NODO EXTRAÍDO", "COLA");

        while (!frontera.isEmpty()) {
            NodoPonderado actual = frontera.poll();

            // Mostrar cola ordenada visualmente
            String colaTexto = frontera.stream()
                    .sorted((a, b) -> {
                        int fA = costos.getOrDefault(a.getId(), Integer.MAX_VALUE) + a.getValorEuristico();
                        int fB = costos.getOrDefault(b.getId(), Integer.MAX_VALUE) + b.getValorEuristico();
                        return Integer.compare(fA, fB);
                    })
                    .map(n -> {
                        int f = costos.getOrDefault(n.getId(), Integer.MAX_VALUE) + n.getValorEuristico();
                        return n.getId() + "(f=" + f + ")";
                    })
                    .collect(Collectors.joining(", "));

            System.out.printf("%-5d | %-15s | [%s]%n", paso++, actual.getId(), colaTexto);

            if (visitados.contains(actual.getId())) continue;
            visitados.add(actual.getId());
            nodosAnalizados++; // 🧠 nodo expandido

            // 📦 Actualizar memoria máxima usada
            maximaMemoria = Math.max(maximaMemoria, frontera.size());

            if (actual.getId().equals(idDestino)) {
                // Reconstruir camino
                List<String> camino = new ArrayList<>();
                String nodo = idDestino;
                while (nodo != null) {
                    camino.add(0, nodo);
                    nodo = padres.get(nodo);
                }

                long tiempoTotal = System.nanoTime() - inicioTiempo;
                imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);

                System.out.println("\nDestino alcanzado: " + idDestino);
                System.out.println("Camino encontrado: " + camino);
                System.out.println("Costo real total (g(n)): " + costos.get(idDestino));
                return;
            }

            // Expandir vecinos
            for (Map.Entry<String, Integer> entry : actual.obtenerValoresConexiones().entrySet()) {
                String idVecino = entry.getKey();
                int costoArista = entry.getValue();

                int nuevoCosto = costos.get(actual.getId()) + costoArista;

                if (!costos.containsKey(idVecino) || nuevoCosto < costos.get(idVecino)) {
                    costos.put(idVecino, nuevoCosto);
                    padres.put(idVecino, actual.getId());
                    NodoPonderado vecino = (NodoPonderado) grafo.obtenerNodo(idVecino);
                    frontera.add(vecino);
                }
            }
        }

        long tiempoTotal = System.nanoTime() - inicioTiempo;
        imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);

        System.out.println("No se encontró un camino hacia el destino: " + idDestino);
    }



    public static List<String> busquedaCostoUniforme(GrafoPonderado grafo, String inicioId, String objetivoId) {
        class NodoCosto implements Comparable<NodoCosto> {
            String id;
            int costo;
            List<String> camino;

            NodoCosto(String id, int costo, List<String> camino) {
                this.id = id;
                this.costo = costo;
                this.camino = new ArrayList<>(camino);
                this.camino.add(id);
            }

            @Override
            public int compareTo(NodoCosto otro) {
                return Integer.compare(this.costo, otro.costo);
            }
        }

        long inicioTiempo = System.nanoTime(); // ⏱ Tiempo de inicio

        PriorityQueue<NodoCosto> cola = new PriorityQueue<>();
        Set<String> visitados = new HashSet<>();

        int nodosAnalizados = 0;       // 🧠
        int maximaMemoria = 0;         // 📦

        cola.add(new NodoCosto(inicioId, 0, new ArrayList<>()));
        maximaMemoria = Math.max(maximaMemoria, cola.size());

        System.out.printf("%-15s %-15s %-50s\n", "Nodo actual", "Costo acumulado", "Camino detallado");

        while (!cola.isEmpty()) {
            NodoCosto actual = cola.poll();

            if (visitados.contains(actual.id)) {
                continue;
            }

            visitados.add(actual.id);
            nodosAnalizados++; // 🧠 nodo expandido

            // Construir camino detallado con los costos acumulados
            StringBuilder caminoDetallado = new StringBuilder();
            int costoParcial = 0;
            for (int i = 0; i < actual.camino.size(); i++) {
                String nodoId = actual.camino.get(i);
                if (i > 0) {
                    String anterior = actual.camino.get(i - 1);
                    int peso = grafo.obtenerPesoConexion(anterior, nodoId);
                    costoParcial += peso;
                }
                caminoDetallado.append(nodoId).append("(").append(costoParcial).append(")");
                if (i < actual.camino.size() - 1) caminoDetallado.append(" → ");
            }

            System.out.printf("%-15s %-15d %-50s\n", actual.id, actual.costo, caminoDetallado);

            if (actual.id.equals(objetivoId)) {
                long tiempoTotal = System.nanoTime() - inicioTiempo;
                imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);

                System.out.println("\n¡Objetivo alcanzado!");
                return actual.camino;
            }

            NodoPonderado nodoActual = (NodoPonderado) grafo.obtenerNodo(actual.id);

            for (Map.Entry<String, Integer> vecino : nodoActual.obtenerValoresConexiones().entrySet()) {
                String vecinoId = vecino.getKey();
                int peso = vecino.getValue();

                if (!visitados.contains(vecinoId)) {
                    cola.add(new NodoCosto(vecinoId, actual.costo + peso, actual.camino));
                }
            }

            maximaMemoria = Math.max(maximaMemoria, cola.size()); // 📦 Actualizar si es necesario
        }

        long tiempoTotal = System.nanoTime() - inicioTiempo;
        imprimirMetricas(tiempoTotal, nodosAnalizados, maximaMemoria);

        System.out.println("No se encontró un camino al nodo destino.");
        return Collections.emptyList();
    }

    // Método auxiliar para mostrar métricas
    private static void imprimirMetricas(long tiempoNano, int nodosAnalizados, int maxMemoria) {
        double tiempoMs = tiempoNano / 1_000_000.0; // ⏱ convertir a milisegundos
        System.out.println("\n📊 Métricas:");
        System.out.printf("   ⏱ Tiempo total: %.4f ms%n", tiempoMs);
        System.out.printf("   🧠 Nodos analizados (temporal): %d%n", nodosAnalizados);
        System.out.printf("   📦 Máxima memoria usada (espacial): %d nodos simultáneos%n", maxMemoria);
    }









}
