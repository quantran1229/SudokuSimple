/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author downy
 */
public class Sudoku extends Application {
    
    @Override
    public void start(Stage primaryStage) {
       primaryStage.setTitle("Ngan's Sudoku");
       primaryStage.setResizable(false);
       Controller controller = new Controller();
       UI ui = new UI(primaryStage,controller);
       ui.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
