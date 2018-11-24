package sudoku;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
        private abstract class Stage
        {
            public Box box;
            public Stage(Box b)
            {
                box = b;
            }
            
            public abstract void changeValue(int value);
        
            public abstract void changeWrongValue(int value);
        
            public abstract void changeOKValue(int value);
        }
        
        private class GuessStage extends Stage
        {
            private GridPane guessBox;
            private boolean[] values;
            public GuessStage(Box b)
            {
                super(b);
                guessBox = new GridPane();
                guessBox.setPrefSize(30, 30);
                values = new boolean[9];
                box.clear();
                box.getChildren().add(guessBox);
                guessBox.setAlignment(Pos.CENTER);
                guessBox.setHgap(2);
            }
        
            @Override
            public void changeValue(int value) {
                if (value!=0)
                {
                    values[value-1] = !values[value-1];
                    guessBox.getChildren().clear();
                    int position = 0;   
                    for (int i = 0;i < 9;i++)
                    {
                        if (values[i])
                        {
                            Text txt = new Text((i+1)+"");
                            txt.setFont(Font.font("Verdana", 9));
                            guessBox.add(txt, position % 3, position / 3);
                            position++;
                        }
                    }
                }
                else
                {
                    guessBox.getChildren().clear();
                    values = new boolean[9];
                }
            }

            @Override
            public void changeWrongValue(int value) {
                int position = -1;
                System.out.println("W");
                for (int i = 0;i < value;i++)
                {
                    if (values[i]) position++;
                }
                if (position != -1)
                {
                    Text txt = (Text)guessBox.getChildren().get(position);//value automatic in guessValue
                    txt.setFill(Color.RED);
                    guessBox.getChildren().set(position, txt);
                }
            }

            @Override
            public void changeOKValue(int value) {
                System.out.println("O");
                int position = -1;
                for (int i = 0;i < value;i++)
                {
                    if (values[i]) position++;
                }
                if (position != -1)
                {
                    Text txt = (Text)guessBox.getChildren().get(position);//value automatic in guessValue
                    txt.setFill(Color.BLUE);
                    guessBox.getChildren().set(position, txt);
                }
            }
        }
        
        private class StageAnswer extends Stage
        {
            public StageAnswer(Box b)
            {
                super(b);
                Text text = new Text();
                text.setFill(Color.BLACK);
                text.setFont(Font.font("Verdana", 15));
                box.clear();
                box.getChildren().add(text);
            }
        
            @Override
            public void changeValue(int value)
            {
                if (!Default)
                {
                    Text text = new Text();
                    text.setFont(Font.font("Verdana", 15));
                    if (value != 0)
                    {
                        text.setText(value+"");
                    }
                    box.getChildren().set(0, text);
                }
            }
        
            @Override
            public void changeWrongValue(int value)
            {
                if (!Default)
                {
                    String oldText = ((Text)box.getChildren().get(0)).getText();
                    Text text = new Text();
                    text.setFill(Color.RED);
                    text.setFont(Font.font("Verdana", 15));
                    text.setText(oldText);
                    box.getChildren().set(0,text);
                }
            }
        
            @Override
            public void changeOKValue(int value)
            {
                if (!Default)
                {
                    String oldText = ((Text)box.getChildren().get(0)).getText();
                    Text text = new Text();
                    text.setFill(Color.BLUE);
                    text.setFont(Font.font("Verdana", 15));
                    text.setText(oldText);
                    box.getChildren().set(0,text);
                }
            }
        }
        
        public boolean Default = false;
        private Stage stage;
        
        public Box(int value)
        {
            super();
            this.setPrefHeight(40);
            this.setPrefWidth(40);
            this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.2))));
            stage = new StageAnswer(this);
            if (value != 0)
            {
                changeValue(value);
                Default = true;
            }
        }
        
        public void changeValue(int value)
        {
            stage.changeValue(value);
        }
        
        public void changeWrongValue(int value)
        {
            stage.changeWrongValue(value);
        }
        
        public void changeOKValue(int value)
        {
            stage.changeOKValue(value);
        }
        
        public void clear()
        {
            this.getChildren().clear();
        }
        
        public boolean getDefault()
        {
            return Default;
        }
        
        public void changeGuessStage()
        {
            stage = new GuessStage(this);
        }
        
        public void changeAnswerStage()
        {
            stage = new StageAnswer(this);
        }
        
        public String getStage()
        {
            if (stage instanceof StageAnswer) return "answer";
            return "guess";
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
    
    private class ResultNumber
    {
        private Stage stage;
        private String result;
        private Service timer;
        
        public ResultNumber(Stage stage)
        {
            this.stage = stage;
            result = "";
            timer = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            int i = 0;
                            while (i<3)
                            {
                                Thread.sleep(1000);
                                i++;
                            }
                            Platform.runLater(new Runnable(){
                                @Override
                                public void run() {
                                    stage.close();
                                }
                            });
                            return null;
                        }
                    };
                }
            };
            timer.start();
        }
        
        private void append(String n)
        {
            if (n.equals("0"))
            {
                result = "0";
            }
            else
            {
                for (int i = 0;i < n.length();i++)
                {
                    int pos = result.indexOf(n.charAt(i));
                    if (pos != -1)
                    {
                        result = result.substring(0, pos) + result.substring(pos+1);
                    }
                    else
                    {
                        result = result + n.charAt(i);
                    }
                }
            }
        }
        
        public void appendAnswer(String n)
        {
            this.append(n);
            stage.close();
            if (timer.isRunning()) timer.cancel();
        }
        
        public void appendGuess(String n)
        {
            this.append(n);
            if (timer.isRunning())
            {
                timer.restart();
            }
        }
        
        public void appendCancel()
        {
            if (timer.isRunning()) timer.cancel();
            this.append("0");
            stage.close();
        }
        
        public String getResult()
        {
            return result;
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
    private boolean hint;
    
    
    public UI(Stage stage,Controller con)
    {
        controller = con;
        this.stage = stage;
        controller.setUI(this);
    }
    
    public void start()
    {
        hint = false;
        mainPane = new BorderPane();
        StackPane timer = new StackPane();
        timer.setPrefHeight(38);
        labelTimer = new Label("00:00");
        labelTimer.setFont(Font.font("Verdana", 20));
        
        HBox toolbox = new HBox();
        toolbox.setSpacing(50);
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
        
        CheckBox hintBox = new CheckBox("Answer");
        hintBox.setSelected(hint);
        hintBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                hint = hintBox.isSelected();
                if (hint) hintBox.setText("Guess");
                else hintBox.setText("Answer");
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
        
        toolbox.getChildren().addAll(reset,hintBox,undo,exit);
        
        timer.getChildren().add(labelTimer);
        controller.startNewGame(28);
        mainPane.setTop(timer);
        mainPane.setBottom(toolbox);
        Scene scene = new Scene(mainPane,360,440);
        scene.getStylesheets().add("/sudoku/css/stylesheet.css");
        stage.setScene(scene);
        stage.show();
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
                boxes[i][j] = new Box(value);
                boxes[i][j].getStyleClass().add("cell");
                boxes[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (!boxes[row][column].getDefault())
                        {
                            Stage newStage = new Stage(StageStyle.TRANSPARENT);
                            newStage.initOwner(stage);
                            newStage.initModality(Modality.APPLICATION_MODAL);
                            //create a window for enter value
                            VBox container = new VBox();
                            container.setSpacing(10);
                            StackPane[][] numberPane = new StackPane[3][3];
                            ResultNumber result = new ResultNumber(newStage);
                            for (int z = 0;z < 3;z++)
                            for (int zi = 0;zi < 3;zi++)
                            {
                                numberPane[z][zi] = new StackPane();
                                numberPane[z][zi].getStyleClass().add("number");
                                numberPane[z][zi].setPrefSize(100, 100);
                                numberPane[z][zi].setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.5))));
                                Text txt = new Text((z*3+zi+1)+"");
                                txt.setFont(Font.font("Verdana", 40));
                                numberPane[z][zi].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        if (hint) result.appendGuess(txt.getText());
                                        else result.appendAnswer(txt.getText());
                                    }
                                });
                                numberPane[z][zi].getChildren().add(txt);
                            }
                            Box3x3 numberBox = new Box3x3(numberPane);
                            //create clear box
                            StackPane clearBox = new StackPane();
                            clearBox.setPrefSize(300, 100);
                            Text txt = new Text("CLEAR");
                            txt.setFont(Font.font("Verdana", 40));
                            clearBox.getChildren().add(txt);
                            clearBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        result.appendCancel();
                                    }
                                });
                            clearBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.5))));
                            clearBox.getStyleClass().add("number");
                            
                            container.getChildren().addAll(numberBox,clearBox);
                            Scene newScene = new Scene(container,300,410);
                            newScene.getStylesheets().add("/sudoku/css/stylesheet2.css");
                            newStage.setScene(newScene);
                            newStage.showAndWait();
                            stage.requestFocus();
                            String valueTxt = result.getResult();
                            if (hint)
                            {
                                int[] values = new int[valueTxt.length()];
                                for (int nu = 0;nu < valueTxt.length();nu++)
                                {
                                    int value = (int)valueTxt.charAt(nu)-48;
                                    values[nu] = value;
                                }
                                controller.addGuess(row, column, values);
                            }
                            else
                                if (valueTxt.length() > 0)
                                {
                                    int value = (int)valueTxt.charAt(0)-48;
                                    controller.addValue(row,column,value);
                                }
                            controller.updateCell();
                            controller.checkFinish();
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
        if (!boxes[row][column].getStage().equals("answer"))
        {
            boxes[row][column].changeAnswerStage();
        }
        boxes[row][column].changeValue(value);
    }
    
    public void changeGuessValue(int row, int column, int[] values) {
        if (boxes[row][column].getStage().equals("answer"))
        {
            boxes[row][column].changeGuessStage();
        }
        for (int i = 0;i < values.length;i++)
        {
            boxes[row][column].changeValue(values[i]);
        }
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
                if (boxes[i][j].getStage().equals("answer"))
                {
                    if (!boxes[i][j].getDefault())
                        if (controller.check(i,j))
                            boxes[i][j].changeOKValue(0);//why zero? because it only change current child node in Box so it does not matter value is...
                        else
                            boxes[i][j].changeWrongValue(0);
                }
                else
                {
                    System.out.println("go in");
                    ArrayList list = controller.getGuess(i, j);
                    System.out.println(list.size());
                    for (int z = 0;z < list.size();z++)
                    {
                        int v = (int)list.get(z);
                        if (controller.check(i, j, v))
                            boxes[i][j].changeOKValue(v);
                        else
                            boxes[i][j].changeWrongValue(v);
                    }
                }
            }
    }
    
    public void endGame()
    {
        Text text = new Text();
        
        String str = "CONGRATULATION \n YOU HAVE SOLVED \n  THIS SUDOKU";
        String str2 = "\n     Yours time:" + timeCount;
        Text text2 = new Text(str2);
        text.setText(str);
        text.setFont(Font.font("Verdana", 40));
        text.setFont(Font.font("Verdana", 10));
        
        VBox box = new VBox();
        box.setSpacing(50);
        box.getChildren().addAll(text,text2);
        mainPane.setCenter(box);
    }
}