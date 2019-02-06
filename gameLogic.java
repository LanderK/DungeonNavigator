import java.util.*;
import java.io.*;
import java.lang.Math.*;
/*
GameLogic Method Contains all the rules of the Game and the set of commands and Accessors needed to give players infomation about the map 


*/
public class gameLogic{

	private Map newMap;
	private int xCoordinate , yCoordinate;// Ints to store the current x and y coordinates
	private int currentGold = 0; // int to store the current gold the player has
	private char charIcon; // Char that stores the icon of the player

	public gameLogic(String mapName,char player) throws FileNotFoundException{
		charIcon = player;
		newMap = new Map(mapName);// makes newMap into a new Map Object
		placePlayer();// Places the player randomly on the map
		newMap.printMap(charIcon);//Prints the map with the charIcon as the icon of the player

	}
	//Method to place the player within the bounds of the map
	private void placePlayer(){
	
		//Makes a new Random positiion for x and y
		xCoordinate =  (int)(newMap.getWidth()*Math.random());
		yCoordinate =  (int)(newMap.getLength()*Math.random());
		
		//if the map position of the player is a wall reroll the random number
		while(getArrayValue(xCoordinate,yCoordinate) =='#'){
			xCoordinate = (int)(newMap.getWidth()*Math.random());
			yCoordinate = (int)(newMap.getLength()*Math.random());
		}
		newMap.setPostition(xCoordinate,yCoordinate);//Set the player with the given coordinates
		
	}
	//The Hello Command called when the user Enter 'HELLO'
	public String helloCommand(){
	
		return "GOLD: " + newMap.getGold();//Returns the number of gold needed to exit the map
	
	}
	//The Move Command, called when the user Enters 'MOVE X', its changes the the x or y coordinates deopending on the given X
	public String moveCommand(char Dir){
		//Checks the X of the MOVE X Entry, to work out which coordinate to change and which way the coordinate needs to be changed
		if(Dir == 'N'){
			//check if the tile that the player wanted to move to is a wall
			if(checkValidMove('N')){
				yCoordinate--;
				newMap.setPostition(xCoordinate,yCoordinate);//Gives the new coordinates to the Map
				newMap.printMap(charIcon);//RePrints the Map
				return "Success";//Returns that the move was a success
			}
		}
		else if(Dir == 'S'){
			if(checkValidMove('S')){
				yCoordinate++;
				newMap.setPostition(xCoordinate,yCoordinate);
				newMap.printMap(charIcon);
				return "Success";
			}
			
		}
		else if(Dir == 'W'){
			if(checkValidMove('W')){
				xCoordinate--;
				newMap.setPostition(xCoordinate,yCoordinate);
				newMap.printMap(charIcon);
				return "Success";
			}
			
		}
		else if(Dir == 'E'){
			if(checkValidMove('E')){
				xCoordinate++;
				newMap.setPostition(xCoordinate,yCoordinate);
				newMap.printMap(charIcon);
				return "Success";
			}
			
		}
		newMap.printMap(charIcon);//RePrints the Map
		return "FAIL";//Returns that the move was a unsuccessful
	
	}
	////The Move Command, called when the user Enters 'QUIT'
	public void quitCommand(){
	
		System.out.println("Game finishes");//Tells the user that the game has finshed 
		System.exit(0);//Exits Gracefully
		
	}
	//the pickup command called when the user enters 'PICKUP'
	public int pickupCommand(){
		//Checks if the tiles the player is on is GOLD
		if(getArrayValue(xCoordinate,yCoordinate) == 'G'){
			currentGold++;//adds 1 to the the current count of gold
			changeCurrentTile('.');//replaces the current tile of the map with '.'
			return currentGold; //returns the current gold
		}
		else{
			return -1; //returns -1 to indicate an error has occured since no gold was on the tile
		}
	
	}
	//This method checks if the next tile that the player want to move to is valid
	private boolean checkValidMove(char Move){
		//Checks depedning on the input of the moveCommand wether the coresponding tile is a wall
		//its will return false if a wall is found and true is it dosn't find one
		if(Move == 'N'){
			if(getArrayValue(xCoordinate,yCoordinate-1) =='#'){
				return false;
			}
		}
		if(Move == 'S'){
			if(getArrayValue(xCoordinate,yCoordinate+1) =='#'){
				return false;
			}
		}
		if(Move == 'E'){
			if(getArrayValue(xCoordinate+1,yCoordinate) =='#'){
				return false;
			}
		}
		if(Move == 'W'){
			if(getArrayValue(xCoordinate-1,yCoordinate) =='#'){
				return false;
			}
		}
		
		return true;
	
	
	}
	//The LookCommand, called when the user inputs 'LOOK'
	public char[][] lookCommand(){
		//Creates a new Char array with size 5x5
		char[][] lookArray = new char[5][5];
	
		//Iterates over the array
		for(int i=0;i<lookArray.length;i++){
			for(int j=0;j<lookArray[i].length;j++){
				//Makes the Corner Values of the Array 'X' (Fog of War)
				if((i==0 && j==0)||(i==4 && j==0)||(i==0 && j==4)||(i==4 && j==4)){
					lookArray[i][j] = 'X';
				}
				else{
					//Makes the the other values equals to the return value of the getArrayValue method
					lookArray[i][j] = getArrayValue(xCoordinate-2+j,yCoordinate-2+i);
				} 
				System.out.print(lookArray[i][j]);//prints Each Element (Can disable since not needed,Debug Code
			}
			System.out.print("\n");//prints new line for lookarray, debug Code
		}
		return lookArray;//returns the Array
	
	}
	
	public void canExit(){
		//Check if the current tile is an exit tile
		if(getArrayValue(xCoordinate,yCoordinate)=='E'){
			//checks if the player has enough Gold to exit the map
			if(currentGold>=newMap.getGold()){
				quitCommand();//runs quit command
			} 
		}
	}
	//returns the current gold to user
	public int getCurrentGold(){
	
		return currentGold;
		
	}
	//returns the gold needed to exit the game
	public int getGoldNeeded(){
	
		return newMap.getGold();
	
	}
	//changes the current tile 
	private void changeCurrentTile(char newTile){
	
		newMap.changeCurrentTile('.');
		
	}
	//gets the Value a postition on the Map array
	private char getArrayValue(int xCoordinate,int yCoordinate){
	
		return newMap.getArrayValue(xCoordinate,yCoordinate);
		
	}
	
		
}
	
	
	


