/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DesignPatterns;

import DataTypes.CoreComponent;
import Evolution.MetaModel;
import java.util.ArrayList;

/**
 *
 * @author Kieran
 */
public interface AntiPatternAnalyser {
    
    public void scanModel(MetaModel model);
    
    public ArrayList<CoreComponent> PerformMutation(ArrayList<CoreComponent> components);
}
