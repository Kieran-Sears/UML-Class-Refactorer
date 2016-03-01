/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DesignPatterns;

import DataTypes.Class.Attribute;
import DataTypes.Class.Operation;
import DataTypes.Class.Parameter;
import DataTypes.Component;
import Evolution.MetaModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Kieran
 */
public class Blob extends AntiPattern implements AntiPatternAnalyser {

    MetaModel model;

    @Override
    public MutationHeuristic scanModel(MetaModel model) {
        this.model = model;
        HashMap<Operation, Attribute> identifyContracts = identifyContracts();
        removeRedundantAssociations();
        migrateAssociates();
        removeTransientAssociations();
        return mutationHeuristic;
    }

    /*
     Identify or categorize related attributes and operations according to contracts.
     identify cohesive sets of operations and attributes that represent contracts.
     e.g. catalog management, like Sort_Catalog and Search_Catalog.
     identify all operations and attributes related to individual items.
     e.g. Print_Item, Delete_Item.
     */
    public HashMap<Operation, Attribute> identifyContracts() {
        ArrayList<Component> components = model.getComponents();
        HashMap<Operation, Attribute> contracts = new HashMap();
        for (Component component : components) {
            if (component instanceof Operation) {
                Operation operation = (Operation) component;
                ArrayList<Parameter> behaviourFeature = operation.getBehaviourFeature();
                Iterator<Parameter> iterator = behaviourFeature.iterator();
                while (iterator.hasNext()) {
                    Parameter parameter = iterator.next();
                    String type = parameter.getType();
                    for (Component contractedAttribute : components) {
                        if (contractedAttribute instanceof Attribute) {
                            Attribute attribute = (Attribute) contractedAttribute;
                            if (attribute.getID().equalsIgnoreCase(type)) {
                                contracts.put(operation, attribute);
                            }
                        }
                    }
                }
            }
        }
        return contracts;
    }

    public void removeRedundantAssociations() {
        /*
         remove  redundant, indirect associations. 
         ITEM class is initially far−coupled to the LIBRARY class in that each item
         really belongs to a CATALOG, which in turn belongs to a LIBRARY.
         */
    }

    public void migrateAssociates() {
        /*
         migrate associates to derived classes to a common base class.
         e.g. once the far−coupling has been removed between the LIBRARY
         and ITEM classes, we need to migrate ITEMs to CATALOGs.
         */
    }

    public void removeTransientAssociations() {
        /*
         Finally, we remove all transient associations, replacing them as appropriate with type
         specifiers to attributes and operations arguments. In our example, a Check_Out_Item or
         a Search_For_Item would be a transient process, and could be moved into a separate
         transient class with local attributes that establish the specific location or search criteria for
         a specific instance of a check−out or search.
         */
    }

}
