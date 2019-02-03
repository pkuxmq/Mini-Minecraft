package Map;

import Block.*;
import Plants.*;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;

public class Map {
    final String flower_type[] = {"allium", "blue_orchid", "dandelion", "houstonia", "oxeye_daisy", "paeonia", "rose", "tulip_orange", "tulip_pink", "tulip_red", "tulip_white"};
    final int flower_type_num = 11;

    public Block Map[][][];
    public Plant PlantMap[][][];
    public Chunk ChunkMap[][][];

    public int x, y, z;

    AssetManager assetManager;

    PhysicsSpace physicsSpace;

    void GenerateMountainwithwater(int map_x, int map_y, int map_z, int mountain_x, int mountain_y, int mountain_z, int edge_height, int water_pos)
    {
        Mountain mountain = new Mountain(mountain_x, mountain_z, mountain_y, edge_height);

        for(int i = 0; i < mountain_x; i++) {
            for (int k = 0; k < mountain_z; k++) {
                int j;
                for (j = 0; j <= mountain.height[i][k]; j++) {
                    double rand = Math.random();

                    if(rand <= 0.5 || j > mountain_y / 3 || j < mountain_y / 4)
                        Map[map_x + i][map_y + j][map_z + k] = new Block_earth(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                    else{
                        Map[map_x + i][map_y + j][map_z + k] = new Block_rock(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                        if(i != 0 && j != 0 && k != 0) {
                            for (int i1 = -1; i1 <= 0; i1++)
                                for (int j1 = -1; j1 <= 0; j1++)
                                    for (int k1 = -1; k1 <= 0; k1++)
                                        if(Map[map_x + i + i1][map_y + j + j1][map_z + k + k1] != null)
                                            Map[map_x + i + i1][map_y + j + j1][map_z + k + k1] = new Block_rock(new Vector3f(map_x + i + i1, map_y + j + j1, map_z + k + k1), assetManager);
                        }
                    }
                }

                if(j >= edge_height) {
                    if(j <=20 )
                        Map[map_x + i][map_y + j - 1][map_z + k] = new Block_earthwithgrass(new Vector3f(map_x + i, map_y + j - 1, map_z + k), assetManager);
                    else {
                        Map[map_x + i][map_y + j - 1][map_z + k] = new Block_earthwithsnow(new Vector3f(map_x + i, map_y + j - 1, map_z + k), assetManager);
                    }


                    double rand = Math.random();
                    if(rand <= 0.07)
                        PlantMap[map_x + i][map_y + j][map_z + k] = new Grass(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                    else if(rand <= 0.08) {
                        int rand_type = (int)(Math.random() * 1000) % flower_type_num;
                        PlantMap[map_x + i][map_y + j][map_z + k] = new Flower(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager, flower_type[rand_type]);
                    }
                    else if(rand <= 0.081 && j < (edge_height + mountain_y) / 2) {
                        Tree tree = new Tree(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                        tree.addtomap(this);
                    }
                }
                else {
                    Map[map_x + i][map_y + edge_height - water_pos][map_z + k] = new Block_water(new Vector3f(map_x + i, map_y + edge_height - water_pos, map_z + k), assetManager);
                }
            }
        }
    }

    void GenerateMountain(int map_x, int map_y, int map_z, int mountain_x, int mountain_y, int mountain_z, int edge_height)
    {
        Mountain mountain = new Mountain(mountain_x, mountain_z, mountain_y, edge_height);

        for(int i = 0; i < mountain_x; i++) {
            for (int k = 0; k < mountain_z; k++) {
                int j;
                for (j = 0; j < mountain.height[i][k]; j++)
                    Map[map_x + i][map_y + j][map_z + k] = new Block_earth(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);

                j++;
                if(j <=20 )
                    Map[map_x + i][map_y + j - 1][map_z + k] = new Block_earthwithgrass(new Vector3f(map_x + i, map_y + j - 1, map_z + k), assetManager);
                else {
                    Map[map_x + i][map_y + j - 1][map_z + k] = new Block_earthwithsnow(new Vector3f(map_x + i, map_y + j - 1, map_z + k), assetManager);
                }


                double rand = Math.random();
                if(rand <= 0.07)
                    PlantMap[map_x + i][map_y + j][map_z + k] = new Grass(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                else if(rand <= 0.08) {
                    int rand_type = (int)(Math.random() * 1000) % flower_type_num;
                    PlantMap[map_x + i][map_y + j][map_z + k] = new Flower(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager, flower_type[rand_type]);
                }
                else if(rand <= 0.081 && j < (edge_height + mountain_y) / 2) {
                    Tree tree= new Tree(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                    tree.addtomap(this);
                }
            }
        }
    }

    void GeneratePlain(int map_x, int map_y, int map_z, int plain_x, int plain_y, int plain_z, int edge_height)
    {
        Plain plain = new Plain(plain_x, plain_z, plain_y, edge_height);

        for(int i = 0; i < plain_x; i++) {
            for(int k = 0; k < plain_z; k++) {
                int j = 0;
                for (j = 0; j < plain.height[i][k]; j++)
                    Map[map_x + i][map_y + j][map_z + k] = new Block_earth(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);

                j++;

                Map[map_x + i][map_y + j - 1][map_z + k] = new Block_earthwithgrass(new Vector3f(map_x + i, map_y + j - 1, map_z + k), assetManager);

                double rand = Math.random();
                if(rand <= 0.07)
                    PlantMap[map_x + i][map_y + j][map_z + k] = new Grass(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                else if(rand <= 0.08) {
                    int rand_type = (int)(Math.random() * 1000) % flower_type_num;
                    PlantMap[map_x + i][map_y + j][map_z + k] = new Flower(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager, flower_type[rand_type]);
                }
                else if(rand <= 0.1) {
                    Tree tree = new Tree(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                    tree.addtomap(this);
                }
            }
        }
    }

    void GeneratePlainwithwater(int map_x, int map_y, int map_z, int plain_x, int plain_y, int plain_z, int edge_height, int water_pos)
    {
        Plain plain = new Plain(plain_x, plain_z, plain_y, edge_height);

        for(int i = 0; i < plain_x; i++) {
            for(int k = 0; k < plain_z; k++) {
                int j = 0;
                for (j = 0; j <= plain.height[i][k]; j++)
                    Map[map_x + i][map_y + j][map_z + k] = new Block_earth(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);

                if(j >= edge_height) {
                        Map[map_x + i][map_y + j - 1][map_z + k] = new Block_earthwithgrass(new Vector3f(map_x + i, map_y + j - 1, map_z + k), assetManager);

                    double rand = Math.random();
                    if (rand <= 0.07)
                        PlantMap[map_x + i][map_y + j][map_z + k] = new Grass(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                    else if (rand <= 0.08) {
                        int rand_type = (int) (Math.random() * 1000) % flower_type_num;
                        PlantMap[map_x + i][map_y + j][map_z + k] = new Flower(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager, flower_type[rand_type]);
                    } else if (rand <= 0.081) {
                        Tree tree = new Tree(new Vector3f(map_x + i, map_y + j, map_z + k), assetManager);
                        tree.addtomap(this);
                    }
                }
                else {
                    Map[map_x + i][map_y + edge_height - water_pos][map_z + k] = new Block_water(new Vector3f(map_x + i, map_y + edge_height - water_pos, map_z + k), assetManager);
                }

            }
        }
    }

    public void InitMap()
    {

        int mountain_x = 128, mountain_y = 25, mountain_z = 128;
        int plain_x = 128, plain_y = 5, plain_z = 128;

        GenerateMountain(0, 0, 0, mountain_x, mountain_y, mountain_z, 5);

        GenerateMountainwithwater(0, 0, mountain_z, mountain_x, mountain_y, mountain_z, 5, 2);

        GeneratePlain(mountain_x, 0, mountain_z, plain_x, plain_y, plain_z, 5);

        GeneratePlainwithwater(mountain_x, 0, 0, plain_x, plain_y, plain_z, 5, 2);
/*
        GenerateMountain(0, 0, mountain_z, mountain_x, mountain_y / 2, mountain_z);

        int plain_x = 128, plain_y = 3, plain_z = 128;

        GeneratePlain(0, 0, 2 * mountain_z, plain_x, plain_y, plain_z);*/

        MaptoChunk();

    }

    private void MaptoChunk()
    {
        for(int i = 0; i < x / 16; i++)
            for(int j = 0; j < y / 16; j++)
                for(int k = 0; k < z / 16; k++)
                    for(int p = 0; p < 16; p++)
                        for(int q = 0; q < 16; q++)
                            for(int r = 0; r < 16; r++){
                                if(Map[16 * i + p][16 * j + q][16 * k + r] != null) {
                                    ChunkMap[i][j][k].blk[p][q][r] = Map[16 * i + p][16 * j + q][16 * k + r];
                                    boolean to_node = false;
                                    for(int a = -1; a <= 1; a++)
                                        for(int b = -1; b <= 1; b++)
                                            for(int c = -1; c <= 1; c++) {
                                                if(16 * i + p + a < 0 || 16 * i + p + a > x || 16 * j + q + b < 0 || 16 * j + q + b > y || 16 * k + r + c < 0 || 16 * k + r + c > z)
                                                    continue;
                                                if (Map[16 * i + p + a][16 * j + q + b][16 * k + r + c] == null || Map[16 * i + p + a][16 * j + q + b][16 * k + r + c].block_type == "water") {
                                                    to_node = true;
                                                    break;
                                                }
                                            }
                                    if(to_node)
                                        Map[16 * i + p][16 * j + q][16 * k + r].addtonode(ChunkMap[i][j][k].chunknode);
                                }
                                if(PlantMap[16 * i + p][16 * j + q][16 * k + r] != null) {
                                    ChunkMap[i][j][k].plt[p][q][r] = PlantMap[16 * i + p][16 * j + q][16 * k + r];
                                    PlantMap[16 * i + p][16 * j + q][16 * k + r].addtonode(ChunkMap[i][j][k].chunknode);
                                }
                            }

    }

    public void add_near_block(Vector3f pos)
    {
        int bx = (int)pos.x, by = (int)pos.y, bz = (int)pos.z;
        for(int i = -1; i <= 1; i++)
            for(int j = -1; j <= 1; j++)
                for(int k = -1; k <= 1; k++){
                    if(bx + i < 0 || bx + i > x || by + j < 0 || by + j > y || bz + k < 0 || bz + k > z)
                        continue;
                    if(Map[bx + i][by + j][bz + k] != null && !Map[bx + i][by + j][bz + k].added)
                        Map[bx + i][by + j][bz + k].addtonode(ChunkMap[(bx + i) / 16][(by + j) / 16][(bz + k) / 16].chunknode);
                }
    }

    public Map(int sx, int sy, int sz, AssetManager assetManager, PhysicsSpace physicsSpace)
    {
        x = sx;
        y = sy;
        z = sz;

        this.assetManager = assetManager;
        this.physicsSpace = physicsSpace;

        Map = new Block[x][y][z];
        PlantMap = new Plant[x][y][z];

        ChunkMap = new Chunk[x / 16][y / 16][z / 16];
        for(int i = 0; i < x / 16; i++)
            for(int j = 0; j < y / 16; j++)
                for(int k = 0; k < z / 16; k++)
                    ChunkMap[i][j][k] = new Chunk();
    }
}
