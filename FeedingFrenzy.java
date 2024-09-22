
//Fish Frenzy - Quinn Reilly and Sara Shee

//import libraries 
import tester.Tester;
import java.util.Random;
import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

//class FishWorld extends World from the funworld java library 
class FishWorld extends World {
  ILoFish backgroundFish;
  PlayerFish player;
  int tickCount;
  Random rand;

  //FishWorld constructor 
  FishWorld(ILoFish backgroundFish, PlayerFish player, int tickCount) {
    this.backgroundFish = backgroundFish;
    this.player = player;
    this.tickCount = tickCount;
    this.rand = new Random();
  }

  FishWorld(ILoFish backgroundFish, PlayerFish player, int tickCount, Random rand) {
    this.backgroundFish = backgroundFish;
    this.player = player;
    this.tickCount = tickCount;
    this.rand = rand;
  }

  /*
   * TEMPLATE
   * Fields:
   * this.backgroundFish ... ILoFish
   * this.player ... PlayerFish
   * this.tickCount ... int
   * 
   * Methods:
   * this.makeScene() ... WorldScene
   * this.onTick() ... World
   * this.onKeyEvent(String key) ... World
   * this.lastScene(String msg) ... WorldScene
   * 
   * Methods on Fields:
   * this.backgroundFish.draw(WorldScene scene) ... WorldScene
   * this.backgroundFish.addFish(int maxSize, Random rand) ... ILoFish
   * this.backgroundFish.checkCollision(int PlayerX, int PlayerY, int PlayerSize) ... boolean
   * this.backgroundFish.move(Random rand) ... ILoFish
   * this.backgroundFish.fishCollideHandler(int x, int y, int size, PlayerFish playerFish) 
   *                                                                             ... ILoFish
   * this.backgroundFish.updateScore(int x, int y, int size, int score) ... int
   * this.backgroundFish.checkGameOver(int x, int y, int size) ... boolean
   * this.backgroundFish.isBiggestFish(int size) ... boolean
   * this.player.draw(WorldScene scene) ... WorldScene
   * this.player.checkCollision(ILoFish backgroundFish) ... boolean
   * this.player.checkGameOver(ILoFish backgroundFish) ... boolean
   * this.player.fishCollideHandler(ILoFish backgroundFish, PlayerFish player, int tickCount) 
   *                                                                             ... World
   * this.player.updateSize() ... PlayerFish
   * this.player.move(String key) ... PlayerFish
   * this.player.checkWalls() ... PlayerFish
   * this.player.scoreToImage() ... WorldImage
   */

  //makeScene method, creates the visual representation for current state of the game 
  public WorldScene makeScene() {
    return this.player.draw(this.backgroundFish.draw(new WorldScene(600, 400)));
  }

  //onTick method, updates the state of the game on each time interval 
  public World onTick() {
    tickCount++;

    if (this.player.checkCollision(this.backgroundFish)) {
      if (player.checkGameOver(this.backgroundFish)) {
        return this.endOfWorld("You've been eaten :(");
      } 
      else {
        return this.player.fishCollideHandler(this.backgroundFish, 
            this.player.updateSize(), this.tickCount, this.rand);
      }
    }
    else if (this.player.isBiggestFish(this.backgroundFish)) {
      return this.endOfWorld("You're the biggest fish in the pond!");
    }
    else if (tickCount % 30 == 0) {
      return new FishWorld(this.backgroundFish.addFish(4, this.rand).move(this.rand), 
          this.player.updateSize(), this.tickCount);
    }
    else if (tickCount % 60 == 0) {
      return new FishWorld(this.backgroundFish.addFish(10, this.rand).move(this.rand), 
          this.player.updateSize(), this.tickCount);
    }
    else {
      return new FishWorld(this.backgroundFish.move(this.rand), this.player.updateSize(), 
          this.tickCount);
    }
  }

  // moves PlayerFish based on key event (up, down, left and right)
  public World onKeyEvent(String key) {
    return new FishWorld(this.backgroundFish, 
        this.player.move(key).updateSize().checkWalls(), tickCount);
  }

