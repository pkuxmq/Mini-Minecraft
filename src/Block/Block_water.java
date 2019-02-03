package Block;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;

import Map.*;

public class Block_water extends  Block {

    static Material mat = null;

    public Block_water(Vector3f p, AssetManager assetManager)
    {
        block_type = "water";
        block_id = idnum++;
        block_hardness = 1;
        pos = p;

        geom = new Geometry("block:" + block_id, One_Quad);
        geom_ceil = new Geometry("block:" + block_id, One_Quad);

        if(mat == null)
        {
            mat = new Material(assetManager, Material_path);
            mat.setColor("Diffuse", new ColorRGBA(0f, 0.3f, 0.7f, 0.8f));
            mat.setColor("Ambient", new ColorRGBA(0f, 0.3f, 0.7f, 0.8f));
            mat.setColor("Specular", new ColorRGBA(0f, 0.3f, 0.7f, 0.8f));
            mat.setBoolean("UseMaterialColors", true);
            mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        geom.rotate(-pi / 2, 0, 0);
        geom.move(new Vector3f(pos.x - 0.5f, pos.y + 0.501f, pos.z + 0.5f));

        geom_ceil.setMaterial(mat);
        geom_ceil.setQueueBucket(RenderQueue.Bucket.Transparent);
        geom_ceil.rotate(pi / 2, 0, 0);
        geom_ceil.move(new Vector3f(pos.x + 0.5f, pos.y + 0.501f, pos.z + 0.5f));
    }

    public void onBlockClicked(Map map) {}
}
