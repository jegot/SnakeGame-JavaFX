package com.example.snakegame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;

public class SnakePane extends AnchorPane {
    private static final double RADIUS = 10;
    private final Random random;
    private int snakeLength;
    private Color bodyColor, headColor, fruitColor;
    private List<Circle> snake;
    private List<Circle> fruits;
    private List<Rectangle> gridSquares;
    private double dx, dy; // movement delta in x and y direction;
    private Timeline timeline;
    private int score;
    private int speed;
    private String playerName;
    private char direction = 'e';
    private boolean needToGrow = false;

    public SnakePane(int snakeLength, Color bodyColor, Color headColor, Color fruitColor, String speedChoice, String playerName) {
        this.snakeLength = snakeLength;
        this.bodyColor = bodyColor;
        this.headColor = headColor;
        this.fruitColor = fruitColor;
        this.playerName = playerName;

        // set movement to right
        this.dx = 2 * RADIUS;
        this.dy = 0;

        snake = new LinkedList<>();
        fruits = new LinkedList<>();
        random = new Random();
        gridSquares = new LinkedList<>();

        if (speedChoice.equals("Slow"))
            speed = 150;
        else if (speedChoice.equals("Fast"))
            speed = 50;
        else
            speed = 90;
    }

    private void fruitController() {
        int numFruits = 4;
        if (fruits.size() < numFruits) {
            int neededFruit = numFruits - fruits.size();
            for (int i = 0; i < neededFruit; i++) {
                int gridIndex = random.nextInt(gridSquares.size());
                Rectangle randomRec = gridSquares.get(gridIndex);
                double xValue = randomRec.getX() - 10;
                double yValue = randomRec.getY() - 10;

                boolean onOtherFruit = false;
                boolean onSnake = false;
                for (int j = 0; j < fruits.size(); j++) {
                    Circle f = fruits.get(j);
                    if (f.getCenterX() == xValue && f.getCenterY() == yValue)
                        onOtherFruit = true;
                }
                for (int j = 0; j < snake.size(); j++) {
                    Circle f = snake.get(j);
                    if (f.getCenterX() == xValue && f.getCenterY() == yValue)
                        onSnake = true;
                }

                if (xValue > 0 && yValue > 0 && !onOtherFruit && !onSnake) {
                    Circle thisFruit = new Circle(xValue, yValue, RADIUS, this.fruitColor);

                    fruits.add(thisFruit);
                    getChildren().add(thisFruit);
                }
            }
        }
        //fruit collision
        for (int i = 0; i < fruits.size(); i++) {
            Circle snakeHead = snake.get(0);
            Circle thisFruit = fruits.get(i);
            if (snakeHead.getCenterY() == thisFruit.getCenterY() &&
                    snakeHead.getCenterX() == thisFruit.getCenterX()) {
                getChildren().remove(thisFruit);
                fruits.remove(thisFruit);
                score++;
                System.out.println("chomp");
                needToGrow = true;
            }
        }
    }

    private  void moveSnake() {
        Circle head = snake.get(0);
        Circle lastSegment = snake.remove(snake.size()-1);

        lastSegment.setCenterX((head.getCenterX()+getWidth()+dx) % getWidth());
        lastSegment.setCenterY((head.getCenterY()+getHeight()+dy) % getHeight());
        lastSegment.setFill(this.headColor);

        head.setFill(this.bodyColor);

        snake.add(0, lastSegment);
    }

    private void createSnake() {
        double centerX = (getWidth()/2)+10;
        double centerY = (getHeight()/2)+10;
        Circle head = new Circle(centerX, centerY, RADIUS, this.headColor);
        head.setStrokeWidth(3);
        head.setStroke(Color.BLACK);
        snake.add(head);

        for (int i = 1; i <= snakeLength; i++) {
            Circle bodySegment = new Circle(centerX - i * 2 *RADIUS, centerY, RADIUS, this.bodyColor);
            bodySegment.setStroke(Color.BLACK);
            bodySegment.setStrokeWidth(3);
            snake.add(bodySegment);
        }
    }

    private void createGrid() {
        for (int k = 0; k < 20; k++) {

            for (int i = 0; i <= 30; i++) {
                Rectangle r = new Rectangle(20, 20);
                r.setX((i * 20));
                int yVal = k * 20;
                r.setY(yVal);
                if (i % 2 == 0)
                    r.setFill(Paint.valueOf(String.valueOf(Color.LIGHTPINK)));
                else
                    r.setFill(Paint.valueOf(String.valueOf(Color.SPRINGGREEN)));

                r.setStroke(Paint.valueOf(String.valueOf(Color.BLACK)));
                gridSquares.add(r);
            }
            for (int i = 0; i <= 30; i++) {
                Rectangle r = new Rectangle(20, 20);
                r.setX((i * 20));
                int yVal = k*20;
                r.setY(yVal);
                if (i % 2 != 0)
                    r.setFill(Paint.valueOf(String.valueOf(Color.LAVENDERBLUSH)));
                else
                    r.setFill(Paint.valueOf(String.valueOf(Color.PALETURQUOISE)));

                r.setStroke(Paint.valueOf(String.valueOf(Color.BLACK)));
                if(k%2 == 0)
                    gridSquares.add(r);
            }
        }
    }

