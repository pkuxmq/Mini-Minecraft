package Plants;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import Map.*;

public class Plant {
    public int plant_id = 0;
    public String plant_type = "null";

    Node parentnode;
    public boolean added;

    static int idnum = 1;

    final float pi = (float)(4 * Math.atan(1));

    Vector3f pos;

    public Geometry geom[] = new Geometry[4];

    static Quad One_Quad = new Quad(1f, 1f);

    static String Material_path = "Common/MatDefs/Light/Lighting.j3md";

    public void addtonode(Node node)
    {
        for(int i = 0; i < 4; i++)
            node.attachChild(geom[i]);
        added = true;
        parentnode = node;
    }

    public void detachfromnode(Node node)
    {
        for(int i = 0; i < 4; i++)
            node.detachChild(geom[i]);
        added = false;
    }

    public void PlantDestroyed(Map map)
    {
        detachfromnode(parentnode);

        map.PlantMap[(int)pos.x][(int)pos.y][(int)pos.z] = null;
    }

    public void PlantClicked(Map map)
    {
        PlantDestroyed(map);

        harvestPlant();
    }

    public void harvestPlant() {}
}
