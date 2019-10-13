import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.scene.control.Label;
import javafx.geometry.*;
import javafx.animation.*;

public class SnakeGame extends Application
{
    private final int SCENE_WIDHT = 600;
    private final int SCENE_HEIGHT = 400;
    private final int BLOCK_SIZE = 20; //size for each block of snakes body
    private final int SNAKE_STARTING_LENGHT = 3;
    private final int SNAKE_STARTING_POSITION_X = SCENE_WIDHT / 2; //set snakes position to
    private final int SNAKE_STARTING_POSITION_Y = SCENE_HEIGHT / 2; //the middle of the map

    private int snakeLenght = SNAKE_STARTING_LENGHT;    //variable lenght of snake
    private int applePositionX = 0;
    private int applePositionY = 0;
    private int tempPositionX = 0; //temporary apple position for making
    private int tempPositionY = 0; //the random values dividable with 20
    private int scoreCount = 0;
    private int snakePositionX = SNAKE_STARTING_POSITION_X; //snakes head position
    private int snakePositionY = SNAKE_STARTING_POSITION_Y;
    private boolean appleEaten = false;
    private boolean moveInitialized = false;

    private int snakeDirection = 0; //0 = right, 1 = left, 2 = up, 3 = down
    private KeyCode pressedKeyCode = KeyCode.RIGHT; //snakes starting direction
                                                    //is right
    AnimationTimer animationTimer;
    Group snakeGroup = new Group();

    //arraylist to remember positions of snakes blocks
    private ArrayList<Point2D> snakePositions = new ArrayList<>();

    public void initGame()
    {
        snakeGroup.getChildren().clear();

        Rectangle border1 = new Rectangle(0, 0, SCENE_WIDHT, BLOCK_SIZE);
        Rectangle border2 = new Rectangle(0, 0, BLOCK_SIZE, SCENE_HEIGHT);
        Rectangle border3 = new Rectangle(0, SCENE_HEIGHT - BLOCK_SIZE,
                                          SCENE_WIDHT, BLOCK_SIZE);
        Rectangle border4 = new Rectangle(SCENE_WIDHT - BLOCK_SIZE,0,
                                          BLOCK_SIZE, SCENE_WIDHT);

        border1.setFill(Color.LIGHTGRAY);
        border2.setFill(Color.LIGHTGRAY);
        border3.setFill(Color.LIGHTGRAY);
        border4.setFill(Color.LIGHTGRAY);

        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Arial", 20));
        snakeGroup.getChildren().addAll(border1, border2, border3, border4,
                                        scoreLabel);

        newApple();

        for(int i = 0; i < snakeLenght; i++)    //setup snake for first frame
        {
            Rectangle snakeBody = new Rectangle(snakePositionX - BLOCK_SIZE * i,
                                                snakePositionY,
                                                BLOCK_SIZE - 1, BLOCK_SIZE - 1);

            snakePositions.add(i, new Point2D(snakePositionX - BLOCK_SIZE * i,
                                           snakePositionY));

            snakeBody.setFill(Color.GREEN);

            snakeGroup.getChildren().add(6 + i, snakeBody);
        }
    }

    public void newApple()
    {
        //lets give apple a temporary position as if the field was a grid of
        //20x20 blocks and then convert it back to pixels
        tempPositionX = ThreadLocalRandom.current().nextInt(1,
                (SCENE_WIDHT - BLOCK_SIZE * 2) / BLOCK_SIZE);
        tempPositionY = ThreadLocalRandom.current().nextInt(1,
                (SCENE_HEIGHT - BLOCK_SIZE * 2) / BLOCK_SIZE);

        applePositionX = tempPositionX * BLOCK_SIZE;
        applePositionY = tempPositionY * BLOCK_SIZE;

        //test that apples position isn't inside snake
        for(int i = 0; i < snakePositions.size(); i++)
        {
            if(applePositionX == snakePositions.get(i).getX() &&
               applePositionY == snakePositions.get(i).getY())
            {
                newApple();
            }
        }

        Rectangle apple = new Rectangle(applePositionX, applePositionY,
                                        BLOCK_SIZE - 1, BLOCK_SIZE - 1);
        apple.setFill(Color.RED);

        //first run we need to add object, otherwise replace it
        if(appleEaten == true)
        {
            snakeGroup.getChildren().set(5, apple);
        }
        else
        {
            snakeGroup.getChildren().add(5, apple);
        }
    }