    private boolean gameOver() {
        boolean gameOver = false;
        Circle head = snake.get(0);
        if ((head.getCenterX() <= 10  && direction == 'w') || (head.getCenterX() >= 590 && direction == 'e') ||
                (head.getCenterY()<= 10 && direction == 'n') || (head.getCenterY()>= 390 && direction == 's')){
            gameOver = true;
            System.out.println("Snake hit wall");
        }
        for (int i = 1; i < snake.size()-1; i++) {
            Circle thisBodySegment = snake.get(i);
            if(head.getCenterX() == thisBodySegment.getCenterX() && head.getCenterY() == thisBodySegment.getCenterY()) {
                gameOver = true;
                System.out.println("Snake hit itself");
            }
        }
        return gameOver;
    }

    private void gameOverMessage(AnchorPane pane) {
        Text text = new Text("Game Over!");
        text.setFont(Font.font("ROGFonts-Regular", 45));
        text.setFill(Color.WHITESMOKE);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(3.0);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setX(45);
        text.setY(50);
        pane.getChildren().add(text);
        Text scoreText = new Text(playerName + "'s Score: " + score);

        scoreText.setFont(Font.font("Courier New", 25));
        scoreText.setStrokeWidth(2);
        scoreText.setStroke(Color.BLACK);
        scoreText.setY(95);
        scoreText.setTextAlignment(TextAlignment.CENTER);
        scoreText.setX(120);
        pane.getChildren().add(scoreText);
        Button againButton = new Button("Play Again");
        againButton.setOnAction(event -> {
            resetGame();
            startGame();
        });
        againButton.setFont(Font.font("ROGFonts-Regular", 15));
        againButton.setTextAlignment(TextAlignment.CENTER);
        againButton.setLayoutX(145);
        againButton.setLayoutY(120);
        pane.getChildren().add(againButton);
    }

    private double getTailX() {
        int t = snakeLength-1;
        Circle tail = snake.get(t);
        return tail.getCenterX();
    }

    private double getTailY() {
        int t = snakeLength-1;
        Circle tail = snake.get(t);
        return tail.getCenterY();
    }

    private void resetGame() {
        score = 0;
        snake.clear();
        fruits.clear();
        getChildren().removeAll(snake);
        getChildren().removeAll(fruits);
        getChildren().removeAll(gridSquares);
        needToGrow = false;
    }

    private void growSnake() {
        double newX = getTailX();
        double newY = getTailY();
        Circle bodySegment = new Circle(newX, newY, RADIUS, this.bodyColor);
        bodySegment.setStroke(Color.BLACK);
        bodySegment.setStrokeWidth(3);
        getChildren().removeAll(snake);
        snake.add(bodySegment);
        getChildren().addAll(snake);
        needToGrow = false;
        System.out.println("Current snake size: " + snake.size());
    }


    public void startGame() {
        createGrid();
        getChildren().addAll(gridSquares);
        createSnake();
        getChildren().addAll(snake);

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(speed),
                event -> {
                    if (gameOver()) {
                        timeline.stop();
                        AnchorPane go = new AnchorPane();
                        go.setPrefSize(400, 200);
                        go.setLayoutX(100);
                        go.setLayoutY(50);
                        gameOverMessage(go);
                        getChildren().add(go);
                    }
                    getTailX(); getTailY();
                    fruitController();
                    if (!gameOver())
                        moveSnake();
                    if (needToGrow)
                        growSnake();
                }));

        timeline.play();
        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    if(direction != 's') {
                        this.dx = 0;
                        this.dy = -2 * RADIUS;
                        moveSnake();
                        direction = 'n'; }
                    break;
                case DOWN:
                    if(direction != 'n') {
                        this.dx = 0;
                        this.dy = 2 * RADIUS;
                        moveSnake();
                        direction = 's'; }
                    break;
                case RIGHT:
                    if(direction != 'w') {
                        this.dx = 2 * RADIUS;
                        this.dy = 0;
                        moveSnake();
                        direction = 'e'; }
                    break;
                case LEFT:
                    if(direction != 'e') {
                        this.dx = -2 * RADIUS;
                        this.dy = 0;
                        moveSnake();
                        direction = 'w'; }
                    break;
            }
        });

        this.requestFocus();
        if (gameOver())
            timeline.stop();

    }
}
