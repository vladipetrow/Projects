package game;

import game.MovementSet.Move;
import java.util.*;
import java.util.List;

public class Map {
    private final static double chanceForBrackableBlock = 0.5;
    private final MapTile[][] tiles;
    private int width;
    private int height;
    private Collection<FloorListener> floorListeners = new ArrayList<>();
    private Player player = null;
    private Collection<Enemy> enemyList = new ArrayList<>();
    private List<Bomb> bombList= new ArrayList<>();
    private Collection<Bomb> explosionList= new ArrayList<>();
    private Collection<Explosion> explosionCoords= new ArrayList<>();
    private boolean isGameOver = false;
	private boolean isGameWin = false;
    public Map(int width, int height, int nrOfEnemies) {
		super();
		this.width = width;
		this.height = height;
		this.tiles = new MapTile[height][width];
		placeBreakable();
		placeUnbreakable();
		spawnEnemies(nrOfEnemies);
    }

    public static int pixelToSquare(int pixelCoord){
	return ((pixelCoord + BombermanPaint.getSquareSize()-1) / BombermanPaint.getSquareSize())-1;
    }

    public MapTile getMapTile(int rowIndex, int colIndex) {
	return tiles[rowIndex][colIndex];
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public Player getPlayer() {
	return player;
    }

    public Collection<Enemy> getEnemyList() {
	return enemyList;
    }

    public Iterable<Bomb> getBombList() {
	return bombList;
    }
	public int getBombListSize() {
	return bombList.size();
    }

    public boolean getIsGameOver() {
	return isGameOver;
    }

	public boolean getIsGameWin(){ return isGameWin; }

    public void addToBombList(Bomb bomb) {
	bombList.add(bomb);
    }

    public void createPlayer(BombermanPaint bombermanComponent, Map floor){
	player = new Player(bombermanComponent, floor);
    }

    public int squareToPixel(int squareCoord){
	return squareCoord * BombermanPaint.getSquareSize();
    }

    public void moveEnemies() {
	if (enemyList.isEmpty()) {
	    isGameWin = true;
	}
	for (Enemy e: enemyList) {
		Move currentDirection = e.getCurrentDirection();

		if (currentDirection == Move.DOWN) {
			e.moveCharacters(Move.DOWN);
		} else if (currentDirection == Move.UP) {
			e.moveCharacters(Move.UP);
		} else if (currentDirection == Move.LEFT) {
			e.moveCharacters(Move.LEFT);
		} else {
			e.moveCharacters(Move.RIGHT);
		}

		if (collisionWithBlock(e)) {
			e.changeDirection();
		}

		if (collisionWithBombs(e)) {
			e.changeDirection();
		}
		if (collisionWithEnemies()) {
			isGameOver = true;
		}
	}
	}

    public void addFloorListener(FloorListener bl) {
	floorListeners.add(bl);
    }

    public void notifyListeners() {
	for (FloorListener b : floorListeners) {
	    b.floorChanged();
	}
    }

    public void bombCreate(){

	Collection<Integer> bombIndexesToBeRemoved = new ArrayList<>();
	explosionList.clear();

	int index = 0;
	for (Bomb b: bombList) {
	    b.setTimeToExplosion(b.getTimeToExplosion() - 1);
	    if(b.getTimeToExplosion() == 0){
		bombIndexesToBeRemoved.add(index);
		explosionList.add(b);
	    }
	    index++;
	}
	for (int i: bombIndexesToBeRemoved){bombList.remove(i);}
    }

    public void explosionHandler(){
	Collection<Explosion> explosionsToBeRemoved = new ArrayList<>();
		for (Explosion e : explosionCoords) {
			e.setDuration(e.getDuration() - 1); // 4sek
			if (e.getDuration() == 0) {
				explosionsToBeRemoved.add(e);
			}
		}
		for (Explosion e : explosionsToBeRemoved) {
			explosionCoords.remove(e);
		}

		for (Bomb e : explosionList) {
			int eRow = e.getRowIndex();
			int eCol = e.getColIndex();
			boolean northOpen = true;
			boolean southOpen = true;
			boolean westOpen = true;
			boolean eastOpen = true;
			explosionCoords.add(new Explosion(eRow, eCol));
			for (int i = 1; i < e.getExplosionRadius() + 1; i++) {
				if (eRow - i >= 0 && northOpen) {
					northOpen = bombCoordCheck(eRow - i, eCol, northOpen);
				}
				if (eRow - i <= height && southOpen) {
					southOpen = bombCoordCheck(eRow + i, eCol, southOpen);
				}
				if (eCol - i >= 0 && westOpen) {
					westOpen = bombCoordCheck(eRow, eCol - i, westOpen);
				}
				if (eCol + i <= width && eastOpen) {
					eastOpen = bombCoordCheck(eRow, eCol + i, eastOpen);
				}
			}
		}
	}

    public void playerInExplosion(){

	for (Explosion exp:explosionCoords) {
	    if(collidingCircles(player, squareToPixel(exp.getColIndex()), squareToPixel(exp.getRowIndex()))){
		isGameOver = true;
	    }
	}
    }

    public void enemyInExplosion(){
	for (Explosion exp:explosionCoords) {
	    Collection<Enemy> enemiesToBeRemoved = new ArrayList<>();
	    for (Enemy e : enemyList) {
		if(collidingCircles(e, squareToPixel(exp.getColIndex()), squareToPixel(exp.getRowIndex()))){
		    enemiesToBeRemoved.add(e);
		}
	    }
	    for (Enemy e: enemiesToBeRemoved ) {
		enemyList.remove(e);
	    }
	}
    }

    private void placeBreakable () {
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		double r = Math.random();
		if (r < chanceForBrackableBlock) {
		    tiles[i][j] = MapTile.BREAKABLEBLOCK;
		}
	    }
	}
	clearSpawn();
    }

    private void clearSpawn () {
	tiles[1][1] = MapTile.FLOOR;
	tiles[1][2] = MapTile.FLOOR;
	tiles[2][1] = MapTile.FLOOR;
    }

    private void placeUnbreakable () {
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		//Makes frame of unbreakable
		if ((i == 0) || (j == 0) || (i == height - 1) || (j == width - 1) || i % 2 == 0 && j % 2 == 0) {
		    tiles[i][j] = MapTile.UNBREAKABLEBLOCK;
		    //Every-other unbreakable
		} else if (tiles[i][j] != MapTile.BREAKABLEBLOCK) {
		    tiles[i][j] = MapTile.FLOOR;
		}
	    }
	}
    }
    private void spawnEnemies (int nrOfEnemies) {
	for (int e = 0; e < nrOfEnemies; e++){
	    while(true) {
		int randRowIndex = 1 + (int) (Math.random() * (height - 2));
		int randColIndex = 1 + (int) (Math.random() * (width - 2));
		if(getMapTile(randRowIndex, randColIndex) != MapTile.FLOOR){
		    continue;
		}
		if(randRowIndex == 1 && randColIndex == 1 || randRowIndex == 1 && randColIndex == 2 || randRowIndex == 2 && randColIndex == 1 || randRowIndex == 3 && randColIndex == 1 || randRowIndex == 1 && randColIndex == 3){
		    continue;
		}
		if((randRowIndex % 2)==0){
		    enemyList.add(new Enemy(squareToPixel(randColIndex) + BombermanPaint.getSquareMiddle(), squareToPixel(randRowIndex) + BombermanPaint.getSquareMiddle(), true));
		}
		else{
		    enemyList.add(new Enemy(squareToPixel(randColIndex) + BombermanPaint.getSquareMiddle(), squareToPixel(randRowIndex) + BombermanPaint.getSquareMiddle(), false));
		}
		break;
	    }
	}
    }
    public boolean collisionWithBombs(MovementSet bombCollision) {
	boolean playerLeftBomb = true;

	for (Bomb bomb : bombList) {
	    if (bombCollision instanceof Player) {
		playerLeftBomb = bomb.isPlayerLeft();
	    }
	    if(playerLeftBomb && collidingCircles(bombCollision, squareToPixel(bomb.getColIndex()), squareToPixel(bomb.getRowIndex()))){
		return true;
	    }
	}
	return false;
    }
    public boolean collisionWithBlock(MovementSet blockCollision){
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		if(getMapTile(i, j) != MapTile.FLOOR){
		    boolean isIntersecting = squareCircleProblem(i, j, blockCollision);
		    if (isIntersecting) {
			return true;
		    }
		}
	    }
	}
	return false;
    }
	public boolean collisionWithEnemies(){
		for (Enemy enemy : enemyList) {
			if(collidingCircles(player, enemy.getX()-BombermanPaint.getSquareMiddle(), enemy.getY()-BombermanPaint.getSquareMiddle())){
				return true;
			}
		}
		return false;
	}


	public boolean squareHasBomb(int rowIndex, int colIndex){
	for (Bomb b: bombList) {
	    if(b.getRowIndex()==rowIndex && b.getColIndex()==colIndex){
		return true;
	    }
	}
	return false;
    }

    public void checkIfPlayerLeftBomb(){
	for (Bomb bomb: bombList) {
	    if(!bomb.isPlayerLeft()){
		if(!collidingCircles(player, squareToPixel(bomb.getColIndex()), squareToPixel(bomb.getRowIndex()))){
		    bomb.setPlayerLeft(true);
		}
	    }
	}
    }

    private boolean bombCoordCheck(int eRow, int eCol, boolean open){
	if(tiles[eRow][eCol] != MapTile.FLOOR){open = false;}
	if(tiles[eRow][eCol] == MapTile.BREAKABLEBLOCK){
	    tiles[eRow][eCol] = MapTile.FLOOR;    // after bomb set the map to FLOOR.
	}
	if(tiles[eRow][eCol] != MapTile.UNBREAKABLEBLOCK){explosionCoords.add(new Explosion(eRow, eCol));}
	return open;
    }

    private boolean collidingCircles(MovementSet movement, int x, int y){
	int a = movement.getX() - x - BombermanPaint.getSquareMiddle();
	int b = movement.getY() - y - BombermanPaint.getSquareMiddle();
	int a2 = a * a;
	int b2 = b * b;
	double c = Math.sqrt(a2 + b2);
	return(movement.getSize() > c); // > 30
    }

    private boolean squareCircleProblem(int row, int col, MovementSet abstractCharacter) {
	//circle-rectangle-collision-detection-intersection
	int posX = abstractCharacter.getX();
	int posY = abstractCharacter.getY();

	int circleRadius = abstractCharacter.getSize() / 2; // 15
	int squareSize = BombermanPaint.getSquareSize();
	int squareCenterX = (col*squareSize)+(squareSize/2); // 20
	int squareCenterY = (row*squareSize)+(squareSize/2); // 20

	int circleDistanceX = Math.abs(posX - squareCenterX);
	int circleDistanceY = Math.abs(posY - squareCenterY);

	if (circleDistanceX > (squareSize/2 + circleRadius)) { return false; }
	if (circleDistanceY > (squareSize/2 + circleRadius)) { return false; }

	if (circleDistanceX <= (squareSize/2)) { return true; }
	if (circleDistanceY <= (squareSize/2)) { return true; }

	int cornerDistance = (circleDistanceX - squareSize/2)^2 +
							      (circleDistanceY - squareSize/2)^2;

	return (cornerDistance <= (circleRadius^2));
    }
}