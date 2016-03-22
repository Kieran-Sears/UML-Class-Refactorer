/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;


import DesignPatterns.AntiPatternAnalyser;
import DesignPatterns.Blob;
import Evolution.MetaModel;

/**
 *
 * @author Kieran
 */
public class DesignPatternsController {
    
    
    public void scanForPatterns(MetaModel model){
    AntiPatternAnalyser APA = new Blob();
    APA.scanModel(model);
        
    }
    
    public void scanForAntiPatterns(MetaModel model){
    // NOTE TO SELF : you were last looking at how dependencies were identified,
        // how associations fit into this, you were coding the blob but wanted to
        // change the relationshipMatrix, to accomodate these relationships rather 
        // than getting the blob to figure them out.
    }
    
    
}
