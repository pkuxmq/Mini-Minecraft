package Plants;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

public class Grass extends Plant {
    String Texture_path = "Textures/Terrain/Terrain/Grass/plant_grass.png";

    static Material mat = null;
    static Texture tex = null;

    public Grass(Vector3f p, AssetManager assetManager)
    {
        plant_type = "grass";
        plant_id = idnum++;
        pos = p;

        if(mat == null)
        {
            mat = new Material(assetManager, Material_path);
            tex = assetManager.loadTexture(Texture_path);
            mat.setTexture("DiffuseMap", tex);
            mat.setFloat("Shininess", 2.0f);
            mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }

        for(int i = 0; i < 4; i++) {
            geom[i] = new Geometry("plant_grass:" + plant_id, One_Quad);
            geom[i].setQueueBucket(RenderQueue.Bucket.Transparent);
            geom[i].setMaterial(mat);
        }
        geom[1].rotate(0, pi, 0);
        geom[2].rotate(0, pi / 2, 0);
        geom[3].rotate(0, pi * 3 / 2, 0);
        geom[0].move(new Vector3f(pos.x - 0.5f, pos.y - 0.5f, pos.z));
        geom[1].move(new Vector3f(pos.x + 0.5f, pos.y - 0.5f, pos.z));
        geom[2].move(new Vector3f(pos.x, pos.y - 0.5f, pos.z + 0.5f));
        geom[3].move(new Vector3f(pos.x, pos.y - 0.5f, pos.z - 0.5f));
    }
}
