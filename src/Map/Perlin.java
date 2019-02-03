package Map;

import java.util.Random;

public class Perlin {

    final int MAX_P = 4;
    float rough = 1;
    float Random_X[][], Random_Y[][];

    int X, Y;
    float height[][];

    Perlin(int x, int y)
    {
        X = x;
        Y = y;
    }

    Perlin(int x, int y, float rough)
    {
        X = x;
        Y = y;
        this.rough = rough;
    }

    void Perlin_init()
    {
        long seed = System.nanoTime();
        Random seedRandom = new Random(seed);

        Random_X = new float[MAX_P + 1][MAX_P + 1];
        Random_Y = new float[MAX_P + 1][MAX_P + 1];

        for (int i = 1; i < MAX_P; i++) {
            for (int j = 1; j < MAX_P; j++) {
                Random_X[i][j] = seedRandom.nextFloat();
                Random_Y[i][j] = seedRandom.nextFloat();
            }
        }
    }

    float Perlin_Cal(float x, float y, int x0, int y0, float eps)
    {
        float delta_x = (x + 1) / eps - x0;
        float delta_y = (y + 1) / eps - y0;

        return delta_x * Random_X[x0][y0] + delta_y * Random_Y[x0][y0];
    }

    float Perlin_Mix(float a, float b, float alpha)
    {
        float beta = 0;
        beta += 6 * Math.pow(alpha, 5);
        beta -= 15 * Math.pow(alpha, 4);
        beta += 10 * Math.pow(alpha, 3);

        return (1 - beta) * a + beta * b;
    }

    float Perlin_Value(float x, float y, int grid_width)
    {
        float eps = 2f / grid_width;

        int x1 = (int)Math.floor((x + 1) / eps);
        int x2 = (int)Math.ceil((x + 1) / eps);
        int y1 = (int)Math.floor((y + 1) / eps);
        int y2 = (int)Math.ceil((y + 1) / eps);

        float product1 = Perlin_Cal(x, y, x1, y1, eps);
        float product2 = Perlin_Cal(x, y, x1, y2, eps);
        float product3 = Perlin_Cal(x, y, x2, y1, eps);
        float product4 = Perlin_Cal(x, y, x2, y2, eps);

        float val1 = Perlin_Mix(product1, product2, (y + 1) / eps - y1);
        float val2 = Perlin_Mix(product3, product4, (y + 1) / eps - y1);

        float val = Perlin_Mix(val1, val2, (x + 1) / eps - x1);

        return val;
    }

    float[][] Perlin()
    {
        Perlin_init();
        height = new float[X][Y];
        for(int i = 0; i < X; i++)
            for(int j = 0; j < Y; j++)
            {
                float x = 2.0f * i / (X - 1) - 1;
                float y = 2.0f * j / (Y - 1) - 1;

                height[i][j] += 1 * Perlin_Value(x, y, 4);
//                if(rough >= 1)
//                   height[i][j] += rough / 10 * Perlin_Value(x, y, 4);
//                if(rough >= 2)
//                    height[i][j] += rough / 100 * Perlin_Value(x, y, 8);
            }

            return height;
    }
}
