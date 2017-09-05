package com.ssubotic.rename_daily_reports;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RenameFiles extends Application
{   
    HashMap<String, String> filenameMap = new HashMap<String, String>();
    File wordBank = new File("res\\keyword bank.txt");
    static boolean folderModeEnabled = false;
    
    public void start(Stage stage)
    {
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
            //Error window popup in case "keyword bank.txt" is missing
            VBox pane = new VBox();
            Text text = new Text("Error.\n\"keyword bank.txt\" not found.");
            text.setTextAlignment(TextAlignment.CENTER);
            text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            pane.setAlignment(Pos.CENTER);
            pane.setPadding(new Insets(25));
            pane.getChildren().add(text);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.setTitle("Error");
            stage.show();
            return;
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
        
        //create radio buttons to determine whether user is renaming reports or their housing folders
        VBox radioButtons = new VBox(10);
        RadioButton reportMode = new RadioButton("Report Mode");
        reportMode.setSelected(true);
        RadioButton folderMode = new RadioButton("Folder Mode");
        ToggleGroup toggleGroup = new ToggleGroup();
        reportMode.setToggleGroup(toggleGroup);
        folderMode.setToggleGroup(toggleGroup);
        radioButtons.getChildren().addAll(reportMode, folderMode);
        //negative left-side padding so that radio buttons are on the left
        radioButtons.setPadding(new Insets(0,0,0,-150));
        
        //the filepath input field, its label, and buttons for submitting input and clearing the field
        Label label = new Label("File Path: ");
        TextField textField = new TextField();
        Button submitButton = new Button("Submit");
        Button clearButton = new Button("Clear");
        
        //adding inner nodes(mode select & path input) to inputPane
        inputPane.getChildren().addAll(radioButtons, label, textField, submitButton, clearButton);
        inputPane.setAlignment(Pos.CENTER);
        
        //add text, image, and inputPane to the primary pane
        primaryPane.getChildren().addAll(text, imageView, inputPane);
        primaryPane.setAlignment(Pos.CENTER);
        primaryPane.setPadding(new Insets(10, 10, 10, 10));
        
        //associate pane with scene and scene with stage, set window size
        Scene scene = new Scene(primaryPane, 800, 400);
        stage.setTitle("Report Renamer");
        stage.setScene(scene);
        stage.show();

        //methods to allow switching between report and folder mode
        reportMode.setOnAction((event) -> {
            switchMode();
        });
        folderMode.setOnAction((event) -> {
            switchMode();
        });
        
        //submits input in the TextField when the submit button is pressed 
        submitButton.setOnAction((event) -> {
            submitFilePath(textField, filenameMap);
            textField.requestFocus();
        });
        
        //allows use of the Enter key instead of having to click the submit button
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ENTER) {
                    submitFilePath(textField, filenameMap);
                    submitButton.arm();
                    PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
                    pause.setOnFinished(e -> submitButton.disarm());
                    pause.play();
                }
            }
        });
        
        //clears the input TextField when the clear button is pressed
        clearButton.setOnAction((event) -> {
            textField.setText("");
            textField.requestFocus();
        });
    }
    
    //called when either the "Report Mode" or "Folder Mode" radio buttons are selected
    private void switchMode() {
        folderModeEnabled = !folderModeEnabled;
    }
    
    private static void submitFilePath(TextField tf, HashMap<String, String> filenameMap) 
    {
        try {
            String input = tf.getText();
            File dir = new File(input);
            File[] reports = dir.listFiles();
            
            //in report mode check each file in the folder to see if it needs renaming
            if (!folderModeEnabled) {
                for (File f : reports) {
                    validateFile(dir, f, filenameMap);
                }
            //in folder mode use the folder name format yyyy-mm-MonthName and the LocalDate class
            //from java.time to rename each internal directory with a different number for the day
            } else {
                String[] tokens = dir.getName().split("-");
                int dayCount = 1;
                for (File f : reports) {
                    LocalDate lc = LocalDate.of(Integer.parseInt(tokens[0]), 
                                                Integer.parseInt(tokens[1]), 
                                                dayCount);
                    String year = tokens[0];
                    String month = tokens[1];
                    String formattedDayCount = (dayCount < 10) ? "0" + String.valueOf(dayCount) 
                                                               : String.valueOf(dayCount);
                    String dayName = lc.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);
                    renameFolder(dir, f, year, month, formattedDayCount, dayName);
                    dayCount++;
                }
            }
        //show error notification in input TextField if previous input was invalid
        } catch (Exception e) {
            tf.setText("Error, invalid filepath");
        }
    }
    
    //check file name against keywords.txt (report mode only)
    private static void validateFile(File dir, File f, HashMap<String, String> filenameMap) 
    {
        //check an all-lowercase version of the file's name against the HashMap's keys
        String currentNameCaseInsensitive = f.getName().toLowerCase();
        for (String s : filenameMap.keySet()) {
            if (currentNameCaseInsensitive.contains(s)) {
                renameFile(dir, f, filenameMap.get(s));
            }    
        }
        return;
    }

    //renaming function when in report mode
    private static void renameFile(File dir, File f, String newName) 
    {
        //create an array of single-character strings out of the directory name
        //this allows the use of a "pseudo-regex" to reformat the date component of the name
        String[] date = dir.getName().split("");
        String fileExtension = getFileExtension(f);
        String mm_dd_yy = date[5] + date[6] + "-" + date[8] + date[9] + "-" + date[2] + date[3];
        f.renameTo(new File(dir.getPath() + "\\" + newName + mm_dd_yy + fileExtension));
        return;
    }

    private static String getFileExtension(File f) 
    {
        String fileName = f.getName();
        //find the last occurrence of a period in the filename to know where the extension starts
        int fileExtensionStartIndex = fileName.lastIndexOf(".");
        if (fileExtensionStartIndex == -1) {
            return "";
        }
        String fileExtension = fileName.substring(fileExtensionStartIndex);
        return fileExtension;
    }
    
    //renaming method used when in folder mode
    private static void renameFolder(File dir, File f, String year, String month, 
            String dayNumber, String dayName) 
    {
        f.renameTo(new File(dir.getPath() + "\\" + year + "-" + month 
                + "-" + dayNumber + "-" + dayName));
    }
}
