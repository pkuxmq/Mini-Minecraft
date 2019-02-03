package Map;

public class Buffer_area {
    int bx, by;
    boolean direction_x;
    float height1[], height2[];
    public float height[][];

    Buffer_area(int x, int y, float h1[], float h2[], boolean d)
    {
        bx = x;
        by = y;
        direction_x = d;
        height1 = h1;
        height2 = h2;

        Buffer_area_init();
    }

    void Buffer_area_init()
    {
        height = new float[bx][by];

        for(int x = 0; x < bx; x++)
        {
            for(int y = 0; y < by; y++)
            {
                if(direction_x)
                    height[x][y] = height1[y] + (float)x / bx * (height2[y] - height1[y]);
                else
                    height[x][y] = height1[x] + (float)y / by * (height2[x] - height1[x]);
            }
        }
    }
}
