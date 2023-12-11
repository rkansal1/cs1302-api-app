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
        loadButton.setOnAction(event -> load());

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
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(nytQuery))
                .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            String body = response.body();
            if (response.statusCode() != 200) {
                throw new Exception("Failed to access NYT API");
            } //if
            NYTResponse nytResponse = GSON.fromJson(body, NYTResponse.class);
            NYTResult[] nytResults = nytResponse.results;

            if (nytResults != null) {
                for (NYTResult n : nytResults) {
                    System.out.println(n);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> alert(e, nytQuery));
        } //catch
    } //load

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

} // ApiApp
