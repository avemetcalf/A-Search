import java.util.Scanner;
import java.util.ArrayList;

public class main {

	 // Create array of nodes
    private Node[][] nodesArray;

    // Create start node
    private Node startNode;

    // Create goal node
    private Node goalNode;

    // Create number boundary for tile
    private int tileBoundary;

    // Create boolean path and set to false
    private boolean pathFound = false;

    // Using array list to create open list
    private ArrayList<Node> openList;

    //Using array list to create closed list
    private ArrayList<Node> closedList;

    // Using array list to create path list
    private ArrayList<Node> finalPath;

 
    public static void main(String[] args) {
        // Create new scanner
        Scanner scanner = new Scanner(System.in);

        // Create new tile board
        AStar tileBoard = new AStar();

        // Print out at beginning of console and open line after
        System.out.println("Starting Board");
        System.out.println("\n");

        //Print out the tiles 
        System.out.println(tileBoard.toString() + "\n");
        // Set play again variable for no to "n"
        char playAgain = 'n';

        // Do while loop
        do {

        	// Tell user to choose a starting row number
            System.out.println("Choose a starting row number");
            System.out.println("\n");

            //Get user input and create variable for starting row 
            System.out.println("Row Number:");
            int startingRow = scanner.nextInt();
 
            // Tell user to choose a starting column number
            System.out.println("Choose a starting column number");
            System.out.println("\n");

            // Get user input and create variable for starting column
            System.out.println("Column Number:");
            int startingColumn = scanner.nextInt();

            // While loop for if starting row and column are unpathable print out an error and ask for new input
            while ((tileBoard.getType(startingRow, startingColumn)).equals(AStar.BLOCKED)) {
                System.out.println("That position is unavailable. Please try again");
               
                // Get user input and create starting row number
                System.out.println("Row Number:");
                startingRow = scanner.nextInt();

                //Get user input and create starting column number
                System.out.println("Choose a starting column number");
                System.out.println("\n");
                System.out.println("Column Number:");
                startingColumn = scanner.nextInt();
                tileBoard.setElement(startingRow, startingColumn, "s");
            }

            // Set element
            tileBoard.setElement(startingRow, startingColumn, "s");
            // Get user input for goal row number
            System.out.println("Choose a goal row number");
            System.out.println("\n");

            // Get user input and create starting row number
            System.out.println("Row Number:");
            int goalRow = scanner.nextInt();

            // Get user input and create starting column number
            System.out.println("Choose a goal column number");
            System.out.println("\n");

            System.out.println("Column Number:");
            int goalColumn = scanner.nextInt();


            // Wile loop for if the tile goal row and column unpathable
            while ((tileBoard.getType(goalRow, goalColumn)).equals(AStar.BLOCKED)) {
            	// Print out error and ask user for a new input
                System.out.println("That position is unavailable. Please try again");

                // Get user input for goal row number
                System.out.println("Row Number:");
                goalRow = scanner.nextInt();

                //Get user input and create starting column number
                System.out.println("Choose a goal column number");
                System.out.println("\n");

                System.out.println("Column Number:");
                goalColumn = scanner.nextInt();
                tileBoard.setElement(goalRow, goalColumn, "g");
            }
            tileBoard.setElement(goalRow, goalColumn, "g");
            // Print out the tile map
            System.out.println("\n\n" + tileBoard.toString());

            // Tiles generate the path from start to goal
            tileBoard.createPath(startingRow, startingColumn, goalRow, goalColumn);

            // Print out solution
            System.out.println("List of Path Nodes" + tileBoard.showPath());
            System.out.println(" ");
            System.out.println("Solution");
            
            // Update the board and print out
            tileBoard.updateTileBoard();
            System.out.println("\n" + tileBoard.tilePathToString());

            // Ask user to play again
            System.out.println("Would you like to play again?");
            System.out.println("Type y for yes or n for no: ");

            playAgain = scanner.next().charAt(0);

            // Rest nodes, paths, start and goal
            tileBoard.resetPath();
            tileBoard.resetNodes();

            System.out.println(" ");

            tileBoard.setElement(startingRow, startingColumn, "~");
            tileBoard.setElement(goalRow, goalColumn, "~");
            
        }
        while (playAgain == 'y' || playAgain == 'Y');
        System.out.println("Thanks for playing");
    }


