package Block;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

public class Block_earthwithsnow extends  Block{
    static String Texture_path = "Textures/Terrain/Terrain/Earth/EarthwithSnow.png";

    static Material mat = null;
    static Texture tex = null;

    public Block_earthwithsnow(Vector3f p, AssetManager assetManager)
    {
        block_type = "earthwithsnow";
        block_id = idnum++;
        block_hardness = 3;
        pos = p;
        grassblock = true;

        geom = new Geometry("block:" + block_id, One_Box);
        geom_ceil = new Geometry("block_ceil:" + block_id, One_Quad);

        if(mat == null) {
            mat = new Material(assetManager, Material_path);
            tex = assetManager.loadTexture(Texture_path);
            mat.setTexture("DiffuseMap", tex);
            mat.setFloat("Shininess", 0.01f);
        }

        if(snow_Surface_mat == null) {
            snow_Surface_mat = new Material(assetManager, Material_path);
            snow_Surface_tex = assetManager.loadTexture(grasssnow_Surface_Texture_path);
            snow_Surface_mat.setTexture("DiffuseMap", snow_Surface_tex);
/*
            snow_Surface_mat.setColor("Diffuse", new ColorRGBA(0.514f, 0.514f, 0.514f, 1));
            snow_Surface_mat.setColor("Ambient", new ColorRGBA(0.514f, 0.514f, 0.514f, 1));
            snow_Surface_mat.setColor("Specular", new ColorRGBA(0.514f, 0.514f, 0.514f, 1));
            snow_Surface_mat.setBoolean("UseMaterialColors", true);*/
            snow_Surface_mat.setFloat("Shininess", 2.0f);
        }

        geom.setMaterial(mat);
        geom.move(pos);

        geom_ceil.setMaterial(snow_Surface_mat);
        geom_ceil.rotate(-pi / 2, 0, 0);
        geom_ceil.move(new Vector3f(pos.x - 0.5f, pos.y + 0.501f, pos.z + 0.5f));

        rigidBody = new RigidBodyControl(0);

        Vector3f box = new Vector3f(1f, 1f, 1f);
        rigidBody.setCollisionShape(new BoxCollisionShape(box));
        rigidBody.setPhysicsLocation(new Vector3f(pos));

        geom.addControl(rigidBody);
    }
}
