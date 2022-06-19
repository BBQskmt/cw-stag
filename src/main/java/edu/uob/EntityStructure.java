package edu.uob;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.util.ArrayList;

public class EntityStructure {
    ArrayList<Graph> clusters;
    ArrayList<Edge> paths;
    ArrayList<Location> locationsList;
    ArrayList<Players> playersList;

    public EntityStructure(ArrayList<Graph> sections)
    {
        clusters = sections.get(0).getSubgraphs();
        paths = sections.get(1).getEdges();

        locationsList = new ArrayList<>();
        playersList = new ArrayList<>();
    }

    public void setLocationArray()
    {
        // store locations and its attributes: characters, artefacts and furniture
        for (Graph cluster : clusters) {

            Node locationDetails = cluster.getNodes(false).get(0);
            String locationName = locationDetails.getId().getId();
            String locationDescription = locationDetails.getAttributes().get("description");
            Location location = new Location(locationName, locationDescription);

            addAttribToList(cluster,location);
            location.setPaths(paths);
            locationsList.add(location);
        }
    }

    public ArrayList<Location> getLocationsList()
    {
        return locationsList;
    }

    public ArrayList<Players> getPlayersList()
    {
        return playersList;
    }

    public void addAttribToList(Graph cluster, Location location)
    {
        for (int j = 0; j < cluster.getSubgraphs().size(); j++) {

            // get subgraph name: artefacts, furniture ...
            ArrayList<Graph> subgraphList = cluster.getSubgraphs();
            Graph subgraph = subgraphList.get(j);
            String subgraphID = subgraph.getId().getId();

            // get specific entity name matching attribute: potion, trapdoor ...
            if (subgraph.getNodes(false).isEmpty()) {
                continue;
            }

            for (int n = 0; n < subgraph.getNodes(false).size(); n++) {
                Node subgraphDetails = subgraph.getNodes(false).get(n);
                String entityName = subgraphDetails.getId().getId();
                String entityDescription = subgraphDetails.getAttributes().get("description");

                switch (subgraphID) {
                    case "artefacts" -> location.setArtefactsList(new Artefacts(entityName, entityDescription));
                    case "furniture" -> location.setFurnitureList(new Furniture(entityName, entityDescription));
                    case "characters" -> location.setCharactersList(new Characters(entityName, entityDescription));
                    case "players" -> playersList.add(new Players(entityName, entityDescription));
                    default -> System.out.println("no match result");
                }
            }
        }
    }
}
