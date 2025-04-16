
import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application{
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	private Mat image = new Mat();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		TextArea textArea = new TextArea();
        textArea.setPrefWidth(300);
        textArea.setPrefHeight(300);

        Button fileChooserButton = new Button("Choose Image");
        fileChooserButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select an Image File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                image = Imgcodecs.imread(selectedFile.getAbsolutePath());
                if (!image.empty()) {
                	HighGui.imshow("Your Image", image);
                	HighGui.waitKey();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
        			alert.setContentText("Failed to load image, please try again");
        			alert.showAndWait();
                }
            }
        });
        
        Button encryptButton = new Button("Hide secret message");
        encryptButton.setOnAction(e -> {
        	if(image.empty()) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setContentText("Failed to load image, please try again");
    			alert.showAndWait();
        	} else {
        		if(textArea.getText().isEmpty()) {
            		Alert alert = new Alert(Alert.AlertType.ERROR);
        			alert.setContentText("textArea is empty, please write a message");
        			alert.showAndWait();
            	} else {
            		SteganographyLSB lsb = new SteganographyLSB();
            		VernamCipherEncryption vernamCipherEncryption = new VernamCipherEncryption();
            		try {
						Mat enc = lsb.LSB(image, vernamCipherEncryption.one_time_pad_encr(textArea.getText()), vernamCipherEncryption.getSeed());
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Image with Secret Message");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if (file != null) {
                        boolean result = Imgcodecs.imwrite(file.getAbsolutePath(), image);
                        if (result) {
                        	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                			alert.setContentText("Image Saved successfully");
                			alert.showAndWait();
                        } else {
                        	Alert alert = new Alert(Alert.AlertType.ERROR);
                			alert.setContentText("Failed Save Image");
                			alert.showAndWait();
                        }
                    }
            	}
        	}
        });
        
        Button decryptButton = new Button("decrypt secret message");
        decryptButton.setOnAction(e -> {
        	FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select The Image With the Secret Message");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                Mat encImage = Imgcodecs.imread(selectedFile.getAbsolutePath());
                if (!encImage.empty()) {
                	DecryptLSB decryptLSB = new DecryptLSB();
                	String decryptedMessage = null;
					try {
						decryptedMessage = decryptLSB.extractLSB(encImage);
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	Stage popupStage = new Stage();
                    popupStage.initModality(Modality.APPLICATION_MODAL);
                    popupStage.setTitle("Message");

                    TextArea messageArea = new TextArea(decryptedMessage);
                    messageArea.setWrapText(true);
                    messageArea.setEditable(false);

                    ScrollPane scrollPane = new ScrollPane(messageArea);
                    scrollPane.setFitToWidth(true);
                    scrollPane.setFitToHeight(true);

                    StackPane layout = new StackPane(scrollPane);
                    Scene scene = new Scene(layout, 400, 300);
                    popupStage.setScene(scene);
                    popupStage.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
        			alert.setContentText("Failed to load image, please try again");
        			alert.showAndWait();
                }
            }
        });
        HBox buttonBox = new HBox(10, fileChooserButton, encryptButton, decryptButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, textArea, buttonBox);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setTitle("File Chooser Example");
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}
