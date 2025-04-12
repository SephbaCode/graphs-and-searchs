package generic;

import weigth_graph.EstadoCoste;
import weigth_graph.GrafoPonderado;
import weigth_graph.NodoPonderado;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Analizador {

    public static <T> void busquedaAmplitud(Grafo<T> grafo, T estadoInicial, T estadoObjetivo) {
        Queue<Nodo<T>> cola = new LinkedList<>();
        Set<T> visitados = new HashSet<>();

        Nodo<T> nodoInicial = grafo.obtenerNodo(estadoInicial);

        if (nodoInicial == null) {
            System.out.println("❌ El nodo inicial no existe en el grafo.");
            return;
        }

        cola.add(nodoInicial);
        visitados.add(nodoInicial.getId());

        int paso = 0;
        System.out.println("🔍 Búsqueda por amplitud" + (estadoObjetivo != null ? " hasta encontrar " + estadoObjetivo : ""));
        System.out.printf("%-6s | %-20s | %-60s\n", "Paso", "Nodo extraído", "Cola actual");
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6d | %-20s | %-60s\n", paso++, "Ninguno", colaToString(cola));

        while (!cola.isEmpty()) {
            Nodo<T> actual = cola.poll();

            if (estadoObjetivo != null && actual.getId().equals(estadoObjetivo)) {
                System.out.printf("%-6d | %-20s | %-60s\n", paso, actual.getId(), colaToString(cola));
                System.out.println("✅ Nodo objetivo encontrado: " + actual.getId());
                return;
            }

            for (Nodo<T> vecino : actual.getConexionesSalientes().values()) {
                if (!visitados.contains(vecino.getId())) {
                    cola.add(vecino);
                    visitados.add(vecino.getId());
                }
            }

            System.out.printf("%-6d | %-20s | %-60s\n", paso++, actual.getId(), colaToString(cola));
        }

        if (estadoObjetivo != null) {
            System.out.println("❌ Nodo objetivo no encontrado en el grafo.");
        }
    }

    private static <T> String colaToString(Queue<Nodo<T>> cola) {
        StringBuilder sb = new StringBuilder();
        for (Nodo<T> nodo : cola) {
            sb.append(nodo.getId()).append(" ");
        }
        return sb.toString().trim();
    }



    // Ya estaba definida: colaToString(...)
    private static <T> String pilaToString(Deque<Nodo<T>> pila) {
        StringBuilder sb = new StringBuilder();
        for (Nodo<T> nodo : pila) {
            sb.append(nodo.getId()).append(" ");
        }
        return sb.toString().trim();
    }

    public static <T> void busquedaProfundidad(Grafo<T> grafo, T estadoInicial, T estadoObjetivo) {
        Deque<Nodo<T>> pila = new ArrayDeque<>();
        Set<T> visitados = new HashSet<>();

        Nodo<T> nodoInicial = grafo.obtenerNodo(estadoInicial);

        if (nodoInicial == null) {
            System.out.println("❌ El nodo inicial no existe en el grafo.");
            return;
        }

        pila.push(nodoInicial);
        visitados.add(nodoInicial.getId());

        int paso = 0;
        System.out.println("🔍 Búsqueda por profundidad" + (estadoObjetivo != null ? " hasta encontrar " + estadoObjetivo : ""));
        System.out.printf("%-6s | %-20s | %-60s\n", "Paso", "Nodo extraído", "Pila actual");
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6d | %-20s | %-60s\n", paso++, "Ninguno", pilaToString(pila));

        while (!pila.isEmpty()) {
            Nodo<T> actual = pila.pop();

            if (estadoObjetivo != null && actual.getId().equals(estadoObjetivo)) {
                System.out.printf("%-6d | %-20s | %-60s\n", paso, actual.getId(), pilaToString(pila));
                System.out.println("✅ Nodo objetivo encontrado: " + actual.getId());
                return;
            }

            // Agregamos los vecinos en orden inverso para mantener orden lógico
            List<Nodo<T>> vecinos = new ArrayList<>(actual.getConexionesSalientes().values());
            Collections.reverse(vecinos);

            for (Nodo<T> vecino : vecinos) {
                if (!visitados.contains(vecino.getId())) {
                    pila.push(vecino);
                    visitados.add(vecino.getId());
                }
            }

            System.out.printf("%-6d | %-20s | %-60s\n", paso++, actual.getId(), pilaToString(pila));
        }

        if (estadoObjetivo != null) {
            System.out.println("❌ Nodo objetivo no encontrado en el grafo.");
        }
    }

    public static <T> void busquedaBidireccional(Grafo<T> grafo, T estadoInicial, T estadoObjetivo) {
        Set<T> visitadosDesdeInicio = ConcurrentHashMap.newKeySet();
        Set<T> visitadosDesdeObjetivo = ConcurrentHashMap.newKeySet();
        AtomicBoolean encontrado = new AtomicBoolean(false);
        Object lock = new Object();

        Nodo<T> nodoInicial = grafo.obtenerNodo(estadoInicial);
        Nodo<T> nodoFinal = grafo.obtenerNodo(estadoObjetivo);

        if (nodoInicial == null || nodoFinal == null) {
            System.out.println("❌ Nodo inicial o final no existe en el grafo.");
            return;
        }

        Runnable busqueda = (Runnable & Serializable) () -> {
            Queue<Nodo<T>> cola = new LinkedList<>();
            boolean esInicio = Thread.currentThread().getName().equals("Inicio");
            Set<T> visitadosLocales = esInicio ? visitadosDesdeInicio : visitadosDesdeObjetivo;
            Set<T> visitadosContrarios = esInicio ? visitadosDesdeObjetivo : visitadosDesdeInicio;
            Nodo<T> origen = esInicio ? nodoInicial : nodoFinal;

            cola.add(origen);
            visitadosLocales.add(origen.getId());

            while (!cola.isEmpty() && !encontrado.get()) {
                Nodo<T> actual = cola.poll();

                if (visitadosContrarios.contains(actual.getId())) {
                    synchronized (lock) {
                        if (!encontrado.get()) {
                            encontrado.set(true);
                            System.out.println("✅ Nodo encontrado por ambas búsquedas: " + actual.getId());
                        }
                    }
                    return;
                }

                for (Nodo<T> vecino : actual.getConexionesSalientes().values()) {
                    if (visitadosLocales.add(vecino.getId())) {
                        cola.add(vecino);
                    }
                }

                System.out.println("🔎 [" + Thread.currentThread().getName() + "] Visitando: " + actual.getId());
            }
        };

        Thread desdeInicio = new Thread(busqueda, "Inicio");
        Thread desdeObjetivo = new Thread(busqueda, "Objetivo");

        desdeInicio.start();
        desdeObjetivo.start();

        try {
            desdeInicio.join();
            desdeObjetivo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!encontrado.get()) {
            System.out.println("❌ No se encontró conexión entre los dos nodos.");
        }
    }

    public static <T> void busquedaPorNivelesDeProfundidad(Grafo<T> grafo, T estadoInicial, T estadoObjetivo, int profundidadMaxima) {
        Nodo<T> nodoInicial = grafo.obtenerNodo(estadoInicial);
        Nodo<T> nodoObjetivo = grafo.obtenerNodo(estadoObjetivo);

        if (nodoInicial == null || nodoObjetivo == null) {
            System.out.println("❌ Nodo inicial o final no existe en el grafo.");
            return;
        }

        System.out.println("🔎 Búsqueda por niveles de profundidad desde " + estadoInicial + " hasta " + estadoObjetivo);


        Set<T> visitados = new HashSet<>();
        System.out.println("👉 Nivel de profundidad límite: " + profundidadMaxima);

        if (dfsLimitado(nodoInicial, estadoObjetivo, profundidadMaxima, visitados)) {
            System.out.println("✅ Nodo objetivo encontrado dentro del límite de profundidad " + profundidadMaxima);
            return;
        }


        System.out.println("❌ Nodo objetivo no encontrado hasta una profundidad de " + profundidadMaxima);
    }

    private static <T> boolean dfsLimitado(Nodo<T> actual, T objetivo, int limite, Set<T> visitados) {
        System.out.println("🧭 Visitando: " + actual.getId() + " | Profundidad restante: " + limite);
        visitados.add(actual.getId());

        if (actual.getId().equals(objetivo)) {
            return true;
        }

        if (limite <= 0) return false;

        for (Nodo<T> vecino : actual.getConexionesSalientes().values()) {
            if (!visitados.contains(vecino.getId())) {
                if (dfsLimitado(vecino, objetivo, limite - 1, visitados)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static <T> void busquedaPorCosteUniforme(GrafoPonderado<T> grafo, T estadoInicial, T estadoFinal) {
        // Verificar si los nodos de inicio y fin existen en el grafo
        NodoPonderado<T> nodoInicial = (NodoPonderado<T>) grafo.obtenerNodo(estadoInicial);
        NodoPonderado<T> nodoFinal = (NodoPonderado<T>) grafo.obtenerNodo(estadoFinal);

        if (nodoInicial == null || nodoFinal == null) {
            System.out.println("Uno de los nodos (inicial o final) no existe en el grafo.");
            return;
        }

        // Cola de prioridad (min-heap) para explorar los nodos por coste acumulado
        PriorityQueue<EstadoCoste<T>> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(EstadoCoste::getCoste));
        Set<T> visitados = new HashSet<>();

        // Añadir el nodo inicial con coste 0
        colaPrioridad.add(new EstadoCoste<>(nodoInicial, 0));

        // Mapa para almacenar el camino recorrido (de donde venimos) y los costes
        Map<T, T> caminos = new HashMap<>();
        Map<T, Integer> costes = new HashMap<>();
        costes.put(nodoInicial.getId(), 0);

        while (!colaPrioridad.isEmpty()) {
            EstadoCoste<T> estadoActual = colaPrioridad.poll();
            T nodoActualId = estadoActual.getNodo().getId();

            // Si ya visitamos este nodo, lo ignoramos
            if (visitados.contains(nodoActualId)) {
                continue;
            }

            // Marcar el nodo como visitado
            visitados.add(nodoActualId);

            // Si hemos llegado al nodo final, reconstruimos el camino
            if (nodoActualId.equals(estadoFinal)) {
                System.out.println("¡Nodo objetivo encontrado!");
                reconstruirCamino(caminos, estadoInicial, estadoFinal);
                // Imprimir el coste total
                System.out.println("Coste total del camino: " + costes.get(estadoFinal));
                return;
            }

            // Explorar los nodos adyacentes
            for (Map.Entry<T, Nodo<T>> entrada : estadoActual.getNodo().getConexionesSalientes().entrySet()) {
                T vecinoId = entrada.getKey();  // La clave es el ID del nodo vecino
                Nodo<T> vecino = entrada.getValue();  // El valor es el nodo vecino (de tipo Nodo<T>)

                // Verificar si el vecino no ha sido visitado
                if (!visitados.contains(vecinoId)) {
                    // Obtener el coste de la conexión
                    int costeConexion = estadoActual.getNodo().obtenerPesoConexionSaliente(vecinoId);

                    // Calcular el nuevo coste sumando el coste de la conexión
                    int nuevoCoste = estadoActual.getCoste() + costeConexion;

                    // Solo actualizar si encontramos un coste menor
                    if (!costes.containsKey(vecinoId) || nuevoCoste < costes.get(vecinoId)) {
                        // Agregar a la cola de prioridad el vecino con su coste actualizado
                        colaPrioridad.add(new EstadoCoste<>((NodoPonderado) vecino, nuevoCoste));

                        // Guardar de dónde venimos (esto es para reconstruir el camino más tarde)
                        caminos.put(vecinoId, estadoActual.getNodo().getId());

                        // Actualizar el coste del vecino
                        costes.put(vecinoId, nuevoCoste);
                    }
                }
            }
        }

        System.out.println("❌ Nodo objetivo no encontrado.");
    }

    // Metodo para reconstruir el camino desde el nodo inicial al final
    private static <T> void reconstruirCamino(Map<T, T> caminos, T estadoInicial, T estadoFinal) {
        List<T> camino = new LinkedList<>();
        T nodoActual = estadoFinal;
        while (nodoActual != null && !nodoActual.equals(estadoInicial)) {
            camino.add(nodoActual);
            nodoActual = caminos.get(nodoActual);
        }
        camino.add(estadoInicial);

        // Imprimir el camino de ida y vuelta
        Collections.reverse(camino);
        System.out.println("Camino desde " + estadoInicial + " hasta " + estadoFinal + ": " + camino);
    }







}