  //creates and returns the final scene when the game ends 
  public WorldScene lastScene(String msg) {
    return (new WorldScene(600, 400)).placeImageXY(new AboveImage(
        new TextImage(msg, Color.black), player.scoreToImage()), 300, 200);
  }
}


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Represents a list of Fish
interface ILoFish {

  /*
   * TEMPLATE
   * Methods:
   * this.draw(WorldScene scene) ... WorldScene
   * this.addFish(int maxSize, Random rand) ... ILoFish
   * this.checkCollision(int PlayerX, int PlayerY, int PlayerSize) ... boolean
   * this.move(Random rand) ... ILoFish
   * this.fishCollideHandler(int x, int y, int size, PlayerFish playerFish) ... ILoFish
   * this.updateScore(int x, int y, int size, int score) ... int
   * this.checkGameOver(int x, int y, int size) ... boolean
   * this.isBiggestFish(int size) ... boolean
   */

  // draws this list of background fish onto given scene
  WorldScene draw(WorldScene scene);

  //adds new fish with a random size (up to maxSize) to this list of background fish 
  ILoFish addFish(int maxSize, Random rand);

  //checks if any fish in this list collide with the player fish 
  boolean checkCollision(int playerX, int playerY, int playerSize);

  // moves each background fish in list in a random way
  ILoFish move(Random rand);

  //handles collision between player and background fish, updates list accordingly
  ILoFish fishCollideHandler(int x, int y, int size, PlayerFish playerFish);

  //updayes the player's score based on collisions with background fish 
  int updateScore(int x, int y, int size, int score);

  //checks if game is over (collision with a larger fish) 
  boolean checkGameOver(int x, int y, int size);

  //determines if the player fish is the biggest fish 
  boolean isBiggestFish(int size);
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Represents an empty list of Fish
class MtLoFish implements ILoFish {

  /*
   * TEMPLATE
   * Fields:
   * (none)
   * 
   * Methods:
   * this.draw(WorldScene scene) ... WorldScene
   * this.addFish(int maxSize, Random rand) ... ILoFish
   * this.checkCollision(int PlayerX, int PlayerY, int PlayerSize) ... boolean
   * this.move(Random rand) ... ILoFish
   * this.fishCollideHandler(int x, int y, int size, PlayerFish playerFish) 
   *                                                               ... ILoFish
   * this.updateScore(int x, int y, int size, int score) ... int
   * this.checkGameOver(int x, int y, int size) ... boolean
   * this.isBiggestFish(int size) ... boolean
   */

  //draws empty list of background fish onto given scene 
  public WorldScene draw(WorldScene scene) {
    return scene;
  }

  // determines if given playerFish XY overlaps the empty list of fish
  public boolean checkCollision(int playerX, int playerY, int playerSize) {
    return false;
  }

  //moves each background fish in the empty list in a random way 
  public ILoFish move(Random rand) {
    return this;
  }

  //handles collision between player and background fish, updates list accordingly
  public ILoFish fishCollideHandler(int x, int y, int size, PlayerFish playerFish) {
    return this;
  }

  //updates players score based on collisions with background fish 
  public int updateScore(int x, int y, int size, int score) {
    return score;
  }

  //check if game is over (collision with a larger fish) 
  public boolean checkGameOver(int x, int y, int size) {
    return false;
  }

  //adds a new fish with a random size (up to max size) to this empty list 
  public ILoFish addFish(int maxSize, Random rand) {
    return this;
  }

  //determines if the player fish is the biggest fish in the pond 
  public boolean isBiggestFish(int size) {
    return true;
  }
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~b~~~~~~~~~~~~~~~~~~~~~~~~
// Represents a non-empty list of Background Fish
class ConsLoFish implements ILoFish {
  BackFish first; 
  ILoFish rest;

