package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class JetFighterApp extends GameApplication {
    private Entity player;
    private final int TILESIZE = 60;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        //Insert game settings on init
        gameSettings.setWidth(800);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Jet Fighter App");
        gameSettings.setVersion("0.1");
//        TODO set true
        gameSettings.setMainMenuEnabled(false);
        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu(){
                return new JetFighterMainMenu();
            }
        });
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameEntityFactory());
//        Get all variables
        PropertyMap state = FXGL.getWorldProperties();

        spawn("background");
        //add offset playersize
        player = spawn("player",getAppWidth() /2 , getAppHeight() / 2);
//        Spawn powerups randomly
        run( () -> {
            if(state.getInt("powerup?") == 0) {
                spawn("powerUp",
//                        random place
                        FXGLMath.random(TILESIZE, getAppWidth() - TILESIZE), FXGLMath.random(TILESIZE, getAppHeight() - TILESIZE));
                state.setValue("powerup?", 1);
            }
            return null;
        }, Duration.seconds(2));

        //Spawning the player in the game in initGame
        /*
        Manier om Entities te spawnen zonder een builder te gebruiken
        player = FXGL.entityBuilder()
                .at(300, 300)
                .view(new Rectangle(25, 25, Color.GREEN))
                .buildAndAttach();
         */
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.A, () -> {
            player.getComponent(PlayerComponent.class).rotateLeft();
        });
        FXGL.onKey(KeyCode.D, () -> {
            player.getComponent(PlayerComponent.class).rotateRight();
        });
        FXGL.onKey(KeyCode.W, () -> {
            player.getComponent(PlayerComponent.class).move();
        });

        FXGL.onKeyDown(KeyCode.SPACE, () ->{
            player.getComponent(PlayerComponent.class).shoot();
        });
        /*
            public void move() {
        Vec2 dir = Vec2.fromAngle(entity.getRotation() - 90)
                .mulLocal(4);
        entity.translate(dir);
    }

    STOLEN FROM https://github.com/AlmasB/FXGLGames/blob/master/Asteroids/src/main/java/com/almasb/fxglgames/PlayerComponent.java#L16
         */

        //Test
        FXGL.onKey(KeyCode.H, () ->{
//            System.out.println(player.angleProperty());
            showMessage("Hello");
        });
        /*
        LONG WAY
        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                player.translateX(5);
            }
        }, KeyCode.D);

         */
    }

    @Override
    protected void initUI() {
        Text timerText = new Text("Timer: ");
        timerText.setTranslateX(0); // x = 50
        timerText.setTranslateY(50); // y = 100
        timerText.textProperty().bind(getip("timer").asString());

        FXGL.getGameScene().addUINode(timerText); // add to the scene graph
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("timer", 999);
        vars.put("powerup?", 0);
    }

    @Override
    protected void initPhysics() {
        PropertyMap state  = FXGL.getWorldProperties();

        onCollisionBegin(EntityType.PLAYER, EntityType.POWERUP, (player1, powerup) ->{
            powerup.removeFromWorld();
            state.setValue("powerup?", 0);
            return null;
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
