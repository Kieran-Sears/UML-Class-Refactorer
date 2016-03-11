/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import Evolution.FitnessMetrics;
import Evolution.MetaModel;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author Kieran
 */
public class FXMLDocumentController implements Initializable {

    ParserController parser;
    EvolutionController evolution;
    DesignPatternsController patterns;
    int generation = 0;

    ObservableList<XYChart.Series<String, Number>> cohesionFitness;
    ObservableList<XYChart.Series<String, Number>> couplingFitness;
    ObservableList<XYChart.Series<String, Number>> methodDistrobutionFitness;

    XYChart.Series<String, Number> cohesionScores;
    XYChart.Series<String, Number> couplingScores;
    XYChart.Series<String, Number> methodDistrobutionScores;

    @FXML
    StackedBarChart<String, Number> chart;
    @FXML
    private TextField populationSize;
    @FXML
    private TextField mutationRate;
    @FXML
    private TextField crossoverRate;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        File file = new File("C:\\Users\\Kieran\\Documents\\NetBeansProjects\\UMLRefactorer\\src/Parser/bookshopCaseScenario.xmi");
        MetaModel model = parser.extractModelFromXMI(file);
        // model.printModelToConsole(); // for debugging
        // TODO Handle Invalid Input
        int popSize = Integer.parseInt(populationSize.getText());
        float mutRate = Float.parseFloat(mutationRate.getText());
        float crossRate = Float.parseFloat(crossoverRate.getText());
        patterns.scanForAntiPatterns(model);
        patterns.scanForPatterns(model);
        evolution.initialiseGA(model, popSize, mutRate, crossRate);
        updateChart(evolution.evolvePopulation());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cohesionScores = new XYChart.Series<>();
        cohesionScores.setName("Cohesion");
        couplingScores = new XYChart.Series<>();
        couplingScores.setName("Coupling");
        methodDistrobutionScores = new XYChart.Series<>();
        methodDistrobutionScores.setName("Method\nDistribution");

        parser = new ParserController();
        evolution = new EvolutionController();
        patterns = new DesignPatternsController();
    }

    public void updateChart(HashMap<String, Object> results) {
        ObservableList<XYChart.Series<String, Number>> stackBarChartData = FXCollections.observableArrayList();
        StackedBarChart.Series<String, Number> series = new StackedBarChart.Series<>();
        series.setName(Integer.toString(generation));
        series.getData().add(new XYChart.Data<>("Cohesion", ((FitnessMetrics) results.get("fitness")).getCohesionBetweenObjectClasses()));
        series.getData().add(new XYChart.Data<>("Coupling", ((FitnessMetrics) results.get("fitness")).getCouplingBetweenObjectClasses()));
        series.getData().add(new XYChart.Data<>("Methods\nDistrobution", ((FitnessMetrics) results.get("fitness")).getWeightedMethodsPerClass()));
        stackBarChartData.addAll(series);
        chart.setData(stackBarChartData);
    }

    public void loadFile(){
//     DirectoryChooser dc = new DirectoryChooser();
//      File file = dc.showDialog(null);
//       MetaModel model = parser.extractModelFromXMI(file);
//        patterns.scanForAntiPatterns(model);
//        patterns.scanForPatterns(model);
//        evolution.initialiseGA(model, popSize, mutRate, crossRate);
//        updateChart(evolution.evolvePopulation());
    }
    
}
