package firs_graph;

import firs_graph.model.Graph;


public class Main {
    public static void main(String[] args){

        Graph graph = new Graph();
        graph.isDirected = true;

        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);

        graph.addLink(1, 2, 1);
        graph.addLink(1, 3, 1);
        graph.addLink(2, 3, 1);
        graph.addLink(2, 4, 1);
        graph.addLink(3, 4, 1);

        graph.print_graph();



        //         Crear y mostrar ventana con el grafo
        //        JFrame frame = new JFrame("Grafo en Java");
        //        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //        frame.setSize(500, 500);
        //        frame.add(new GraphPanel(graph));
        //        frame.setVisible(true);
        
        
    }
}