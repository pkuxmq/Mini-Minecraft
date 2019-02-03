package Plants;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;

import Block.*;
import Map.*;
import com.jme3.scene.Node;

import java.util.Random;

public class Tree {

    static int trunk_height[] = {8,10,12,16}, leaves_height[] = {5,6,7,8};
    static int blocknum[] = {8 + 48 + 24 + 24 + 8 + 5,16 + 48 + 48 + 24 + 24  + 8 + 5,
                        24 + 80 + 48 + 48+ 24 + 24  + 8 + 5,32 + 80 +  80 + 48 + 48 + 24 + 24  + 8 + 5};
    int r;
    Block blk[];

    Vector3f pos;

    public boolean added;

    public Tree(Vector3f p, AssetManager assetManager)
    {
        pos = p;
        Random seedRandom = new Random();
        r = Math.abs(seedRandom.nextInt()%4);
        blk = new Block[blocknum[r]];
        for(int i = 0; i < trunk_height[r]; i++) {
            blk[i] = new Block_wood(new Vector3f(pos.x, pos.y + i, pos.z), assetManager);
        }

        int leave_blk_num = trunk_height[r];
        int leaves_width[][] = {{3, 2, 2, 1, 0},{3,3,2,2,1,0},{4,3,3,2,2,1,0},{4,4,3,3,2,2,1,0}};

        for(int t = 0; t < leaves_height[r]; t++) {
            for (int i = -leaves_width[r][t]; i <= leaves_width[r][t]; i++) {
                for (int j = -leaves_width[r][t]; j <= leaves_width[r][t]; j++) {
                    if(t < leaves_height[r] - 1 && i == 0 && j == 0)
                        continue;
                    blk[leave_blk_num++] = new Block_leaves(new Vector3f(pos.x + i, pos.y + trunk_height[r] - leaves_height[r] + t, pos.z + j), assetManager);
                }
            }
        }

        blk[leave_blk_num++] = new Block_leaves(new Vector3f(pos.x - 1, pos.y + trunk_height[r] - leaves_height[r] - 1 + leaves_height[r], pos.z), assetManager);
        blk[leave_blk_num++] = new Block_leaves(new Vector3f(pos.x + 1, pos.y + trunk_height[r] - leaves_height[r] - 1  + leaves_height[r], pos.z), assetManager);
        blk[leave_blk_num++] = new Block_leaves(new Vector3f(pos.x, pos.y + trunk_height[r] - leaves_height[r] - 1  + leaves_height[r], pos.z - 1), assetManager);
        blk[leave_blk_num++] = new Block_leaves(new Vector3f(pos.x, pos.y + trunk_height[r] - leaves_height[r] - 1 + leaves_height[r], pos.z + 1), assetManager);
    }

    public void addtonode(Node node)
    {
        for(int i = 0; i < blocknum[r]; i++)
            blk[i].addtonode(node);
        added = true;
    }

    public void detachfromnode(Node node)
    {
        for(int i = 0; i < blocknum[r]; i++)
            blk[i].detachfromnode(node);
        added = false;
    }

    public void addtomap(Map map)
    {
        for(int i = 0; i < blocknum[r]; i++)
        {
            if(blk[i] != null)
                blk[i].addtomap(map);
        }
    }

}