  //constructor 
  ConsLoFish(BackFish first, ILoFish rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE
   * Fields:
   * this.first ... BackFish
   * this.rest ... ILoFish
   * 
   * Methods:
   * this.draw(WorldScene scene) ... WorldScene
   * this.addFish(int maxSize, Random rand) ... ILoFish
   * this.checkCollision(int PlayerX, int PlayerY, int PlayerSize) ... boolean
   * this.move(Random rand) ... ILoFish
   * this.fishCollideHandler(int x, int y, int size, PlayerFish playerFish) ... ILoFish
   * this.updateScore(int x, int y, int size, int score) ... int
   * this.checkGameOver(int x, int y, int size) ... boolean
   * this.isBiggestFish(int size) ... boolean
   * 
   * Methods on Fields:
   * this.first.draw(WorldScene scene) ... WorldScene
   * this.first.didCollide(int x, int y, int otherSize) ... boolean
   * this.first.move(Random rand) ... BackFish
   * this.first.checkWalls() ... BackFish
   * this.first.eatHandler(int pSize, ILoFish rest) ... ILoFish
   * this.first.updateScore(int x, int y, int size, int score) ... int
   * this.first.isBiggerThan(int size) ... boolean
   * this.rest.draw(WorldScene scene) ... WorldScene
   * this.rest.addFish(int maxSize, Random rand) ... ILoFish
   * this.rest.checkCollision(int PlayerX, int PlayerY, int PlayerSize) ... boolean
   * this.rest.move(Random rand) ... ILoFish
   * this.rest.fishCollideHandler(int x, int y, int size, 
   *                                PlayerFish playerFish) ... ILoFish
   * this.rest.updateScore(int x, int y, int size, int score) ... int
   * this.rest.checkGameOver(int x, int y, int size) ... boolean
   * this.rest.isBiggestFish(int size) ... boolean
   */

  //draws this list of background fish onto the given scene 
  public WorldScene draw(WorldScene empty) {
    return this.first.draw(this.rest.draw(empty));
  }

  // determines if given playerFish XY overlaps any of the fish in the list
  public boolean checkCollision(int playerX, int playerY, int playerSize) {
    return this.first.didCollide(playerX, playerY, playerSize) 
        || this.rest.checkCollision(playerX, playerY, playerSize);
  }

  //moves each background fish in the list in a random way 
  public ILoFish move(Random rand) {
    return new ConsLoFish(this.first.move(rand).checkWalls(), this.rest.move(rand));
  }

  //handles collision between player fish and background fish 
  public ILoFish fishCollideHandler(int x, int y, int pSize, PlayerFish playerFish) {
    if (this.first.didCollide(x, y, pSize)) {
      return this.first.eatHandler(pSize, this.rest);
    } 
    else {
      return new ConsLoFish(this.first, 
          this.rest.fishCollideHandler(x, y, pSize, playerFish));
    }
  }

  //upodates the player's score based on collisions with background fish 
  public int updateScore(int x, int y, int size, int score) {
    if (this.first.didCollide(x, y, size)) {
      return this.first.updateScore(x,  y, size, score);
    } 
    else {
      return this.rest.updateScore(x, y, size, score);
    }
  }

  //checks if the game is over due to a collision with a larger fish 
  public boolean checkGameOver(int x, int y, int size) {
    return (this.first.didCollide(x, y, size)
        && this.first.isBiggerThan(size)) || this.rest.checkGameOver(x, y, size);
  }

  //adds a new fish with a randoms size (up to maxSize) to the list 
  public ILoFish addFish(int maxSize, Random rand) {
    int randSize = rand.nextInt(maxSize);
    int randX = rand.nextInt(350) + 25;
    int randY = rand.nextInt(1) * 559;
    int dir;
    if (randX == 599) {
      dir = -1;
    }
    else {
      dir = 1;
    }
    return new ConsLoFish(new BackFish(randSize, randX, randY, dir), 
        new ConsLoFish(this.first, this.rest));
  }


  //determines if the player fish is the biggest fish in the pond 
  public boolean isBiggestFish(int size) {
    if (this.first.isBiggerThan(size) || this.first.isSameSize(size)) {
      return false;
    }
    else {
      return this.rest.isBiggestFish(size);
    }

  }

}  

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Abstract class of Fish
abstract class AFish {
  Color color;
  int size;
  int x;
  int y;
  int dirFacing;
  WorldImage fishImage;

