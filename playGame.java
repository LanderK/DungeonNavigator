import java.util.*;
import java.io.*;
import java.lang.Math.*;
/*The playGame Class contains evething a real player will need to play Dungeon of Doom

*/
public class playGame{

	private static Scanner inputScanner = new Scanner(System.in);///Scanner to take userinput
	private gameLogic Logic;

	public static void main(String[] args) throws FileNotFoundException{
		
		int ErrorCount=0;
		//Welcomes you to the The Dungeon, and prompts the user for input indicating what map they want to navigate
		System.out.println("Welcome to Dungeon of Doom"+"\n");
		System.out.println("Please Enter Name of Map");
		
		while(true){
			try{
				playGame Game = new playGame(getMapName());// starts the game with a map name gotton back from the getMapName method
			}
			catch(FileNotFoundException e){
				if(ErrorCount++<5){
					System.out.println("Error: No map exists with that name \nPlease enter a map that exists:"); //Printing error messages to tell the use than no map was found 
				}
				else{
				//Informs user than the progam will exit, since too many error have happened on the main Menu
					System.out.println("Too many Errors\nExiting...");
					System.exit(0);
				}
				}
			catch(NoSuchElementException e){
				if(ErrorCount++<5){
					System.out.println("Error: Map is in the wrong format \nPlease enter a map with the correct Format:"); //Printing error messages to tell that the given map is of the wrong format
				}
				else{
				//Informs user than the progam will exit, since too many error have happened on the main Menu
					System.out.println("Too many Errors\nExiting...");
					System.exit(0);
				}
				
			}
		}
		
		
	
	
	}
	
	public playGame(String mapName) throws FileNotFoundException{
	
		Logic = new gameLogic(mapName,'P');//makes new Object of Gamelogic
		String command;//String to store the most recent input of the user
		
		
		while(true){
		
			command = inputScanner.nextLine();//Makes the command string equal to the most recent user input from the command line 
			//When the User Enters Hello Run the helloCommand from gamelogic
			if(command.toUpperCase().equals("HELLO")){
				System.out.println(Logic.helloCommand());
			}
			//When the User enters Move 'X' run moveCommand from gamelogic with parameters as X  and check if it can exit using the canExit method in gamelogic
			else if(command.toUpperCase().equals("MOVE N")){
				System.out.println(Logic.moveCommand('N'));
				Logic.canExit();
			}
			
			else if(command.toUpperCase().equals("MOVE E")){
				System.out.println(Logic.moveCommand('E'));
				Logic.canExit();
			}
			
			else if(command.toUpperCase().equals("MOVE S")){
				System.out.println(Logic.moveCommand('S'));
				Logic.canExit();
			}
			
			else if(command.toUpperCase().equals("MOVE W")){
				System.out.println(Logic.moveCommand('W'));
				Logic.canExit();
			}
			//When the User Enters Pickup Run the pictkup Command from gameLogic
			else if(command.toUpperCase().equals("PICKUP")){
				int x = Logic.pickupCommand();// make set an int equals to the return of pickupCommand
				if(x ==-1){
					System.out.println("FAIL");//Message telling the user that it failed to pick up anything
				}
				else{
					System.out.println("SUCCESS, GOLD COINDS: "+ x);//message telling the user that it successfully pick up gold and how many gold pieces the player is currently carrying
				}
			}
			//When the User Enters Look, Run the lookCommand from gameLogic
			else if(command.toUpperCase().equals("LOOK")){
				Logic.lookCommand();
			}
			//When the User Enters Quit Run the quitCommand from gameLogic
			else if(command.toUpperCase().equals("QUIT")){
				Logic.quitCommand();
			}
			//the user has enter nothing or something the that the program doesnt understand so prints an error message telling them
			else{
				System.out.println("Error: No command with that name");
			}
		
		}
		
		
	}
	// Methods that get the map name from the user
	private static String getMapName(){
	
		String GivenName = inputScanner.nextLine();
		System.out.println("Loading...");
		return GivenName;
	
	}
	


}