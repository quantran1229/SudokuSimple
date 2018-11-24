/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;

public class Game {
    private int[][] cells;
    private ArrayList[][] guess;
    
    public Game()
    {
        cells = new int[9][9];
        guess = new ArrayList[9][9];
        for (int i = 0;i<9;i++)
            for (int j = 0;j < 9;j++)
            {
                guess[i][j] = new ArrayList();
            }
    }
    
    public void setValue(int row,int column,int value)
    {
        cells[row][column] = value;
    }
    
    public int getValue(int row,int column)
    {
        return cells[row][column];
    }
    
    public void setGuessValue(int row,int column,int value)
    {
        if (value != 0)
        {
            if (guess[row][column].contains(value))
            {
                guess[row][column].remove(guess[row][column].indexOf(value));
            }
            else
            {
                guess[row][column].add(value);
            }
        }
        else
        {
            guess[row][column].clear();
        }
        System.out.println(guess[row][column].toString());
    }
    
    public void setGuessValue(int row,int column,int[] values)
    {
        guess[row][column].clear();
        for(int i = 0;i < values.length;i++)
        {
            if (values[i] != 0)
            {
                guess[row][column].add(values[i]);
            }
            else
            {
                guess[row][column].clear();
            }
        }
    }
    
    public ArrayList getGuessValue(int row,int column)
    {
        return guess[row][column];
    }
    
    public ArrayList[][] getGuess()
    {
        return guess;
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
