package game;


import com.almasb.fxgl.animation.AnimationBuilder;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class PlayerComponent extends Component{
    private boolean isPowerUp = false;



    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(entity.getCenter());
    }

    public void rotateLeft(){
        entity.rotateBy(-5);
    }

    public void rotateRight(){
        entity.rotateBy(5);
    }

    public void move(){
        Vec2 dir = Vec2.fromAngle(entity.getRotation() - 90)
                .mulLocal(6);
        entity.translate(dir);
    }

    public void shoot() {
//        Zelfde angle als bij move, deze wordt in een 2D point omgezet en dan meegeven aan de spawnData
        Vec2 dir = Vec2.fromAngle(entity.getRotation() - 90);
        spawn("bullet", new SpawnData(entity.getX(), entity.getY()).put("dir", dir.toPoint2D()));
        if(isPowerUp) {
            for (int i = 0; i < 5; i++) {
                getGameTimer().runAtInterval(() -> {
                    spawn("bullet", new SpawnData(entity.getX(), entity.getY()).put("dir", dir.toPoint2D()));
                }, Duration.seconds(1/2));
            }
        }
    }


    public void shootBeam(){

    }

    public boolean isPowerUp(){
        return isPowerUp;
    }

    public  void setPowerUp(boolean b){
        isPowerUp = b;
    }
}
