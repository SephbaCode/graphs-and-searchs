import Jugs_problem.JugsProblemGraph;
import generic.Analizador;
import weigth_graph.GrafoPonderado;
import weigth_graph.NodoPonderado;

import java.util.Arrays;
import java.util.List;

public class main {

    public static void main(String[] args) {


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


        NodoPonderado<String> nodoA = new NodoPonderado<>("A");
        NodoPonderado<String> nodoB = new NodoPonderado<>("B");
        NodoPonderado<String> nodoC = new NodoPonderado<>("C");

        // Crear el grafo ponderado
        GrafoPonderado<String> grafo = new GrafoPonderado<>();
        grafo.agregarNodo(nodoA);
        grafo.agregarNodo(nodoB);
        grafo.agregarNodo(nodoC);

        // Conectar nodos con pesos
        grafo.conectar("A", "B", 5);
        grafo.conectar("A", "C", 2);
        grafo.conectar("B", "C", 3);

        // Obtener peso de una conexión
        System.out.println("Peso de A -> B: " + grafo.obtenerPesoConexion("A", "B"));
        System.out.println("Peso de A -> C: " + grafo.obtenerPesoConexion("A", "C"));
        System.out.println("Peso de B -> C: " + grafo.obtenerPesoConexion("B", "C"));

        // Imprimir el grafo
        System.out.println(grafo.toString());






    }
}
