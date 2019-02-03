package Main_function;

import Block.Block_earth;
import Block.Block_rock;
import Block.Block_wood;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.material.Material;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

import com.jme3.font.BitmapFont;
import com.jme3.font.Rectangle;

import Map.*;
import Item.*;
import  Animal.*;
import org.lwjgl.Sys;

import java.awt.*;

public class Main_function extends SimpleApplication implements ActionListener {

    static final int sx = 512, sy = 64, sz = 512;
    static final int sight = 64, chunk_sight = sight / 16;
    Map map;
    Vector3f precampos = new Vector3f(), curcampos = new Vector3f();
    int pre_chunk_x, pre_chunk_y, pre_chunk_z;
    Monkey monkey[];
    int monkey_num = 20;

    static BulletAppState bulletAppState;
    static PhysicsSpace physicsSpace;
    public int flag = 0;
    public int flagid = 0;
    private Spatial cross;
    public Spatial pickuang = new Geometry();
    public Spatial pictool = new Geometry();

    public BitmapText txtwood;
    public BitmapText txtrock;
    public BitmapText txtearth;

    private Ray ray = new Ray();
    final float ray_length = 10f;
    public Item p;
    public Material_Earth earthma = new Material_Earth();
    public Material_Stone stonema = new Material_Stone();
    public Material_Wood woodma = new Material_Wood();
    public Tool_Axe axetool = new Tool_Axe();
    int Material_id = 4;
    static  public boolean buttonpressed = false;
    public final static String Clicked_str = "clicked";

    static private int pressed_type; // 0没有点击，1开始新游戏，2载入游戏

    public static void main(String[] args)
    {

        AppSettings settings = new AppSettings(true);
        settings.setTitle("Mini Minecraft");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        System.out.println(scrnsize.height);
        System.out.println(scrnsize.width);

        settings.setFullscreen(false);
        settings.setResolution(scrnsize.width, scrnsize.height);

        Main_function app = new Main_function();

        app.setSettings(settings);
        app.setShowSettings(false);

        StartFrame start = new StartFrame();
        start.test1();
        while(pressed_type == 0){
            System.out.print("");
        }
        System.out.println(2);
        if(pressed_type == 1)
           app.start();

    }

    public static void getpressed(int pressed){
        pressed_type = pressed;
        System.out.println(1);
    }

    //********************************************************************************************

    public final static String FORWARD = "forward";
    public final static String BACKWARD = "backward";
    public final static String LEFT = "left";
    public final static String RIGHT = "right";
    public final static String JUMP = "jump";
    public final static String ITEM1 = "item1";
    public final static String ITEM2 = "item2";
    public final static String ITEM3 = "item3";
    public final static String ITEM4 = "item4";
    public final static String CHANGE_GRAVITY = "change_gravity";

    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();