  //constructor for AFish 
  AFish(int size, int x, int y, int dirFacing) {
    this.size = size;
    this.color = this.sizeColor();
    this.x = x;
    this.y = y;
    this.dirFacing = dirFacing;
    this.fishImage = this.drawFish();

  }

  // Draws a fish based on size and direction facing
  public WorldImage drawFish() {
    if (this.dirFacing == -1) {
      //fish facing left 
      return new BesideImage(
          new EllipseImage(this.size * 9, this.size * 5, OutlineMode.SOLID, this.color),
          new RotateImage(
              new EquilateralTriangleImage(this.size * 6, OutlineMode.SOLID, this.color), 30));
    } else {
      //fish facing right 
      return new BesideImage(
          new RotateImage(
              new EquilateralTriangleImage(this.size * 6,  OutlineMode.SOLID, this.color), -30), 
          new EllipseImage(
              this.size * 9, this.size * 5, OutlineMode.SOLID, this.color));
    }
  }

  //abstract method to draw fish on the given scene 
  public abstract WorldScene draw(WorldScene background);

  //checks if this fish collides with another fish at (x, y) with given size
  public boolean didCollide(int x, int y, int otherSize) {
    return 
        ((x - otherSize * 7 < this.x + this.size * 7) 
            && (y + otherSize * 2.5 > this.y - this.size * 2.5)) 
        && ((x + otherSize * 7 > this.x - this.size * 7) 
            && (y - otherSize * 2.5 < this.y + this.size * 2.5));
  }

  //determines the color of the fish based on its size
  Color sizeColor() {
    if (this.size == 1) {
      return Color.pink;
    } else if (this.size == 2) {
      return Color.red;
    } else if (this.size == 3) {
      return Color.yellow;
    } else if (this.size == 4) {
      return Color.green;
    } else if (this.size == 5) {
      return Color.cyan;
    } else if (this.size == 6) {
      return Color.BLUE;
    } else if (this.size == 7) {
      return Color.magenta;
    } else if (this.size == 8) {
      return Color.pink;
    } else if (this.size == 9) {
      return Color.lightGray;
    } else if (this.size == 10) {
      return Color.darkGray;
    } else {
      return Color.black;
    }
  }
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Represents the Players Fish
class PlayerFish extends AFish {
  int score;

  //constructor for PlayerFish
  PlayerFish(int size, int x, int y, int dirFacing, int score) {
    super(size, x, y, dirFacing);
    this.score = score;
  }

  //Checks if the player fish is the biggest fish in the background fish list
  public boolean isBiggestFish(ILoFish backgroundFish) {
    return backgroundFish.isBiggestFish(this.size);
  }

  //Updates the size of the player fish based on the score
  public PlayerFish updateSize() {   
    return new PlayerFish(((score - score % 100) / 100) + 3, 
        this.x, this.y, this.dirFacing, this.score);
  }

  // Converts the score to a text image
  public WorldImage scoreToImage() {
    return new TextImage("score: " + Integer.toString(this.score), Color.black);
  } 

  // Checks if the game is over based on the background fish list
  public boolean checkGameOver(ILoFish backgroundFish) {
    return backgroundFish.checkGameOver(this.x, this.y, this.size);
  }

  // draws the PlayerFish onto the given worldScene
  public WorldScene draw(WorldScene background) {

    return background.placeImageXY(this.fishImage, this.x, this.y)
        .placeImageXY(this.scoreToImage(), 40, 15);
  }