    /** Constructor for AStar search */
    public main(Node[][] nodeArray, int startRow, int startColumn, int goalRow, int goalColumn, int boundary) {

        // Set boundary
        tileBoundary = boundary;

        // Set node array
        nodesArray = nodeArray;

        // Initialize the open list
        openList = new ArrayList<>();

        // Initialize the closed list
        closedList = new ArrayList<>();

        // Set goal node to goal row and column
        goalNode = nodesArray[goalRow][goalColumn];

        // Set start node to start row and column
        startNode = nodesArray[startRow][startColumn];

        // Set the G of start to zero
        startNode.setG(0);

        // Set the H of start to calc heuristic of start
        startNode.setH(calcHeuristic(startNode));

        // Set the F of start to g+h using method
        startNode.setF();

        // Set the parent of start to null
        startNode.setParent(null);

        // Add start node to open list and search
        openList.add(startNode);
        searchBoard();

    }

    /**
     * Search tiles
     * Pull from open list
     * Generate path
     * Add node to closed list
     */
    public void searchBoard() {

    	// Create node: currentNode
        Node currentNode;

        // While the size of the open list isn't zero set the current node by using the find lowest value
        while (openList.size() != 0) {
            currentNode = findLowestValue();

            // Remove current node from open list
            openList.remove(currentNode);

            // If the current node equals the goal node set pathFound to true
            if (currentNode.equals(goalNode)) {
                pathFound = true;

                // Generate solution path
                createPath();

            }

            // Generate neighbors of current node
            createNeighbors(currentNode);

            // Add current node to closed list
            closedList.add(currentNode);

        }
    }

    /**
     * Generate node neighbors
     * Check if node is valid
     * Calculate cost of moving
     * Set G, H, F and update parent
     * Add node to open list if not there already
     */
    public void createNeighbors(Node node) {
    	
    	// Set the row to node row
        int row = node.getRow();

        // Set the column to node column
        int column = node.getCol();

        // Loop through neighbors
        for (int i = 0; i < 8; i++) {

        	// Create check node
            Node checkNode;
            try {
            	// Go through all for loop and set the node row and column based on surroundings
                // Node is one above | End if i=0
                if (i == 0) {
                    checkNode = nodesArray[row + 1][column];
                }

                // Node is one below | End if i=1
                else if (i == 1) {
                    checkNode = nodesArray[row - 1][column];
                }

                // Node is one to the right | End if i=2
                else if (i == 2) {
                    checkNode = nodesArray[row][column + 1];
                }

                // Node is one to the left | End if i=3
                else if (i == 3) {
                    checkNode = nodesArray[row][column - 1];
                }

                // Node is diagonal bottom left | End if i=4
                else if (i == 4) {
                    checkNode = nodesArray[row - 1][column - 1];
                }

                // Node is diagonal bottom right | End if i=5
                else if (i == 5) {
                    checkNode = nodesArray[row - 1][column + 1];
                }

                // Node is diagonal top left | End if i=6
                else if (i == 6) {
                    checkNode = nodesArray[row + 1][column - 1];
                }

                // Node is diagonal top right | End
                else {
                    checkNode = nodesArray[row + 1][column + 1];
                }

            }
            catch (Exception e) {
                continue;
            }

            // See if the checked node is valid
            if (isValidMove(checkNode)) {

            	// Create a cost of moving and set
                int costOfMoving = node.getG();

                // If less than 4 then add 10 to costOfMoving
                if (i < 4) {
                    costOfMoving = costOfMoving + 10;

                }
                
                // Add 14 to costOfMoving because its diagonal
                else {
                    costOfMoving = costOfMoving + 14;

                }

                if (checkNode.getG() == 0 || (checkNode.getG() > 0 && costOfMoving < checkNode.getG())) {

                	// Set checkNode to costOfMoving based on if statement
                    checkNode.setG(costOfMoving);

                    // Set the H of the checked node to calcHeuristic
                    checkNode.setH(calcHeuristic(checkNode));

                    // Set F of node to checkNode
                    checkNode.setF();

                    // Set parent node to checkNode
                    checkNode.setParent(node);

                    // Add to openList if the open list has no checked node
                    if (!openList.contains(checkNode)) {
                        openList.add(checkNode);
                    }
                }
            }
        }
    }

