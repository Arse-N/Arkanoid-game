package com.polytech.arkanoid;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ArkanoidController implements Initializable {

    @FXML
    private Pane scene;

    @FXML
    private Label text1;

    @FXML
    private Label text2;

    @FXML
    private Label text4;

    @FXML
    private Label text5;

    @FXML
    private Label text6;

    @FXML
    private Label highScore;

    @FXML
    private Circle circle;

    @FXML
    private Rectangle paddle;

    @FXML
    private Rectangle bottomZone;

//    @FXML
//    private Rectangle body;

    @FXML
    private Button startButton;

    @FXML
    private Button easy;

    @FXML
    private Button normal;

    @FXML
    private Button hard;

    private final int paddleStartSize = 200;

    Robot robot = new Robot();

    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double dX = -1;
    double dY = -3;
    long wins = 0;
    long loses = 0;
    long points = 0;
    long hPoints = 0;
    double height = 0;
    String mode = "";
    Double d = 0.0;
    Timeline timeline = setDuration(10.0);
//    long record = 0;

    //1 Frame evey 10 millis, which means 100 FPS
    private Timeline setDuration(Double d){
       return new Timeline(new KeyFrame(Duration.millis(d), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                movePaddle();
                checkCollisionPaddle(paddle);
                circle.setLayoutX(circle.getLayoutX() + dX);
                circle.setLayoutY(circle.getLayoutY() + dY);

                if (!bricks.isEmpty()) {
                    bricks.removeIf(brick -> checkCollisionBrick(brick));
                } else {
                    timeline.stop();
                    circle.setVisible(false);
                    paddle.setVisible(false);
                    text1.setVisible(true);
                    startButton.setVisible(true);
                    startButton.setText("Restart");
                    wins++;
                    text5.setText("Wins: " + wins);
                    paddle.setWidth(paddleStartSize);
                    if(points > hPoints){
                        highScore.setText("High:  " + points);
                        hPoints = points;
                    }
                    points = 0;
                    text4.setText("Score: " + points);
                }
                checkCollisionScene(scene);
                checkCollisionBottomZone();
            }
        }));
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paddle.setWidth(paddleStartSize);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void init(Double d) {
        timeline = setDuration(d);
        paddle.setWidth(paddleStartSize);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    void startGameButtonAction(ActionEvent event) {
        startButton.setVisible(false);
        text1.setVisible(false);
        text2.setVisible(false);
        easy.setVisible(true);
        normal.setVisible(true);
        hard.setVisible(true);
    }

    @FXML
    void easyModeButtonAction(ActionEvent event) {
        easy.setVisible(false);
        normal.setVisible(false);
        hard.setVisible(false);
        paddle.setVisible(true);
        circle.setVisible(true);
        dX = -1;
        dY = -3;
        height = 100;
        mode = "easy";
        init(10.0);
        startGame();
    }

    @FXML
    void normalModeButtonAction(ActionEvent event) {
        easy.setVisible(false);
        normal.setVisible(false);
        hard.setVisible(false);
        text1.setVisible(false);
        text2.setVisible(false);
        paddle.setVisible(true);
        circle.setVisible(true);
        dX = -1;
        dY = -3;
        height = 160;
        mode = "normal";
        circle.setLayoutX(300);
        circle.setLayoutY(300);
        init(10.0);
        startGame();
    }

    @FXML
    void hardModeButtonAction(ActionEvent event) {
        easy.setVisible(false);
        normal.setVisible(false);
        hard.setVisible(false);
        text1.setVisible(false);
        text2.setVisible(false);
        paddle.setVisible(true);
        circle.setVisible(true);
        dX = -1;
        dY = -3;
        height = 160;
        mode = "hard";
        circle.setLayoutX(300);
        circle.setLayoutY(300);
        init(5.0);
        startGame();
    }

    public void startGame() {
        createBricks();
        timeline.play();
    }

    public void checkCollisionScene(Node node) {
        Bounds bounds = node.getBoundsInLocal();
        boolean rBorder = circle.getLayoutX() >= (bounds.getMaxX() - circle.getRadius());
        boolean lBorder = circle.getLayoutX() <= (bounds.getMinX() + circle.getRadius());
        boolean bBorder = circle.getLayoutY() >= (bounds.getMaxY() - circle.getRadius());
        boolean tBorder = circle.getLayoutY() <= (bounds.getMinY() + circle.getRadius());

        if (rBorder || lBorder) {
            dX *= -1;
        }
        if (bBorder || tBorder) {
            dY *= -1;
        }
    }


    public boolean checkCollisionBrick(Rectangle brick) {

        if (circle.getBoundsInParent().intersects(brick.getBoundsInParent())) {
            boolean rightBorder = circle.getLayoutX() >= ((brick.getX() + brick.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (brick.getX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((brick.getY() + brick.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (brick.getY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                dX *= -1;
            }
            if (bottomBorder || topBorder) {
                dY *= -1;
            }
            if(mode.equals("easy")){
                points += 100;
            } else if(mode.equals("normal")){
                paddle.setWidth(paddle.getWidth() - (0.01 * paddle.getWidth()));
                points += 100;
            } else if (mode.equals("hard")){
                paddle.setWidth(paddle.getWidth() - (0.02 * paddle.getWidth()));
                points += 200;
            }
            scene.getChildren().remove(brick);

            text4.setText("Score: " + points);
            return true;
        }
        return false;
    }


    public void createBricks() {
       double width = 560.0;

        int spaceCheck = 1;
        int brickColor = -25;
        for (double i = height; i > 0; i = i - 20) {
            brickColor += 25;
            for (double j = width; j > 0; j = j - 25) {
                if (spaceCheck % 2 == 0) {
                    Rectangle rectangle = new Rectangle(j, i, 45, 15);
                    rectangle.setFill(Color.rgb(brickColor, brickColor, brickColor));
                    scene.getChildren().add(rectangle);
                    bricks.add(rectangle);
                }
                spaceCheck++;
            }
        }
    }

    public void movePaddle() {
        Bounds bounds = scene.localToScreen(scene.getBoundsInLocal());
        double sceneXPos = bounds.getMinX();

        double xPos = robot.getMouseX();
        double paddleWidth = paddle.getWidth();

        if (xPos >= sceneXPos + (paddleWidth / 2) && xPos <= (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
            paddle.setLayoutX(xPos - sceneXPos - (paddleWidth / 2));
        } else if (xPos < sceneXPos + (paddleWidth / 2)) {
            paddle.setLayoutX(0);
        } else if (xPos > (sceneXPos + scene.getWidth()) - (paddleWidth / 2)) {
            paddle.setLayoutX(scene.getWidth() - paddleWidth);
        }
    }

    public void checkCollisionPaddle(Rectangle paddle) {

        if (circle.getBoundsInParent().intersects(paddle.getBoundsInParent())) {

            boolean rightBorder = circle.getLayoutX() >= ((paddle.getLayoutX() + paddle.getWidth()) - circle.getRadius());
            boolean leftBorder = circle.getLayoutX() <= (paddle.getLayoutX() + circle.getRadius());
            boolean bottomBorder = circle.getLayoutY() >= ((paddle.getLayoutY() + paddle.getHeight()) - circle.getRadius());
            boolean topBorder = circle.getLayoutY() <= (paddle.getLayoutY() + circle.getRadius());

            if (rightBorder || leftBorder) {
                dX *= -1;
            }
            if (bottomBorder || topBorder) {
                dY *= -1;
            }
        }
    }

    public void checkCollisionBottomZone() {
        if (circle.getBoundsInParent().intersects(bottomZone.getBoundsInParent())) {
            timeline.stop();
            bricks.forEach(brick -> scene.getChildren().remove(brick));
            bricks.clear();
            startButton.setVisible(true);
            startButton.setText("Restart");
            text2.setVisible(true);
            loses++;
            text6.setText("Loses: " + loses);
//            paddle.setWidth(paddleStartSize);
            if(points > hPoints){
                highScore.setText("High:  " + points);
                hPoints = points;
            }
            points = 0;
            text4.setText("Score: " + points);
            paddle.setVisible(false);
            circle.setVisible(false);

            System.out.println("Game over!");
        }
    }
}