    private void initKeys() {
        inputManager.addMapping(LEFT, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(RIGHT, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addMapping(ITEM1, new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping(ITEM2, new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping(ITEM3, new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping(ITEM4, new KeyTrigger(KeyInput.KEY_4));

        inputManager.addMapping(CHANGE_GRAVITY, new KeyTrigger(KeyInput.KEY_TAB));

        inputManager.addListener(this, LEFT, RIGHT, FORWARD, BACKWARD, JUMP,ITEM1, ITEM2 ,ITEM3, ITEM4, CHANGE_GRAVITY);
    }

    private void initPlayer() {
        // 使用胶囊体作为玩家的碰撞形状
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.3f, 1.8f, 1);
        float stepHeight = 0.5f;// 玩家能直接登上多高的台阶？

        // 使用CharacterControl来控制玩家物体
        player = new CharacterControl(capsuleShape, stepHeight);
        player.setJumpSpeed(10);// 起跳速度
        player.setFallSpeed(55);// 坠落速度
        player.setGravity(9.8f * 3);// 重力加速度

        int x = 128, z = 128;
        int y;
        for(y = 0; map.Map[x][y][z] != null; y++);
        y += 3;
        player.setPhysicsLocation(new Vector3f(x, y, z));// 位置

        bulletAppState.getPhysicsSpace().add(player);
    }

    private void change_gravity_mode()
    {
        System.out.println(1111);
        if(player.getGravity() == 9.8f * 3)
            player.setGravity(1f * 3);
        else
            player.setGravity(9.8f * 3);
    }


    //**************************************************************************************

    @Override
    public void simpleInitApp()
    {
        bulletAppState = new BulletAppState(new Vector3f(0, 0, 0), new Vector3f(sx, sy, sz));//物理控制状态；
        stateManager.attach(bulletAppState);

        physicsSpace = bulletAppState.getPhysicsSpace();
        if(physicsSpace == null)
            System.err.println("xxxxx");

        map = new Map(sx, sy, sz, assetManager, physicsSpace);      //
        map.InitMap();


        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(100, -100, 100));

        AmbientLight ambient = new AmbientLight();//环境光；

        ColorRGBA lightColor = new ColorRGBA(); //设置颜色
        sun.setColor(lightColor.mult(0.8f));
        ambient.setColor(lightColor.mult(0.5f));

        rootNode.addLight(sun);
        rootNode.addLight(ambient);

        cam.setLocation(new Vector3f(64, 30, 64));
        cam.setRotation(new Quaternion(0.036418974f, 0.94834185f, -0.11822353f, 0.29213792f));

        precampos.set(cam.getLocation());
        curcampos.set(cam.getLocation());

        int chunk_x = (int)(curcampos.x / 16), chunk_y = (int)(curcampos.y / 16), chunk_z = (int)(curcampos.z / 16);

        for(int i = Math.max(0, chunk_x - chunk_sight); i <= Math.min(sx / 16 - 1, chunk_x + chunk_sight); i++) {
            for (int j = Math.max(0, chunk_y - chunk_sight); j <= Math.min(sy / 16 - 1, chunk_y + chunk_sight); j++) {
                for (int k = Math.max(0, chunk_z - chunk_sight); k <= Math.min(sz / 16 - 1, chunk_z + chunk_sight); k++) {
                    map.ChunkMap[i][j][k].addtonode(rootNode);
                }
            }
        }

        for(int i = Math.max(0, (int)curcampos.x - 3); i <= Math.min(sx - 1, (int)curcampos.x + 3); i++){
            for(int j = 0; j < sy; j++) {
                for(int k = Math.max(0, (int)curcampos.z - 3); k <= Math.min(sz - 1, (int)curcampos.z + 3); k++) {
                    if(map.Map[i][j][k] != null)
                        map.Map[i][j][k].addtophysicsSpace(physicsSpace);
                }
            }
        }

        pre_chunk_x = chunk_x;
        pre_chunk_y = chunk_y;
        pre_chunk_z = chunk_z;

        flyCam.setMoveSpeed(5);

        viewPort.setBackgroundColor(new ColorRGBA(0.6f, 0.7f, 0.9f, 1));

        cross = makeCross();

//        earthma.addout();
        pickuang = getPicture("Textures/Terrain/Terrain/out.png",86,80,-74,18);
        guiNode.attachChild(pickuang);
        pickuang = getPicture("Textures/Terrain/Terrain/out.png",86,80,6,18);
        guiNode.attachChild(pickuang);
        pickuang = getPicture("Textures/Terrain/Terrain/out.png",86,80,86,18);
        guiNode.attachChild(pickuang);
        pickuang = getPicture("Textures/Terrain/Terrain/out.png",86,80,180,18);
        guiNode.attachChild(pickuang);

       pickuang = getPicture("Textures/Terrain/Terrain/kuang.png",110,110,170,5);
       guiNode.attachChild(pickuang);

        pictool = getPicture("Textures/Terrain/Terrain/axe.png",80,80,180,20);
        guiNode.attachChild(pictool);

        pictool = getPicture("Textures/Terrain/Terrain/earth.png",85,75,-80,20);
        guiNode.attachChild(pictool);

        pictool = getPicture("Textures/Terrain/Terrain/rock.png",60,70,20,20);
        guiNode.attachChild(pictool);

        pictool = getPicture("Textures/Terrain/Terrain/wood.png",60,75,100,20);
        guiNode.attachChild(pictool);

        numsetinit();
        inputManager.addMapping(Clicked_str, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, Clicked_str);

        //*********
        initKeys();
        initPlayer();
        //*********

        monkey = new Monkey[monkey_num];
        for(int i = 0; i < monkey_num; i++) {
            Vector3f newpos = new Vector3f(108 + 2 * i, 6, 108 + 2 * i);
            while(map.Map[(int)newpos.x][(int)newpos.y][(int)newpos.z] == null)
                newpos.y -= 1;
            while(map.Map[(int)newpos.x][(int)newpos.y][(int)newpos.z] != null)
                newpos.y += 1;
            newpos.y -= 0.5;
            monkey[i] = new Monkey(newpos, assetManager, 30 + i);
            rootNode.attachChild(monkey[i].spatial);
        }
    }

    private void numsetinit(){
        int i = 0;
        float x = 0.5f * (cam.getWidth());
        float y = 0;

        String txtB = String.valueOf(i); BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        txtearth = new BitmapText(fnt, false);
        txtearth.setBox(new Rectangle(x - 20, y+20 , x , y));
        txtearth.setSize(fnt.getPreferredSize() * 0.75f);
        txtearth.setText(txtB);
        txtearth.setLocalTranslation(0, txtearth.getHeight(), 0);
        guiNode.attachChild(txtearth);

        txtwood = new BitmapText(fnt, false);
        txtwood.setBox(new Rectangle(x + 150, y + 20, x , y));
        txtwood.setSize(fnt.getPreferredSize() * 0.75f);
        txtwood.setText(txtB);
        txtwood.setLocalTranslation(0, txtwood.getHeight(), 0);
        guiNode.attachChild(txtwood);

        txtrock = new BitmapText(fnt, false);
        txtrock.setBox(new Rectangle(x + 70, y+20, x , y));
        txtrock.setSize(fnt.getPreferredSize() * 0.75f);
        txtrock.setText(txtB);
        txtrock.setLocalTranslation(0, txtrock.getHeight(), 0);
        guiNode.attachChild(txtrock);

    }
    private Spatial getPicture(String s,int width_2,int height_2,int xx,int yy){
        float width = cam.getWidth();
        float height = cam.getHeight();
        Quad quad = new Quad(width_2,height_2);


        float x = 0.5f * (cam.getWidth())+xx;
        float y = 0 + yy;


        Geometry geom = new Geometry("Picture",quad);

        geom.setLocalTranslation(x, y, 0);
        Texture tex = assetManager.loadTexture(s);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTexture("ColorMap", tex);

        geom.setMaterial(mat);

        return geom;
    }
    @Override
    public void simpleUpdate(float tpf)
    {
        curcampos.set(cam.getLocation());

        int chunk_x = (int)(curcampos.x / 16), chunk_y = (int)(curcampos.y / 16), chunk_z = (int)(curcampos.z / 16);

        if(Math.abs(curcampos.x - precampos.x) >= 8 || Math.abs(curcampos.y - precampos.y) >= 8 || Math.abs(curcampos.z - precampos.z) >= 8) {

            for(int i = Math.max(0, chunk_x - chunk_sight); i <= Math.min(sx / 16 - 1, chunk_x + chunk_sight); i++)
                for(int j = Math.max(0, chunk_y - chunk_sight); j <= Math.min(sy / 16 - 1, chunk_y + chunk_sight); j++)
                    for(int k = Math.max(0, chunk_z - chunk_sight); k <= Math.min(sz / 16 - 1, chunk_z + chunk_sight); k++)
                        if(!map.ChunkMap[i][j][k].added)
                            map.ChunkMap[i][j][k].addtonode(rootNode);

            for(int i = 0; i < sx / 16; i++)
                for(int j = 0; j < sy / 16; j++)
                    for(int k = 0; k < sz / 16; k++)
                        if(map.ChunkMap[i][j][k].added && (Math.abs(i - chunk_x) > chunk_sight || Math.abs(j - chunk_y) > chunk_sight || Math.abs(k - chunk_z) > chunk_sight)) {
                            map.ChunkMap[i][j][k].detachfromnode(rootNode);
                            System.out.println("Detached");
                        }

            precampos.set(curcampos);
        }

        for(int i = Math.max(0, (int)curcampos.x - 3); i <= Math.min(sx - 1, (int)curcampos.x + 3); i++){
            for(int j = 0; j < sy; j++) {
                for(int k = Math.max(0, (int)curcampos.z - 3); k <= Math.min(sz - 1, (int)curcampos.z + 3); k++) {
                    if(map.Map[i][j][k] != null && !map.Map[i][j][k].padded)
                        map.Map[i][j][k].addtophysicsSpace(physicsSpace);
                }
            }
        }

        for(int i = Math.max(0, (int)curcampos.x - 5); i <= Math.min(sx - 1, (int)curcampos.x + 5); i++){
            for(int k = Math.max(0, (int)curcampos.z - 5); k <= Math.min(sz - 1, (int)curcampos.z + 5); k++) {
                if((Math.abs(i - (int)curcampos.x) > 3 || Math.abs(k - (int)curcampos.z) > 3))
                    for(int j = 0; j < sy; j++)
                        if(map.Map[i][j][k] != null && map.Map[i][j][k].padded)
                           map.Map[i][j][k].removefromphysicsSpace(physicsSpace);
            }
        }

        //***********

        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);

        // 计算运动方向
        boolean changed = false;
        if (left) {
            walkDirection.addLocal(camLeft);
            changed = true;
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
            changed = true;
        }
        if (up) {
            walkDirection.addLocal(camDir);
            changed = true;
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
            changed = true;
        }
        if (changed) {
            walkDirection.y = 0;// 将行走速度的方向限制在水平面上。
            walkDirection.normalizeLocal();// 单位化
            walkDirection.multLocal(0.2f);// 改变速率
        }

        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());

        //***********

        for(int i = 0; i < monkey_num; i++) {
            if (monkey[i].action_time > 0) {
                monkey[i].action_time--;
                if (monkey[i].isWalking) {
                    Vector3f pos = monkey[i].getPos();
                    Vector3f direction = monkey[i].getDirection();

                    Vector3f newpos = new Vector3f(pos.x + monkey[i].speed * tpf * direction.x, pos.y, pos.z + monkey[i].speed * tpf * direction.z);

                    while(map.Map[Math.round(newpos.x)][(int)(newpos.y + 0.5)][Math.round(newpos.z)] == null)
                        newpos.y -= 1;
                    while(map.Map[(int)newpos.x][(int)(newpos.y + 0.5)][(int)newpos.z] != null)
                        newpos.y += 1;

                    monkey[i].setPos(newpos);
                }
            } else {
                monkey[i].set_action_time();
                monkey[i].animalAI();
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf)
    {
        if(isPressed)
        {
            if(name.equals(Clicked_str))
               clicked();
        }
        if (LEFT.equals(name)) {
            left = isPressed;
        } else if (RIGHT.equals(name)) {
            right = isPressed;
        } else if (FORWARD.equals(name)) {
            up = isPressed;
        } else if (BACKWARD.equals(name)) {
            down = isPressed;
        } else if (JUMP.equals(name) && isPressed) {
            player.jump();
        }
        else if(ITEM1.equals(name)&&isPressed){
            Material_id = 1;
            guiNode.detachChild(pickuang);
            pickuang = getPicture("Textures/Terrain/Terrain/kuang.png",110,110,-90,5);
            guiNode.attachChild(pickuang);
        }
        else if(ITEM2.equals(name)&&isPressed){
            Material_id = 2;
            guiNode.detachChild(pickuang);
            pickuang = getPicture("Textures/Terrain/Terrain/kuang.png",110,110,-8,3);
            guiNode.attachChild(pickuang);
        }
        else if(ITEM3.equals(name)&&isPressed){
            Material_id = 3;

            guiNode.detachChild(pickuang);
            pickuang = getPicture("Textures/Terrain/Terrain/kuang.png",110,110,74,0);
            guiNode.attachChild(pickuang);
        }
        else if(ITEM4.equals(name)&&isPressed){
            Material_id = 4;

            guiNode.detachChild(pickuang);
            pickuang = getPicture("Textures/Terrain/Terrain/kuang.png",110,110,170,5);
            guiNode.attachChild(pickuang);
        }
        else if(CHANGE_GRAVITY.equals(name) && isPressed)
        {
            change_gravity_mode();
        }
    }

    private Spatial makeCross()
    {
        BitmapText text = guiFont.createLabel("+");
        text.setColor(ColorRGBA.White);
        text.setSize(40f);

        float x = (cam.getWidth() - text.getLineWidth()) * 0.5f;
        float y = (cam.getHeight() + text.getLineHeight()) * 0.5f;
        text.setLocalTranslation(x, y, 0);

        guiNode.attachChild(text);

        return text;
    }

    private Ray updateRay()
    {
        ray.setOrigin(cam.getLocation());

        ray.setDirection(cam.getDirection());

        return ray;
    }

    private void changeNum(int id){
        Item now;
        if(id == 1){
            now = earthma;
            guiNode.detachChild(txtearth);
            int i = now.num;
            float x = 0.5f * (cam.getWidth());
            float y = 0;

            String txtB = String.valueOf(i);
            BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
            txtearth = new BitmapText(fnt, false);
            if(i < 10)txtearth.setBox(new Rectangle(x - 10, y+20 , x , y));
            else txtearth.setBox(new Rectangle(x - 12, y+20 , x , y));
            txtearth.setSize(fnt.getPreferredSize() * 0.75f);
            txtearth.setText(txtB);
            txtearth.setLocalTranslation(0, txtearth.getHeight(), 0);
            guiNode.attachChild(txtearth);
        }
        else if(id == 2){
            now = stonema;
            guiNode.detachChild(txtrock);
            int i = now.num;
            float x = 0.5f * (cam.getWidth());
            float y = 0;

            String txtB = String.valueOf(i);
            BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
            txtrock = new BitmapText(fnt, false);
            if(i < 10)txtrock.setBox(new Rectangle(x + 70, y+20 , x , y));
            else txtrock.setBox(new Rectangle(x + 64, y+20 , x , y));
            txtrock.setSize(fnt.getPreferredSize() * 0.75f);
            txtrock.setText(txtB);
            txtrock.setLocalTranslation(0, txtrock.getHeight(), 0);
            guiNode.attachChild(txtrock);
        }
        else if(id == 3){
            now = woodma;
            guiNode.detachChild(txtwood);
            int i = now.num;
            float x = 0.5f * (cam.getWidth());
            float y = 0;

            String txtB = String.valueOf(i);
            BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
            txtwood = new BitmapText(fnt, false);
            if(i < 10)txtwood.setBox(new Rectangle(x +150, y+20 , x , y));
            else txtwood.setBox(new Rectangle(x + 146, y+20 , x , y));
            txtwood.setSize(fnt.getPreferredSize() * 0.75f);
            txtwood.setText(txtB);
            txtwood.setLocalTranslation(0, txtwood.getHeight(), 0);
            guiNode.attachChild(txtwood);
        }
    }
    private void AddAnBlock(CollisionResults results){

    }
    private void clicked()
    {
        Ray ray = updateRay();
        ray.setLimit(ray_length);

        CollisionResults results = new CollisionResults();

        String ss = new String();
        rootNode.collideWith(ray, results);

        if(Material_id == 1){
            p = earthma;
        }
        else if(Material_id == 2){
            p = stonema;
        }
        else if(Material_id == 3){
            p = woodma;
        }
        else if(Material_id == 4){
            p = axetool;
        }
        System.out.println(Material_id);
        int nowtool = p.gettool();
        if(results.size() > 0) {
            Vector3f closest = results.getClosestCollision().getContactPoint();
            Geometry collisiongeom = results.getClosestCollision().getGeometry();
            String collisiongeomname = collisiongeom.getName();

            if (collisiongeomname.contains("block")) {
                int id_index = collisiongeomname.indexOf(":");

                int id = Integer.parseInt(collisiongeomname.substring(id_index + 1));

                Vector3f curpos = cam.getLocation();
                for(int i = (int)Math.max(0, curpos.x - ray_length - 1); i <= (int)Math.max(sx - 1, curpos.x + ray_length + 1); i++) {
                    for (int j = (int) Math.max(0, curpos.y - ray_length - 1); j <= (int) Math.max(sy - 1, curpos.y + ray_length + 1); j++) {
                        for (int k = (int) Math.max(0, curpos.z - ray_length - 1); k <= (int) Math.max(sz - 1, curpos.z + ray_length + 1); k++) {
                            if (map.Map[i][j][k] != null && map.Map[i][j][k].block_id == id) {
                                if(nowtool == 0){
                                    if(p.num <= 0)continue;
                                    Vector3f camerad = cam.getDirection();
                                    int ii = (int)Math.round(closest.x - 0.01 * camerad.x);
                                    int jj = (int)Math.round(closest.y -  0.01 * camerad.y);
                                    int kk = (int)Math.round(closest.z - 0.01 * camerad.z);
                                    int itemid = p.getid();
                                    p.num--;
                                    changeNum(p.getid());
                                    if(itemid == 1)
                                        map.Map[ii][jj][kk] = new Block_earth(new Vector3f(ii,jj,kk), assetManager);
                                    else if(itemid == 2)
                                        map.Map[ii][jj][kk] = new Block_rock(new Vector3f(ii,jj,kk), assetManager);
                                    else if(itemid == 3)
                                        map.Map[ii][jj][kk] = new Block_wood(new Vector3f(ii,jj,kk), assetManager);
                                    map.ChunkMap[ii / 16][jj / 16][kk / 16].blk[ii - 16 * (ii / 16)][jj - 16 * (jj / 16)][kk - 16 * (kk / 16)] = map.Map[ii][jj][kk];
                                    map.Map[ii][jj][kk].addtonode(map.ChunkMap[ii / 16][jj / 16][kk / 16].chunknode);

                                    continue;
                                }
                                else if(nowtool == 1)
                                    ss = map.Map[i][j][k].block_type;
                                    if(ss.equals("earth")||ss.equals("earthwithgrass")||ss.equals("earthwithsnow")) p = earthma;
                                    else if(ss.equals("wood"))p = woodma;
                                    else if(ss.equals("rock")) p = stonema;
                                    flagid = map.Map[i][j][k].onBlockClicked(map,p,flag,flagid);
                                    if(flagid != 0){
                                        changeNum(flagid);
                                        flag = 0;
                                    }
                            }
                        }
                    }
                }
            }
            else if (collisiongeomname.contains("plant")) {
                int id_index = collisiongeomname.indexOf(":");

                int id = Integer.parseInt(collisiongeomname.substring(id_index + 1));

                Vector3f curpos = cam.getLocation();
                for(int i = (int)Math.max(0, curpos.x - ray_length - 1); i <= (int)Math.max(sx - 1, curpos.x + ray_length + 1); i++) {
                    for (int j = (int) Math.max(0, curpos.y - ray_length - 1); j <= (int) Math.max(sy - 1, curpos.y + ray_length + 1); j++) {
                        for (int k = (int) Math.max(0, curpos.z - ray_length - 1); k <= (int) Math.max(sz - 1, curpos.z + ray_length + 1); k++) {
                            if (map.PlantMap[i][j][k] != null && map.PlantMap[i][j][k].plant_id == id) {
                                map.PlantMap[i][j][k].PlantClicked(map);
                            }
                        }
                    }
                }
            }
        }
    }
}