  // Overrides abstract DrawFish function to create a Playerfish with an outline
  public WorldImage drawFish() {
    if (this.dirFacing == -1) {
      return new OverlayImage((new BesideImage(
          new EllipseImage(this.size * 9, this.size * 5, 
              OutlineMode.OUTLINE, Color.black),
          new RotateImage(
              new EquilateralTriangleImage(this.size * 6, 
                  OutlineMode.OUTLINE, Color.black), 30))), 
          super.drawFish()); 
    } else {
      return new OverlayImage((new BesideImage(
          new RotateImage(
              new EquilateralTriangleImage(this.size * 6,  
                  OutlineMode.OUTLINE, Color.black), -30), 
          new EllipseImage(
              this.size * 9, this.size * 5, OutlineMode.OUTLINE, 
              Color.black))), super.drawFish());
    }

  }

  // moves the PlayerFish according to key input (up, down, left, right)
  public PlayerFish move(String key) {
    if (key.equals("up")) {
      return new PlayerFish(this.size, this.x, this.y - 4, this.dirFacing, this.score);
    } 
    else if (key.equals("down")) {
      return new PlayerFish(this.size, this.x, this.y + 4, this.dirFacing, this.score);
    }
    else if (key.equals("left")) {
      return new PlayerFish(this.size, this.x - 4, this.y, -1, this.score);
    }
    else if (key.equals("right")) {
      return new PlayerFish(this.size, this.x + 4, this.y,  1, this.score);
    }
    else {
      return this;
    } 
  }

  //Checks if the PlayerFish is out of bounds and wraps around if necessary
  public PlayerFish checkWalls() {
    if (x > 600) {
      return new PlayerFish(this.size, 0, this.y, this.dirFacing, this.score);
    }
    else if (x < 0) {
      return new PlayerFish(this.size, 600, this.y, this.dirFacing, this.score);
    }
    else if (y > 400) {
      return new PlayerFish(this.size, this.x, 0, this.dirFacing, this.score);
    }
    else if (y < 0) {
      return new PlayerFish(this.size, this.x, 400, this.dirFacing, this.score);
    }
    else {
      return this;
    }
  }

  //Checks if the PlayerFish collides with any background fish
  public boolean checkCollision(ILoFish backgroundFish) {
    return backgroundFish.checkCollision(this.x, this.y, this.size);
  }

  //Handles collision with background fish and updates the world state
  public World fishCollideHandler(ILoFish backgroundFish, 
      PlayerFish player, int tickCount, Random rand) {
    return new FishWorld(backgroundFish.fishCollideHandler(this.x, 
        this.y, this.size, this).move(rand),
        this.updateScore(backgroundFish), tickCount);
  }

  //Updates the score of the PlayerFish based on collisions with background fish
  PlayerFish updateScore(ILoFish backgroundFish) {
    return new PlayerFish(this.size, this.x, this.y, this.dirFacing, 
        backgroundFish.updateScore(this.x, this.y, this.size, this.score));
  }

  // Handler for when the PlayerFish eats another fish (returns an empty list)
  public ILoFish eatHandler(int pSize, ILoFish rest) {
    return new MtLoFish();
  }

}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Represents a Background Fish
class BackFish extends AFish {

  //constructor for BackFish 
  BackFish(int size, int x, int y, int dirFacing) {
    super(size, x, y, dirFacing);
  }

  // Checks if the current fish is bigger than the given size
  public boolean isBiggestFish(int size) {
    return size > this.size;
  }

  // Checks if two fish are the same size
  public boolean isSameSize(int size) {
    return this.size == size;
  }

  //Checks if the current fish is bigger than the given size
  public boolean isBiggerThan(int size) {
    return this.size > size;
  }

  //Updates the score if the PlayerFish eats this fish
  public int updateScore(int x, int y, int size, int score) {
    if (this.size < size) {
      return this.size * 10 + score;
    }
    else {
      return score;
    }
  }

  //Draws the BackFish onto the given WorldScene
  public WorldScene draw(WorldScene background) {
    return background.placeImageXY(this.fishImage, this.x, this.y);
  }

