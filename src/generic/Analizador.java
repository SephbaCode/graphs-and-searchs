package generic;



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
        int nodosAnalizados = 0;
        int maxNodosSimultaneos = 0;
        long tiempoInicio = System.nanoTime();

        System.out.println("🔍 Búsqueda por amplitud" + (estadoObjetivo != null ? " hasta encontrar " + estadoObjetivo : ""));
        System.out.printf("%-6s | %-20s | %-60s\n", "Paso", "Nodo extraído", "Cola actual");
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6d | %-20s | %-60s\n", paso++, "Ninguno", colaToString(cola));

        while (!cola.isEmpty()) {
            Nodo<T> actual = cola.poll();
            nodosAnalizados++;

            if (estadoObjetivo != null && actual.getId().equals(estadoObjetivo)) {
                System.out.printf("%-6d | %-20s | %-60s\n", paso, actual.getId(), colaToString(cola));
                long duracion = System.nanoTime() - tiempoInicio;
                System.out.println("✅ Nodo objetivo encontrado: " + actual.getId());
                imprimirMetricas(duracion, nodosAnalizados, maxNodosSimultaneos);
                return;
            }

            for (Nodo<T> vecino : actual.getConexionesSalientes().values()) {
                if (!visitados.contains(vecino.getId())) {
                    cola.add(vecino);
                    visitados.add(vecino.getId());
                }
            }

            maxNodosSimultaneos = Math.max(maxNodosSimultaneos, cola.size() + 1); // +1 por el nodo actual
            System.out.printf("%-6d | %-20s | %-60s\n", paso++, actual.getId(), colaToString(cola));
        }

        long duracion = System.nanoTime() - tiempoInicio;
        System.out.println("❌ Nodo objetivo no encontrado en el grafo.");
        imprimirMetricas(duracion, nodosAnalizados, maxNodosSimultaneos);
    }

    private static void imprimirMetricas(long duracionNano, int nodosAnalizados, int maxNodosSimultaneos) {
        System.out.println("📊 Métricas:");
        System.out.println("   ⏱ Tiempo total: " + (duracionNano / 1_000_000.0) + " ms");
        System.out.println("   🧠 Nodos analizados (temporal): " + nodosAnalizados);
        System.out.println("   📦 Máxima memoria usada (espacial): " + maxNodosSimultaneos + " nodos simultáneos");
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
        int nodosAnalizados = 0;
        int maxNodosSimultaneos = 0;
        long tiempoInicio = System.nanoTime();

        System.out.println("🔍 Búsqueda por profundidad" + (estadoObjetivo != null ? " hasta encontrar " + estadoObjetivo : ""));
        System.out.printf("%-6s | %-20s | %-60s\n", "Paso", "Nodo extraído", "Pila actual");
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6d | %-20s | %-60s\n", paso++, "Ninguno", pilaToString(pila));

        while (!pila.isEmpty()) {
            Nodo<T> actual = pila.pop();
            nodosAnalizados++;

            if (estadoObjetivo != null && actual.getId().equals(estadoObjetivo)) {
                System.out.printf("%-6d | %-20s | %-60s\n", paso, actual.getId(), pilaToString(pila));
                long duracion = System.nanoTime() - tiempoInicio;
                System.out.println("✅ Nodo objetivo encontrado: " + actual.getId());
                imprimirMetricas(duracion, nodosAnalizados, maxNodosSimultaneos);
                return;
            }

            // Inversión para mantener orden lógico
            List<Nodo<T>> vecinos = new ArrayList<>(actual.getConexionesSalientes().values());
            Collections.reverse(vecinos);

            for (Nodo<T> vecino : vecinos) {
                if (!visitados.contains(vecino.getId())) {
                    pila.push(vecino);
                    visitados.add(vecino.getId());
                }
            }

            maxNodosSimultaneos = Math.max(maxNodosSimultaneos, pila.size() + 1); // +1 por el nodo actual
            System.out.printf("%-6d | %-20s | %-60s\n", paso++, actual.getId(), pilaToString(pila));
        }

        long duracion = System.nanoTime() - tiempoInicio;
        System.out.println("❌ Nodo objetivo no encontrado en el grafo.");
        imprimirMetricas(duracion, nodosAnalizados, maxNodosSimultaneos);
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

    private static <T> String pilaToStringNivel(Deque<NodoNivel<T>> pila) {
        StringBuilder sb = new StringBuilder();
        for (NodoNivel<T> nodo : pila) {
            sb.append(nodo.nodo.getId()).append(" ");
        }
        return sb.toString().trim();
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

        class EstadoHilo {
            Queue<Nodo<T>> cola = new LinkedList<>();
            Nodo<T> nodoActual = null;
            String nombre;
            Set<T> visitados;
            int nodosAnalizados = 0;
            int maxNodosSimultaneos = 0;

            EstadoHilo(String nombre, Nodo<T> origen, Set<T> visitedSet) {
                this.nombre = nombre;
                this.visitados = visitedSet;
                cola.add(origen);
                visitados.add(origen.getId());
            }

            boolean paso(Set<T> visitadosContrario) {
                if (cola.isEmpty()) return false;
                nodoActual = cola.poll();
                nodosAnalizados++;

                if (visitadosContrario.contains(nodoActual.getId())) {
                    synchronized (lock) {
                        if (!encontrado.get()) {
                            encontrado.set(true);
                            System.out.println("\n✅ Nodo encontrado por ambas búsquedas: " + nodoActual.getId());
                        }
                    }
                    return false;
                }

                for (Nodo<T> vecino : nodoActual.getConexionesSalientes().values()) {
                    if (visitados.add(vecino.getId())) {
                        cola.add(vecino);
                    }
                }

                maxNodosSimultaneos = Math.max(maxNodosSimultaneos, cola.size() + 1);
                return true;
            }

            String getColaComoTexto() {
                return cola.stream().map(Nodo::getId).map(Object::toString).reduce("", (a, b) -> a + " " + b).trim();
            }
        }

        EstadoHilo inicio = new EstadoHilo("Inicio", nodoInicial, visitadosDesdeInicio);
        EstadoHilo objetivo = new EstadoHilo("Objetivo", nodoFinal, visitadosDesdeObjetivo);

        long tiempoInicio = System.nanoTime();
        int paso = 0;

        System.out.printf("\n%-6s | %-22s | %-25s | %-22s | %-25s\n", "Paso", "Nodo extraído (Inicio)", "Cola (Inicio)", "Nodo extraído (Objetivo)", "Cola (Objetivo)");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");

        while (!encontrado.get() && (!inicio.cola.isEmpty() || !objetivo.cola.isEmpty())) {
            boolean pasoInicio = inicio.paso(visitadosDesdeObjetivo);
            boolean pasoObjetivo = objetivo.paso(visitadosDesdeInicio);

            System.out.printf("%-6d | %-22s | %-25s | %-22s | %-25s\n",
                    paso++,
                    pasoInicio ? inicio.nodoActual.getId().toString() : "-",
                    pasoInicio ? inicio.getColaComoTexto() : "-",
                    pasoObjetivo ? objetivo.nodoActual.getId().toString() : "-",
                    pasoObjetivo ? objetivo.getColaComoTexto() : "-"
            );
        }

        long duracion = System.nanoTime() - tiempoInicio;

        if (!encontrado.get()) {
            System.out.println("❌ No se encontró conexión entre los dos nodos.");
        }

        // Imprimir métricas finales
        System.out.println("\n📊 Métricas de búsqueda bidireccional:");
        System.out.printf("⏱ Tiempo total: %.2f ms\n", duracion / 1_000_000.0);
        System.out.println("🧠 Nodos analizados desde Inicio: " + inicio.nodosAnalizados);
        System.out.println("🧠 Nodos analizados desde Objetivo: " + objetivo.nodosAnalizados);
        System.out.println("📦 Máx nodos simultáneos desde Inicio: " + inicio.maxNodosSimultaneos);
        System.out.println("📦 Máx nodos simultáneos desde Objetivo: " + objetivo.maxNodosSimultaneos);
        System.out.println("📊 Total nodos analizados: " + (inicio.nodosAnalizados + objetivo.nodosAnalizados));
    }



    public static <T> void busquedaPorNivelesDeProfundidad(Grafo<T> grafo, T estadoInicial, T estadoObjetivo, int profundidadMaxima) {
        Nodo<T> nodoInicial = grafo.obtenerNodo(estadoInicial);
        Nodo<T> nodoObjetivo = grafo.obtenerNodo(estadoObjetivo);

        if (nodoInicial == null || nodoObjetivo == null) {
            System.out.println("❌ Nodo inicial o final no existe en el grafo.");
            return;
        }

        System.out.println("🔎 Búsqueda por niveles de profundidad desde " + estadoInicial + " hasta " + estadoObjetivo);
        System.out.println("👉 Límite de profundidad: " + profundidadMaxima);

        Set<T> visitados = new HashSet<>();
        Deque<NodoNivel<T>> pila = new ArrayDeque<>();
        pila.push(new NodoNivel<>(nodoInicial, 0));
        visitados.add(nodoInicial.getId());

        long tiempoInicio = System.nanoTime();
        int paso = 0;
        int maxNodosSimultaneos = 1;
        int nodosAnalizados = 0;

        System.out.printf("\n%-6s | %-20s | %-12s | %-30s | %-30s\n", "Paso", "Nodo extraído", "Profundidad", "Vecinos agregados", "Pila actual");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");

        while (!pila.isEmpty()) {
            NodoNivel<T> actual = pila.pop();
            nodosAnalizados++;

            StringBuilder vecinosAgregados = new StringBuilder();

            if (actual.nodo.getId().equals(estadoObjetivo)) {
                System.out.printf("%-6d | %-20s | %-12d | %-30s | %-30s\n",
                        paso++, actual.nodo.getId(), actual.nivel, "-", pilaToStringNivel(pila));
                System.out.println("✅ Nodo objetivo encontrado: " + actual.nodo.getId());
                long duracion = System.nanoTime() - tiempoInicio;
                imprimirMetricas(duracion, nodosAnalizados, maxNodosSimultaneos);
                return;
            }

            if (actual.nivel < profundidadMaxima) {
                List<Nodo<T>> vecinos = new ArrayList<>(actual.nodo.getConexionesSalientes().values());
                Collections.reverse(vecinos);  // para mantener el orden natural

                for (Nodo<T> vecino : vecinos) {
                    if (!visitados.contains(vecino.getId())) {
                        pila.push(new NodoNivel<>(vecino, actual.nivel + 1));
                        visitados.add(vecino.getId());
                        vecinosAgregados.append(vecino.getId()).append(" ");
                    }
                }
            }

            maxNodosSimultaneos = Math.max(maxNodosSimultaneos, pila.size() + 1);

            System.out.printf("%-6d | %-20s | %-12d | %-30s | %-30s\n",
                    paso++, actual.nodo.getId(), actual.nivel, vecinosAgregados.toString().trim(), pilaToStringNivel(pila));
        }

        long duracion = System.nanoTime() - tiempoInicio;
        System.out.println("❌ Nodo objetivo no encontrado hasta una profundidad de " + profundidadMaxima);
        imprimirMetricas(duracion, nodosAnalizados, maxNodosSimultaneos);
    }

    // Clase interna para manejar el nodo con su nivel
    private static class NodoNivel<T> {
        Nodo<T> nodo;
        int nivel;

        NodoNivel(Nodo<T> nodo, int nivel) {
            this.nodo = nodo;
            this.nivel = nivel;
        }
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





