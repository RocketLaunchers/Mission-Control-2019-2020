package mission_control; 

import eu.hansolo.medusa.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;  
import java.util.Timer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 


public class Mission_Control extends Application {
    // SHUTDOWN FLAG USED TO KILL THE OTHER THREADS
    volatile boolean shutdown = false;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        MenuBar _menu = menumake();
        
        
        // VARIABLES USED TO CONFIGER THE GAUGES OF THE GUI
        String title = "Mission Control"; 
        
        Gauge _alt = gaugemake(10000, "Altitude", "m");
        Gauge _spd = gaugemake(1000, "Speed", "m/s");
        Gauge _acc = gaugemake(300, "Acceleration", "m/s^2");
        
        StackPane root = new StackPane();
        root.getChildren().addAll(_alt, _spd, _acc); 

        
        GridPane telelmtry_information = new GridPane();  
        
//        telelmtry_information.add(w_alt, 0, 0);
//        telelmtry_information.add(w_spd, 1, 0);
//        telelmtry_information.add(w_acc, 2, 0);
        
        telelmtry_information.add(_alt, 0, 1);
        telelmtry_information.add(_spd, 1, 1);
        telelmtry_information.add(_acc, 2, 1);
        telelmtry_information.setPadding(new Insets(5, 0, 5, 0)); 
 
        
        BorderPane box = new BorderPane();  
        box.setTop(_menu);  
        box.setCenter(telelmtry_information); 
        
        root.getChildren().addAll(box); 
        Scene scene = new Scene(root, 650, 250);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show(); 
        
        // LET THE GUI START UP FIRST
        Platform.runLater(() -> { 
            System.out.println("Reading Data");
            DataReader( _alt, _spd, _acc);
        });
    }
    
    private void DataReader(Gauge _alt, Gauge _spd, Gauge _acc) {
        
        Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception {  
                // GRAB THE FILE TO READ
                try { 
                    Scanner scanner; 
                    scanner = new Scanner(new File("filename.txt"));

                    System.out.println("Success");
                    while (scanner.hasNextDouble() && !shutdown) {
                        try { 
                            double line = scanner.nextDouble();
                            TimeUnit.SECONDS.sleep(1);   

                            _alt.setValue(line);
                            _spd.setValue(line);
                            _acc.setValue(line);

                            System.out.println("num:" +line + " alt:" + _alt.getValue());

                        } catch (InterruptedException ex) { Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex); }
                    }   
                    scanner.close(); // CLOSE THE FILE READER

                } catch (FileNotFoundException ex) { 
                    System.out.println("Fail");

                    Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex); 
                }  
                return null ;
            }
        };
//        task.messageProperty().addListener((obs, oldMessage, newMessage) -> label.setText(newMessage));
        new Thread(task).start();
    }   
    public Gauge gaugemake(int _max, String _lable, String _unit) {
        Gauge temp = GaugeBuilder.create()
                    .tickLabelLocation(TickLabelLocation.OUTSIDE)
                    .startAngle(315)
                    .angleRange(270)
                    .minValue(0)
                    .maxValue(_max*1.25)
                    .zeroColor(Color.ORANGE)
                    .areas(new Section(_max, (_max*1.25), Color.RED))
                    .majorTickMarkType(TickMarkType.TRIANGLE)
                    .build();
    //        speed.setSkin(new ModernSkin(speed));  //ModernSkin : you guys can change the skin
        temp.setTitle(_lable);  //title
        temp.setUnit(_unit);  //unit
    //        speed.setValue(50.00); //deafult position of needle on gauage
        temp.setAnimated(true); 
        temp.setThreshold(85);
        temp.setThresholdVisible(true); 
        temp.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL); 
        return temp;
    }
    
    public MenuBar menumake () { 
        Menu m = new Menu("File"); 
        
        MenuItem m1 = new MenuItem("Copy"); 
        MenuItem m2 = new MenuItem("Save");   
        m.getItems().add(m1); 
        m.getItems().add(m2);  
        
        MenuBar mb = new MenuBar(); 
        mb.getMenus().add(m);  
        return mb;
    }
    
    
    
    /* 
     * ON CLOSE OF THE PRIMARY STAGE THIS FUNCTION WILL BE USED TO CLOSE THE 
     * CURRENTLY RUNNING THREADS IN THE BACKGROUND
     */
    @Override
    public void stop(){
        System.out.println("Stage is Closing");
        System.out.println("Threads Closing");
        
        shutdown = true; 
    }
    
    
    public static void main(String[] args) { launch(args); }   
}
