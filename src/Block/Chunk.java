package Block;

import Plants.Plant;
import com.jme3.scene.Node;

public class Chunk {
    public Block blk[][][];
    public Plant plt[][][];

    public Node chunknode = new Node();
    public boolean added = false;

    public Chunk()
    {
        blk = new Block[16][16][16];
        plt = new Plant[16][16][16];
    }


    public void addtonode(Node node)
    {
        node.attachChild(chunknode);
        added = true;
    }

    public void detachfromnode(Node node)
    {
        node.detachChild(chunknode);
        added = false;
    }
}
