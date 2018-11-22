/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.LinkedList;
import java.util.Random;
import javafx.concurrent.Task;

/**
 *
 * @author downy
 */
public class Controller {
    private class Step
    {
        private int row;
        private int column;
        private int value;
        
        public Step(int r,int c,int v)
        {
            row = r;
            column = c;
            value = v;
        }
        
        public int getRow()
        {
            return row;
        }
        
        public int getColumn()
        {
            return column;
        }
        
        public int getValue()
        {
            return value;
        }
    }
    
    private UI ui;
    
    private Game game;
    
    private Thread timer;
    
    private LinkedList<Step> steps;
    
    public Controller()
    {

    }
    
    public void setUI(UI nUI)
    {
        ui = nUI;
    }
    
    public void startNewGame(int number)
    {
        steps = new LinkedList();
        do{
            game = new Game();
            recursive(game,number);
        } while (!hasSolution(game));
        ui.createBoxes(game.getCells());
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int n = 0;
                while(1>0)
                {
                    ui.updateTimer(n);
                    Thread.sleep(1000);
                    n++;
                }
            }
        };
        timer = new Thread(task);
        timer.setDaemon(true);
        timer.start();
    }
    
    public void stopTimer()
    {
        timer.interrupt();
    }
    
    public boolean recursive(Game game,int n)
    {
        if (n < 0) return true;
        Random random = new Random();
        int row = random.nextInt(9) + 0;
        int column = random.nextInt(9) + 0;
        int value = random.nextInt(9) + 1;
        if (game.getValue(row, column) == 0)
        {
            game.setValue(row, column, value);
            if (game.check(row,column,value))
            {
                return recursive(game,n-1);
            }
            else
            {
                game.setValue(row, column, 0);
                return recursive(game,n);
            }
        }
        else return recursive(game,n);
    }

    private boolean hasSolution(Game game) {
        int[][] cells = new int[9][9];
        for (int i = 0;i<9;i++)
            for (int j = 0;j<9;j++)
                cells[i][j] = game.getValue(i, j);
        return recursiveSolution(cells);
    }

    private boolean recursiveSolution(int[][] cells) {
        for (int row = 0;row < 9;row++)
        {
            for (int column = 0;column < 9; column++)
                if (cells[row][column] == 0)
                {
                    for (int value = 1;value < 10;value++)
                    {
                        boolean check = true;
                        for (int i = 0;i < 9;i++)
                            if (cells[row][i] == value && i != column)
                            {
                                check =  false;
                                break;
                            }
                        //check column
                        if (check)
                        for (int i = 0;i < 9;i++)
                            if (cells[i][column] == value && i != row)
                            {
                                check = false;
                                break;
                            }
                        
                        //check box
                        if (check)
                        for (int i = (row/3)*3;i < (row/3)*3+3;i++)
                        {
                            for (int j = (column/3)*3;j < (column/3)*3+3;j++)
                                if ((i != row || j != column) && cells[i][j] == value)
                                {
                                    check = false;
                                    break;
                                }
                            if (!check) break;
                        }
                        
                        if (check)
                        {
                            cells[row][column] = value;
                            if (recursiveSolution(cells)) return true;
                            cells[row][column] = 0;
                        }
                    }
                    return false;
                }
        }
        return true;
    }

    public void undoStep()
    {
        if (!steps.isEmpty())
        {
            Step undo = steps.pollLast();
            game.setValue(undo.getRow(), undo.getColumn(), undo.getValue());
            ui.changeValue(undo.getRow(), undo.getColumn(), undo.getValue());
            updateCell();
        }
    }
    
    public void addValue(int row, int column, int value) {
        steps.add(new Step(row,column,game.getValue(row, column)));
        game.setValue(row, column, value);
        ui.changeValue(row, column, value);
    }

    public void updateCell() {
        ui.updateCell();
    }
    
    public boolean check(int row,int column)
    {
        return game.check(row, column, game.getValue(row, column));
    }

    public int checkFinish() {
        for (int row = 0;row < 9;row++)
            for (int column = 0;column < 9; column++)
                if (game.getValue(row, column) == 0)
                    return 0;
        timer.interrupt();
        ui.endGame();
        return 1;
    }
    
    public void resetGame()
    {
        if (timer.isAlive()) timer.interrupt();
        startNewGame(28);
    }
    
    public void end()
    {
        timer.interrupt();
    }
}