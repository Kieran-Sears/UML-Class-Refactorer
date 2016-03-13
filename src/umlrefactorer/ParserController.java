/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import Evolution.MetaModel;
import Parser.Parser;
import Parser.XMI_Parser;
import java.io.File;

/**
 *
 * @author Kieran
 */
public class ParserController {

    Parser parser;

    
    public MetaModel extractModelFromXMI(File file){
    parser = new XMI_Parser(file);
        MetaModel model = parser.getModel();
        model.initialiseDependenciesAndFitness();
        return model;
    }
    
}
