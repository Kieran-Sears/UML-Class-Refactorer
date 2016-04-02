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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
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
    ObservableList<XYChart.Series<Number, Number>> overallFitnessList = FXCollections.observableArrayList();
    XYChart.Series<Number, Number> overallFitnessSeries = new XYChart.Series();

    @FXML
    private Button generateButton;
    @FXML
    private LineChart<Number, Number> overallFitnessChart;
    @FXML
    private StackedAreaChart<String, Number> eliteFitnessChart;
    @FXML
    private TextField populationSize;
    @FXML
    private TextField numOfGens;
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
            overallFitnessChart.getData().clear();
            int popSize = Integer.parseInt(populationSize.getText());
            float mutRate = Float.parseFloat(mutationRate.getText());
            evolution.initialiseGA(original, popSize, mutRate);
            overallFitnessList.add(overallFitnessSeries);
            overallFitnessChart.setData(overallFitnessList);
            ArrayList<MetaModel> evolvePopulation = evolution.evolvePopulation(Integer.parseInt(numOfGens.getText()));
            updateOverallChart(evolvePopulation);
            updateEliteChart(evolvePopulation);
            generateButton.setText("Next Generation");
            generateButton.setOnAction((event) -> {
                iterate();
            });
        }
    }

    public void iterate() {
        generation++;
        ArrayList<MetaModel> evolvePopulation = evolution.evolvePopulation(Integer.parseInt(numOfGens.getText()));
        updateOverallChart(evolvePopulation);
        updateEliteChart(evolvePopulation);
    }

    public void updateOverallChart(ArrayList<MetaModel> evolvePopulation) {
        double overallFitness = 0;
        for (MetaModel model : evolvePopulation) {
            overallFitness += model.getFitness().getOverallFitness();
        }
        overallFitnessSeries.getData().add(new XYChart.Data<>(generation, overallFitness));
    }

    public void updateEliteChart(ArrayList<MetaModel> evolvePopulation) {
        ObservableList<XYChart.Series<String, Number>> observableList = FXCollections.observableArrayList();
        for (MetaModel model : evolvePopulation) {
            int populationMember = evolvePopulation.indexOf(model);
            XYChart.Series<String, Number> series = new XYChart.Series();
            series.setName(String.valueOf("N." + populationMember));
            System.out.println("Cohesion" + model.getFitness().getCohesionBetweenObjectClasses()
            + "Coupling" + model.getFitness().getCouplingBetweenObjectClasses()
            + "Methods\nDistribution" + model.getFitness().getWeightedMethodsPerClass());
            series.getData().add(new XYChart.Data<>("Methods\nDistribution", model.getFitness().getWeightedMethodsPerClass()));
            series.getData().add(new XYChart.Data<>("Cohesion", model.getFitness().getCohesionBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Coupling", model.getFitness().getCouplingBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Methods\nDistribution", model.getFitness().getWeightedMethodsPerClass()));
            observableList.add(series);
            eliteFitnessChart.setData(observableList);
            eliteFitnessChart.getYAxis().autoRangingProperty().setValue(false);
            ((NumberAxis) eliteFitnessChart.getYAxis()).setUpperBound(100);
            ((NumberAxis) eliteFitnessChart.getYAxis()).setTickUnit(10);
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
            overallFitnessChart.getData().clear();
            evolution = new EvolutionController();
            patterns = new DesignPatternsController();
            viewer = new ModelViewer();
            generateButton.setText("Generate Population");
            generateButton.setOnAction((event) -> {
                initialiseGA();
            });
            XYChart.Series<Number, Number> series1 = new XYChart.Series();
            series1.getData().add(new XYChart.Data(generation, model.getFitness().getOverallFitness()));
            overallFitnessChart.getData().add(series1);
            ArrayList<MetaModel> add = new ArrayList();
            add.add(model);
            updateEliteChart(add);
        } catch (IllegalArgumentException e) {
            System.out.println("user cancelled loading file");
        }
    }

    private void setOnMouseEventsOnSeries(Node node, MetaModel model) {

        node.setOnMouseClicked((MouseEvent t) -> {
            // class diagram view
            Stage stage1 = new Stage();
            File view = viewer.generateModelView(model);
            WebView webview = new WebView();
            WebEngine engine = webview.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.load("File:///" + view.getAbsolutePath());

            // fitness chart view
            ObservableList<XYChart.Series<String, Number>> observableList = FXCollections.observableArrayList();
            XYChart.Series<String, Number> series = new XYChart.Series();
            series.getData().add(new XYChart.Data<>("Cohesion", model.getFitness().getCohesionBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Coupling", model.getFitness().getCouplingBetweenObjectClasses()));
            series.getData().add(new XYChart.Data<>("Methods\nDistribution", model.getFitness().getWeightedMethodsPerClass()));
            observableList.add(series);
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            yAxis.autoRangingProperty().setValue(false);
            yAxis.setUpperBound(100);
            yAxis.setTickUnit(10);
            AreaChart<String, Number> fitnessChart = new AreaChart(xAxis, yAxis, observableList);

            // actual measurements for fitness
            Label cohesion = new Label("cohesion /" + model.getFitness().getCohesionBetweenObjectClasses());
            Label coupling = new Label("coupling /" + model.getFitness().getCouplingBetweenObjectClasses());
            Label WMPC = new Label("WMPC /" + model.getFitness().getWeightedMethodsPerClass());
            
            // render the scene
            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER);
            hBox.getChildren().setAll(webview, fitnessChart);
            final VBox vBox = new VBox(5);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().setAll(hBox, cohesion, coupling, WMPC);
            Scene scene = new Scene(vBox, 1200, 800);
            stage1.setScene(scene);
            stage1.show();
        });

    }

    @FXML
    public void reset() {
        if (original != null) {
            overallFitnessChart.getData().clear();
            XYChart.Series<Number, Number> series = new XYChart.Series();
            series.getData().add(new XYChart.Data<>(generation, original.getFitness().getOverallFitness()));
            overallFitnessChart.getData().add(series);
            setOnMouseEventsOnSeries(series.getNode(), original);
        }
        parser = new ParserController();
        evolution = new EvolutionController();
        patterns = new DesignPatternsController();
        viewer = new ModelViewer();
        generation = 0;
        generateButton.setText("Generate Population");
        generateButton.setOnAction((event) -> {
            initialiseGA();
        });

    }

}
