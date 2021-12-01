package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
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
        getSettings().setGlobalSoundVolume(0.1);


        spawn("background");
        //add offset playersize
        player = spawn("player", getAppWidth() /2 , getAppHeight() /2 );

        spawn("wall", player.getX() + 60, player.getY());


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
    protected void onUpdate(double tpf) {
        //        Get all variables
        PropertyMap state = FXGL.getWorldProperties();
        //        Spawn powerups randomly
        run( () -> {
            if(state.getInt("powerUpSpawned?") == 0 &&
                !player.getComponent(PlayerComponent.class).isPowederedUp()) {
                  Entity powerUp = spawn("powerUp",
//                        random place
                        FXGLMath.random(TILESIZE, getAppWidth() - TILESIZE) + player.getX() + 50, FXGLMath.random(TILESIZE, getAppHeight() - TILESIZE));
                state.setValue("powerUpSpawned?", 1);
//                Timer to remove powerup after 3 seconds B
//                Bugged
//                runOnce(() -> {
//                    powerUp.removeFromWorld();
//                    state.setValue("powerUpSpawned?", 0);
//                    return null;
//                }, Duration.seconds(3));
            }
            return null;
        }, Duration.seconds(2));
//        Timer on powerup
        if(player.getComponent(PlayerComponent.class).isPowederedUp())
        run(() -> {
            player.getComponent(PlayerComponent.class).setPowederedUp(false);
            return null;
        }, Duration.seconds(5));

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
//            System.out.println(player.angleProperty());
            showMessage("Hello");
        });
    }

    @Override
    protected void initUI() {
        Text timerText = new Text("Timer");
        timerText.setTranslateX(0); // x = 50
        timerText.setTranslateY(50); // y = 100
        timerText.setFill(Color.WHITE);
        timerText.textProperty().bind(getip("timer").asString());
        FXGL.getGameScene().addUINode(timerText); // add to the scene graph
        Text livesText = new Text("");
        livesText.setTranslateX(0); // x = 50
        livesText.setTranslateY(60); // y = 100
        livesText.setFill(Color.RED);
        livesText.textProperty().bind(getip("lives").asString());
        FXGL.getGameScene().addUINode(livesText); // add to the scene graph

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
            player.getComponent(PlayerComponent.class).setPowederedUp(true);
            state.setValue("powerUpSpawned?", 0);
            return null;
        });

        onCollisionBegin(EntityType.PLAYER, EntityType.WALL, (player1, wall) ->{
            play("oof.wav");
            player.setPosition(getAppWidth() /2 , getAppHeight() /2 );
            state.increment("lives", -1);
            return null;
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
