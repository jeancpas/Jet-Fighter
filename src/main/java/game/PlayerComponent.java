package game;


import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;


public class PlayerComponent extends Component{

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
        Vec2 dir = Vec2.fromAngle(entity.getRotation() - 90);
        spawn("bullet", new SpawnData(entity.getX(), entity.getY()).put("dir", dir.toPoint2D()));
    }
}
