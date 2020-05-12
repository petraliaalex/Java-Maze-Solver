/*
Alex Petralia | CSCI 145
Assignment 1 | Java Terminal IO
Authoured: 2-6-15
*/

import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class MazeSolver {

   public static int borderwidth = 40;
      
   public static int cellsize = 30;
   
   public static int sleeptime = 0;
   
   static String input;
   
   static File inputFile;
   
   public static int gridWidth;
   
   public static int gridHeight;
   
   public static char[][] grid;
   
   public static int[] myArray;
   
   static final String usage = "Usage: input_file_name cell_size border_width sleep_time";
   
   //Handle the Scanner input
   public static void processInput() throws FileNotFoundException {
      Scanner input = new Scanner(inputFile);
      String line = input.nextLine();
      System.out.println(line);
   }

   // Run and print a soluton for DrawMaze
   public static void main(String[] args) throws FileNotFoundException {
      if (handleArguments(args)) {
         Scanner input = new Scanner(inputFile);
         int h = input.nextInt();
         int w = input.nextInt();
         int gridWidth = 2*w+1;
         int gridHeight = 2*h+1;
         grid = readMazeFile(args);
         DrawMaze.draw(grid);
         int[] myArray = DrawMaze.draw(grid);
         if (solveMaze(grid, myArray[0], myArray[1], myArray[0], myArray[1])) {
            System.out.println("Solved!");
         } else {
            System.out.println("Maze has no solution.");
         }
      }
   }
   
   // Handle the input arguments
   static boolean handleArguments(String[] args) {
   
      if (args.length < 1 || args.length > 4) {
         System.out.println("Wrong number of command line arguments.");
         System.out.println(usage);
         return false;
      }
      try {
         if (args.length == 2) {
            cellsize = Integer.parseInt(args[1]);
         }
         if (args.length == 3) {
            borderwidth = Integer.parseInt(args[2]);
         }
         if (args.length == 4) {
            sleeptime = Integer.parseInt(args[3]);
         }
      } 
      catch (NumberFormatException ex) {
         System.out.println("input_file_name must be a file name.");
         System.out.println(usage);
         return false;
      }
      
   
      inputFile = new File(args[0]);
      if (!inputFile.canRead()) {
         System.out.println("The file " + args[0] + " cannot be opened for input.");
         return false;
      }
      return true;
   }
   
   
   
   // Read the file describing the maze.
   
   static char[][] readMazeFile(String[] args) throws FileNotFoundException {
      Scanner input = new Scanner(inputFile);
      int h = input.nextInt();
      int w = input.nextInt();
      int gridWidth = 2*w+1;
      int gridHeight = 2*h+1;
      char[][] grid = new char[gridWidth][gridHeight];
      
      
      input.nextLine();
      for(int i = 0; i < 2*h+1; i++) {
         String line = input.nextLine();
         int charCount = 0;
         for(int j = 0; j <= line.length(); j++) {
            int b = line.length();
            if (charCount <= line.length()) {
               if (charCount == line.length()) {
                  charCount = 0;
                  System.out.println("");
               } else {
                  grid[j][i] = line.charAt(charCount);
                  charCount++;
                  System.out.print(grid[j][i]);
               }
            }
         }
      }
      return grid;
   }

   

   // Solve the maze. 
   // I used '@' to mark every spot that has already been moved to so the program will try a different path when backtracking. I also used
   // "drawStart" to mark spaces that have already visited when looking at the maze being executed so I could debug the program correctly.
   static boolean solveMaze(char[][] grid, int oldRow, int oldCol, int newRow, int newCol) {
      
      DrawMaze.move(oldRow - 1, oldCol - 1, newRow - 1, newCol - 1);
      
      if(grid[newCol][newRow] == 'E') {
         return true;
      }
      
      grid[oldCol][oldRow] = '@';
      
      
      
      //east
      if (newCol != grid.length && grid[newCol + 1][newRow] != '|' && (grid[newCol + 2][newRow] == ' ' || grid[newCol + 2][newRow] == 'E')) {
         grid[newCol][newRow] = '@';
         if(solveMaze(grid, newRow, newCol, newRow, newCol + 2)) {
            return true;
         }
      }
      //north
      if (newRow != 1 && grid[newCol][newRow - 1] != '-' && (grid[newCol][newRow - 2] == ' ' || grid[newCol][newRow -2] == 'E')) {
         grid[newCol][newRow] = '@';
         if(solveMaze(grid, newRow, newCol, newRow - 2, newCol)) {
            return true;
         }   
      }
      //west
      if (newCol != 1 && grid[newCol - 1][newRow] != '|' && (grid[newCol - 2][newRow] == ' ' || grid[newCol - 2][newRow] == 'E')) {
         grid[newCol][newRow] = '@';
         if(solveMaze(grid, newRow, newCol, newRow, newCol - 2)) {
            return true;
         }   
      }
      //south
      if (newRow != grid[0].length && grid[newCol][newRow + 1] != '-' && (grid[newCol][newRow + 2] == ' ' || grid[newCol][newRow + 2] == 'E')) {
         grid[newCol][newRow] = '@';
         if(solveMaze(grid, newRow, newCol, newRow + 2, newCol)) {
            return true;
         }   
      }
      
      //unmove
      DrawMaze.unmove(oldRow - 1, oldCol - 1, newRow - 1, newCol - 1);
      grid[newCol][newRow] = '@';
      //DrawMaze.drawStart(newRow - 1, newCol - 1);
      return false;
      
   }
}
