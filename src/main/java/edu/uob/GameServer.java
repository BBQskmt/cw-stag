package edu.uob;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/** This class implements the STAG server. */
public final class GameServer {

    private static final char END_OF_TRANSMISSION = 4;
    EntityStructure entityStructure;

    public static void main(String[] args) throws IOException {
        File entitiesFile = Paths.get("extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8888);
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer(File, File)}) otherwise we won't be able to mark
    * your submission correctly.
    *
    * <p>You MUST use the supplied {@code entitiesFile} and {@code actionsFile}
    *
    * @param entitiesFile The game configuration file containing all game entities to use in your game
    * @param actionsFile The game configuration file containing all game actions to use in your game
    *
    */
    public GameServer(File entitiesFile, File actionsFile) {
        // TODO implement your server logic here
        //read entitiesFile
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entitiesFile);
            parser.parse(reader);
            Graph wholeDocument = parser.getGraphs().get(0);
            ArrayList<Graph> sections = wholeDocument.getSubgraphs();
            setEntityStruct(sections);

        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException was thrown when attempting to read basic entities file");
        } catch (ParseException pe) {
            System.out.println("ParseException was thrown when attempting to read basic entities file");
        }
    }

    public void setEntityStruct(ArrayList<Graph> sections)
    {

        entityStructure = new EntityStructure(sections);
        entityStructure.setLocationArray();
        ArrayList<Location> locationsList = entityStructure.getLocationsList();

        // Print for self use
//        for (Location eachLocation : locationsList) {
//            System.out.println();
//            System.out.println("location name: " + eachLocation.getName());
//            ArrayList<Artefacts> arrayListOne = eachLocation.getArtefactsList();
//            for (Artefacts artefacts : arrayListOne) System.out.println("artefacts description: " + artefacts.getDescription());
//
//            ArrayList<Characters> arrayListTwo = eachLocation.getCharactersList();
//            for (Characters characters : arrayListTwo) System.out.println("character name: " + characters.getName());
//
//            ArrayList<Furniture> arrayListThree = eachLocation.getFurnitureList();
//            for (Furniture furniture : arrayListThree) System.out.println("furniture name: " + furniture.getName());
//
//            ArrayList<String> PathList = eachLocation.getPath();
//            for (String path : PathList) System.out.println("path ToName: " + path);
//        }

    }


    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming game commands and carries out the corresponding actions.
    */
    public String handleCommand(String command) {
        // TODO implement your server logic here
        if (command.equals("Amy: look"))
        {

            Location startLocation = entityStructure.getLocationsList().get(0);
            String locationString = "You're in " + startLocation.getDescription() + "." + "\n";

            String artefactString = "You can see: \n";
            ArrayList<Artefacts> artefactsArrayList = startLocation.getArtefactsList();
            for (int i = 0; i < artefactsArrayList.size(); i++)
            {
                String artefact = artefactsArrayList.get(i).getDescription();
                artefactString = artefactString + artefact + "\n";
            }

            String pathString = "You can acess from here: \n";
            ArrayList<String> pathsArrayList = startLocation.getPath();
            for (int j = 0; j < pathsArrayList.size(); j++)
            {
                String path = pathsArrayList.get(j);
                pathString = pathString + path + "\n";
            }

            return (locationString + artefactString + pathString);
        }
        else {
            return "Thanks for your message: " + command;
        }

    }

    //  === Methods below are there to facilitate server related operations. ===

    /**
    * Starts a *blocking* socket server listening for new connections. This method blocks until the
    * current thread is interrupted.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * you want to.
    *
    * @param portNumber The port to listen on.
    * @throws IOException If any IO related operation fails.
    */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }
        }
    }

    /**
    * Handles an incoming connection from the socket server.
    *
    * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
    * * you want to.
    *
    * @param serverSocket The client socket to read/write from.
    * @throws IOException If any IO related operation fails.
    */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            System.out.println("Connection established");
            String incomingCommand = reader.readLine();
            if(incomingCommand != null) {
                System.out.println("Received message from " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();

            }
        }
    }
}
