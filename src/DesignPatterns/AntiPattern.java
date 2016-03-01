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
public abstract class AntiPattern implements AntiPatternAnalyser {
    
    MetaModel model;
    MutationHeuristic mutationHeuristic;

    public AntiPattern() {
        this.mutationHeuristic = new MutationHeuristic();
    }
    
}
