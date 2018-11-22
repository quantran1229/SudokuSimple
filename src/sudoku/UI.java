package sudoku;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author downy
 */
public class UI {
    private class Box extends StackPane
    {
        boolean Default = false;
        public Box(int column,int row,int value)
        {
            super();
            this.setPrefHeight(40);
            this.setPrefWidth(40);
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.2))));
            Text text = new Text();
            text.setFill(Color.BLACK);
            text.setFont(Font.font("Verdana", 15));
            if (value != 0) 
            {
                text.setText(value+"");
                Default = true;
            }
            this.getChildren().add(text);
        }
        
        public void changeValue(int value)
        {
            if (!Default)
            {
                this.getChildren().clear();
                Text text = new Text();
                text.setFont(Font.font("Verdana", 15));
                if (value != 0)
                {
                    text.setText(value+"");
                }
                this.getChildren().add(text);
            }
        }
        
        public void changeWrongValue()
        {
            if (!Default)
            {
                String oldText = ((Text)this.getChildren().get(0)).getText();
                this.getChildren().clear();
                Text text = new Text();
                text.setFill(Color.RED);
                text.setFont(Font.font("Verdana", 15));
                text.setText(oldText);
                this.getChildren().add(text);
            }
        }
        
        public void changeOKValue()
        {
            if (!Default)
            {
                String oldText = ((Text)this.getChildren().get(0)).getText();
                this.getChildren().clear();
                Text text = new Text();
                text.setFill(Color.BLUE);
                text.setFont(Font.font("Verdana", 15));
                text.setText(oldText);
                this.getChildren().add(text);
            }
        }
        
        public void clear()
        {
            this.getChildren().clear();
        }
        
        public boolean getDefault()
        {
            return Default;
        }
    }
    
    private class Box3x3<T extends Pane> extends GridPane
    {
        public Box3x3(T[][] pane)
        {
            super();
            this.setVgap(0);
            this.setHgap(0);
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.5))));
            for (int i = 0;i < pane.length;i++)
                for (int j = 0;j < pane[i].length;j++)
                {
                    this.add(pane[i][j],j,i);
                }
        }
    }
    
    //private class field
    private final Controller controller;
    private final Stage stage;
    private Box[][] boxes;
    private Box3x3[][] hugeBoxes;
    private Box3x3 mainBox;
    private BorderPane mainPane;
    private Label labelTimer;
    
    
    public UI(Stage stage,Controller con)
    {
        controller = con;
        this.stage = stage;
        controller.setUI(this);
    }
    
    public void start()
    {
        mainPane = new BorderPane();
        StackPane timer = new StackPane();
        timer.setPrefHeight(38);
        labelTimer = new Label("00:00");
        labelTimer.setFont(Font.font("Verdana", 20));
        
        HBox toolbox = new HBox();
        toolbox.setSpacing(100);
        toolbox.setPadding(new Insets(10,20,10,10));
        
        Button reset = new Button("Reset");
        reset.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                controller.resetGame();
            }
        });
        
        Button undo = new Button("Undo");
        undo.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                controller.undoStep();
            }
        });
        
        Button exit = new Button("Exit");
        exit.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                controller.end();
                Platform.exit();
                System.exit(0);
            }
        });
        
        toolbox.getChildren().addAll(reset,undo,exit);
        
        timer.getChildren().add(labelTimer);
        controller.startNewGame(28);
        mainPane.setTop(timer);
        mainPane.setBottom(toolbox);
        Scene scene = new Scene(mainPane,360,440);
        stage.setScene(scene);
        stage.show();
    }
    
    public void resetBoxes()
    {
        mainPane.setCenter(new Button("A"));
    }
    
    public void createBoxes(int[][] cells)
    {
        boxes = new Box[9][9];
        hugeBoxes = new Box3x3[3][3];
        for (int i = 0; i <9 ;i++)
            for (int j = 0;j<9;j++)
            {
                int column = j;
                int row = i;
                int value = cells[i][j];
                boxes[i][j] = new Box(row%3,column%3,value);
                boxes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (!boxes[row][column].getDefault())
                        {
                            Stage newStage = new Stage(StageStyle.TRANSPARENT);
                            newStage.initOwner(stage);
                            newStage.initModality(Modality.APPLICATION_MODAL);
                            //create a window for enter value
                            GridPane newPane = new GridPane();
                            newPane.setHgap(5);
                            newPane.setVgap(5);
                            TextField text = new TextField();
                            text.setPrefWidth(30);
                            Button btn = new Button("Enter");
                            Button btn2 = new Button("Cancel");
                            Button btn3 = new Button("Clear");
                            btn3.setPrefWidth(52);
                            btn.setOnAction(new EventHandler() {
                                @Override
                                public void handle(Event event) {
                                    String str = text.getText();
                                    if (str != "" && str.length() == 1 && ((int)str.charAt(0) > 48 && (int)str.charAt(0) < 58))
                                    {
                                        int value = (int)str.charAt(0)-48;
                                        controller.addValue(row,column,value);
                                        newStage.close();
                                        controller.updateCell();
                                        controller.checkFinish();
                                    }
                                }
                            });
                            
                            btn2.setOnAction(new EventHandler() {
                                @Override
                                public void handle(Event event) {
                                    newStage.close();
                                }
                            });
                            
                            btn3.setOnAction(new EventHandler() {
                                @Override
                                public void handle(Event event) {
                                    boxes[row][column].clear();
                                    controller.addValue(row, column, 0);
                                    newStage.close();
                                    controller.updateCell();
                                }
                            });
                            
                            newPane.add(text,0,0);
                            newPane.add(btn,0,1);
                            newPane.add(btn2,1,1);
                            newPane.add(btn3,1,0);
                            
                            Scene newScene = new Scene(newPane,108,55);
                            newStage.setScene(newScene);
                            newStage.showAndWait();
                        }
                    }
                });
            }
        
        for (int i = 0;i < 3;i++)
            for (int j = 0;j < 3;j++)
            {
                Box[][] smallBoxes = new Box[3][3];
                for (int ii = i*3; ii < i*3+3;ii++)
                    for (int ij = j*3;ij < j*3 +3;ij++)
                        smallBoxes[ii-i*3][ij-j*3] = boxes[ii][ij];
                hugeBoxes[i][j] = new Box3x3(smallBoxes);
            }
        mainBox = new Box3x3(hugeBoxes);   
        mainPane.setCenter(mainBox);
    }

    public void changeValue(int row, int column, int value) {
        boxes[row][column].changeValue(value);
    }
    
    private String timeCount;
    
    public void updateTimer(int number)
    {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                String txt = "";
                int time = number;
                if (number/3600 >= 1)
                {
                    if (number/3600 >= 10)
                        txt = number/3600 + ":";
                    else txt = "0" + number/3600 + ":";
                    time = number%3600;
                }
                
                if (time / 60 >= 1)
                {
                    if (time/60 >= 10)
                        txt = txt + time/60 + ":";
                    else txt = txt + "0" + time/60 + ":";
                }
                else txt = txt +"00:";
                if (time%60 >= 10)
                        txt = txt +time%60;
                else txt = txt + "0" + time%60;
                
                labelTimer.setText(txt);
                timeCount = txt;
            }
        });
    }
    
    public void updateCell()
    {
        for (int i = 0; i <9 ;i++)
            for (int j = 0;j<9;j++)
            {
                if (!boxes[i][j].getDefault())
                    if (controller.check(i,j))
                        boxes[i][j].changeOKValue();
                    else
                        boxes[i][j].changeWrongValue();
            }
    }
    
    public void endGame()
    {
        Text text = new Text();
        
        String str = "CONGRATULATION \n YOU HAVE SOLVED \n  THIS SUDOKU";
        String str2 = "\n     Yours time:" + timeCount;
        Text text2 = new Text(str2);
        text.setText(str);
        text.setFont(Font.font("Verdana", 25));
        text.setFont(Font.font("Verdana", 10));
        
        VBox box = new VBox();
        box.setSpacing(50);
        box.getChildren().addAll(text,text2);
        mainPane.setCenter(box);
    }
}