package application;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is a game of Space Invaders.
 * 
 * @author Kevin Tang
 *
 */
public class SpaceInvaders {

	private Scene scene;
	private Pane pane = new Pane();
	private Group group;
	
	private int numOfInvaders = 25;
	private int life = 3;
	
	private boolean shot;
	private boolean canPressSpace;
	
	private Rectangle ship;
	private Rectangle bullet;
	private Rectangle invaderBullet;
	private Rectangle[] invaders;
	private Rectangle[] barriers;
	private Rectangle[] lifePics;
	
	private int[] barrierLife;
	private double[] invaderLocations;
	private double[] invaderLocationsY;
	private boolean[] moveRight;
	private boolean[] moveLeft;
	
	private Image shipPic;
	private ImagePattern shipPattern;
	private Image invaderImg;
	private ImagePattern invaderPattern;
	
	private Image barrierImg;
	private ImagePattern barrierPattern;
	private Image barrierImg1;
	private ImagePattern barrierPattern1;
	private Image barrierImg2;
	private ImagePattern barrierPattern2;
	private Image barrierImg3;
	private ImagePattern barrierPattern3;
	private Image barrierImg4;
	private ImagePattern barrierPattern4;
	
	private Timeline moveBullet;
	private Timeline invaderLine;
	
	/**
	 * This is the constructor.
	 * 
	 * @param s is a stage that is passed in.
	 */
	public SpaceInvaders(Stage s) {
		shot = false;
		canPressSpace = true;
		group = new Group();
		scene = new Scene(pane, 640, 840);
		Image background = new Image("gamebackground.jpg", 640, 840, false, true);
		BackgroundImage bgImage = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT, null);
		pane.setBackground(new Background(bgImage));
		invaders();
		spaceship();
		bullet();
		barriers();
		
