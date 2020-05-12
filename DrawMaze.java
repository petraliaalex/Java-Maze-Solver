/*
Alex Petralia | CSCI 145
Assignment 1 | Java Terminal IO
Authoured: 2-6-15
*/

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class DrawMaze {

   public static final Color WALL_COLOR = Color.RED;
   public static final Color START_COLOR = Color.BLUE;
   public static final Color PATH_COLOR = Color.BLACK;
   public static final Color END_COLOR = Color.ORANGE;
   public static final Color BACKGROUND = new Color(0.9f, 0.9f, 0.9f);
   
//   static int gridWidth;

//   static int gridHeight;
   
//    static String input;
//    
//    static File inputFile;

   // Width of border in pixels
   public static int borderwidth = 40;
   
   // Size of maze cell in pixels
   public static int cellsize = 30;
   
   // Time to sleep after a move (in milliseconds)
   // Change to default to 50. 1000 gives time to observe the program.
   public static int sleeptime = 50;
   
   // The drawing panel
   static DrawingPanel panel = null;

   // Utility routines.
   // celltop computes the y coordinate for the top of a cell in row r
   // (rows are numbered starting at zero.)  cellleft computes the x
   // coordinate for the left of a cell in column c (columns are also
   // numbered from zero. (cellleft(c), celltop(r)) gives the (x, y)
   // coordinates of the top left of a cell.
   static int celltop(int r) {
      return borderwidth + cellsize / 2 * r;
   }
   
   static int cellleft(int c) {
      return borderwidth + cellsize / 2 * c;
   }
   
   // Constants for drawStart
   static final double SZ_WLINE = 0.05;
   static final double SZ_RHEAD = 0.17;
   static final double SZ_YHEAD = 0.25;
   static final double SZ_XCENTR = 0.51;
   static final double SZ_YARM = 0.50;
   static final double SZ_XARM = 0.1;
   static final double SZ_YCROTCH = 0.67;
   static final double SZ_YFEET = 0.90;
   static final double SZ_XFEET = 0.25;
   
   public static void drawStart(int r, int c) {
      Graphics g = panel.getGraphics();
      g.setColor(START_COLOR);
      int top = celltop(r);
      int left = cellleft(c);
      int right = left + cellsize;
      
      int rhead = (int)Math.round(SZ_RHEAD * cellsize);
      int wline = (int)Math.round(SZ_WLINE * cellsize);
      wline = Math.max(1, wline + (1 - wline % 2));
      int yhead = top + (int)Math.round((SZ_YHEAD - SZ_RHEAD) * cellsize);
      int xcentr = left + (int)Math.round(SZ_XCENTR * cellsize);
      int xhead = left + (int)Math.round((SZ_XCENTR - SZ_RHEAD) * cellsize);
      int yneck = top + -1 + (int)Math.round((SZ_YHEAD + SZ_RHEAD) * cellsize);
      int ycrotch = top + (int)Math.round(SZ_YCROTCH * cellsize);
      int yarm = top + (int)Math.round(SZ_YARM * cellsize);
      int xarm = (int)Math.round(SZ_XARM * cellsize);
      int xfeet = (int)Math.round(SZ_XFEET * cellsize);
      int yfeet = top + (int)Math.round(SZ_YFEET * cellsize);
      g.fillOval(xhead, yhead, 2 * rhead, 2 * rhead);
      int crotchleft = xcentr - wline / 2;
      g.fillRect(crotchleft, yneck, wline, ycrotch - yneck);
      g.fillRect(left + xarm, yarm - wline / 2, cellsize - 2 * xarm, wline);
      int leftlegleft = left + xfeet - wline / 2;
      int rightlegleft = left + cellsize - xfeet - wline / 2; 
      Polygon leg = new Polygon(new int[] {crotchleft, crotchleft + wline,
                                           leftlegleft + wline, leftlegleft},
                                new int[] {ycrotch, ycrotch, yfeet, yfeet}, 4);
      g.fillPolygon(leg);
      leg = new Polygon(new int[] {crotchleft, crotchleft + wline,
                                   rightlegleft + wline, rightlegleft},
                        new int[] {ycrotch, ycrotch, yfeet, yfeet}, 4);
      g.fillPolygon(leg);
   }
   
   // Constants for drawEnd
   static final double SZ_RIN = 0.2;
   static final double SZ_ROUT = 0.4;
   static final int N_PTS = 8;
   
   static void drawEnd(int r, int c) {
      Graphics g = panel.getGraphics();
      g.setColor(END_COLOR);
      int x0 = cellleft(c) + cellsize / 2;
      int y0 = celltop(r) + cellsize / 2;
      Polygon star = new Polygon();
      double inradius = SZ_RIN * cellsize;
      double outradius = SZ_ROUT * cellsize;
      
      for (int i = 0; i < N_PTS; i++) {
         double angle = (2 * i * Math.PI) / N_PTS ;
         int x = x0 + (int)Math.round(outradius * Math.cos(angle));
         int y = y0 + (int)Math.round(outradius * Math.sin(angle));
         star.addPoint(x, y);
         angle += Math.PI / N_PTS;
         x = x0 + (int)Math.round(inradius * Math.cos(angle));
         y = y0 + (int)Math.round(inradius * Math.sin(angle));
         star.addPoint(x, y);
      }
      g.fillPolygon(star);
   }

   //draw function
   public static int[] draw(char[][] grid) throws FileNotFoundException {

      int h = cellsize * grid[0].length + 1 + 2 * borderwidth;
      int w = cellsize * grid.length + 1 + 2 * borderwidth;
      
      if (panel == null) {
         panel = new DrawingPanel(w, h);
      }
      panel.clear();
      Graphics g = panel.getGraphics();
      g.setColor(BACKGROUND);
      g.fillRect(0, 0, w, h);
      g.setColor(WALL_COLOR);
      
      char hLine = grid[1][0];
      char vLine = grid[0][1];
      for(int row = 0; row < grid[0].length; row++) {
         for(int col = 0; col < grid.length; col++) {
            if (col % 2 != 0 || col == 1) {
               if (grid[col][row] == hLine) {
                  g.drawLine(cellleft(col-1), celltop(row), cellleft(col+1), celltop(row));
               }
            }else if (row % 2 != 0 || row == 1) {
               if (grid[col][row] == vLine) {
                  g.drawLine(cellleft(col), celltop(row-1), cellleft(col), celltop(row+1));
               }
            }
         } 
      }
      
      int[] myArray = new int[2];
      for (int i = 0; i < grid[0].length; i++) {
         for(int j = 0; j < grid.length; j++) {
            if (grid[j][i] == 'S') {
               drawStart(i-1, j-1);
               myArray[0] = i;
               myArray[1] = j;
            }
            if (grid[j][i] == 'E') {
               drawEnd(i-1, j-1);
               
            }
         }
      }
      return myArray;
   }
   
   public static void move(int r0, int c0, int r1, int c1) {
      Graphics g = panel.getGraphics();
      g.setColor(PATH_COLOR);
      int delta = cellsize / 2;
      g.drawLine(cellleft(c0) + delta, celltop(r0) + delta,
                 cellleft(c1) + delta, celltop(r1) + delta);
      panel.sleep(sleeptime);
   }
   
   public static void unmove(int r0, int c0, int r1, int c1) {
      Graphics g = panel.getGraphics();
      g.setColor(BACKGROUND);
      int delta = cellsize / 2;
      g.drawLine(cellleft(c0) + delta, celltop(r0) + delta,
                 cellleft(c1) + delta, celltop(r1) + delta);
      panel.sleep(sleeptime);
   }

}
