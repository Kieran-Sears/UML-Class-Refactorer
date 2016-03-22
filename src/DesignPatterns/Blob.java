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
import DataTypes.CoreComponent;
import Evolution.MetaModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Kieran
 */
public class Blob extends AntiPattern implements AntiPatternAnalyser {

    private HashMap<Operation, ArrayList<Attribute>> contracts;

    @Override
    public void scanModel(MetaModel model) {
        ArrayList<CoreComponent> components = model.getComponents();
        contracts = new HashMap();
        for (Component component : components) {
            if (component instanceof Operation) {
                Operation operation = (Operation) component;
                ArrayList<Parameter> behaviourFeature = operation.getParameters();
                Iterator<Parameter> iterator = behaviourFeature.iterator();
                while (iterator.hasNext()) {
                    Parameter parameter = iterator.next();
                    String type = parameter.getType();
                    for (Component contractedAttribute : components) {
                        if (contractedAttribute instanceof Attribute) {
                            Attribute attribute = (Attribute) contractedAttribute;
                            if (attribute.getID().equalsIgnoreCase(type)) {
                                if (contracts.containsKey(operation)) {
                                    ArrayList<Attribute> get = contracts.get(operation);
                                    get.add(attribute);
                                    contracts.put(operation, get);
                                } else {
                                    ArrayList<Attribute> attributes = new ArrayList();
                                    contracts.put(operation, attributes);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     identify cohesive sets of operations and attributes that represent contracts.
     e.g. catalog management, like Sort_Catalog and Search_Catalog.
     identify all operations and attributes related to individual items.
     e.g. Print_Item, Delete_Item.
     */
    /*
     remove  redundant, indirect associations. 
     ITEM class is initially far−coupled to the LIBRARY class in that each item
     really belongs to a CATALOG, which in turn belongs to a LIBRARY.
     */
    /*
     migrate associates to derived classes to a common base class.
     e.g. once the far−coupling has been removed between the LIBRARY
     and ITEM classes, we need to migrate ITEMs to CATALOGs.
     */
    /*
     Finally, we remove all transient associations, replacing them as appropriate with type
     specifiers to attributes and operations arguments. In our example, a Check_Out_Item or
     a Search_For_Item would be a transient process, and could be moved into a separate
     transient class with local attributes that establish the specific location or search criteria for
     a specific instance of a check−out or search.
     */
    @Override
    public ArrayList<CoreComponent> PerformMutation(ArrayList<CoreComponent> components) { 
        
        Iterator<Map.Entry<Operation, ArrayList<Attribute>>> iterator = contracts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Operation, ArrayList<Attribute>> next = iterator.next();
            Operation operation = next.getKey();
            components.remove(operation);
            ArrayList<Attribute> attributes = next.getValue();
            for (Attribute attribute : attributes) {
                components.remove(attribute);
            }
              Random random = new Random();
            int nextInt = 1 + (random.nextInt( components.size() - 1 ));
            components.add(nextInt, operation);
            for (Attribute attribute : attributes) {
                components.add(nextInt++, attribute);
            }
        }
        
        return components;
    }

}
