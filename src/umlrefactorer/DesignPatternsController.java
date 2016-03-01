/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;


import DesignPatterns.AntiPatternAnalyser;
import DesignPatterns.Blob;
import DesignPatterns.MutationHeuristic;
import Evolution.MetaModel;

/**
 *
 * @author Kieran
 */
public class DesignPatternsController {
    
    
    public void scanForPatterns(MetaModel model){
    AntiPatternAnalyser APA = new Blob();
        MutationHeuristic mutationHeuristic = APA.scanModel(model);
        
    }
    
    public void scanForAntiPatterns(MetaModel model){
   
    }
    
    
}
