/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DesignPatterns;

import Evolution.MetaModel;

/**
 *
 * @author Kieran
 */
public interface AntiPatternAnalyser {
    
    public boolean scanModel(MetaModel model);
    
    public MetaModel PerformMutation(MetaModel model);
}
