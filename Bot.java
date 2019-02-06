import java.util.*;
import java.io.*;
import java.lang.Math.*;
/*
The Bot Class it contians everything that an AI can function to move about the map and knonw what to do in certain conditions

The main Pathfinding algorithm i have used in the ai of the bot is a Breadth First Search, that is used to so the bot can navigate around the walls

*/
public class Bot{

	private char[][] lookArray; // Stores the Array that the will be returned from the lookArrayCommand in gamelogic, this is the only way the bot knows what is around it after each turn 
	private int goaly =0 ,goalx=0; //Stores the postion of the point of interest that the bot wants to walk towards
	private gameLogic Logic;
	 
	public static void main(String[] args)throws FileNotFoundException{
	
		//Welcomes you to the The Dungeon, and prompts the user for input indicating what map they want the bot to navigate
		System.out.println("Welcome to the Dungeon of Doom"+"\n");
		System.out.println("Please Enter Name of Map");
		Scanner inputScanner = new Scanner(System.in);
		int ErrorCount=0; // Int to store the ammount of error occured in the menu screen, this stops the user from continualy entering a wrong map name or a map with incorect formating
		
		while(true){
			try{
				String GivenMap = inputScanner.nextLine();
				System.out.println("Loading..."); // Telling the user that the program is trying to load the map
				Bot Bot1 = new Bot(GivenMap); //makes a new bot
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
	
	public Bot(String mapName)throws FileNotFoundException{
	
		Logic = new gameLogic(mapName,'B'); //Runs the gameLogic class constructor
		AI(); // Runs the Ai Method
	
	
	}
	// The Main Block of AI Code
	public void AI(){
		
		while(true){
			//System.out.println("New Turn"); Debug Code
			//Runs if gold is less than the gold needed to exit the map
			while(Logic.getCurrentGold()<Logic.getGoldNeeded()){
				lookArray = Logic.lookCommand(); //Runs the lookArray of gameLogic
				//checks the tile underneath the bot to see if it contains gold
				if(lookArray[2][2]=='G'){
					int x = Logic.pickupCommand();
					if(x !=-1){
						System.out.println("SUCCESS, GOLD COINDS: "+ x);
					}
				}
				else{
					//System.out.println("trying to move"); Debug Code
					movePosition('G');//Starts the MovePositon method, with parameter indicating looking for Gold
				}
			}
			//Runs if the gold is more or Equal to the gold needed to exit
			while(Logic.getCurrentGold()>=Logic.getGoldNeeded()){
				lookArray = Logic.lookCommand();
				movePosition('E');//Starts the MovePositon method, with parameter indicating looking for an Exit
			}	
		}
	}
	
	//Generates a new Random postion for the bot to move to
	public void randomPostition(){
	
		//System.out.println("Random Postition"); Debug Code
		Random randomNumber = new Random(); // New Random 
		goaly = randomNumber.nextInt(4); // Makes goaly and new Random number between 0 and 4
		goalx = randomNumber.nextInt(4);// Makes goalx and new Random number between 0 and 4
		//check if the postion on the new goal co-ordinates isnt a wall or in the Fog of war of the lookArray and its not the middle tile of lookArray
		while(lookArray[goaly][goalx]=='#' || lookArray[goaly][goalx]=='X' || (goaly==2 && goalx==2)){
			//System.out.println("New Random postion");
			goaly = randomNumber.nextInt(4);
			goalx = randomNumber.nextInt(4);
		}

				
	}
	
	public void movePosition(char goalChar){
		
		//System.out.println("Finding place to move too"); Debug Code
		//Check if it can find atleast one gold in the lookArray
		if(ArraySeach(goalChar)){
			Stack<Character> Movement = BreadthFS(goalChar); //Makes a character stack from the return of BreadthFS
			//checks if the Stack is not empty indicating that no route to the goal was found
			if(!Movement.isEmpty()){
				emptyMovementStack(Movement);// runs the  emptyMovementStack method
			}
			else{
				//creates new RandomPostion
				randomPostition();
				//Redefinees the Movement Stack with new goal character
				Movement = BreadthFS(goalChar);
				emptyMovementStack(Movement);
			}
		}
		else{
			//creates new RandomPostion
			randomPostition();
			//Makes a character stack from the return of BreadthFS
			Stack<Character> Movement = BreadthFS(goalChar);
			emptyMovementStack(Movement);
		}
	
	}
	//Uses the Stack generated in findRoute method to move the bot around the map
	public void emptyMovementStack(Stack<Character> Movement){
		
		//System.out.println("MOVING"); Debug Code
		//Runs while the Stack has elements in it
		while(!Movement.isEmpty()){
			char moveChar = Movement.pop();//Sets the next move equal to the top element of the stack
			System.out.println("Move "+moveChar+": "+Logic.moveCommand(moveChar)); // Prints a Message indicationg the movement of the bot, while runing gameLogics moveCommand with movechar parameter
			Logic.canExit();//checks if the bot can exit on current tile
			wait(2); //waits 2 seconds
		}
		
	
	}
	
	public boolean ArraySeach(char goalChar){
	
	//Searchs for the GoalChar in the lookArray array
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(lookArray[i][j]==goalChar){
					//sets the goal coordinates of the goalChar found  and returns true since a gold/exit was found
					goaly = i;
					goalx = j;
					return true;
					
				}
			}
		}
		//no gold/exit found so return false
		return false;
	}
	
	//the Main Method for the Breadth First Search
	//It Starts From the Starting postion and marks the distance to it as 1
	//it then looks at all of the neighbors of the curent tile and marks then with a corresponding distance
	//this loops until it find the goal co-ordinates or finds a goalChar closer to the start than the goal co-ordinates
	public Stack<Character> BreadthFS(char goalChar){
	
		Queue <int[]> openQueue = new LinkedList<int[]>();// Makes a queue of int arrays (Nodes) that store the coordinates of tiles in lookArray,
		int[][] BFSArray = new int[5][5];// New Int array to store the distances to the tiles from the stating position 
		int[] start = {2,2};// starting postion
		openQueue.add(start);// adds the starting positions to the queue
		boolean foundRoute = false ;// Boolean to indicate that the goal was found
		BFSArray[2][2] = 1;//sets the starting position value of BFSArray equal to 1 
		
		//while the queue has elements in it
		while(!(openQueue.size()==0)){
			int[] Node = openQueue.remove();//remove the first element
			//checks if the Coordinates of the Node is equal to the the goal Coordinates or its lookArray element is also a goal character 
			if((Node[0]== goaly && Node[1]==goalx) || lookArray[Node[0]][Node[1]]==goalChar){
				openQueue.clear();//Empty the Queue
				//System.out.println("Found Path"); Debug Code
				foundRoute = true;
			}
			else{
				for(int i=-1;i<2;i++){
					//Makes a new neighborNode 
					int[] neighborNodey ={Node[0]+i,Node[1]};
					//checks the if the Neighbor Node ( Y Co-ordinate Neighbors) in in the bounds of a 5x5 array, that it hasnt been already marked in the BFSArray , and its Lookarray value isnt a wall or X 
					if((Node[0]+i<5 && Node[0]+i>=0) && (BFSArray[Node[0]+i][Node[1]]==0)&& (lookArray[Node[0]+i][Node[1]]!='#')&& (lookArray[Node[0]+i][Node[1]]!='X')){
						openQueue.add(neighborNodey);//Adds the Neighbor to the Queue
						BFSArray[neighborNodey[0]][neighborNodey[1]] = BFSArray[Node[0]][Node[1]]+1; //Marks the Neighbor Nodee  by seting the BFSArray value to the the value of the Parent Node +1
					}
					int[] neighborNodex ={Node[0],Node[1]+i};
					//checks the if the Neighbor Node (X Co-ordinate Neighbors) in in the bounds of a 5x5 array, that it hasnt been already marked in the BFSArray , and its Lookarray value isnt a wall or X 
					if((Node[1]+i<5 && Node[1]+i>=0) && (BFSArray[Node[0]][Node[1]+i]==0)&& (lookArray[Node[0]][Node[1]+i]!='#')&& (lookArray[Node[0]][Node[1]+i]!='X')){
						openQueue.add(neighborNodex);
						BFSArray[neighborNodex[0]][neighborNodex[1]] = BFSArray[Node[0]][Node[1]]+1; //Marks the Neighbor Nodee  by seting the BFSArray value to the the value of the Parent Node +1
					}
				}
			}
			
		}
		return findRoute(BFSArray,foundRoute);//Returns a Stack by Running the findRoute method with the completed BFSArray and the foundRoute boolean
	
	
	}
	//Finds the route for the Bot to traverse, given the BFSArray that contains the distance to each tile
	public Stack<Character> findRoute(int[][] BFSArray, boolean foundRoute){
	

		Stack<Character> MovementStack = new Stack<Character>();//New Characater Statck to store the move 
		//checkArray(BFSArray); Debug Code
		//checks if the foundRoute is true so it know if it needs to search for a route or return an empty stack
		if(foundRoute){
			//System.out.println("Calculating Route to ("+goaly+","+goalx+") "+BFSArray[goaly][goalx]+" Tiles"); Debug Code
			// Starts from the goal co-ordinates in the BFSArray and Searchs its neighbors for the first number that is one less than it, when found cit changes the goal co-ordinates to the tile it just found
			// and adds a Movement char to the stack, that is in the oposite direction of the way the goal cordinates moved
			// this continues until the start value in the BFSArray is reached eg 1
			for(int i=BFSArray[goaly][goalx];i>1;i--){
				if((goaly+1<=4) && (BFSArray[goaly+1][goalx]==i-1)){
					goaly++;
					MovementStack.add('N');
				}
				else if((goaly-1>=0) && BFSArray[goaly-1][goalx]==i-1){
					goaly--;
					MovementStack.add('S');
				}
				else if((goalx+1<=4) && BFSArray[goaly][goalx+1]==i-1){
					goalx++;
					MovementStack.add('W');
				}
				else if((goalx-1>=0) && BFSArray[goaly][goalx-1]==i-1){
					goalx--;
					MovementStack.add('E');
				}
				//System.out.println(MovementStack); Degug Code
			}
			//System.out.println(MovementStack); Degug Code
			return MovementStack;//Returns the stack
		}
		else{
			//System.out.println("No Route Found"); Degug Code
			return MovementStack;//Returns the stack
		}

	}
	
	//wait command used to delay the the proccess of the bot, so the user can read what the bot is doing (This Method was given to us by one of the Lab Exercises in CM10227)
	public void wait (int k){
	// sets two longs to value 0
	long time0=0; 
	long time1=0;

	time0 = System.currentTimeMillis();// sets the first long value to the Current Time in Milliseconds 

	//Updates time1 to the current Time in Milliseconds each time the new time is more than 1000 Milliseconds times the given constant 
	do{
		time1 = System.currentTimeMillis();
	}
	while ((time1-time0) < k * 1000);

	}
	

	//Debug method used to check the BFSArray and the values in it
	/*public void checkArray(int[][] BFSArray){
	
		System.out.println("");
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				System.out.print(BFSArray[i][j]);
			}
			System.out.print("\n");
		}
	
	}*/
	


}