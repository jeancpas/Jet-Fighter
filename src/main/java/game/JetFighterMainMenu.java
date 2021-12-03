package game;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

public class JetFighterMainMenu extends FXGLMenu {
    public JetFighterMainMenu() {
        super(MenuType.MAIN_MENU);
        var btnStart = new JetFighterButton("Start new game", this::fireNewGame);
        var btnExit = new JetFighterButton("Exit", this::fireExit);
        var gameTitle = FXGL.getUIFactoryService().newText("Jet Fighter");

        btnStart.setTranslateX(FXGL.getAppWidth() / 2 - 200 / 2);
        btnStart.setTranslateY(FXGL.getAppHeight() / 2 - 40 / 2);

        btnExit.setTranslateX(FXGL.getAppWidth() / 2 - 200 / 2);
        btnExit.setTranslateY(FXGL.getAppHeight() / 2 - 40 / 2 + 40);

        gameTitle.fillProperty().set(Color.BLUE);
        gameTitle.setTranslateX(FXGL.getAppWidth() / 2 - 200 / 2);
        gameTitle.setTranslateY(FXGL.getAppHeight() /3);
        getContentRoot().getChildren().add(btnStart);
        getContentRoot().getChildren().add(btnExit);
        getContentRoot().getChildren().add(gameTitle);


    }

    private static class JetFighterButton extends StackPane {
        public JetFighterButton(String name, Runnable action) {

            var bg = new Rectangle(200, 40);
            bg.setStroke(Color.WHITE);

            var text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 18);

            bg.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );

            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.BLACK).otherwise(Color.WHITE)
            );

            setOnMouseClicked(e -> action.run());

            getChildren().addAll(bg, text);
        }
    }
    }
