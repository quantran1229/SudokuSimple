/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;
public class Game {
    private int[][] cells;
    
    public Game()
    {
        cells = new int[9][9];
    }
    
    public void setValue(int row,int column,int value)
    {
        cells[row][column] = value;
    }
    
    public int getValue(int row,int column)
    {
        return cells[row][column];
    }
    
    public boolean check(int row,int column,int value)
    {
        //check row
        for (int i = 0;i < 9;i++)
            if (cells[row][i] == value && i != column)
                return false;
        
        //check column
        for (int i = 0;i < 9;i++)
            if (cells[i][column] == value && i != row)
                return false;
        //check box
        for (int i = (row/3)*3;i < (row/3)*3+3;i++)
            for (int j = (column/3)*3;j < (column/3)*3+3;j++)
                if ((i != row || j != column) && cells[i][j] == value)
                    return false;
        
        return true;
    }
    
    public int[][] getCells()
    {
        return cells;
    }
    
    public void printOut()
    {
        for (int i = 0;i<9;i++)
        {
            for (int j = 0;j<9;j++)
            {
                System.out.print(cells[i][j]);
            }
            System.out.println();
        }
    }
}
