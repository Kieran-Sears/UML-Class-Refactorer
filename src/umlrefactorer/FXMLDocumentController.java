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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
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

    ObservableList<XYChart.Series<String, Number>> ChartData;

    @FXML
    private Button generateButton;
    @FXML
    AreaChart<String, Number> chart;
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
        ChartData.clear();
        int popSize = Integer.parseInt(populationSize.getText());
        float mutRate = Float.parseFloat(mutationRate.getText());
        float crossRate = Float.parseFloat(crossoverRate.getText());
        patterns.scanForAntiPatterns(original);
        patterns.scanForPatterns(original);
        evolution.initialiseGA(original, popSize, mutRate, crossRate);
        ArrayList<MetaModel> evolvePopulation = evolution.evolvePopulation();
        for (MetaModel metaModel : evolvePopulation) {
            updateChart(metaModel, evolvePopulation.indexOf(metaModel));
        }
        generateButton.setText("Next Generation");
        generateButton.setOnAction((event) -> {
            iterate();
        });

    }

    public void iterate() {
        ChartData.clear();
        ArrayList<MetaModel> evolvePopulation = evolution.evolvePopulation();
        generation++;
        for (MetaModel metaModel : evolvePopulation) {
            patterns.scanForAntiPatterns(original);
            patterns.scanForPatterns(original);
            updateChart(metaModel, evolvePopulation.indexOf(metaModel));
        }
    }

    public void updateChart(MetaModel model, int populationMember) {
        StackedBarChart.Series<String, Number> modelResults = new StackedBarChart.Series<>();
        modelResults.setName(Integer.toString(populationMember));

        System.out.println("cohesion "
                + model.getFitness().getCohesionBetweenObjectClasses() + "\ncoupling "
                + model.getFitness().getCouplingBetweenObjectClasses() + "\nwmpc "
                + model.getFitness().getWeightedMethodsPerClass());

        modelResults.getData().add(new XYChart.Data<>("Cohesion", model.getFitness().getCohesionBetweenObjectClasses()));
        modelResults.getData().add(new XYChart.Data<>("Coupling", model.getFitness().getCouplingBetweenObjectClasses()));
        modelResults.getData().add(new XYChart.Data<>("Methods\nDistribution", model.getFitness().getWeightedMethodsPerClass()));
       
        ChartData.addAll(modelResults);
        chart.setData(ChartData);
        setOnMouseEventsOnSeries(modelResults.getNode(), model);
    }

    public void loadFile() {
        FileChooser fc = new FileChooser();
        File developerFastFindFile = new File(System.getProperty("user.dir") + "\\testcases");
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
                WebView webview = new WebView();  // the thumbnail preview of the artefact in cell
                WebEngine engine = webview.getEngine();
                engine.setJavaScriptEnabled(true);
                engine.load("File:///" + view.getAbsolutePath());

                final VBox wizBox = new VBox(5);
                wizBox.setAlignment(Pos.CENTER);
                wizBox.getChildren().setAll(webview);

                Scene scene = new Scene(wizBox, 200, 200);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(chart.getScene().getWindow());
                stage.showAndWait();
                
            }
        });

    }

}
