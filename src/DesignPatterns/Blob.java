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
        System.out.println("cycling components");
        for (Component component : components) {
            if (component instanceof Operation) {
                Operation operation = (Operation) component;
                System.out.println("identified operation " + operation.getName());
                ArrayList<Parameter> parameters = operation.getParameters();
                Iterator<Parameter> iterator = parameters.iterator();
                while (iterator.hasNext()) {
                    Parameter parameter = iterator.next();
                    System.out.println("found operation parameter " + parameter.getName() + " : " + parameter.getType());
                    String type = parameter.getType();
                    for (Component contractedAttribute : components) {
                        if (contractedAttribute instanceof Attribute) {
                            Attribute attribute = (Attribute) contractedAttribute;
                            if (attribute.getDependency() != null) {
                                System.out.println("attempting to match to suitable 'collection' attribute + " + type + " - "  + attribute.getDependency());
                                if (attribute.getDependency().equalsIgnoreCase(type)) {
                                    System.out.println("located match at " + components.indexOf(contractedAttribute));
                                    if (contracts.containsKey(operation)) {
                                        ArrayList<Attribute> get = contracts.get(operation);
                                        get.add(attribute);
                                        contracts.put(operation, get);
                                        System.out.println("adding to contracts " + operation.getName() + " / " + attribute.getName());
                                    } else {
                                        ArrayList<Attribute> attributes = new ArrayList();
                                        attributes.add(attribute);
                                        contracts.put(operation, attributes);
                                        System.out.println("adding to contracts " + operation.getName() + " / " + attribute.getName());
                                    }
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
//
//        int counter = 0;
//        for (CoreComponent component : components) {
//            counter++;
//             if (component instanceof DataTypes.Class.Class) {
//                 if (counter < )
//                 counter = 0;
//             }
//            
//            if (component instanceof Attribute) {
//            if ( ((Attribute)component).getDependency() != null ) {
//            
//            }
//            }
//        }
//        
        
        Iterator<Map.Entry<Operation, ArrayList<Attribute>>> iterator = contracts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Operation, ArrayList<Attribute>> next = iterator.next();
            Operation operation = next.getKey();
            components.remove(operation);
            System.out.println("removed \n" + operation.getName());
            ArrayList<Attribute> attributes = next.getValue();
            
            for (Attribute attribute : attributes) {
                components.remove(attribute);
                System.out.println("removed \n" + attribute.getName());
            }
            Random random = new Random();
            int nextInt = 1 + (random.nextInt(components.size() - 1));
            components.add(nextInt, operation);
            System.out.println("added \n" + operation.getName());
            for (Attribute attribute : attributes) {
                components.add(nextInt++, attribute);
                System.out.println("added \n" + attribute.getName());
            }
        }

        return components;
    }

}
