package game;


import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class PlayerComponent extends Component{
    private boolean isPoweredUp = false;



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
                .mulLocal(4);
        entity.translate(dir);
    }
//    https://github.com/AlmasB/FXGLGames/blob/5f99eee3c03deac2501cb813cabbdc2c9f692ebc/Asteroids/src/main/java/com/almasb/fxglgames/PlayerComponent.java
    public void shoot() {
        //        Zelfde angle als bij move, deze wordt in een 2D point omgezet en dan meegeven aan de spawnData
        Vec2 dir = Vec2.fromAngle(entity.getRotation() - 90);
        if(!isPoweredUp()) {
            spawn("bullet", new SpawnData(entity.getCenter()).put("dir", dir.toPoint2D()));
        }
        else{
            System.out.println("PowerUp!");
            spawn("bullet", new SpawnData(entity.getCenter()).put("dir", dir.toPoint2D()));
            getGameTimer().runOnceAfter( () ->{
                spawn("bullet", new SpawnData(entity.getCenter()).put("dir", dir.toPoint2D()));
            }, Duration.seconds(0.5));

        }
    }

    public void startPowerUpTimer(){
        this.isPoweredUp = true;
        getGameTimer().runOnceAfter(() ->{
            this.isPoweredUp = false;
        }, Duration.seconds(5));
    }

    public boolean isPoweredUp(){
        return isPoweredUp;
    }

}
