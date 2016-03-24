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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
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

    ObservableList<XYChart.Series<String, Number>> ChartData;

    @FXML
    private Button generateButton;
    @FXML
    AnchorPane displayPane;
    @FXML
    LineChart<String, Number> chart;
    @FXML
    private TextField populationSize;
    @FXML
    private TextField mutationRate;
    @FXML
    private TextField crossoverRate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ChartData = FXCollections.observableArrayList();
        parser = new ParserController();
        evolution = new EvolutionController();
        patterns = new DesignPatternsController();
        viewer = new ModelViewer();
        generateButton.setText("Generate Population");
        generateButton.setOnAction((event) -> {
            initialiseGA();
        });
         LineChart.Series<String, Number> modelResults = new LineChart.Series<>();
        modelResults.setName(Integer.toString(generation));
        modelResults.getData().add(new XYChart.Data<>("Cohesion", 0));
        modelResults.getData().add(new XYChart.Data<>("Coupling", 0));
        modelResults.getData().add(new XYChart.Data<>("Methods\nDistribution", 0));
        ChartData.addAll(modelResults);
        chart.setData(ChartData);
    }

    @FXML
    private void initialiseGA() {
        int popSize = Integer.parseInt(populationSize.getText());
        float mutRate = Float.parseFloat(mutationRate.getText());
        float crossRate = Float.parseFloat(crossoverRate.getText());
        patterns.scanForAntiPatterns(original);
        patterns.scanForPatterns(original);
        evolution.initialiseGA(original, popSize, mutRate, crossRate);
        ArrayList<MetaModel> evolvePopulation = evolution.evolvePopulation();
        for (MetaModel metaModel : evolvePopulation) {
            updateChart(metaModel, evolvePopulation.indexOf(metaModel));
            File view = viewer.generateModelView(metaModel);
            WebView webview = new WebView();  // the thumbnail preview of the artefact in cell
            WebEngine engine = webview.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.load("File:///" + view.getAbsolutePath());
            displayPane.getChildren().add(webview);
        }
        generateButton.setText("Next Generation");
        generateButton.setOnAction((event) -> {
            iterate();
        });

    }

    public void iterate() {
        ChartData = FXCollections.observableArrayList();
        ArrayList<MetaModel> evolvePopulation = evolution.evolvePopulation();
        generation++;
        for (MetaModel metaModel : evolvePopulation) {
            patterns.scanForAntiPatterns(original);
            patterns.scanForPatterns(original);
            updateChart(metaModel, evolvePopulation.indexOf(metaModel));
            File view = viewer.generateModelView(metaModel);
            WebView webview = new WebView();  // the thumbnail preview of the artefact in cell
            WebEngine engine = webview.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.load("File:///" + view.getAbsolutePath());
            displayPane.getChildren().add(webview);
        }
    }

    public void updateChart(MetaModel model, int populationMember) {
        ChartData.clear();
        StackedBarChart.Series<String, Number> modelResults = new StackedBarChart.Series<>();
        modelResults.setName(Integer.toString(populationMember));
        modelResults.getData().add(new XYChart.Data<>("Cohesion", model.getFitness().getCohesionBetweenObjectClasses()));
        modelResults.getData().add(new XYChart.Data<>("Coupling", model.getFitness().getCouplingBetweenObjectClasses()));
        modelResults.getData().add(new XYChart.Data<>("Methods\nDistribution", model.getFitness().getWeightedMethodsPerClass()));
        ChartData.addAll(modelResults);
        chart.setData(ChartData);
    }

    public void loadFile() {
        FileChooser fc = new FileChooser();
        File developerFastFindFile = new File(System.getProperty("user.dir")+"\\testcases");
        if (developerFastFindFile.exists()) {
            fc.setInitialDirectory(developerFastFindFile);
        }
        try {
            File file = fc.showOpenDialog(null);
            MetaModel model = parser.extractModelFromXMI(file);
            patterns.scanForAntiPatterns(model);
            patterns.scanForPatterns(model);
            updateChart(model, 0);
            original = model;
            File view = viewer.generateModelView(model);
            WebView webview = new WebView();  // the thumbnail preview of the artefact in cell
            WebEngine engine = webview.getEngine();
            engine.setJavaScriptEnabled(true);
            engine.load("File:///" + view.getAbsolutePath());
            displayPane.getChildren().add(webview);
        } catch (IllegalArgumentException e) {
            System.out.println("user cancelled loading file");
        }
    }

}
