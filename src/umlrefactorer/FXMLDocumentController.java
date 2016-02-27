/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import DataTypes.MetaModel;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Kieran
 */
public class FXMLDocumentController implements Initializable {

    ParserController parser;
    EvolutionController evolution;
    DesignPatternsController patterns;

    @FXML
    private Label label;
    @FXML
    private TextField populationSize;
    @FXML
    private TextField numOfGenerations;
    @FXML
    private TextField mutationRate;
    @FXML
    private TextField crossoverRate;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        File file = new File("C:\\Users\\Kieran\\Documents\\NetBeansProjects\\UMLRefactorer\\testcases/bookshop_2-1.xmi");
        MetaModel model = parser.extractModelFromXMI(file);
       // model.printModelToConsole(); // for debugging
        // TODO Handle Invalid Input
        int popSize = Integer.parseInt(populationSize.getText());
        float mutRate = Float.parseFloat(mutationRate.getText());
        float crossRate = Float.parseFloat(crossoverRate.getText());
        int gens = Integer.parseInt(numOfGenerations.getText());
        
        evolution.initialiseGA(model, popSize, mutRate, crossRate);
        evolution.evolvePopulation(gens);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        parser = new ParserController();
        evolution = new EvolutionController();
        patterns = new DesignPatternsController();
    }

}
