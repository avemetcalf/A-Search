import java.util.ArrayList;

public class AStar {

	 // Create a Node array list named finalPath
    private ArrayList<Node> finalPath;

    // blocked represented by "x", pathable represented by "-"
    public static String BLOCKED = "x";
    public static String PATHABLE = "-";

    // Tile String Array
    private String[][] tileBoard;
    // Tile String Array path
    private String[][] tilePath;
    // Node Array
    private Node[][] nodeArray;

    /** Tile Construct */
    public AStar() {
        // Tile board set to 15x15
        tileBoard = new String[15][15];
        createTileBoard();
        // Node Array set to 15x15
        nodeArray = new Node[15][15];
        createNodes();
        // Path set to 15x15
        tilePath = new String[15][15];
    }

    /** Create Tile Method */
    public void createTileBoard() {
        //Loop through rows and columns
        for (int i = 0; i < 15; i++) {
            // Loop through rows and Columns again
            for (int j = 0; j < 15; j++) {
                // random variable
                double random = Math.random();
                // If probability is less than 10% set to Blocked
                if (random < 0.10) {
                    tileBoard[i][j] = BLOCKED;
                }
                  // If probability is more than 10% set to Pathable
                else {
                    tileBoard[i][j] = PATHABLE;
                }
            }
        } 
    }

    /** Create Nodes Method*/
    public void createNodes() {
        // Loop through rows and columns
        for (int i = 0; i < 15; i++) {
            // Loop through rows and columns again
            for (int j = 0; j < 15; j++) {
                // Set to pathable value if its pathable
                int type;
                if (tileBoard[i][j].equals(PATHABLE)) {
                    type = Node.PATHABLE;
                }
                  // Set to unpathable if not
                else {
                    type = Node.UNPATHBALE;
                }
                // Node type is set
                nodeArray[i][j] = new Node(i, j, type);
            } 
        } 
    }

    public void createPath(int startRow, int startColumn, int goalRow, int goalColumn) {
        // New A* Search
        main search = new main(nodeArray, startRow, startColumn, goalRow, goalColumn, 15);

        // Return path if found in search
        if (search.pathFound()) {
            finalPath = search.getNodePath();
        }
        // Set to null if path not found in search
        else {
            finalPath = null;
        } 
    }

    public String showPath() {
        // if the path is null then return that path cannot be found
        if (finalPath == null) {
            return "The Path cannot be found";
        }
        // else it loops through path size
        else {
            String result = " ";
            for (int i = finalPath.size() - 1; i >= 0; i--) {
                // return the result string
                result = result + finalPath.get(i).toString() + " ";
            }
            return result;
        }
    }
    public void updateTileBoard() {
        // reset the tile path
        resetTilePath();
        // if the path isn't null then create a counter for the path aka "1"
        if (finalPath != null) {
            int counter = 1;
            // loop through the path size
            for (int i = finalPath.size() - 1; i >= 0; i--) {
                // Get final path
                Node nextNode = finalPath.get(i);
               // Get row
                int row = nextNode.getRow();
                // Get column
                int column = nextNode.getCol();
                // Set path and increase counter
                tilePath[row][column] = " " + counter;
                counter++;
            }
        }
    }
    public void resetTilePath() {
        // Loop for rows
        for (int i = 0; i < 15; i++) {
            // Loop for columns
            for (int j = 0; j < 15; j++) {
            	// Set path of tile to first tile board
                tilePath[i][j] = tileBoard[i][j];
            }
        }
    }

    public void resetNodes() {
        // Create new node and node method
        nodeArray = new Node[15][15];
        createNodes();
    }

    public void resetPath() {
        // Clear the path
        finalPath.clear();
    }

    public String[][] getTileBoard() {
        // Method to return the tile board
        return tileBoard;
    }

    public String[][] getTilePath() {
        // Method to return the tile path
        return tilePath;
    }

    public String getType(int row, int column) {
        // Return the rows and columns for the tile board
        return tileBoard[row][column];
    }

    public void setElement(int row, int column, String symbol) {
        //Set tile board to symbol
        tileBoard[row][column] = symbol;
    }

    public String toString() {
        // create empty result string
        String result = "";
        result = result + "\t";
        // For loop to loop through the map
        for (int i = 0; i < 15; i++) {
            result = result + i + "\t";
        }

        result = result + "\n";

        // Loop through rows and add to result
        for (int i = 0; i < 15; i++) {
            result = result + i + "\t";
            // Loop through columns and add to result
            for (int j = 0; j < 15; j++) {
                result = result + tileBoard[i][j] + "\t";
            }

            result = result + "\n";
        }

        return result;
    }

    public String tilePathToString() {
    	// Create an empty result string and new line
        String result = "";
        result = result + "\t";
        // For loop to loop through map and add it to the result
        for (int i = 0; i < 15; i++) {
            result = result + i + "\t";
        }

        result = result + "\n";

        // Loop through the rows and add to result
        for (int i = 0; i < 15; i++) {
            result = result + i + "\t";
            // Loop through the columns and add to result
            for (int j = 0; j < 15; j++) {
                result = result + tilePath[i][j] + "\t";
            }
       
            result = result + "\n";
        }

        return result;
    }
}
