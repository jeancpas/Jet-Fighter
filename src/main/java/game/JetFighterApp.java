package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class JetFighterApp extends GameApplication {
    private Entity player;
    private final int TILESIZE = 60;
    private final int startY = 50;
    private final int offset = 20;
    @Override
    protected void initSettings(GameSettings gameSettings) {
        //Insert game settings on init
        gameSettings.setWidth(800);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Jet Fighter App");
        gameSettings.setVersion("0.1");
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu(){
                return new  JetFighterMainMenu();
            }
        });
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new GameEntityFactory());
        getSettings().setGlobalSoundVolume(0.1);

        spawn("background");
        //add offset playersize
        player = spawn("player", getAppWidth() /2 , getAppHeight() /2 );

        spawn("wall", player.getX() + 100, player.getY());

    }

    @Override
    protected void onUpdate(double tpf) {
        //        Get all variables
        PropertyMap state = FXGL.getWorldProperties();
        //        Spawn powerups randomly
        run(() -> {
//            Check if powerup is al gespawned en player is niet poweredup
            if(state.getInt("powerUpSpawned?") == 0 &&
                !player.getComponent(PlayerComponent.class).isPoweredUp()) {
                  Entity powerUp = spawn("powerUp",
//                        random place ver van de speler
                        FXGLMath.random(TILESIZE, getAppWidth() - TILESIZE), FXGLMath.random(TILESIZE, getAppHeight() - TILESIZE));
                state.setValue("powerUpSpawned?", 1);
            }
            return null;
        }, Duration.seconds(2));
        getGameTimer().runAtInterval (() ->{
            state.increment("timer", -1);
        },Duration.seconds(1));



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
        //Test
        FXGL.onKey(KeyCode.H, () ->{
            showMessage("Hello");
        });
    }

    @Override
    protected void initUI() {
        Text timerText = getUIFactoryService().newText("Timer");
        timerText.setTranslateX(0); // x = 50
        timerText.setTranslateY(startY); // y = 100
        timerText.textProperty().bind(getip("timer").asString("Time left: %d "));


        Text livesText = getUIFactoryService().newText("");
        livesText.setTranslateX(0); // x = 50
        livesText.setTranslateY(startY + offset); // y = 100
        livesText.setFill(Color.RED);
        livesText.textProperty().bind(getip("lives").asString("Lives: %d"));


        Text scoreText = getUIFactoryService().newText("");
        scoreText.setTranslateX(0); // x = 50
        scoreText.setTranslateY(startY + offset*2); // y = 100
        scoreText.textProperty().bind(getip("score").asString("Score: %d"));

        FXGL.getGameScene().addUINode(timerText); // add to the scene graph
        FXGL.getGameScene().addUINode(livesText); // add to the scene graph
        FXGL.getGameScene().addUINode(scoreText); // add to the scene graph

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("timer", 999);
        vars.put("lives", 4);
        vars.put("powerUpSpawned?", 0);
        vars.put("score", 0);
    }

    @Override
    protected void initPhysics() {
        PropertyMap state  = FXGL.getWorldProperties();

        onCollisionBegin(EntityType.PLAYER, EntityType.POWERUP, (player1, powerup) ->{
            powerup.removeFromWorld();
            play("machinegun.wav");
            player.getComponent(PlayerComponent.class).startPowerUpTimer();
            state.setValue("powerUpSpawned?", 0);
            return null;
        });

        onCollisionBegin(EntityType.PLAYER, EntityType.WALL, (player1, wall) ->{
            play("oof.wav");
            player.setPosition(getAppWidth() /2 , getAppHeight() /2 );
            state.increment("lives", -1);
            return null;
        });

        onCollision(EntityType.BULLET, EntityType.WALL, (bullet, wall) ->{
            wall.removeFromWorld();
            bullet.removeFromWorld();
            state.increment("score", 100);
            return null;
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
