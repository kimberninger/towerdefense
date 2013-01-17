package group150.helper;

import java.util.Random;

import group150.model.FieldStatus;

/**
 * @author Philipp Mario Gauer
 * @author Kim Werner Berninger
 * @author Pascal Franke
 * @author Tobias Sebastian Engert
 * 
 * 
 * @version 1.1 17.02.2012 
 *
 */
public class MapRandomizer {
	public enum Difficulty {
		EASY,
		NORMAL,
		HARD,
		EXTREME
	}
	/**
	 * @param rows Anzahl der ReihenenBreite des Feldes
	 * @param columns Hoehe des Feldes
	 * @param spawns Anzahl der Spawn(FALLS REALISIERBAR)
	 * @param targets Anzahl der Ziele (FALLS REALISIERBAR)
	 * @param difficulty Schwierigkeit des Levels
	 * @return fertiges Level
	 */
	public static String randomMap(int rows, int columns, int spawns, int targets, Difficulty difficulty) {
		FieldStatus[][] fields = new FieldStatus[rows][columns];

		Random rnd = new Random();
		int pathCounter = 1;
		int row;
		int column;
		boolean isDone = false;
		int pathVar ;
		int loopCounter = 0;
		//int turnCounter = 0;
		int direction ;
		int blockCounter = 0 ;
		
		
		while (pathCounter != 0) {
		
			
			if((rows * columns) < 350){
				
				
				
				switch(difficulty){
					case EASY:
						pathCounter = (rows * columns) / 5;
						blockCounter = (rows * columns) / 20;
						break;
						
					case NORMAL:
						pathCounter = (rows * columns) / 10;
						blockCounter = (rows * columns) / 10;
						break;
						
					case HARD :
						pathCounter = (rows * columns) / 15;
						blockCounter = (rows * columns) / 7;
						break;
						
						
					case EXTREME :
						pathCounter = (rows * columns) / 20;
						blockCounter = (rows * columns) / 5;
						break;
						}
			}
			
			else if((rows * columns) < 2000){
					switch(difficulty){
					case EASY:
						pathCounter = (rows * columns) / 15;
						blockCounter = (rows * columns) / 60;
						break;
						
					case NORMAL:
						pathCounter = (rows * columns) / 30;
						blockCounter = (rows * columns) / 30;
						break;
						
					case HARD :
						pathCounter = (rows * columns) / 45;
						blockCounter = (rows * columns) / 20;
						break;
						
						
					case EXTREME :
						pathCounter = (rows * columns) / 60;
						blockCounter = (rows * columns) / 15;
						break;
						}
		}
			else {
				switch(difficulty){
					case EASY:
						pathCounter = (rows * columns) / 30;
						blockCounter = (rows * columns) / 120;
						break;
						
					case NORMAL:
						pathCounter = (rows * columns) / 60;
						blockCounter = (rows * columns) / 60;
						break;
						
					case HARD :
						pathCounter = (rows * columns) / 90;
						blockCounter = (rows * columns) / 40;
						break;
						
						
					case EXTREME :
						pathCounter = (rows * columns) / 120;
						blockCounter = (rows * columns) / 30;
						break;
				}
			}					
	//	System.out.println("Dies ist der " + turnCounter + ". Durchlauf");	
	//	turnCounter++;
		
		loopCounter = (rows * columns) * 10 ;
		row = rnd.nextInt((rows - 2)) +1;
		column = rnd.nextInt((columns - 2)) +1;
		isDone = false;
				
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				fields[i][j] = FieldStatus.EMPTY;
			}
		}

		fields[row][column] = FieldStatus.SPAWN;
		FieldStatus lastStatus = FieldStatus.SPAWN;

		while(isDone == false) {
			 direction = rnd.nextInt(3);
			try {
				switch (direction){
					case 0:
						if (fields[row+3][column] == FieldStatus.EMPTY ) {
							fields[row+2][column] = FieldStatus.PATH_DOWN;
							fields[row+1][column] = FieldStatus.PATH_DOWN;
							row += 2;
							lastStatus = FieldStatus.PATH_DOWN;
							isDone = true;
						}
						break;
					case 1:
						if (fields[row][column+3] == FieldStatus.EMPTY ) {
							fields[row][column+2] = FieldStatus.PATH_RIGHT;
							fields[row][column+1] = FieldStatus.PATH_RIGHT;
							column += 2;
							lastStatus = FieldStatus.PATH_RIGHT;
							isDone = true;
						}
						break;
					case 2:	
						if (fields[row-3][column] == FieldStatus.EMPTY) {
							fields[row-2][column] = FieldStatus.PATH_UP;
							fields[row-1][column] = FieldStatus.PATH_UP;
							row -= 2;
							lastStatus = FieldStatus.PATH_UP;
							isDone = true;
						}
						break;
					case 3:
						if(fields[row][column-3] == FieldStatus.EMPTY ){
							fields[row][column-2] = FieldStatus.PATH_LEFT;
							fields[row][column-1] = FieldStatus.PATH_LEFT;
							column -= 2;
							lastStatus = FieldStatus.PATH_LEFT;
							isDone = true;
						}
						break;
				}
			}
			catch (ArrayIndexOutOfBoundsException ex) {
				
			}
		}

		while (pathCounter > 0 && loopCounter > 0) {
			 pathVar = rnd.nextInt(3);
			 loopCounter--;
			try {
				switch(pathVar) {
					case 1:
						if (fields[row+2][column] == FieldStatus.EMPTY && fields[row+1][column] == FieldStatus.EMPTY ) {
							fields[row+1][column] = FieldStatus.PATH_DOWN;
							fields[row][column] = FieldStatus.PATH_DOWN;
							row += 1;
							lastStatus = FieldStatus.PATH_DOWN;
							pathCounter--;
						}
						break;
					case 0:
						if (fields[row][column+2] == FieldStatus.EMPTY && fields[row][column+1] == FieldStatus.EMPTY ) {
							fields[row][column+1] = FieldStatus.PATH_RIGHT;
							fields[row][column] = FieldStatus.PATH_RIGHT;
							column = column + 1;
							lastStatus = FieldStatus.PATH_RIGHT;
							pathCounter--;
						}
						break;
					case 2:
						if (fields[row-2][column] == FieldStatus.EMPTY && fields[row-1][column] == FieldStatus.EMPTY) {
							fields[row-1][column] = FieldStatus.PATH_UP;
							fields[row][column] = FieldStatus.PATH_UP;
							row -= 1;
							lastStatus = FieldStatus.PATH_UP;
							pathCounter--;
						}
						break;
					case 3:
						if (fields[row][column-2] == FieldStatus.EMPTY && fields[row][column-1] == FieldStatus.EMPTY ) {
							fields[row][column-1] = FieldStatus.PATH_LEFT;
							fields[row][column] = FieldStatus.PATH_LEFT;
							column -= 1;
							lastStatus = FieldStatus.PATH_LEFT;
							pathCounter--;
						}
						break;
				}	
			}
			catch (ArrayIndexOutOfBoundsException ex) {	
			}
		}
	
		fields[row][column] = FieldStatus.TARGET;

		
		while (blockCounter > 0){
			row = rnd.nextInt(rows);
			column = rnd.nextInt(columns) ;
			
			if(fields[row][column] == FieldStatus.EMPTY){
				fields[row][column] = FieldStatus.BLOCKED;
				blockCounter--;
			}			
		}
	} // ende while pathCounter
		
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++)
				sb.append(fields[i][j].getRepresentation());
			sb.append("\n");
		}

		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(MapRandomizer.randomMap(20, 20 ,2,2,Difficulty.EXTREME));
	
	}
}