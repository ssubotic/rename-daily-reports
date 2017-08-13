package com.ssubotic.renamefiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RenameFiles extends Application
{   
    public void start(Stage stage)
    {
        //pane set up
        VBox primaryPane = new VBox(25);
        
        Text text = new Text("This tool is for renaming nightly hotel reports. To use, copy and paste the current directory's complete filename.");
        Image image = new Image("img.png", 750, 250, false, false);
        ImageView imageView = new ImageView(image);
        //use a horizontal box node so label and textField are on the same line
        HBox inputPane = new HBox(10);
        Label label = new Label("File Path: ");
        TextField textField = new TextField();
        Button button = new Button("Submit");
        inputPane.getChildren().add(label);
        inputPane.getChildren().add(textField);
        inputPane.setAlignment(Pos.CENTER);
        inputPane.getChildren().add(button);
        
        //add text, image, and inputPane to the primary pane
        primaryPane.getChildren().add(text);
        primaryPane.getChildren().add(imageView);
        primaryPane.getChildren().add(inputPane);
        primaryPane.setAlignment(Pos.CENTER);
        primaryPane.setPadding(new Insets(10, 10, 10, 10));
        
        //associate pane with scene and scene with stage, set window size
        Scene scene = new Scene(primaryPane, 800, 400);
        stage.setTitle("Report Renamer");
        stage.setScene(scene);
        stage.show();
        
        button.setOnAction((event) -> {
            try {
                String input = textField.getText();
                File dir = new File(input);
                File[] reports = dir.listFiles();
                for (File f : reports) {
                    renameFile(dir, f);
                    System.out.println(f);
                }
            } catch (Exception e) {
                textField.setText("Error, invalid filepath");
            }
        });
        
    }
    
    public static void renameFile(File dir, File f) 
    {
        if (f.getName().toLowerCase().contains("stat")) {
            f.renameTo(new File(dir.getPath() + "\\A.01 HotelStats_"));
            return;
        } 
        else if (f.getName().toLowerCase().contains("tran")) {
            f.renameTo(new File(dir.getPath() + "\\A.02 TransactionBalRpt_"));
            return;
        }
        else if (f.getName().toLowerCase().contains("shift")) {
            f.renameTo(new File(dir.getPath() + "\\A.03 ShiftRec_"));
            return;
        }
        else if (f.getName().toLowerCase().contains("rate")) {
            f.renameTo(new File(dir.getPath() + "\\A.04 RateDiscrpRpt "));
            return;
        }
        else if (f.getName().toLowerCase().contains("guest")) {
            f.renameTo(new File(dir.getPath() + "\\A.05 GstLedgrRpt "));
            return;
        }
        else if (f.getName().toLowerCase().contains("aging")) {
            f.renameTo(new File(dir.getPath() + "\\A.06 AR-AgingDetailRpt "));
            return;
        }
        else if (f.getName().toLowerCase().contains("table")) {
            f.renameTo(new File(dir.getPath() + "\\Table of Contents "));
            return;
        }
    }
    
}
