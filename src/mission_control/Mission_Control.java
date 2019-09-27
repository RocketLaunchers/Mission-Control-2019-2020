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
        String title = "Mission Control"; 
        
        // FUNCTION USED TO CONFIGER THE GAUGES OF THE GUI
                // gaugemake(MAX_VALUE, GAUGE_NAME, UNITS);
        Gauge _alt = gaugemake(10000, "Altitude", "m"); 
        Gauge _spd = gaugemake(1000, "Speed", "m/s");
        Gauge _acc = gaugemake(300, "Acceleration", "m/s^2");
        
        StackPane root = new StackPane();
        root.getChildren().addAll(_alt, _spd, _acc); 

        // MAKE THE VISUALS IN THE GUI
        MenuBar _menu = menumake(); // FUNCTION MAKES THE MENU BAR
        GridPane _main = new GridPane(); // ORGENIZES THE COMPONENTS IN A GRID
        _main.add(_alt, 0, 1);  // (OBJECT, X, Y)
        _main.add(_spd, 1, 1);
        _main.add(_acc, 2, 1);
        _main.setPadding(new Insets(5, 0, 5, 0));  
        
        BorderPane grid_main = new BorderPane();  // SETS EVERYTHING ON THE GUI
        grid_main.setTop(_menu);  
        grid_main.setCenter(_main); 
        
        root.getChildren().add(grid_main);
        Scene scene = new Scene(root, 650, 250);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show(); 
        
        // LET THE GUI START UP FIRST THEN CONTINUE ON
        Platform.runLater(() -> { 
            DataReader( _alt, _spd, _acc);
        });
    }
    
    /*
     * SEPERATE FUNCTION FOR SIMPLICITY, ITS ANOTHER THREAD THAT GRABS DATA AND 
     * ADJUSTS THE INFORMATION ON THE GUI
    */
    private void DataReader(Gauge _alt, Gauge _spd, Gauge _acc) { 
        Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception {   
                try { 
                    Scanner scanner = new Scanner(new File("filename.txt")); // GRABS THE FILE
                    
                    // SO LONG AS THERE ARE MORE DOUBLES, AND THE SHUTDOWN FLAG ISN'T ACTIVE
                    while (scanner.hasNextDouble() && !shutdown) { 
                        try { 
                            double line = scanner.nextDouble(); // GRAB THE NEXT (DOUBLE)
                            TimeUnit.SECONDS.sleep(1); // DELAY
                            
                            
                            // CHANGES THE VARIABLES
                            _alt.setValue(line);
                            _spd.setValue(line);
                            _acc.setValue(line);
                             
//                            double _alt_high = _alt.getThreshold(); 
//                            if(_alt_high < line) { 
//                                _alt.setThreshold(line); 
//                                System.out.println(line);
//                            }
                            
//                            double _spd_high = _spd.getThreshold();
//                            if(_spd_high < line) { _spd.setThreshold(line); }
//                            double _acc_high = _acc.getThreshold();  
//                            if(_acc_high < line) { _acc.setThreshold(line); }

                            System.out.println("~// num:" +line + " alt:" + _alt.getValue()); 

                        } catch (InterruptedException ex) { Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex); }
                    } scanner.close(); // CLOSE THE FILE READER 
                } catch (FileNotFoundException ex) { Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex); }  
                return null ;
            }
        };
//        task.messageProperty().addListener((obs, oldMessage, newMessage) -> label.setText(newMessage));
        new Thread(task).start();
    }   
    
    // A FUNCTION THAT MAKES THE GAUGES (SEPERATE TO SAVE SPACE) 
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
                    .threshold(_max)
                    .build();
    //        speed.setSkin(new ModernSkin(speed));  //ModernSkin : you guys can change the skin
        temp.setTitle(_lable);  //title
        temp.setUnit(_unit);  //unit 
        temp.setAnimated(true);  
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
    
    // MAIN THAT DOSE THE START FUNCTION
    public static void main(String[] args) { launch(args); }   
}
