import java.io.*;
import java.util.*;
/*
The Map Class, Contains all infomation about the map that could be usefull for both the logic of the game and to the players
Its main purpose is to read the map file and generate a Map array from it 

*/
public class Map {
	
	private char[][] mapArray ; //Int Array to store the Map into
	private int Length, Width; //Ints to store the length and width of the map
	private Scanner mapScanner ; // Scanner that will be used to scan the map file
	private File mapFile; // the Map File
	private int goldNeeded;// Int to store the gold Needed
	private int currentX = 0, currentY = 0; // Current position of the player

	//Map Constructor
	public Map(String mapName) throws FileNotFoundException{
	
		mapFile = new File(mapName+".txt");//Creates new File equal to the name given by the user + .txt
		readMap();//Runs the readMap Method 
		mapArray = new char[Length][Width]; // Makes mapArray equals to a new char array with size length*width
		fillArray();//Runs the fillArray method
	
	}
	//Fills ths array with the coresponding elements in the map file 
	private void fillArray() throws FileNotFoundException{
		
		mapScanner = new Scanner(mapFile);//Scanner for the mapFile
		//skips the lines with the name and Gold needed on them since they are not needed in the array
		mapScanner.nextLine();
		mapScanner.nextLine();
		//Itterates over the Array filling in the corresponing values of the Array with elements from the MapFile
		for(int i=0;i<mapArray.length;i++){
			String currentLine = mapScanner.nextLine();//sets String equal to the current line
			//interates over the String 
			for(int j=0;j<currentLine.length();j++){
				mapArray[i][j] =  currentLine.charAt(j);//Fills the Array with each character of the currentLine String
			}
		}
		mapScanner.close();//Closes the scanner
	
	}
	//reads the Map file to determine the Length, Width of the Map and gold needed to win 
	private void readMap() throws FileNotFoundException{
	
	mapScanner = new Scanner(mapFile);//Scanner for the MapFile
		//sets the Length and width = 0
		Length = 0;
		Width = 0;
		mapScanner.nextLine();//Skip a line 
		
		//Finds the Next int on the current line (will throw exection if it cant find it )
		while(!mapScanner.hasNextInt()){
			mapScanner.next();
		}
		//makes goldNeeded = to the int found in the file
		goldNeeded = mapScanner.nextInt();
		mapScanner.nextLine();//Moves to the next line in the File
		
		//Interates Over Each Line for each line the file still has in the file
		while(mapScanner.hasNextLine()){
			Length++;//Increases length for each line
			Width = mapScanner.nextLine().length(); // sets width to the length of that line
		}
		mapScanner.close();//closes the scanner
	}
	//Prints the Maps,so the user can see it
	public void printMap(char charIcon){
	
		//iterrates over each Element of the array printing them then when a line is complete print a new line statement
		for(int i=0;i<mapArray.length;i++){
			for(int j=0;j<mapArray[i].length;j++){
				//if the i and j are equal to the coordinates of the player print the charIcon instead of the Map Element
				if(i==currentY && j==currentX){
					System.out.print(charIcon);
				}
				else{
					System.out.print(mapArray[i][j]);
				}
				
			}
			System.out.print("\n");
		}
	
	}
	//Returns the value of an element at a set place in the array
	public char getArrayValue(int x, int y) {
		try{
			return mapArray[y][x];//Returns the element
		}
		//if x or y where out of bounds return '#', this is used for the lookCommand in gameLogic to form an infitie wall if the players looks outside the edge of the map
		catch(ArrayIndexOutOfBoundsException e){
			return '#';
		}
	}
	//returns the width of the Map
	public int getWidth(){
	
		return Width;
	
	}
	//returns the length of the Map
	public int getLength(){
	
		return Length;
	
	}
	//sets the Current co-ordinates of the player
	public void setPostition(int x, int y){
		currentX = x ;
		currentY = y;
	}
	//returns the Gold needed to win 
	public int getGold(){
	
		return goldNeeded;

	}
	// Lets gameLogic change the current tile the player is on(used in pickupCommand)
	public void changeCurrentTile(char newTile){
	
		mapArray[currentY][currentX] = newTile;
	
	}
	




}