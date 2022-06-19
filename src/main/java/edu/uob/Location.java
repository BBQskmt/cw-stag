package edu.uob;

import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Node;

import java.util.ArrayList;

public class Location extends GameEntity{

    ArrayList<Characters> charactersList;
    ArrayList<Artefacts> artefactsList;
    ArrayList<Furniture> furnitureList;

    ArrayList<String> pathsList;

    public Location(String name, String description) {
        super(name, description);
        charactersList = new ArrayList<>();
        artefactsList = new ArrayList<>();
        furnitureList = new ArrayList<>();
        pathsList = new ArrayList<>();
    }

    public void setCharactersList(Characters character)
    {
        charactersList.add(character);
    }

    public ArrayList<Characters> getCharactersList()
    {
        return charactersList;
    }

    public void setArtefactsList(Artefacts artefact)
    {
        artefactsList.add(artefact);
    }

    public ArrayList<Artefacts> getArtefactsList()
    {
        return artefactsList;
    }

    public void setFurnitureList(Furniture furniture)
    {
        furnitureList.add(furniture);
    }

    public ArrayList<Furniture> getFurnitureList()
    {
        return  furnitureList;
    }

    public void setPaths(ArrayList<Edge> paths)
    {
        for (int i = 0; i < paths.size(); i++)
        {
            Edge pathDetail = paths.get(i);
            Node fromLocation = pathDetail.getSource().getNode();
            Node toLocation = pathDetail.getTarget().getNode();
            String fromName = fromLocation.getId().getId();
            String toName = toLocation.getId().getId();

            if (!fromName.equals(name))
            {
                continue;
            }
            pathsList.add(toName);
        }
    }

    public ArrayList<String> getPath() {
        return pathsList;
    }

    public void setFirstLocation ()
    {

    }


}


