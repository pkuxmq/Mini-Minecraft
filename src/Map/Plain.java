package Map;

public class Plain {
    int px, py;
    int max_height = 5;
    int edge_height = 0;
    public float height[][];

    Plain(int sx, int sy)
    {
        px = sx;
        py = sy;

        Plain_init();
    }

    Plain(int sx, int sy, int h, int edge_height)
    {
        px = sx;
        py = sy;
        max_height = h;
        this.edge_height = edge_height;

        Plain_init();
    }

    Plain(int sx, int sy, int h)
    {
        px = sx;
        py = sy;
        max_height = h;

        Plain_init();
    }

    void Plain_init()
    {
        Perlin perlin = new Perlin(px, py, 0.5f);
        height = perlin.Perlin();

        float cur_max_height = 0;
        for(int i = 0; i < px; i++)
            for(int j = 0; j < py; j++)
                cur_max_height = Math.max(cur_max_height, Math.abs(height[i][j]));
        for(int i = 0; i < px; i++)
            for(int j = 0; j < py; j++) {
                height[i][j] = height[i][j] / cur_max_height * max_height;
                height[i][j] = Math.abs(height[i][j] + edge_height);
            }

    }


}
