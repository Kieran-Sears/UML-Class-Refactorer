/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DataTypes.Class.Attribute;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
import DataTypes.CoreComponent;
import Evolution.MetaModel;
import Evolution.RelationshipMatrix;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kieran
 */
public class ModelViewer {

    private HashMap<String, ArrayList<String>> UsesMap;
    private ArrayList<String> methodList, attributeList;
    ArrayList<String> classNames;

    public ModelViewer() {
        UsesMap = new HashMap<>();
        methodList = new ArrayList<>();
        attributeList = new ArrayList<>();
        classNames = new ArrayList<>();
    }

    public File generateModelView(MetaModel model) {

        HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Operation>> classMethodsMap = new HashMap<>();
        HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Attribute>> classAttributesMap = new HashMap<>();
        ArrayList<DataTypes.Class.Class> classesPresent = new ArrayList<>();

        UsesMap.clear();
        methodList.clear();
        attributeList.clear();

//1. Read through the profile Ã‚Â and make  lists of which methods and attributes are in which class
        ArrayList<CoreComponent> components = model.getComponents();
        if (components != null) {

            DataTypes.Class.Class classe = null;
            for (CoreComponent component : components) {
                if (component instanceof DataTypes.Class.Attribute) {
                    Attribute attribute = (DataTypes.Class.Attribute) component;
                    ArrayList<Attribute> get = classAttributesMap.get(classe);
                    get.add(attribute);
                    classAttributesMap.put(classe, get);
                } else if (component instanceof DataTypes.Class.Operation) {
                    Operation operation = (DataTypes.Class.Operation) component;
                    ArrayList<Operation> get = classMethodsMap.get(classe);
                    get.add(operation);
                    classMethodsMap.put(classe, get);
                } else if (component instanceof DataTypes.Class.Class) {
                    classe = (DataTypes.Class.Class) component;
                    classMethodsMap.put(classe, new ArrayList<>());
                    classAttributesMap.put(classe, new ArrayList<>());
                    classesPresent.add((DataTypes.Class.Class) component);
                } else {
                    System.out.println("Unknown component in model. ERROR");
                }

            }
            String jointjsClassesScript = generateClassesData(classMethodsMap, classAttributesMap, classesPresent);
            String jointjsCouplingScript = getCouplingData(model.getDependencies(), classesPresent);
            return generateHTMLView(jointjsClassesScript, jointjsCouplingScript);
        }
        System.out.println("Error : no components for Model");
        return null;
    }

    public String generateClassesData(HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Operation>> classMethodsMap,
            HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Attribute>> classAttributesMap,
            ArrayList<DataTypes.Class.Class> classesPresent) {
        //3. For each of the classes create a javascript that will display a box that looks like a 
        // UML class with the method and attribute names in (if present - their numerical idÃ¢â‚¬â„¢s if not)
        // initialsie the s tring and the poosiution values
        String jointjsClassesScript = "";
        double xpos = 0, ypos = 0, height = 0;
        double vertex = 0.0, numVertices = (double) classesPresent.size();
        double halfBoxSize = 250;

        for (int i = 0; i < classesPresent.size(); i++) {
            Class get = classesPresent.get(i);

            //put the class boxes at the vertices of a regular polyogn
            xpos = halfBoxSize + halfBoxSize * Math.cos(2 * Math.PI * vertex / numVertices);

            ypos = halfBoxSize + halfBoxSize * Math.sin(2 * Math.PI * vertex / numVertices);
            vertex++;
            //next thtee lines build up the height programmatically
            height = 10; //always be a name
            //then add 10 for each attribute
            if (classAttributesMap.containsKey(get)) {
                ArrayList<Attribute> memberslist = classAttributesMap.get(get);
                height += 20 * (memberslist.size());
            } else {
                height += 10;
            }
            if (classMethodsMap.containsKey(get)) {
                ArrayList<Operation> memberslist = classMethodsMap.get(get);
                height += 20 * (memberslist.size());
            } else {
                height += 10;
            }
            //and 10 for each method
            //create the string to add to our html
            //TODO change the box colour according to cohesion
            String textToAdd = get.getName()
                    + ": new uml.Class({position: { x:"
                    + xpos + "  , y: " + ypos + "},size: { width: 150, height: " + height + " },name:'"
                    + get.getName() + "',attributes: [";
            if (classAttributesMap.containsKey(get)) {
                ArrayList<Attribute> memberslist = classAttributesMap.get(get);
                for (int j = 0; j < memberslist.size(); j++) {
                    Attribute get1 = memberslist.get(j);
                    textToAdd = textToAdd + "'" + get1.getName() + "'";
                    if (j != memberslist.size()) { // may require minus 1
                        textToAdd = textToAdd + ",";
                    }
                }
            }

            textToAdd = textToAdd + "], \n     methods: [";
            if (classMethodsMap.containsKey(get)) {
                ArrayList<Operation> memberslist = classMethodsMap.get(get);
                for (int j = 0; j < memberslist.size(); j++) {
                    Operation get1 = memberslist.get(j);
                    textToAdd = textToAdd + "'" + get1.getName() + "'";
                    if (j != memberslist.size()) {
                        textToAdd = textToAdd + ",";
                    }
                }
            }
            textToAdd = textToAdd + "]\n})";
            //now add this string to the one we are building up
            jointjsClassesScript = jointjsClassesScript + textToAdd;
            //and finally either a comma if ther are more classes por a semicolon if this is the last
            if (i != classesPresent.size()) {
                jointjsClassesScript = jointjsClassesScript + ",\n";
            }
        }
        return jointjsClassesScript;
    }

