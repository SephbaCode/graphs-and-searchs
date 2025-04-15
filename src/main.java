import Jugs_problem.JugsProblemGraph;
import generic.Analizador;
import weight_graph_v2.*;

import java.util.Arrays;
import java.util.List;

public class main {

    public static void main(String[] args) {

        //grafosCiegas();
        //grafoEuristico();
        cargaGrafo();









    }

    private static void grafosCiegas ( ) {
        JugsProblemGraph jugsProblemGraph = new JugsProblemGraph();

        jugsProblemGraph.generate_graph();

        // Definimos el nodo inicial y el objetivo
        List<Integer> estadoInicial = Arrays.asList(0, 0);
        List<Integer> estadoObjetivo = Arrays.asList(4, 0);

        System.out.println("=======================================================================================");
        Analizador.busquedaAmplitud(jugsProblemGraph, estadoInicial, estadoObjetivo);
        System.out.println("=======================================================================================");
        Analizador.busquedaProfundidad(jugsProblemGraph, estadoInicial, estadoObjetivo);
        System.out.println("=======================================================================================");
        Analizador.busquedaBidireccional(jugsProblemGraph, estadoInicial, estadoObjetivo);
        System.out.println("=======================================================================================");
        Analizador.busquedaPorNivelesDeProfundidad(jugsProblemGraph, estadoInicial, estadoObjetivo,7);
    }

    private static void grafoEuristico ( ) {
        GrafoPonderado grafo = new GrafoPonderado();

        // Crear nodos
        NodoPonderado nodoA = new NodoPonderado("A", 0);
        NodoPonderado nodoB = new NodoPonderado("B", 1);
        NodoPonderado nodoC = new NodoPonderado("C", 2);
        NodoPonderado nodoD = new NodoPonderado("D", 3);
        NodoPonderado nodoE = new NodoPonderado("E", 4);

        // Agregar nodos al grafo
        grafo.agregarNodo(nodoA);
        grafo.agregarNodo(nodoB);
        grafo.agregarNodo(nodoC);
        grafo.agregarNodo(nodoD);
        grafo.agregarNodo(nodoE);

        // Conectar nodos con pesos
        grafo.conectar("A", "B", 1);
        grafo.conectar("A", "C", 4);
        grafo.conectar("B", "C", 2);
        grafo.conectar("B", "D", 5);
        grafo.conectar("C", "D", 1);
        grafo.conectar("D", "E", 3);

        // Imprimir el grafo
        System.out.println(grafo.toString());

        // Realizar búsqueda heurística
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por gradiente");
        EuristicSearchs.BusquedaGradiente(grafo, "A", "E", "min");

    }

    private static void cargaGrafo(){
        GrafoPonderado grafo = new GrafoPonderado();
        String rutaNodos = "src/data/heuristicas.csv";
        String rutaAristas = "src/data/conexiones.csv";

        grafo.cargarGrafoDesdeCSV(rutaNodos, rutaAristas);

        //System.out.println(grafo);

        // Realizar búsqueda heurística
        System.out.println("=======================================================================================");
        System.out.println("Busqueda por gradiente");
        EuristicSearchs.BusquedaGradiente(grafo, "CY", "HZ", "min");


    }


}
