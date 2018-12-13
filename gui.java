package GUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Model.SongList;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.geometry.*;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("restriction")

public class gui extends Application {
	static int count = 1;
	String buttonVal = "*";
	static Stage openStage = new Stage();
	static WebView webview = new WebView();

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		menuRunner();
	}

	public void menuRunner() {
		Stage menuStage = new Stage();
		menuStage.setResizable(false);

		GridPane menuPane = new GridPane();
		menuPane.setHgap(50);
		menuPane.setVgap(50);
		menuPane.setPadding(new Insets(50, 50, 50, 50));

		Scene menuScene = new Scene(menuPane, 500, 300);

		Button make = new Button("Make your own JukeBox!");
		make.setPrefHeight(100);
		make.setPrefWidth(400);
		make.setStyle("-fx-font: 30 Dinpro; -fx-base: #fa58ac; -fx-text-fill: white;");

		make.setOnAction(e -> {
			menuStage.close();
			jukeBoxMaker();
		});

		Button play = new Button("Play a JukeBox!");
		play.setPrefHeight(100);
		play.setPrefWidth(400);
		play.setStyle("-fx-font: 30 Dinpro; -fx-base: #81daf5;-fx-text-fill: white;");

		play.setOnAction(e -> {
			menuStage.close();
			jukeBoxPlayer();
		});

		menuPane.add(make, 0, 0);
		menuPane.add(play, 0, 1);

		menuStage.setTitle("Main Menu");
		menuStage.setScene(menuScene);
		menuStage.show();
	}

	public void jukeBoxMaker() {
		count = 1;
		SongList songs = new SongList();

		Stage makeStage = new Stage();
		makeStage.setResizable(false);

		GridPane makePane = new GridPane();
		makePane.setHgap(25);
		makePane.setVgap(25);
		makePane.setPadding(new Insets(25, 25, 25, 25));

		Label titleLabel = new Label("Enter Title");
		titleLabel.setStyle("-fx-font: 30 Dinpro; -fx-text-fill: #fa58ac;");
		titleLabel.setPrefHeight(50);
		titleLabel.setPrefWidth(400);

		TextField title = new TextField();
		title.setStyle("-fx-font: 20 Dinpro;");
		title.setPrefHeight(75);
		title.setPrefWidth(400);

		Label linkLabel = new Label("Enter Youtube Link");
		linkLabel.setStyle("-fx-font: 30 Dinpro; -fx-text-fill: #81daf5;");
		linkLabel.setPrefHeight(50);
		linkLabel.setPrefWidth(400);

		TextField link = new TextField();
		link.setStyle("-fx-font: 20 Dinpro;");
		link.setPrefHeight(75);
		link.setPrefWidth(400);

		Button addSong = new Button("Add Song!");
		addSong.setStyle("-fx-font: 30 Dinpro; -fx-base: #da81f5;-fx-text-fill: white;");
		addSong.setAlignment(Pos.BASELINE_CENTER);
		addSong.setPrefHeight(75);
		addSong.setPrefWidth(400);

		addSong.setOnAction(e -> {
			if (title.getText().isEmpty()) {
				title.setText("Please give a title!");
			} else if (link.getText().isEmpty() || !link.getText().contains("watch?v=")) {
				link.setText("Please give us a valid link!");
			} else {
				songs.addSong(link.getText(), title.getText(), count);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("SUCCESS");
				alert.setHeaderText(null);
				alert.setContentText("Song #" + count + " \"" + title.getText() + "\" added!");
				count++;
				
				alert.show();
				
				title.setText("");
				link.setText("");
			}
		});

		Button allDone = new Button("All Done!");
		allDone.setStyle("-fx-font: 30 Dinpro; -fx-base: #da81f5;-fx-text-fill: white;");
		allDone.setAlignment(Pos.BASELINE_CENTER);
		allDone.setPrefHeight(75);
		allDone.setPrefWidth(400);

		allDone.setOnAction(e -> {
			makeStage.close();
			playNowMenu(songs);
		});

		Scene makeScene = new Scene(makePane, 450, 500);

		makePane.add(titleLabel, 0, 0);
		makePane.add(title, 0, 1);
		makePane.add(linkLabel, 0, 2);
		makePane.add(link, 0, 3);
		makePane.add(addSong, 0, 4);
		makePane.add(allDone, 0, 5);

		makeStage.setTitle("Make the JukeBox!");
		makeStage.setScene(makeScene);
		makeStage.show();
	}

	public void playNowMenu(SongList songs) {
		Stage menuStage = new Stage();
		menuStage.setResizable(false);

		GridPane menuPane = new GridPane();
		menuPane.setHgap(50);
		menuPane.setVgap(50);
		menuPane.setPadding(new Insets(50, 50, 50, 50));

		Scene menuScene = new Scene(menuPane, 500, 300);

		Button play = new Button("Play this JukeBox!");
		play.setPrefHeight(100);
		play.setPrefWidth(400);
		play.setStyle("-fx-font: 30 Dinpro; -fx-base: #fa58ac; -fx-text-fill: white;");

		play.setOnAction(e -> {
			menuStage.close();
			jukeBoxPlayer(songs);
		});

		Button save = new Button("Save File For Later!");
		save.setPrefHeight(100);
		save.setPrefWidth(400);
		save.setStyle("-fx-font: 30 Dinpro; -fx-base: #81daf5;-fx-text-fill: white;");

		save.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text File", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
            
            fileChooser.setTitle("Save Juke Box Song List");
			File file = fileChooser.showSaveDialog(menuStage);
            if (file != null) {
                try {
                	ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
            		output.writeObject(songs);
            		output.close();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

			menuStage.close();
			menuRunner();
		});

		menuPane.add(play, 0, 0);
		menuPane.add(save, 0, 1);

		menuStage.setTitle("Play or Save?");
		menuStage.setScene(menuScene);
		menuStage.show();
	}

	public void jukeBoxPlayer(SongList songs) {
		openStage.setResizable(false);
		Stage primaryStage  = new Stage();
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(0);
		gridPane.setVgap(0);
		gridPane.setPadding((new Insets(0,0,0,0)));
		
		TextField currentSong = new TextField();
		currentSong.setPrefHeight(35);
		currentSong.setPrefWidth(75);
		gridPane.add(currentSong, 0, 0, 750, 1);
		currentSong.setEditable(false);
		
		Button one = new Button("*");
		one.setPrefHeight(100);
		one.setPrefWidth(75);
		gridPane.add(one,0,1);
		one.setStyle("-fx-font: 35 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #33FFF6;"); 
		one.setOnAction(e -> {
			buttonVal = "*";
		});
		
		Button two = new Button("T");
		two.setPrefHeight(100);
		two.setPrefWidth(75);
		gridPane.add(two, 1, 1);
		two.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #1EEEE5;");
		two.setOnAction(e -> {
			buttonVal =  "T";
		});
		
		Button three = new Button("E");
		three.setPrefHeight(100);
		three.setPrefWidth(75);
		gridPane.add(three, 2, 1);
		three.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #22D5CD;");
		three.setOnAction(e -> {
			buttonVal = "E";
		});
		
		Button four = new Button("C");
		four.setPrefHeight(100);
		four.setPrefWidth(75);
		gridPane.add(four, 3, 1);
		four.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #2EC5BE;");
		four.setOnAction(e -> {
			buttonVal = "C";
		});
		
		Button five = new Button("H");
		five.setPrefHeight(100);
		five.setPrefWidth(75); 
		gridPane.add(five, 4, 1);
		five.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #679FA5;");
		five.setOnAction(e -> {
			buttonVal = "H";
		});
		
		Button six = new Button("N");
		six.setPrefHeight(100);
		six.setPrefWidth(75);
		gridPane.add(six, 5, 1);
		six.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #A97A9A;");
		six.setOnAction(e -> {
			buttonVal = "N";
		});
		
		Button seven = new Button("I");
		seven.setPrefHeight(100);
		seven.setPrefWidth(75);
		gridPane.add(seven, 6, 1);
		seven.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #BD84AA;");
		seven.setOnAction(e -> {
			buttonVal = "I";
		});
		
		Button eight = new Button("K");
		eight.setPrefHeight(100);
		eight.setPrefWidth(75);
		gridPane.add(eight, 7, 1);
		eight.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #EC9DD1;");
		eight.setOnAction(e -> {
			buttonVal = "K";
		});
		
		Button nine = new Button("A");
		nine.setPrefHeight(100);
		nine.setPrefWidth(75);
		gridPane.add(nine, 8, 1);
		nine.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #FC99DA;");
		nine.setOnAction(e -> {
			buttonVal = "A";
		});
		
		Button ten = new Button("!");
		ten.setPrefHeight(100);
		ten.setPrefWidth(75);
		gridPane.add(ten, 9, 1);
		ten.setStyle("-fx-font: 35 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #FD75CF;");
		ten.setOnAction(e -> {
			buttonVal = "!";
		});
		
		Button eleven = new Button("0");
		eleven.setPrefHeight(100);
		eleven.setPrefWidth(75);
		gridPane.add(eleven, 0, 2);
		eleven.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #33FFF6;");
		eleven.setOnAction(e -> {
			buttonVal += "0"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
			
		});
		
		Button twelve = new Button("1");
		twelve.setPrefHeight(100);
		twelve.setPrefWidth(75);
		gridPane.add(twelve, 1, 2);
		twelve.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #1EEEE5;");
		twelve.setOnAction(e -> {
			buttonVal += "1"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button thirteen = new Button("2");
		thirteen.setPrefHeight(100);
		thirteen.setPrefWidth(75);
		gridPane.add(thirteen, 2, 2);
		thirteen.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #22D5CD;");
		thirteen.setOnAction(e -> {
			buttonVal += "2"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button fourteen = new Button("3");
		fourteen.setPrefHeight(100);
		fourteen.setPrefWidth(75);
		gridPane.add(fourteen, 3, 2);
		fourteen.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #2EC5BE;");
		fourteen.setOnAction(e -> {
			buttonVal += "3"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button fifteen = new Button("4");
		fifteen.setPrefHeight(100);
		fifteen.setPrefWidth(75);
		gridPane.add(fifteen, 4, 2);
		fifteen.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #679FA5;");
		fifteen.setOnAction(e -> { 
			buttonVal += "4"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button sixteen = new Button("5");
		sixteen.setPrefHeight(100);
		sixteen.setPrefWidth(75);
		gridPane.add(sixteen, 5, 2);
		sixteen.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #A97A9A;");
		sixteen.setOnAction(e -> {
			buttonVal += "5"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button seventeen = new Button("6");
		seventeen.setPrefHeight(100);
		seventeen.setPrefWidth(75);
		gridPane.add(seventeen, 6, 2);
		seventeen.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #BD84AA;");
		seventeen.setOnAction(e -> {
			buttonVal += "6"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button eighteen = new Button("7");
		eighteen.setPrefHeight(100);
		eighteen.setPrefWidth(75);
		gridPane.add(eighteen, 7, 2);
		eighteen.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #EC9DD1;");
		eighteen.setOnAction(e -> { 
			buttonVal += "7"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button nineteen = new Button("8");
		nineteen.setPrefHeight(100);
		nineteen.setPrefWidth(75);
		gridPane.add(nineteen, 8, 2);
		nineteen.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #FC99DA;");
		nineteen.setOnAction(e -> {
			buttonVal += "8"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button twenty = new Button("9");
		twenty.setPrefHeight(100);
		twenty.setPrefWidth(75);
		gridPane.add(twenty, 9, 2);
		twenty.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #FD75CF;");
		twenty.setOnAction(e -> {
			buttonVal += "9"; 

			webview.getEngine().load(null);
			openStage.hide();
			
			String link = songs.getLink(buttonVal);
			
			//Stage hiddenStage = new Stage();
			
			GridPane hiddenPane = new GridPane();
			
			/*WebView browser = new WebView();
			WebEngine webEngine = browser.getEngine();
			webEngine.load(link);

			hiddenPane.add(browser, 0, 0);*/
			
			//WebView webview = new WebView();
		    webview.getEngine().load(link);
		    
		    hiddenPane.add(webview, 0, 0);
		    
		    Scene hiddenScene = new Scene(hiddenPane, 1, 1);
		    //hiddenScene.setRoot(root);
		    
		    openStage.setTitle("");
			openStage.setScene(hiddenScene);
			openStage.show();
			
			
			currentSong.setText(songs.getTitle(buttonVal));
		});
		
		Button volUp = new Button("X");
		volUp.setPrefHeight(100);
		volUp.setPrefWidth(75);
		gridPane.add(volUp, 10, 1);
		volUp.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #FC61C8");
		volUp.setOnAction(e -> {
			primaryStage.close();
			openStage.close();
		});
		
		Button volDown = new Button("");
		volDown.setPrefHeight(100);
		volDown.setPrefWidth(75);
		gridPane.add(volDown, 10, 2);
		volDown.setStyle("-fx-font: 30 DINpro; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-base: #FC61C8");
		volDown.setOnAction(e -> {
			openStage.close();
			openStage = new Stage();
		});
		
		TextArea songlist = new TextArea();
		songlist.setPrefHeight(300);
		songlist.setPrefWidth(750);
		//gridPane.add(songlist, 0, 3, 750, 3);
		songlist.setEditable(false);
		songlist.setWrapText(true);
		songlist.setText(songs.toString());
		
		ScrollPane scrollpane = new ScrollPane();
		scrollpane.setPrefSize(750, 300);
		scrollpane.setContent(songlist);
		
		gridPane.add(scrollpane, 0, 3, 750, 300);

		Scene scene = new Scene(gridPane, 750, 550);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setTitle("JukeBox");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void jukeBoxPlayer() {
		Stage menuStage = new Stage();
		menuStage.setResizable(false);

		GridPane menuPane = new GridPane();
		menuPane.setHgap(50);
		menuPane.setVgap(50);
		menuPane.setPadding(new Insets(50, 50, 50, 50));

		Scene menuScene = new Scene(menuPane, 500, 300);

		Button choose = new Button("Choose File!");
		choose.setPrefHeight(100);
		choose.setPrefWidth(400);
		choose.setStyle("-fx-font: 30 Dinpro; -fx-base: #fa58ac; -fx-text-fill: white;");

		Button menu = new Button("Return Main Menu!");
		menu.setPrefHeight(100);
		menu.setPrefWidth(400);
		menu.setStyle("-fx-font: 30 Dinpro; -fx-base: #81daf5;-fx-text-fill: white;");

		choose.setOnAction(e -> {
			SongList songs = new SongList();
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fileChooser.showOpenDialog(menuStage);
			
			if(selectedFile != null) {
				try{
					ObjectInputStream input = new ObjectInputStream(new FileInputStream(selectedFile));
					SongList savedList = (SongList) input.readObject();
					songs = savedList;
					jukeBoxPlayer(songs);
					menuStage.close();
					input.close();
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
			}
		});

		menu.setOnAction(e -> {
			menuStage.close();
			menuRunner();
		});

		menuPane.add(choose, 0, 0);
		menuPane.add(menu, 0, 1);

		menuStage.setTitle("Choose What to Do!");
		menuStage.setScene(menuScene);
		menuStage.show();
	}
}
