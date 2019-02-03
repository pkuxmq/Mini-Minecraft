package Plants;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

public class Flower extends Plant {
    String Flower_type;
    final static int flower_type_num = 11;

    static String Texture_path = "Textures/Terrain/Terrain/Flower/";

    static Material mat[] = new Material[flower_type_num];
    static Texture tex[] = new Texture[flower_type_num];

    public Flower(Vector3f p, AssetManager assetManager, String flower_type)
    {
        plant_type = "flower";
        plant_id = idnum++;
        pos = p;

        Flower_type = flower_type;

        int type_num = flower_type2type_num(Flower_type);
        if(mat[type_num] == null)
        {
            mat[type_num] = new Material(assetManager, Material_path);
            tex[type_num] = assetManager.loadTexture(Texture_path + "flower_" + Flower_type + ".png");
            mat[type_num].setTexture("DiffuseMap", tex[type_num]);
            mat[type_num].setFloat("Shininess", 2.0f);
            mat[type_num].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }

        for(int i = 0; i < 4; i++) {
            geom[i] = new Geometry("plant_flower_" + Flower_type + ":" + plant_id, One_Quad);
            geom[i].setQueueBucket(RenderQueue.Bucket.Transparent);
            geom[i].setMaterial(mat[type_num]);
        }
        geom[1].rotate(0, pi, 0);
        geom[2].rotate(0, pi / 2, 0);
        geom[3].rotate(0, pi * 3 / 2, 0);
        geom[0].move(new Vector3f(pos.x - 0.5f, pos.y - 0.5f, pos.z));
        geom[1].move(new Vector3f(pos.x + 0.5f, pos.y - 0.5f, pos.z));
        geom[2].move(new Vector3f(pos.x, pos.y - 0.5f, pos.z + 0.5f));
        geom[3].move(new Vector3f(pos.x, pos.y - 0.5f, pos.z - 0.5f));
    }

    int flower_type2type_num(String flower_type)
    {
        switch(flower_type)
        {
            case "allium": return 0;
            case "blue_orchid": return 1;
            case "dandelion": return 2;
            case "houstonia": return 3;
            case "oxeye_daisy": return 4;
            case "paeonia": return 5;
            case "rose": return 6;
            case "tulip_orange": return 7;
            case "tulip_pink": return 8;
            case "tulip_red": return 9;
            case "tulip_white": return 10;
            default: return 0;
        }
    }
}