  //Moves the BackFish in a random direction
  public BackFish move(Random rand) {
    return new BackFish(this.size, this.x + 2 * this.dirFacing, 
        this.y + (rand.nextInt(10) - 5), this.dirFacing);
  }

  // Checks if the BackFish is out of bounds and wraps around if necessary
  public BackFish checkWalls() {
    if (x > 600) {
      return new BackFish(this.size, 0, this.y, this.dirFacing);
    }
    else if (x < 0) {
      return new BackFish(this.size, 600, this.y, this.dirFacing);
    }
    else if (y > 400) {
      return new BackFish(this.size, this.x, 0, this.dirFacing);
    }
    else if (y < 0) {
      return new BackFish(this.size, this.x, 400, this.dirFacing);
    }
    else {
      return this;
    }
  }

  // Checks if the BackFish collides with any background fish (always false)
  public boolean checkCollision(ILoFish backgroundFish) {
    return false;
  }

  // Handles the eating of this BackFish by the PlayerFish
  public ILoFish eatHandler(int pSize, ILoFish rest) {
    if (this.size < pSize) {
      return rest;
    }
    else {
      return new ConsLoFish(this, rest);
    }
  }

  // Handles collision with the PlayerFish and updates the world state
  public World fishCollideHandler(ILoFish backgroundFish, PlayerFish player, int tickCount) {
    return new FishWorld(backgroundFish, player, tickCount);
  }
}


class ExamplesFrenzy {
  PlayerFish player = new PlayerFish(3, 30, 60, 1, 0);

  BackFish back1 = new BackFish(1, 70, 80, 1);
  BackFish back2 = new BackFish(2, 120, 200, -1);
  BackFish back3 = new BackFish(4, 100, 300, 1);
  BackFish back4 = new BackFish(3, 75, 190, -1);
  BackFish back5 = new BackFish(5, 60, 310, -1);
  BackFish back6 = new BackFish(1, 200, 160, -1);
  BackFish back7 = new BackFish(1, 80, 300, 1);
  BackFish back8 = new BackFish(1, 270, 350, -1);
  BackFish back9 = new BackFish(1, 500, 200, 1);
  BackFish back10 = new BackFish(1, 400, 160, -1);
  BackFish back11 = new BackFish(1, 260, 300, 1);
  BackFish back12 = new BackFish(1, 599, 60, -1);
  BackFish back13 = new BackFish(1, 30, 200, 1);
  BackFish back14 = new BackFish(2, 30, 230, -1);
  BackFish back15 = new BackFish(4, 300, 100, 1);
  BackFish back16 = new BackFish(3, 500, 270, -1);
  BackFish back17 = new BackFish(5, 100, 10, 1);
  BackFish back18 = new BackFish(6, 400, 50, -1);
  BackFish back19 = new BackFish(7, 400, 300, 1);
  BackFish back20 = new BackFish(8, 10, 370, -1);
  BackFish back21 = new BackFish(9, 400, 100, 1);
  BackFish back22 = new BackFish(4, 300, 300, 1);



  ILoFish mt = new MtLoFish();
  ILoFish testFishList = new ConsLoFish(this.back1,
      new ConsLoFish(this.back2, new ConsLoFish(this.back3, new ConsLoFish(this.back4,
          new ConsLoFish(this.back5, new ConsLoFish(this.back6,
              new ConsLoFish(this.back7, new ConsLoFish(this.back8, 
                  new ConsLoFish(this.back9, 
                      new ConsLoFish(this.back10,
                          new ConsLoFish(this.back11, 
                              new ConsLoFish(this.back12,
                                  new ConsLoFish(this.back13,
                                      new ConsLoFish(this.back14, 
                                          new ConsLoFish(this.back15,
                                              new ConsLoFish(this.back16,
                                                  new ConsLoFish(this.back17,
                                                      new ConsLoFish(this.back18, 
                                                          new ConsLoFish(this.back19, 
                                                              new ConsLoFish(this.back20,
                                                                  new ConsLoFish(this.back21, 
                                                                      new ConsLoFish(this.back22,
                                                                          this.mt
                                                                          ))))))))))))))))))))));

