package Block;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.font.BitmapText;


import Map.*;
import Item.*;
import Main_function.Main_function;

public class Block {
    public int block_id = 0;
    public String block_type = "null";
    int block_hardness;



    Node parentnode;
    PhysicsSpace physicsSpace;
    public boolean added = false, padded = false;
    boolean grassblock = false, snowblock = false;

    static int idnum = 1;

    final float pi = (float)(4 * Math.atan(1));

    Vector3f pos;

    public Geometry geom, geom_ceil;

    static Box One_Box = new Box(0.5f, 0.5f, 0.5f);
    static Quad One_Quad = new Quad(1f, 1f);

    static String Material_path = "Common/MatDefs/Light/Lighting.j3md";
    static String grass_Surface_Texture_path = "Textures/Terrain/Terrain/Grass/Grass__.png";
    static String grasssnow_Surface_Texture_path = "Textures/Terrain/Terrain/Grass/Snow.png";

    static Material grass_Surface_mat = null, snow_Surface_mat = null;
    static Texture grass_Surface_tex = null, snow_Surface_tex = null;

    RigidBodyControl rigidBody;

    public void addtonode(Node node)
    {
        node.attachChild(geom);
        if(geom_ceil != null)
            node.attachChild(geom_ceil);
        added = true;
        parentnode = node;

    }

    public void detachfromnode(Node node)
    {
        node.detachChild(geom);
        if(block_type.contains("grass") || block_type.contains("snow"))
            node.detachChild(geom_ceil);
        added = false;
    }

    public void addtophysicsSpace(PhysicsSpace physicsSpace)
    {
        physicsSpace.add(rigidBody);
        padded = true;

        this.physicsSpace = physicsSpace;
    }

    public void removefromphysicsSpace(PhysicsSpace physicsSpace)
    {
        physicsSpace.remove(rigidBody);
        padded = false;
    }

    public void addtomap(Map map)
    {
        if(pos.x >= 0 && pos.x <= map.x - 1 && pos.y >= 0 && pos.y <= map.y - 1 && pos.z >= 0 && pos.z <= map.z - 1)
            map.Map[(int)pos.x][(int)pos.y][(int)pos.z] = this;
    }

    public boolean idequals(int id)
    {
        if(block_id == id)
            return true;
        return false;
    }

    public void BlockDestroyed(Map map,Item p)
    {

        detachfromnode(parentnode);

        if(padded)
            removefromphysicsSpace(physicsSpace);
        map.Map[(int)pos.x][(int)pos.y][(int)pos.z] = null;

        int chunk_x = (int)(pos.x / 16), chunk_y = (int)(pos.y / 16), chunk_z = (int)(pos.z / 16);

        map.ChunkMap[chunk_x][chunk_y][chunk_z].blk[(int)pos.x - chunk_x * 16][(int)pos.y - chunk_y * 16][(int)pos.z - chunk_z * 16] = null;

        map.add_near_block(pos);

    }

    public void onBlockActivated() {}

    public int onBlockClicked(Map map,Item p,int flag,int flagid)
    {
        block_hardness--;

        if(block_hardness <= 0) {
            if(p.num < 99) {
                p.num++;
                flagid = p.getid();
                flag = 1;
            }
            BlockDestroyed(map,p);

        }
        if(flag == 1)return flagid;
        else return 0;
    }

    public void onEntityCollidedWithBlock() {}

}
