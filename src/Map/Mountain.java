package Map;

public class Mountain {
    int mx, my;
    int max_height = 50;
    int edge_height = 0;
    public float height[][];

    Mountain(int sx, int sy)
    {
        mx = sx;
        my = sy;

        Mountain_init();
    }

    Mountain(int sx, int sy, int h)
    {
        mx = sx;
        my = sy;
        max_height = h;

        Mountain_init();
    }

    Mountain(int sx, int sy, int h, int edge_height)
    {
        mx = sx;
        my = sy;
        max_height = h;
        this.edge_height = edge_height;

        Mountain_init();
    }

    void Mountain_init()
    {
        Perlin perlin = new Perlin(mx, my, 5);
        height = perlin.Perlin();

        float cur_max_height = 0;
        for(int i = 0; i < mx; i++)
            for(int j = 0; j < my; j++)
                cur_max_height = Math.max(cur_max_height, Math.abs(height[i][j]));
        for(int i = 0; i < mx; i++)
            for(int j = 0; j < my; j++) {
                height[i][j] = height[i][j] / cur_max_height * max_height;
                height[i][j] = Math.abs(height[i][j] + edge_height);
            }

    }


}
