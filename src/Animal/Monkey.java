package Animal;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

public class Monkey {
    Vector3f pos;
    Vector3f direction;
    int animalid = 0;
    int animalnum = 0;
    int stopnum = 0;
    int walknum = 0;
    public boolean isWalking;
    public int action_time;
    private int per_action_time = 30;
    public static float speed = 0.5f;

    public Spatial spatial;

    private AnimControl animControl;
    private AnimChannel animChannel;


    int lastmove = 0;

    /**
     * 动画事件监听器
     */
    private AnimEventListener animEventListener = new AnimEventListener() {
        @Override
        public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
            if ("JumpStart".equals(animName)) {
                // “起跳”动作结束后，紧接着播放“着地”动画。
                channel.setAnim("JumpEnd");
                channel.setLoopMode(LoopMode.DontLoop);
                channel.setSpeed(1.5f);

            } else if ("JumpEnd".equals(animName)) {
                // “着地”后，根据按键状态来播放“行走”或“闲置”动画。
                if (isWalking) {
                    channel.setAnim("Walk");
                    channel.setLoopMode(LoopMode.Loop);
                } else {
                    channel.setAnim("Idle");
                    channel.setLoopMode(LoopMode.Loop);
                }
            }
        }

        @Override
        public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        }
    };

    public Monkey(Vector3f p, AssetManager assetManager, int per_action_time){
        pos = p;
        spatial = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        spatial.move(pos);

        // 动画控制器
        animControl = spatial.getControl(AnimControl.class);
        animControl.addListener(animEventListener);

        animChannel = animControl.createChannel();
        // 播放“闲置”动画
        animChannel.setAnim("Idle");
        animalid = animalnum++;

        isWalking = false;

        this.per_action_time = per_action_time;

        action_time = per_action_time;

        direction = new Vector3f(0, 0, 0);

    }
    public void animalAI(){
        int i = animalrand();
        if(i == 1){
            animChannel.setAnim("Walk");
            animChannel.setLoopMode(LoopMode.Loop);// 循环播放
        }
        else if(i == 2){
            spatial.rotate(0,30,0);

        }
        else if(i == 3){
            animChannel.setAnim("Idle");
            animChannel.setLoopMode(LoopMode.Loop);
        }
        else{
            animChannel.setAnim("JumpStart");
            animChannel.setLoopMode(LoopMode.DontLoop);
            animChannel.setSpeed(1.5f);
        }

    }
    public int animalrand(){
        double rand = Math.random();
        if(rand <= 0.3){    //前进
            if(walknum >= 5){
                walknum = 0;
                isWalking = false;
                return 3;
            }
            walknum++;
            lastmove = 1;
            isWalking = true;
            return 1;
        }
        if(rand <= 0.6){
            if(lastmove == 2){
                lastmove = 1;
                isWalking = true;
                return 1;
            }
            else {
                lastmove = 2;
                isWalking = false;
                return 2;  //转身；
            }
        }
        if(rand <= 0.9){
            if(stopnum >= 10){
                stopnum = 0;
                if(rand <= 0.7) {
                    isWalking = false;
                    return 2;
                }
                else{
                    isWalking = true;
                    return 1;
                }
            }
            stopnum++;
            isWalking = false;
            return 3;       //停止
        }
        isWalking = false;
        return 4;       //跳跃

    }

    int getAnimalid(){
        return animalid;
    }

    public void set_action_time()
    {
        action_time = per_action_time;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
        spatial.setLocalTranslation(pos);
    }

    public Vector3f getDirection() {
        return spatial.getLocalRotation().getRotationColumn(2);
    }
}
