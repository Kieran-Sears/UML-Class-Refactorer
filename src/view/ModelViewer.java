/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import DataTypes.Class.Attribute;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
import DataTypes.Class.Parameter;
import DataTypes.CoreComponent;
import Evolution.MetaModel;
import Evolution.RelationshipMatrix;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kieran
 */
public class ModelViewer {

    public File generateModelView(MetaModel model) {

        HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Operation>> classMethodsMap = new HashMap<>();
        HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Attribute>> classAttributesMap = new HashMap<>();
        ArrayList<DataTypes.Class.Class> classesPresent = new ArrayList<>();

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

            // for debugging
//            for (DataTypes.Class.Class classee : classesPresent) {
//                System.out.println("\n" + classee.getName());
//                ArrayList<Operation> methods = classMethodsMap.get(classee);
//                ArrayList<Attribute> attributes = classAttributesMap.get(classee);
//                        for (Attribute attribute : attributes) {
//                            System.out.println(attribute.getName());
//                }
//                        for (Operation method : methods) {
//                            System.out.println(method.getName());
//                }
//                        
//            }
            String jointjsClassesScript = generateClassesData(classMethodsMap, classAttributesMap, classesPresent, model.getDependencies());
            String jointjsCouplingScript = getCouplingData(model.getDependencies(), classesPresent);
            return generateHTMLView(jointjsClassesScript, jointjsCouplingScript);
        }
        System.out.println("Error : no components for Model");
        return null;
    }

    public String generateClassesData(HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Operation>> classMethodsMap,
            HashMap<DataTypes.Class.Class, ArrayList<DataTypes.Class.Attribute>> classAttributesMap,
            ArrayList<DataTypes.Class.Class> classesPresent, RelationshipMatrix matrix) {

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

            height = 10;

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

            String textToAdd = get.getName()
                    + ": new uml.Class({position: { x:"
                    + xpos + "  , y: " + ypos + "},size: { width: 200, height: " + height + " },name:'"
                    + get.getName() + "',attributes: [";

            if (classAttributesMap.containsKey(get)) {
                ArrayList<Attribute> memberslist = classAttributesMap.get(get);
                for (int j = 0; j < memberslist.size(); j++) {
                    Attribute get1 = memberslist.get(j);
                    textToAdd = textToAdd + "'" + get1.getName() + "'";
                    if (j != memberslist.size() - 1) {
                        textToAdd = textToAdd + ",";
                    }
                }
            }

            textToAdd = textToAdd + "], \n     methods: [";
            if (classMethodsMap.containsKey(get)) {
                ArrayList<Operation> memberslist = classMethodsMap.get(get);
                for (int j = 0; j < memberslist.size(); j++) {
                    Operation get1 = memberslist.get(j);
                    textToAdd = textToAdd + "'" + get1.getName() + "(";
                    Iterator<Parameter> iterator = get1.getParameters().iterator();
                    while (iterator.hasNext()) {
                        Parameter next = iterator.next();
                        if (iterator.hasNext()){
                       textToAdd += matrix.ReverseLookupClass( matrix.lookupClass(next.getType())) + ", ";
                        }
                        else {
                         textToAdd += matrix.ReverseLookupClass( matrix.lookupClass(next.getType()));
                        }
                            
                    }
                            
                          textToAdd +=   ")'";
                    if (j != memberslist.size() - 1) {
                        textToAdd = textToAdd + ",";
                    }
                }
            }
            textToAdd = textToAdd + "]\n})";

            jointjsClassesScript = jointjsClassesScript + textToAdd;

            if (i != classesPresent.size() - 1) {
                jointjsClassesScript = jointjsClassesScript + ",\n";
            }
        }
        return jointjsClassesScript;
    }

    public String getCouplingData(RelationshipMatrix matrix, ArrayList<DataTypes.Class.Class> classesPresent) {

        String jointjsCouplingScript = "";
        HashMap<Integer, String> classNames = new HashMap();
        int[][] associationMatrix = matrix.getAssociationMatrix();
        for (int i = 0; i < associationMatrix.length; i++) {
            String ReverseLookupClass = matrix.ReverseLookupClass(i);
            for (DataTypes.Class.Class classe : classesPresent) {
                if (ReverseLookupClass.equalsIgnoreCase(classe.getName())) {
                    classNames.put(i, classe.getName());
                }
            }
        }

        for (int i = 0; i < associationMatrix.length; i++) {
            int[] row = associationMatrix[i];
            for (int j = 0; j < row.length; j++) {
                if (i != j && associationMatrix[i][j] != 0) {
                    jointjsCouplingScript = jointjsCouplingScript
                            + "new joint.dia.Link({ source: { id: classes."
                            + classNames.get(i) + ".id }, target: { id: classes."
                            + classNames.get(j) + ".id }})," + "\n";
                }
            }
        }
        return jointjsCouplingScript;
    }

    public File generateHTMLView(String jointjsClassesScript, String jointjsCouplingScript) {
        BufferedWriter writer = null;
        try {
            String outHtmlPath = System.getProperty("user.dir") + "\\generated files\\"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())+".htm";
            String htmlFile = "";
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\src\\view\\UMLdisplay.htm"));
            String temp;

            while ((temp = reader.readLine()) != null) {

                if (temp.contains("paperscroller link")) {
                temp = "<link rel=\"stylesheet\" type=\"text/css\" href=\"File:///" + System.getProperty("user.dir") + "/lib/webview/joint.ui.paperScroller.css\" />";
                }
              
                
                
                if (temp.contains("script sources")) {
                    temp = " <link rel=\"stylesheet\" type=\"text/css\" href=\"File:///" + System.getProperty("user.dir") + "/lib/webview/joint.css\" />\n"
                            + "    <script src=\"File:///" + System.getProperty("user.dir") + "/lib/webview/jquery.min.js\"></script>\n"
                            + "    <script src=\"File:///" + System.getProperty("user.dir") + "/lib/webview/lodash.min.js\"></script>\n"
                            + "    <script src=\"File:///" + System.getProperty("user.dir") + "/lib/webview/backbone-min.js\"></script>\n"
                            + "    <script src=\"File:///" + System.getProperty("user.dir") + "/lib/webview/joint.js\"></script>";
                } 

                  htmlFile += temp + "\n";
                
                if (temp.contains("var classes")) {
                    htmlFile += jointjsClassesScript;
                }

                if (temp.contains("var relations")) {
                    htmlFile += jointjsCouplingScript;
                }
            }
           
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

}
