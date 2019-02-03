package Block;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

public class Block_rock extends Block{

    static String Texture_path = "Textures/Terrain/Terrain/Rock/Rock.jpg";

    static Material mat = null;
    static Texture tex = null;

    public Block_rock(Vector3f p, AssetManager assetManager)
    {
        block_type = "rock";
        block_id = idnum++;
        block_hardness = 5;
        pos = p;

        geom = new Geometry("block:" + block_id, One_Box);

        if(mat == null)
        {
            mat = new Material(assetManager, Material_path);
            tex = assetManager.loadTexture(Texture_path);
            mat.setTexture("DiffuseMap", tex);
            mat.setFloat("Shininess", 2.0f);
        }

        geom.setMaterial(mat);
        geom.move(pos);

        rigidBody = new RigidBodyControl(0);

        Vector3f box = new Vector3f(1f, 1f, 1f);
        rigidBody.setCollisionShape(new BoxCollisionShape(box));
        rigidBody.setPhysicsLocation(new Vector3f(pos));

        geom.addControl(rigidBody);
    }

}
