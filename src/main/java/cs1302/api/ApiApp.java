package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Separator;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.net.URI;
import java.net.http.HttpResponse.BodyHandlers;

import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    private final String nytKey = "fnZeEiDLqZ6RLRzv2g8rBbSS7ofmqlZo";

    private Stage stage;
    private Scene scene;
    private VBox root;
    private HBox inputLayer;
    private Label locationLabel;//try text
    private TextField cityBox;
    private TextField zipCodeBox;
    private Label dateLabel;
    private ComboBox<String> dateBox;
    private Text instructions;
    private Button loadButton;
    private HBox mainBox;
    private HBox buttonLayer;
    private Button prevButton;
    private Button nextButton;
    private ImageView[] imageArr;
    private ArrayList<ImageView> articImages;

    private NYTResponse nytResponses;
    private NYTResult[] nytResults;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox(10);

        inputLayer = new HBox();
        locationLabel = new Label("Location: ");
        cityBox = new TextField("City");
        zipCodeBox = new TextField("Zip Code");
        dateLabel = new Label("Dates:");
        dateBox = new ComboBox<String>(); //add ranges
        dateBox.getItems().addAll();
        instructions = new Text("Enter your location to get events near you with good weather.");
        loadButton = new Button("Load");
        mainBox = new HBox();
        buttonLayer = new HBox();
        prevButton = new Button("Prev");
        nextButton = new Button("Next");

        imageArr = new ImageView[20];
        //populate mainBox with 20 default images
        for (int i = 0; i < 5; i++) {
            VBox tempVbox = new VBox();
            for (int j = 0; j < 4; j++) {
                Image tempImage = new Image("file:resources/default.png");
                ImageView tempImageView = new ImageView(tempImage);
                imageArr[(i * 4 + j)] = tempImageView;
                tempVbox.getChildren().addAll(tempImageView);
            } //for
            mainBox.getChildren().addAll(tempVbox);
        } //for
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // demonstrate how to load local asset using "file:resources/"
        Image bannerImage = new Image("file:resources/readme-banner.png");
        ImageView banner = new ImageView(bannerImage);
        banner.setPreserveRatio(true);
        banner.setFitWidth(640);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        inputLayer.getChildren().addAll(locationLabel, cityBox, zipCodeBox,
                                        dateLabel, dateBox, loadButton);
        buttonLayer.getChildren().addAll(prevButton, nextButton);
        root.getChildren().addAll(inputLayer, instructions, mainBox, buttonLayer);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));
        Runnable r = () -> load();
        loadButton.setOnAction(event -> startThread(r));

    } // start

    /**
     * Load button action
     */
    public void load() {
        instructions.setText("Loading...");
        String category = "arts";
        String nytQuery = "https://api.nytimes.com/svc/topstories/v2/" +
            category + ".json?api-key=" + nytKey;
        try {
            HttpRequest nytRequest = HttpRequest.newBuilder()
                .uri(URI.create(nytQuery))
                .build();
            HttpResponse<String> nytResponse = HTTP_CLIENT.send(nytRequest,
                                                                BodyHandlers.ofString());
            String body = nytResponse.body();
            if (nytResponse.statusCode() != 200) {
                throw new Exception("Failed to access NYT API");
            } //if
            nytResponses = GSON.fromJson(body, NYTResponse.class);
            nytResults = nytResponses.results;

            if (nytResults == null) {
                throw new Exception("Failed to access NYT API");
            } // if
            loadArt(nytResults[0].des_facet);
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> alert(e, nytQuery));
        } //catch
    } //load

    public void loadArt(String[] words) {
        ArrayList<String> urls = new ArrayList<>();
        for (String word : words) {
            String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);
            String artQuery = "https://api.artic.edu/api/v1/artworks/search?q=" + encodedWord;
            try {
                //queries api searching for term
                HttpRequest articRequest = HttpRequest.newBuilder()
                    .uri(URI.create(artQuery))
                    .build();
                HttpResponse<String> articResponse = HTTP_CLIENT.send(articRequest,
                                                                      BodyHandlers.ofString());
                String articBody = articResponse.body();
                if (articResponse.statusCode() != 200) {
                    throw new Exception("Failed to access ARTIC API");
                } // if
                ArticResponse articResponses = GSON.fromJson(articBody, ArticResponse.class);
                ArticData[] articData = articResponses.data;
                //constructs image links
                for (ArticData a : articData) {
                    try {
                        System.out.println("api link: " + a.api_link); //delete
                        HttpRequest imageRequest = HttpRequest.newBuilder()
                            .uri(URI.create(a.api_link))
                            .build();
                        HttpResponse<String> imageResponse = HTTP_CLIENT.send(imageRequest,
                                                                              BodyHandlers.ofString());
                        String imageBody = imageResponse.body();
                        if (imageResponse.statusCode() != 200) {
                            throw new Exception("Failed to access ARTIC API");
                        } // if
                        ArticArt art = GSON.fromJson(imageBody, ArticArt.class);
                        ArticArtwork artwork = art.data;
                        String image_url = artwork.iiif_url + "/" + artwork.image_id +
                            "/full/843,/0/default.jpg";
                        System.out.println(image_url);
                        Image artImage = new Image(image_url);
                        ImageView artImageView = new ImageView(artImage);
                        articImages.add(artImageView);
                    } catch (Exception e) {
                        System.out.println("Failed to access " + a.api_link);
                    } // try-catch
                } // for
                Platform.runLater(() -> updateImages());
            } catch (Exception e) {
                e.printStackTrace();
            } // try-catch
        } // for
    }

    private void updateImages() {
        int numImages = articImages.size();
        if (numImages < 20) {
            throw new IllegalArgumentException("Not enough artworks provided");
        }
        int[] alreadyIn = new int[20];
        int i = 0;
        boolean b = true;
        while (i < 20) {
            int randNum = (int)(Math.random() * numImages);
            for (int j = 0; j < i; j++) {
                if (alreadyIn[j] == randNum) {
                    b = false;
                    break;
                }
            }
            if (b) {
                alreadyIn[i] = randNum;
                imageArr[i] = articImages.get(i);
                i++;
            }
        }
    }
    /**
     * Displays error on screen
     * @param uri -- the uri that was given when the exception was thrown
     * @param e -- the thrown exception
     */
    private void alert(Throwable e, String uri) {
        TextArea text = new TextArea(uri + "\n" + e.toString());
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
        instructions.setText("Last attempt failed");
    } // alert

    private void startThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    }

} // ApiApp