  ILoFish list2 = new ConsLoFish(this.back1, this.mt);


  FishWorld world = new FishWorld(this.testFishList, this.player, 0, new Random(2));
  FishWorld copy = new FishWorld(this.testFishList, this.player, 0, new Random(2));
  FishWorld drawWorld = new FishWorld(list2, this.player, 0);


  // Test for makeScene method 
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.world.makeScene(), 
        this.player.draw(this.testFishList.draw(new WorldScene(600,400))));
  }

  //Test for onKeyEvent method for each key event
  //up 
  boolean testOnKeyEventUp(Tester t) {
    return t.checkExpect(this.world.onKeyEvent("up"), 
        new FishWorld(this.testFishList, new PlayerFish(3, 30, 56, 1, 0), 0, new Random(2)));
  }

  //down 
  boolean testOnKeyEventDown(Tester t) {
    return t.checkExpect(this.world.onKeyEvent("down"), new FishWorld(this.testFishList,
        new PlayerFish(3, 30, 64, 1, 0), 0));
  }

  //left
  boolean testOnKeyEventLeft(Tester t) {
    return t.checkExpect(this.world.onKeyEvent("left"), new FishWorld(this.testFishList, 
        new PlayerFish(3, 26, 60, -1, 0), 0));
  }

  //right 
  boolean testOnKeyEventRight(Tester t) {
    return t.checkExpect(this.world.onKeyEvent("right"), new FishWorld(this.testFishList, 
        new PlayerFish(3, 34, 60, 1, 0), 0));
  }


  //test lastScene
  boolean testLastScene(Tester t) {
    return t.checkExpect(this.world.lastScene("Game Over"), new WorldScene(600, 400).placeImageXY(
        new AboveImage(new TextImage("Game Over", Color.black), 
            this.player.scoreToImage()), 300, 200));
  }

  //test for updating size method updates the size correctly 
  boolean testUpdateSize(Tester t) {
    return t.checkExpect(new PlayerFish(3, 30, 60, 1, 150).updateSize().size, 4)
        && t.checkExpect(new PlayerFish(3, 30, 60, 1, 250).updateSize().size, 5)
        && t.checkExpect(new PlayerFish(3, 30, 60, 1, 90).updateSize().size, 3);
  }

  //tests if moving the player to the left updates the x coordinate correctly
  boolean testPlayerMove(Tester t) {
    return t.checkExpect(this.player.move("left").x, 26);
  }

  //makes a playerFish out of bounds to see if it wraps around correctly
  boolean testCheckWalls(Tester t) {
    return t.checkExpect(new PlayerFish(5, 610, 60, 1, 0).checkWalls().x, 0);
  }

  //tests for the methods for MtLoFish
  boolean testMtLoFish(Tester t) {
    MtLoFish emptyFish = new MtLoFish();
    return t.checkExpect(new MtLoFish().draw(new WorldScene(600, 400)), 
        new WorldScene(600, 400))
        && t.checkExpect(emptyFish.checkCollision(30, 60, 3), false)
        && t.checkExpect(emptyFish.move(new Random(2)), emptyFish)
        && t.checkExpect(emptyFish.fishCollideHandler(30, 60, 3, player), emptyFish)
        && t.checkExpect(emptyFish.updateScore(30, 60, 5, 100), 100)
        && t.checkExpect(emptyFish.checkGameOver(30, 60, 3), false)
        && t.checkExpect(emptyFish.addFish(10, new Random(2)), emptyFish)
        && t.checkExpect(emptyFish.isBiggestFish(5), true);
  }

