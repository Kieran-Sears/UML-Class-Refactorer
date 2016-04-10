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
 *
 * The following is a condensed guide extracted from "Anti-Patterns (Refactoring
 * Software, Architectures, and Projects in Crisis)" by William H.Brown, Raphael
 * C. Malveau, Hays W. "Skip" McCormick III and Thomas J. Mowbray. Copyright
 * 1998. Published by John Wiley & Sons, Inc.
 */
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
public class Blob extends AntiPattern implements AntiPatternAnalyser {

    private ArrayList<Integer> flaggedClassIndexes = new ArrayList();
    private HashMap<Operation, ArrayList<Attribute>> contracts;

    @Override
    public boolean scanModel(MetaModel model) {

        // find and flag classes with below average number of attributes / methods
        int counter = 0;
        boolean blobDetected = false;
        flaggedClassIndexes = new ArrayList();
        for (CoreComponent component : model.getComponents()) {
            counter++;
            if (component instanceof DataTypes.Class.Class) {
                if (counter < ((model.getNumberOfAttributes() + model.getNumberOfOperations()) / model.getNumberOfClasses()) / 2 + 1 && model.getComponents().indexOf(component) != 0) {
                    flaggedClassIndexes.add(model.getComponents().indexOf(component));
                }
                if ((counter / (model.getNumberOfAttributes() + model.getNumberOfOperations()) * 100) > 80 && model.getComponents().indexOf(component) != 0) {
                    blobDetected = true;
                }
                counter = 0;
            }
        }

        if (blobDetected) {
            ArrayList<CoreComponent> components = model.getComponents();
            contracts = new HashMap();
            //   System.out.println("cycling components");
            for (Component component : components) {
                if (component instanceof Operation) {
                    Operation operation = (Operation) component;
                    //        System.out.println("identified operation " + operation.getName());
                    ArrayList<Parameter> parameters = operation.getParameters();
                    Iterator<Parameter> iterator = parameters.iterator();
                    while (iterator.hasNext()) {
                        Parameter parameter = iterator.next();
                        //     System.out.println("found operation parameter " + parameter.getName() + " : " + parameter.getType());
                        String type = parameter.getType();
                        for (Component contractedAttribute : components) {
                            if (contractedAttribute instanceof Attribute) {
                                Attribute attribute = (Attribute) contractedAttribute;
                                if (attribute.getDependency() != null) {
                                    //        System.out.println("attempting to match to suitable 'collection' attribute + " + type + " - " + attribute.getDependency());
                                    if (attribute.getDependency().equalsIgnoreCase(type)) {
                                        //             System.out.println("located match at " + components.indexOf(contractedAttribute));
                                        if (contracts.containsKey(operation)) {
                                            ArrayList<Attribute> get = contracts.get(operation);
                                            get.add(attribute);
                                            contracts.put(operation, get);
                                            //      System.out.println("adding to contracts " + operation.getName() + " / " + attribute.getName());
                                        } else {
                                            ArrayList<Attribute> attributes = new ArrayList();
                                            attributes.add(attribute);
                                            contracts.put(operation, attributes);
                                            //   System.out.println("adding to contracts " + operation.getName() + " / " + attribute.getName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("blob has been detected");
            return blobDetected;
        }
        return blobDetected;
    }

    @Override
    public MetaModel PerformMutation(MetaModel model) {

        // new model and components created due to previous errors caused in assigning new components to existing models
        MetaModel replacement = new MetaModel();
        ArrayList<CoreComponent> components = new ArrayList();
        for (CoreComponent component : model.getComponents()) {
            components.add(component);
        }

        // move collections at random to flagged classes
        for (CoreComponent component : model.getComponents()) {
            if (component instanceof Attribute) {
                if (((Attribute) component).getDependency() != null) {
                    components.remove(component);
                    int nextInt = new Random().nextInt(flaggedClassIndexes.size());
                    Integer get = flaggedClassIndexes.get(nextInt);
                    components.add(get, component);
                }
            }
        }
        // cycle contracts and move operations to randomly allocated collection attributes
        // TODO ensure that models with operation parameters that are objects which dont have collections to reference dont cause errors here
        Iterator<Map.Entry<Operation, ArrayList<Attribute>>> iterator = contracts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Operation, ArrayList<Attribute>> next = iterator.next();
            Operation operation = next.getKey();
            components.remove(operation);
            System.out.println("removed \n" + operation.getName());
            ArrayList<Attribute> attributes = next.getValue();
            int indexOf = components.indexOf(attributes.get(new Random().nextInt(attributes.size())));
            components.add(indexOf + 1, operation);
            System.out.println("added \n" + operation.getName());
        }
        replacement.setComponents(components);
        replacement.sortMethodDependencies();
        replacement.setNumberOfAttributes(model.getNumberOfAttributes());
        replacement.setNumberOfOperations(model.getNumberOfOperations());
        replacement.setNumberOfClasses(model.getNumberOfClasses());
        return replacement;
    }
}
