package game;


import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class PlayerComponent extends Component{
    private boolean IsPowederedUp = false;



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
        spawn("bullet", new SpawnData(entity.getCenter()).put("dir", dir.toPoint2D()));
        if(IsPowederedUp) {
            showMessage("Pew pew!");
        }
    }

    public boolean isPowederedUp(){
        return IsPowederedUp;
    }

    public  void setPowederedUp(boolean b){
        IsPowederedUp = b;
    }
}
