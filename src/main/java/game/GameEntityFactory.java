package game;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppHeight;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAppWidth;

public class GameEntityFactory implements EntityFactory {


    @Spawns("background")
    public Entity newBackground(SpawnData data){
        return FXGL.entityBuilder(data)
                .view(new Rectangle(getAppWidth(), getAppHeight()))
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .with(new PlayerComponent())
                .viewWithBBox("player.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }
    @Spawns("enemy")
    public Entity newEnemy(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .with(new PlayerComponent())
                .viewWithBBox("player.png")
                .with(new CollidableComponent(true)) 
                .buildAndAttach();
    }

    @Spawns("powerUp")
    public Entity newPowerUp(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.POWERUP)
                .viewWithBBox("powerUp.png")
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data){
        Point2D dir = data.get("dir");

        return FXGL.entityBuilder(data)
                .type(EntityType.BULLET)
                .viewWithBBox(new Rectangle(60  , 20, Color.BLUE))
                .with(new ProjectileComponent(dir, 600 ))
                .with(new CollidableComponent(true))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .viewWithBBox("wall.png")
                .with(new CollidableComponent(true))
                .build();
    }
}