    public String getCouplingData(RelationshipMatrix matrix, ArrayList<DataTypes.Class.Class> classesPresent) {

     
//4. Create arrows whose thickness represents out of class uses and put it on the output frame Ã‚Â (showing the coupling)
        String jointjsCouplingScript = "";        //6.1
//for each pair of classes
        int[][] associationMatrix = matrix.getAssociationMatrix();

        for (int i = 0; i < associationMatrix.length; i++) {
            int[] row = associationMatrix[i];

            for (int j = 0; j < row.length; j++) {

                if (i != j) {
                    for (int k = 0; k < row[k]; k++) {
                        jointjsCouplingScript = jointjsCouplingScript
                                + "new joint.dia.Link({ source: { id: classes."
                                + classesPresent.get(i) + ".id }, target: { id: classes."
                                + classesPresent.get(j) + ".id }})," + "\n";
                    }
                }
            }
        }
        return jointjsCouplingScript;
    }

    public File generateHTMLView(String jointjsClassesScript, String jointjsCouplingScript) {
        BufferedWriter writer = null;
        try {
        
            String outHtmlPath = "C:\\Users\\Kieran\\Documents\\NetBeansProjects\\UMLRefactorer\\src\\view\\modelOutput.htm";
            String htmlFile = "";
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Kieran\\Documents\\NetBeansProjects\\UMLRefactorer\\src\\view\\UMLdisplay.htm"));
            String temp;
            //so copy each line of the original html file into the new one
            while ((temp = reader.readLine()) != null) {
                // copy the line from the old file into the new one
                htmlFile += temp + "\n";
                //we want to insert the new classes into the java script in the place they have been defined
                if (temp.contains("var classes")) {
                    htmlFile += jointjsClassesScript;
                }
                //and the coupling between them similarly
                if (temp.contains("var relations")) {
                    htmlFile += jointjsCouplingScript;
                }
            }
            new File(outHtmlPath).delete();
            writer = new BufferedWriter(new FileWriter(outHtmlPath));
            writer.write(htmlFile);
            return new File(outHtmlPath);
          
        } catch (IOException ex) {
            Logger.getLogger(ModelViewer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ModelViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        return null;
    }

    public void html() {
        /*
         <!DOCTYPE html>
         <html>
         <head>
         <link rel="canonical" href="http://www.jointjs.com/" />
         <meta name="description" content="IPAT Demo: Software Class Model Evolution for the Cinema Booking System Problem" />
         <meta name="keywords" content="IPAT,JointJS, JavaScript, diagrams, diagramming library, UML, charts" />
         <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700" rel="stylesheet" type="text/css" />
         <link rel="stylesheet" href="http://www.jointjs.com/css/style.css" />
         <link rel="stylesheet" href="http://www.jointjs.com/css/joint.css" />
         <link rel="stylesheet" href="http://www.jointjs.com/css/tutorial.css" />
         <link rel="stylesheet" type="text/css" href="joint.ui.paperScroller.css" />
         <title>IPAT Demo: Software Class Model Evolution.</title>
         </head>
         <body>
         <div id="paper" class="paper"/></div>
         <!--<script src="../joint.min.js"></script>-->
         <script src="http://jointjs.com/js/joint.js"></script>
         <script src="http://jointjs.com/js/joint.shapes.uml.js"></script>
         <script>
         var graph = new joint.dia.Graph;
         var paper = new joint.dia.Paper({
         el: $('#paper'),
         width: 1000,
         height: 600,
         gridSize: 1,
         model: graph
         });
         var uml = joint.shapes.uml;
         //empty container fo the classes which get add by UMLProcessor
         var classes = {};
         //added each class to the graph as a cell
         _.each(classes, function(c) { c.updateRectangles});
         _.each(classes, function(c) { graph.addCell(c); });
         //empty array fo the links which get add by UMLProcessor
         var relations = [];
         //add the links ot the graph
         _.each(relations, function(r) { graph.addCell(r); });
         //make the multiple links seperate
         _.each(classes, function(c){ adjustVertices(graph,c);});
         paper.scaleContentToFit();
         var myAdjustVertices = _.partial(adjustVertices, graph);
         // adjust vertices when a cell is removed or its source/target was changed
         graph.on('add remove change:source change:target', myAdjustVertices);
         // also when an user stops interacting with an element.
         paper.on('cell:pointerup', myAdjustVertices);
         </script>
         <link rel="stylesheet" href="http://www.jointjs.com/vendor/prism/prism.css" type="text/css" />
         <script src="http://www.jointjs.com/vendor/prism/prism.js"></script>
         </body>
         </html>
         */
    }

}