    public void snakeMove()
    {
        //if apple has been eaten, add new block to snakes tale
        if(appleEaten == true)
        {
            Rectangle snakeBody = new Rectangle(0, 0);
            snakeGroup.getChildren().add(snakeGroup.getChildren().size() - 1,
                                         snakeBody);

            snakePositions.add(snakePositions.size() - 1, new Point2D(0, 0));
            appleEaten = false;
        }

        //remove last block of snake
        snakeGroup.getChildren().remove(snakeGroup.getChildren().size() - 1);
        snakePositions.remove(snakePositions.size() - 1);

        //add new block to right direction and update heads current position
        switch (snakeDirection)
        {
            case 0:
                Rectangle snakeBody = new Rectangle(snakePositionX + BLOCK_SIZE,
                                                    snakePositionY,
                                                    BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                snakeBody.setFill(Color.GREEN);
                snakeGroup.getChildren().add(6, snakeBody);
                snakePositionX = snakePositionX + BLOCK_SIZE;
                break;
            case 1:
                Rectangle snakeBody1 = new Rectangle(snakePositionX - BLOCK_SIZE,
                                                    snakePositionY,
                                                    BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                snakeBody1.setFill(Color.GREEN);
                snakeGroup.getChildren().add(6, snakeBody1);
                snakePositionX = snakePositionX - BLOCK_SIZE;
                break;
            case 2:
                Rectangle snakeBody2 = new Rectangle(snakePositionX,
                                                    snakePositionY - BLOCK_SIZE,
                                                    BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                snakeBody2.setFill(Color.GREEN);
                snakeGroup.getChildren().add(6, snakeBody2);
                snakePositionY = snakePositionY - BLOCK_SIZE;
                break;
            case 3:
                Rectangle snakeBody3 = new Rectangle(snakePositionX,
                                                    snakePositionY + BLOCK_SIZE,
                                                    BLOCK_SIZE - 1, BLOCK_SIZE - 1);
                snakeBody3.setFill(Color.GREEN);
                snakeGroup.getChildren().add(6, snakeBody3);
                snakePositionY = snakePositionY + BLOCK_SIZE;
                break;
            default :
                break;
        }

        //add snakes new position to arraylist
        snakePositions.add(0, new Point2D(snakePositionX, snakePositionY));
        moveInitialized = false;

        //System.out.print(snakePositions.get(0) + " ");
        //System.out.println(snakePositions.get(snakePositions.size() - 1));

    }
    public void testForCollisions()
    {
        //test if snake and apple collides
        if(snakePositionX == applePositionX &&
           snakePositionY == applePositionY)
        {
            appleEaten = true;
            newApple();
            snakeLenght++;
            scoreCount++;
            updateScore();
        }
        //test if snake and wall collides
        if(snakePositionX < BLOCK_SIZE ||
           snakePositionY < BLOCK_SIZE ||
           snakePositionX > SCENE_WIDHT - BLOCK_SIZE * 2 ||
           snakePositionY > SCENE_HEIGHT - BLOCK_SIZE * 2)
        {
            gameOver();
        }
        //test if snakes head collides with its body
        for(int i = 1; i < snakePositions.size(); i++)
        {
            if(snakePositionX == snakePositions.get(i).getX() &&
               snakePositionY == snakePositions.get(i).getY())
            {
                gameOver();
            }
        }
    }

    public void updateScore()
    {
        Label scoreLabel = new Label("Score: " + scoreCount);
        scoreLabel.setFont(Font.font("Arial", 20));
        snakeGroup.getChildren().set(4, scoreLabel);
    }

    public void gameOver()
    {
        //remove all objects from screen and add game over texts
        snakeGroup.getChildren().clear();

        Label gameOverLabel = new Label("Game Over");
        gameOverLabel.setTranslateX(200);
        gameOverLabel.setTranslateY(150);
        gameOverLabel.setFont(Font.font("Arial", 40));

        Label gameOverScore = new Label("Your score was: " + scoreCount);
        gameOverScore.setTranslateX(200);
        gameOverScore.setTranslateY(200);
        gameOverScore.setFont(Font.font("Arial", 25));
        snakeGroup.getChildren().addAll(gameOverLabel, gameOverScore);

        //set values to starting state
        snakeLenght = SNAKE_STARTING_LENGHT;
        scoreCount = 0;
        snakePositionX = SNAKE_STARTING_POSITION_X;
        snakePositionY = SNAKE_STARTING_POSITION_Y;
        snakeDirection = 0;

    }

    public void start( Stage stage )
    {
        stage.setTitle("SnakeGame.java");

        Scene scene = new Scene( snakeGroup, SCENE_WIDHT, SCENE_HEIGHT);
        scene.setFill(Color.WHITE);

        initGame();

        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed((KeyEvent event)->
        {
            pressedKeyCode = event.getCode();

            //move only if a move hasn't already been initialized in current frame
            if(moveInitialized == false)
            {
                //switch directions only if snake isn't moving to opposite direction,
                //snake can't go straight backwards
                switch (pressedKeyCode)
                {
                    case RIGHT:
                        if(snakeDirection != 1)
                        {
                            snakeDirection = 0;
                        }
                        break;
                    case LEFT:
                        if(snakeDirection != 0)
                        {
                            snakeDirection = 1;
                        }
                        break;
                    case UP:
                        if(snakeDirection != 3)
                        {
                            snakeDirection = 2;
                        }
                        break;
                    case DOWN:
                        if(snakeDirection != 2)
                        {
                            snakeDirection = 3;
                        }
                        break;
                    default:
                        break;
                }
            moveInitialized = true;
            }
      });

      animationTimer = new AnimationTimer()
      {
          int frameCount = 0;

          @Override
          public void handle(long currentNanoTime)
          {
              if(frameCount % 8 == 0)  //control game speed with executing
              {                         //only at every 12th frame
                  snakeMove();
                  testForCollisions();
              }
              frameCount++;
          }
      };
      animationTimer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
