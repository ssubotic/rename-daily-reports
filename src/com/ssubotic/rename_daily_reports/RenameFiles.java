package com.ssubotic.rename_daily_reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

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
        HashMap<String, String> filenameMap = new HashMap<String, String>();
        File wordBank = new File("res\\keyword bank.txt");
        try {
            /*
             * Inputs key-value pairs of Strings from "res/keyword bank.txt" into filenameMap.
             * In the file, each key and value is alone on their own line, in the event of an 
             * odd number of lines, the last key is simply discarded.
             */
            Scanner in = new Scanner(wordBank);
            while (in.hasNextLine()) {
                String temp = in.nextLine();
                if (in.hasNextLine()) {
                    filenameMap.put(temp, in.nextLine());
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Program exiting.");
            System.exit(0);
        }
        
        //main pane setup
        VBox primaryPane = new VBox(25);
        //create primaryPane nodes
        Text text = new Text("This tool is for renaming archival hotel reports. "
                + "To use, copy and paste the current directory's complete filename.");
        Image image = new Image("img.png", 750, 250, false, false);
        ImageView imageView = new ImageView(image);
        //use a horizontal box node around label and textField so that they're the same line
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
        
        //ActionEvent for the "Submit" button press action
        button.setOnAction((event) -> {
            try {
                String input = textField.getText();
                File dir = new File(input);
                File[] reports = dir.listFiles();
                for (File f : reports) {
                    renameFile(dir, f, filenameMap);
                }
            } catch (Exception e) {
                textField.setText("Error, invalid filepath");
            }
        });
        
    }
    
    //check filename against filenameMap's keySet of Strings (keywords)
    public static void renameFile(File dir, File f, HashMap<String, String> filenameMap) 
    {
        String currentNameCaseInsensitive = f.getName().toLowerCase();
        String date = dir.getName();
        for (String s : filenameMap.keySet()) {
            if (currentNameCaseInsensitive.contains(s)) {
                f.renameTo(new File(dir.getPath() + "\\" + filenameMap.get(s) + date));
                return;
            }
        }
    }
}