    /**
     * Generate the path once the goal is reached through backtracking 
     * Add parent nodes to array list
     */
    public void createPath() {
    	// Create a new path variable
        finalPath = new ArrayList<>();

        // Set the currentNode to goalNode
        Node currentNode = goalNode;

        // While the parent of the currentNode isn't null
        // Add the current node to the path and set the parent node
        while (currentNode.getParent() != null) {
            finalPath.add(currentNode);
            currentNode = currentNode.getParent();
        }

        finalPath.add(currentNode);

    }

    /** Get to node path method */
    public ArrayList<Node> getNodePath() {
        return finalPath;
    }

    /** Return if path is not found method*/
    public boolean pathFound() {
        return pathFound;

    }

    /** Return if move is valid, not in closed list, and is pathable  */
    public boolean isValidMove(Node node) {
        if (isWithinBounds(node) && pathable(node) && !closedList.contains(node)) {
            return true;
        }
        else {
            return false;
        }

    }

    /** Check if node is within the bounds of the environment method*/
    public boolean isWithinBounds(Node node) {
    	// Set the row to the node row
        int row = node.getRow();

        // Set the column to the node column
        int column = node.getCol();

        if (row >= 0 && row < tileBoundary && column >= 0 && column < tileBoundary) {
            return true;
        }

        else {
            return false;
        }
    }

    /** Check if node is pathable method*/
    public boolean pathable(Node node) {
    	// if node is pathable return true
        if (node.getType() == "") {					/** FIX */
            return true;
        }
        else {
            return false;
        }
    }

    /** Find the lowest f value method */
    public Node findLowestValue() {
        // if the size of the openlist isnt zero
    	// If the size of openList isn't zero create a lowest node and set openList to 0
        if (openList.size() != 0) {
            Node lowestFNode = openList.get(0);

            // Loop for openList
            for (int i = 1; i < openList.size(); i++) {

            	// If openList is less than lowest F then set to the lowest open list node
                if (openList.get(i).getF() < lowestFNode.getF()) {
                    lowestFNode = openList.get(i);
                }
            }
            return lowestFNode;

        }
        return null;
    }

    /** Calculate the heuristic value of the node method*/
    public int calcHeuristic(Node node) {
    	// Create currentRow and set to node row
        int currentRow = node.getRow();

        // Create currenColumn and set to node column
        int currentColumn = node.getCol();

        // Set the heuristic to 0
        int heuristic = 0;

        // while currentRow is less than goal row
        // Increase the currentRow and add 10 to heuristic
        while (currentRow < goalNode.getRow()) {
            currentRow++;
            heuristic = heuristic + 10;
        }

        // While currentRow is greater than goal row
        // Decrease the currenRow and add 10 to heuristic
        while (currentRow > goalNode.getRow()) {
            currentRow--;
            heuristic = heuristic + 10;
        }

        // While currentColumn is greater than goal column
        // Decrease the currenColumn and add 10 to heuristic
        while (currentColumn < goalNode.getCol()) {
            currentColumn++;
            heuristic = heuristic + 10;
        }

        // While currentColumn is greater than goal column
        // Decrease the currenColumn and add 10 to heuristic
        while (currentColumn > goalNode.getCol()) {
            currentColumn--;
            heuristic = heuristic + 10;
        }
        return heuristic;
    }
}
