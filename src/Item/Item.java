package Item;

import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.material.Material;
import com.jme3.scene.shape.Quad;
import com.jme3.app.SimpleApplication;
import com.jme3.texture.Texture;
import Map.*;
import Item.*;

import com.jme3.app.*;


public class Item {
    public int num = 0;
    public int maxStackSize = 0;
    public int maxDamage = 0;

    public Spatial pickuang = new Geometry();
    public Spatial picout = new Geometry();

    
    public int gettool(){
        return 0;
    }
    public int getid(){
        return 0;
    }
}