		Runnable r = () -> {
			EventHandler<ActionEvent> shooter = event -> {
				Platform.runLater(() -> {
					shootBullet();
					collision();
					invaderShoot();
				});
			};
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.00125), shooter);
			moveBullet = new Timeline();
			moveBullet.setCycleCount(Timeline.INDEFINITE);
			moveBullet.getKeyFrames().add(keyFrame);
		};

		Thread t = new Thread(r);
		t.setDaemon(true);
		t.start();
		
		Runnable k = () -> {
			EventHandler<ActionEvent> invaderMove = event -> {
				Platform.runLater(() -> {
					invaderMovement();
				
				});
			};
			KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.15), invaderMove);
			invaderLine = new Timeline();
			invaderLine.setCycleCount(Timeline.INDEFINITE);
			invaderLine.getKeyFrames().add(keyFrame2);
		};

		Thread g = new Thread(k);
		g.setDaemon(true);
		g.start();
		
	    group.setOnKeyPressed(createKeyHandler()); // left-right key presses move the rectangle
		group.requestFocus();
	}
	
	/**
	 * This method is the movement of the ship's bullet.
	 * 
	 */
	private void shootBullet() {
		if (bullet.getY() >= 0) {
			bullet.setY(bullet.getY() - 4);
			
		} else {
			bullet.setY(1);
			bullet.setX(-50);
			canPressSpace = true;
			//moveBullet.pause();
		}
	}
	
	/**
	 * This methods returns the game pane.
	 * 
	 * @return the game pane.
	 */
	public Pane getPane() {
		return pane;
	}
	
	/**
	 * This method gets the scene.
	 * 
	 * @return the scene.
	 */
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * This method creates the spaceship object.
	 * 
	 */
	public void spaceship() {
		ship = new Rectangle(30,30, Color.RED);
		ship.setX(300);
		ship.setY(725);
		shipPic = new Image("spaceCraft3.png");
		shipPattern = new ImagePattern(shipPic);
		ship.setFill(shipPattern);
		group.getChildren().add(ship);
		pane.getChildren().add(group);
	}
	
	/**
	 * This method creates the bullet.
	 * 
	 */
	public void bullet() {
		bullet = new Rectangle(5, 10, Color.RED);
		bullet.setX(-30);
		bullet.setY(840);
		group.getChildren().add(bullet);
		
		invaderBullet = new Rectangle(5, 10, Color.RED);
		invaderBullet.setX(-30);
		invaderBullet.setY(840);
		group.getChildren().add(invaderBullet);
	}
	
	/**
	 * This method creates the invaders.
	 * 
	 * @return a rectangle.
	 */
	private Rectangle createInvaders() {
		Rectangle s = new Rectangle(30 , 30, Color.RED);
		return s;
	}
	
	/**
	 * This method creates the barriers.
	 * 
	 * @return bar, a rectangle.
	 */
	private Rectangle createBarriers() {
		Rectangle bar = new Rectangle(75, 75, Color.RED);
		return bar;
	}
	
	/**
	 * This method initializes and places the barriers.
	 * 
	 */
	private void barriers() {
		barriers = new Rectangle[4];
		barrierLife = new int[4];
		lifePics = new Rectangle[3];
		for (int i = 0; i < lifePics.length; i++) {
			lifePics[i] = new Rectangle(30,30, Color.RED);
			lifePics[i].setFill(shipPattern);
			if (i == 0) {
				lifePics[i].setX(50);
				lifePics[i].setY(10);
				
			} else {
				lifePics[i].setX(lifePics[i - 1].getX() + 35);
				lifePics[i].setY(lifePics[i - 1].getY());
			}
			group.getChildren().add(lifePics[i]);
			
		}
		for (int i = 0; i < barriers.length; i++) {
			barrierLife[i] = 5;
			barriers[i] = createBarriers();
			barrierImg = new Image("FullBarrier.png");
			barrierPattern = new ImagePattern(barrierImg);
			barrierImg1 = new Image("Barrier1.png");
			barrierPattern1 = new ImagePattern(barrierImg1);
			barrierImg2 = new Image("Barrier2.png");
			barrierPattern2 = new ImagePattern(barrierImg2);
			barrierImg3 = new Image("Barrier3.png");
			barrierPattern3 = new ImagePattern(barrierImg3);
			barrierImg4 = new Image("Barrier4.png");
			barrierPattern4 = new ImagePattern(barrierImg4);
			barriers[i].setFill(barrierPattern);
			if (i == 0) {
				barriers[i].setX(50);
				barriers[i].setY(625);
			} else {
				barriers[i].setX(barriers[i - 1].getX() + 150);
				barriers[i].setY(barriers[0].getY());
			}
			group.getChildren().add(barriers[i]);
		}
		
	}
	
	/**
	 * This method creates the initializes the invaders and places them.
	 * 
	 */
	private void invaders() {
		invaders = new Rectangle[numOfInvaders];
		invaderLocations = new double[numOfInvaders];
		invaderLocationsY = new double[numOfInvaders];
		moveRight = new boolean[numOfInvaders];
		moveLeft = new boolean[numOfInvaders];
		// finds the x values for each invader
		for (int i = 0; i < numOfInvaders; i++) {
			if (i == 0) {
				invaderLocations[i] = 100;
			}
			if (i != 0 && i % 5 == 0) {
				invaderLocations[i] = 1 * 100;
			}
			if (i != 0 && i % 5 == 1) {
				invaderLocations[i] = 2 * 100;
			}
			if (i != 0 && i % 5 == 2) {
				invaderLocations[i] = 3 * 100;	
			}
			if (i != 0 && i % 5 == 3) {
				invaderLocations[i] = 4 * 100;
			}
			if (i != 0 && i % 5 == 4) {
				invaderLocations[i] = 5 * 100;	
			}
		}
		// finds the Y values for each invader
		for (int i = 0; i < numOfInvaders; i++) {
			if (i < 5) {
				invaderLocationsY[i] = 50;
			}
			if (i < 10 && i >= 5) {
				invaderLocationsY[i] = 100;
			}
			if (i < 15  && i >= 10) {
				invaderLocationsY[i] = 150;
			}
			if (i < 20  && i >= 15) {
				invaderLocationsY[i] = 200;
			}
			if (i < 25  && i >= 20) {
				invaderLocationsY[i] = 250;
			}
		}
		invaderImg = new Image("invader.png");
		invaderPattern = new ImagePattern(invaderImg);
		// sets the invaders x and y values
		for (int i = 0; i < numOfInvaders; i++) {
			//System.out.println("" + i + ": " + invaderLocationsY[i]);
			invaders[i] = createInvaders();
			invaders[i].setX(invaderLocations[i]);
			invaders[i].setY(invaderLocationsY[i]);
			invaders[i].setFill(invaderPattern);
			moveRight[i] = true;
			moveLeft[i] = false;
			group.getChildren().add(invaders[i]);
		}
	}

	/**
	 * This method creates the invader's movements.
	 * 
	 */
	private void invaderMovement() {
		
		for (int i = 0; i < numOfInvaders; i++) {
			if (moveLeft[i] == true) {
				invaders[i].setX(invaders[i].getX() - 10);
			}
			if (moveRight[i] == true) {
				invaders[i].setX(invaders[i].getX() + 10);
			}
		}
		for (int i = 0; i < numOfInvaders; i++) {
			if (invaders[i].getX() > 590) {
				for (int k = 0; k < numOfInvaders; k++) {
					moveLeft[k] = true;
					moveRight[k] = false;
					invaders[k].setY(invaders[k].getY() + 5);
				}
			}
			if (invaders[i].getX() < 20) {
				for (int k = 0; k < numOfInvaders; k++) {
					moveLeft[k] = false;
					moveRight[k] = true;
					invaders[k].setY(invaders[k].getY() + 5);
				}
			}
		}
	}
	
	/**
	 * This method controls the bullets coming from the invaders.
	 * 
	 */
	private void invaderShoot() {
		if (shot == false) {
			if (Math.random() < 0.3) {
				shot = true;
				Random rand = new Random();
				invaderBullet.setX(invaders[rand.nextInt((24 - 0) + 1) + 0].getX() + 12.5);
				invaderBullet.setY(invaders[rand.nextInt((24 - 0) + 1) + 0].getY() + 10);
			}
		}
		if (shot == true) {
			invaderBullet.setY(invaderBullet.getY() + 2);
			if (invaderBullet.getY() > 840) {
				invaderBullet.setX(-30);
				invaderBullet.setY(840);
				shot = false;
			}
		}
		
		
	}
	
	/**
	 * This method is the collision between objects.
	 * 
	 */
	private void collision() {
		for (int i = 0; i < barriers.length; i++) {
			if (invaderBullet.getBoundsInParent().intersects(barriers[i].getBoundsInParent())) {
				invaderBullet.setX(-30);
				invaderBullet.setY(840);
				shot = false;
				barrierLife[i] --;
				if (barrierLife[i] == 4) {
					barriers[i].setFill(barrierPattern1);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 3) {
					barriers[i].setFill(barrierPattern2);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 2) {
					barriers[i].setFill(barrierPattern3);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 1) {
					barriers[i].setFill(barrierPattern4);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 0) {
					group.getChildren().remove(barriers[i]);
					barriers[i] = new Rectangle();
				}
			}
		}
		for (int i = 0; i < barriers.length; i++) {
			if (bullet.getBoundsInParent().intersects(barriers[i].getBoundsInParent())) {
				bullet.setX(-30);
				bullet.setY(840);
				shot = false;
				barrierLife[i] --;
				if (barrierLife[i] == 4) {
					barriers[i].setFill(barrierPattern1);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 3) {
					barriers[i].setFill(barrierPattern2);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 2) {
					barriers[i].setFill(barrierPattern3);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 1) {
					barriers[i].setFill(barrierPattern4);
					System.out.println(barrierLife[i]);
				}
				if (barrierLife[i] == 0) {
					group.getChildren().remove(barriers[i]);
					barriers[i] = new Rectangle();
				}
			}
		}
		if (invaderBullet.getBoundsInParent().intersects(ship.getBoundsInParent())) {
			invaderBullet.setX(-30);
			invaderBullet.setY(840);
			life --;
			group.getChildren().remove(lifePics[life]);
			lifePics[life] = new Rectangle();
		}
		for (int i = 0; i < invaders.length; i++) {
			if (invaders[i].getBoundsInParent().intersects(ship.getBoundsInParent())) {
				life --;
				group.getChildren().remove(lifePics[life]);
				lifePics[life] = new Rectangle();
			}
			if (bullet.getBoundsInParent().intersects(invaders[i].getBoundsInParent())) {
				group.getChildren().remove(invaders[i]);
				invaders[i].setY(900);
				bullet.setX(-30);
				bullet.setY(840);
			}
		}
	}

	/**
	 * Return a key event handler that moves to the rectangle to the left or the
	 * right depending on what key event is generated by the associated node.
	 * 
	 * @return the key event handler
	 */
	public EventHandler<KeyEvent> createKeyHandler() {
		return event -> {
			switch (event.getCode()) {
			case LEFT: // KeyCode.LEFT
				if (ship.getX() - 10 > 0) {
					ship.setX(ship.getX() - 10);
				}
				break;
			case RIGHT: // KeyCode.RIGHT
				if (ship.getX() + 10 < 610) {
					ship.setX(ship.getX() + 10);
				}
				break;
			case UP: // KeyCode.UP
				if (ship.getY() - 10 > 620) {
					ship.setY(ship.getY() - 10);
				}
				
				break;
			case DOWN: // KeyCode.DOWN
				if (ship.getY() - 10 < 750) {
					ship.setY(ship.getY() + 10);
				}
				break;
			case SPACE: // KeyCode.SPACE
				if (canPressSpace == true) {
					bullet.setX(ship.getX() + 12.5);
					bullet.setY(ship.getY() - 10);
					moveBullet.play();
					invaderLine.play();
					canPressSpace = false;
				}
			default:
			} // switch
		};
	} // createKeyHandler

}
