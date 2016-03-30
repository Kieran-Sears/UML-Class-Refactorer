/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import Evolution.MetaModel;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.ModelViewer;

/**
 *
 * @author Kieran
 */
public class FXMLDocumentController implements Initializable {

    ParserController parser;
    EvolutionController evolution;
    DesignPatternsController patterns;
    ModelViewer viewer;
    int generation = 0;
    MetaModel original;

    @FXML
    private Button generateButton;
    @FXML
    AreaChart<String, Number> chart;
    @FXML
    private TextField populationSize;
    @FXML
    private TextField mutationRate;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
         parser = new ParserController();
        generateButton.setText("Generate Population");
        generateButton.setOnAction((event) -> {
            initialiseGA();
        });
    }

    @FXML
    private void initialiseGA() {
        if (original == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No File Found");
            alert.setHeaderText(null);
            alert.setContentText("No XMI file has been loaded, Select a Model for Evolving.");
            alert.showAndWait();
        } else {
            chart.getData().clear();
            int popSize = Integer.parseInt(populationSize.getText());
            float mutRate = Float.parseFloat(mutationRate.getText());
            evolution.initialiseGA(original, popSize, mutRate);
            updateChart(evolution.evolvePopulation());
            generateButton.setText("Next Generation");
            generateButton.setOnAction((event) -> {
                iterate();
            });
        }
    }

    public void iterate() {
        chart.getData().clear();
        generation++;
        updateChart(evolution.evolvePopulation());
    }

    public void updateChart(ArrayList<MetaModel> evolvePopulation) {
        ObservableList<XYChart.Series<String, Number>> answer = FXCollections.observableArrayList();
        for (MetaModel model : evolvePopulation) {
            int populationMember = evolvePopulation.indexOf(model);
            XYChart.Series<String, Number> series = new XYChart.Series();
            series.setName(String.valueOf("N." + populationMember));
          //  System.out.println("Cohesion " + model.getFitness().getCohesionBetweenObjectClasses());
           // System.out.println("Coupling " + model.getFitness().getCouplingBetweenObjectClasses());
          //  System.out.println("Methods\nDistribution " + model.getFitness().getWeightedMethodsPerClass());
            series.getData().add(new XYChart.Data<>("Cohesion", model.getFitness().getCohesionBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Coupling", model.getFitness().getCouplingBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Methods\nDistribution", model.getFitness().getWeightedMethodsPerClass()));
            answer.add(series);
            chart.setData(answer);
            setOnMouseEventsOnSeries(series.getNode(), model);
        }

    }

    public void loadFile() {
        FileChooser fc = new FileChooser();
        // start for faster testing
        File developerFastFindFile = new File(System.getProperty("user.dir") + "\\testcases");
        if (developerFastFindFile.exists()) {
            fc.setInitialDirectory(developerFastFindFile);
        }
        // end for faster testing
        try {
            File file = fc.showOpenDialog(null);
            MetaModel model = parser.extractModelFromXMI(file);
            original = model;
            chart.getData().clear();
            evolution = new EvolutionController();
            patterns = new DesignPatternsController();
            viewer = new ModelViewer();
            generateButton.setText("Generate Population");
            generateButton.setOnAction((event) -> {
                initialiseGA();
            });
            XYChart.Series series = new XYChart.Series();
            series.getData().add(new XYChart.Data<>("Cohesion", model.getFitness().getCohesionBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Coupling", model.getFitness().getCouplingBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Methods\nDistribution", model.getFitness().getWeightedMethodsPerClass()));
            chart.getData().add(series);
            setOnMouseEventsOnSeries(series.getNode(), model);
        } catch (IllegalArgumentException e) {
            System.out.println("user cancelled loading file");
        }
    }

    private void setOnMouseEventsOnSeries(Node node, MetaModel model) {

        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Stage stage = new Stage();
                File view = viewer.generateModelView(model);
                WebView webview = new WebView();
                WebEngine engine = webview.getEngine();
                engine.setJavaScriptEnabled(true);
                engine.load("File:///" + view.getAbsolutePath());

                final VBox wizBox = new VBox(5);
                wizBox.setAlignment(Pos.CENTER);
                wizBox.getChildren().setAll(webview);

                Scene scene = new Scene(wizBox, 800, 600);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(chart.getScene().getWindow());
                stage.showAndWait();

            }
        });

    }

    @FXML
    public void reset() {
        if (original != null) {
            chart.getData().clear();
            XYChart.Series series = new XYChart.Series();
            series.getData().add(new XYChart.Data<>("Cohesion", original.getFitness().getCohesionBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Coupling", original.getFitness().getCouplingBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Methods\nDistribution", original.getFitness().getWeightedMethodsPerClass()));
            chart.getData().add(series);
            setOnMouseEventsOnSeries(series.getNode(), original);
        } 
        parser = new ParserController();
        evolution = new EvolutionController();
        patterns = new DesignPatternsController();
        viewer = new ModelViewer();
        generateButton.setText("Generate Population");
        generateButton.setOnAction((event) -> {
            initialiseGA();
        });

    }

}
