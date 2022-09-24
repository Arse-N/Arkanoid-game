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
    private Circle circle;

    @FXML
    private Rectangle paddle;

    @FXML
    private Rectangle bottomZone;

//    @FXML
//    private Rectangle body;

    @FXML
    private Button startButton;

    private final int paddleStartSize = 200;

    Robot robot = new Robot();

    private ArrayList<Rectangle> bricks = new ArrayList<>();

    double dX = -1;
    double dY = -3;
    long wins = 0;
    long loses = 0;
    long points = 0;
//    long record = 0;

    //1 Frame evey 10 millis, which means 100 FPS
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
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
                points = 0;
                text4.setText("Score: " + points);

            }

            checkCollisionScene(scene);
            checkCollisionBottomZone();
        }
    }));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paddle.setWidth(paddleStartSize);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    @FXML
    void startGameButtonAction(ActionEvent event) {
        startButton.setVisible(false);
        text1.setVisible(false);
        text2.setVisible(false);
        paddle.setVisible(true);
        circle.setVisible(true);

        dX = -1;
        dY = -3;

        circle.setLayoutX(300);
        circle.setLayoutY(300);
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

//            paddle.setWidth(paddle.getWidth() - (0.10 * paddle.getWidth()));
            scene.getChildren().remove(brick);
            points += 100;
            text4.setText("Score: " + points);
            return true;
        }
        return false;
    }


    public void createBricks() {
        double width = 560;
        double height = 100;

        int spaceCheck = 1;
        int brickCount = 0;
        for (double i = height; i > 0; i = i - 20) {
            for (double j = width; j > 0; j = j - 25) {
                if (spaceCheck % 2 == 0) {
                    Rectangle rectangle = new Rectangle(j, i, 50, 15);
                    brickCount++;
                    if (brickCount % 5 == 0) {
                        rectangle.setFill(Color.rgb(27, 115, 98));
                    } else if (brickCount % 4 == 0) {
                        rectangle.setFill(Color.rgb(49, 195, 213));
                    } else if (brickCount % 3 == 0) {
                        rectangle.setFill(Color.rgb(148, 33, 157));
                    } else if (brickCount % 2 == 0) {
                        rectangle.setFill(Color.rgb(87, 84, 213));
                    } else {
                        rectangle.setFill(Color.WHITE);
                    }
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
            points = 0;
            text4.setText("Score: " + points);
            paddle.setVisible(false);
            circle.setVisible(false);

            System.out.println("Game over!");
        }
    }
}