  // Tests for the methods for ConsLoFish
  boolean testConsLoFish(Tester t) {
    //  BackFish back1 = new BackFish(1, 70, 80, 1);
    ConsLoFish consFish = new ConsLoFish(this.back1, this.mt);

    //expected
    ILoFish addedFish = consFish.addFish(10, new Random(2));

    return t.checkExpect(new ConsLoFish(this.back1, this.mt).draw(new WorldScene(600, 400)), 
        this.back1.draw(new WorldScene(600, 400)))
        && t.checkExpect(consFish.checkCollision(30, 60, 3), false)
        && t.checkExpect(consFish.checkCollision(70, 80, 3), true)
        && t.checkExpect(consFish.move(new Random(2)), new ConsLoFish(
            new BackFish(1, 72, 83, 1), this.mt.move(new Random(2))))
        && t.checkExpect(new ConsLoFish(this.back1, this.mt)
            .fishCollideHandler(30, 60, 3, player), 
            new ConsLoFish(this.back1, this.mt))
        && t.checkExpect(consFish.updateScore(70, 80, 3, 100), 110)
        && t.checkExpect(consFish.checkGameOver(30, 60, 3), false)
        && t.checkExpect(addedFish instanceof ConsLoFish, true) 
        // this line verifies the type of addedFish
        && t.checkExpect(consFish.isBiggestFish(5), true)
        && t.checkExpect(consFish.isBiggestFish(1), false);
  }


  // Tests for the methods of the BackFish
  boolean testBackFish(Tester t) {
    BackFish fish = new BackFish(3, 100, 100, 1);

    // expected
    WorldScene expectedScene = fish.draw(new WorldScene(600, 400));

    return t.checkExpect(fish.isBiggestFish(5), true)
        && t.checkExpect(fish.isBiggestFish(2), false)
        && t.checkExpect(fish.move(new Random(2)), new BackFish(3, 102, 103, 1))
        && t.checkExpect(fish.isBiggerThan(2), true)
        && t.checkExpect(fish.isBiggerThan(4), false)
        && t.checkExpect(fish.updateScore(30, 60, 3, 100), 100)
        && t.checkExpect(fish.updateScore(30, 60, 4, 100), 130)
        && t.checkExpect(fish.draw(new WorldScene(600, 400)), expectedScene)
        && t.checkExpect(fish.checkWalls(), new BackFish(3, 100, 100, 1))
        && t.checkExpect(new BackFish(3, 601, 100, 1).checkWalls(), new BackFish(3, 0, 100, 1))
        && t.checkExpect(fish.eatHandler(3, this.mt), new ConsLoFish(fish, this.mt))
        && t.checkExpect(fish.eatHandler(4, this.mt), this.mt);
  }

  Boolean testPlayerFish(Tester t) {
    PlayerFish play = new PlayerFish(3, 30, 60, 1, 0);
    ConsLoFish consFish = new ConsLoFish(new BackFish(3, 100, 100, 1), this.mt);

    return t.checkExpect(play.isBiggestFish(consFish), false)
        && t.checkExpect(play.isBiggestFish(this.mt), true)
        // && t.checkExpect(play.draw(new WorldScene(600, 400)), expectedScene)
        && t.checkExpect(play.checkWalls(), new PlayerFish(3, 30, 60, 1, 0))
        && t.checkExpect(new PlayerFish(3, 601, 60, 1, 0).checkWalls(), 
            new PlayerFish(3, 0, 60, 1, 0))
        && t.checkExpect(new BackFish(3, 601, 100, 1).checkWalls(), new BackFish(3, 0, 100, 1))
        && t.checkExpect(play.eatHandler(3, consFish), this.mt)
        && t.checkExpect(play.eatHandler(4, consFish), this.mt);

  }

  //   test onTick 
  boolean testOnTick(Tester t) {   
    return t.checkExpect(this.copy.onTick(), 
        new FishWorld(this.testFishList.move(new Random(2)), 
            player, this.copy.tickCount, new Random(2)));
  }


  //test for big bang 
  boolean testBigBang(Tester t) {
    FishWorld world = new FishWorld(this.testFishList, this.player, 0, new Random(2));
    int worldWidth = 600;
    int worldHeight = 400;
    double tickRate = .1;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }
}