package starcommand;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.TickLabelLocation;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.TickMarkType;
import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class StarCommand extends Application {
    @Override
    public void start(Stage primaryStage) {
        
//        
//        
//        
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
        
        
    GridPane telelmtry_information = new GridPane();
        Gauge speed = GaugeBuilder.create()
                    .tickLabelLocation(TickLabelLocation.OUTSIDE)
                    .startAngle(315)
                    .angleRange(270)
                    .minValue(0)
                    .maxValue(500)
                    .zeroColor(Color.ORANGE)
                    .majorTickMarkType(TickMarkType.TRIANGLE)
                    .build();
//        speed.setSkin(new ModernSkin(speed));  //ModernSkin : you guys can change the skin
        speed.setTitle("Altitude");  //title
        speed.setUnit("m / s");  //unit
//        speed.setValue(50.00); //deafult position of needle on gauage
        speed.setAnimated(true); 
        speed.setThreshold(85);
        speed.setThresholdVisible(true); 
        speed.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL); 

        Gauge altitude = GaugeBuilder.create()
                    .tickLabelLocation(TickLabelLocation.OUTSIDE)
                    .startAngle(315)
                    .angleRange(270)
                    .minValue(0)
                    .maxValue(500)
                    .zeroColor(Color.ORANGE)
                    .majorTickMarkType(TickMarkType.TRIANGLE)
                    .build();
//        Altitude.setSkin(new ModernSkin(Altitude));  //ModernSkin : you guys can change the skin
        altitude.setTitle("Altitude");  //title
        altitude.setUnit("m / s");  //unit
//        Altitude.setValue(50.00); //deafult position of needle on gauage
        altitude.setAnimated(true); 
        altitude.setThreshold(85);
        altitude.setThresholdVisible(true); 
        altitude.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL); 

        Gauge acceleration = GaugeBuilder.create()
                    .tickLabelLocation(TickLabelLocation.OUTSIDE)
                    .startAngle(315)
                    .angleRange(270)
                    .minValue(0)
                    .maxValue(500)
                    .zeroColor(Color.ORANGE)
                    .majorTickMarkType(TickMarkType.TRIANGLE)
                    .build();
//        acceleration.setSkin(new ModernSkin(acceleration));  //ModernSkin : you guys can change the skin
        acceleration.setTitle("Acceleration");  //title
        acceleration.setUnit("m / s^2");  //unit
//        acceleration.setValue(50.00); //deafult position of needle on gauage
        acceleration.setAnimated(true); 
        acceleration.setThreshold(85);
        acceleration.setThresholdVisible(true); 
        acceleration.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL); 

        telelmtry_information.setPadding(new Insets(5, 0, 5, 0));
        telelmtry_information.setVgap(4);
        telelmtry_information.setHgap(4);
        telelmtry_information.add(altitude, 0, 0);
        telelmtry_information.add(speed, 1, 0);
        telelmtry_information.add(acceleration, 2, 0);
        
        StackPane root = new StackPane();
//        root.getChildren().add(btn);
        root.getChildren().addAll(telelmtry_information); 
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}


