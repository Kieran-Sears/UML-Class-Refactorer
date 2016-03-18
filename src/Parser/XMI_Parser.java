/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import Evolution.MetaModel;
import DataTypes.Class.Class;
import DataTypes.Class.Parameter;
import DataTypes.Class.Operation;
import DataTypes.Class.Association;
import DataTypes.Class.Attribute;
import DataTypes.CoreComponent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Kieran
 */
public class XMI_Parser extends Parser {

    public static String ModelId;
    Document doc;
    public XPath xpath;
    public XPathExpression expr;

    public XMI_Parser(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            doc = factory.newDocumentBuilder().parse(file);
            XPathFactory xPathfactory = XPathFactory.newInstance();
            xpath = xPathfactory.newXPath();
            xpath.setNamespaceContext(new XMI_NameSpace());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMI_Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public MetaModel getModel() {
        try {
            MetaModel model = new MetaModel();

            // set the namespace for the ID's for this document
            expr = xpath.compile("/XMI/XMI.content/UML:Model");
            NodeList xmlModel = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < xmlModel.getLength(); i++) {
                Node xmlModelId = xmlModel.item(i);
                String idValue = xmlModelId.getAttributes().getNamedItem("xmi.id").getNodeValue();
                ModelId = idValue.replace(idValue.substring(idValue.lastIndexOf(":")), "");
            }

            model.setComponents(extractComponents());
            model.setAssociations(extractAssocations());
            return model;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMI_Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<CoreComponent> extractComponents() {
        ArrayList<CoreComponent> componentArray = new ArrayList();
        try {
            expr = xpath.compile("//packagedElement[@xmi:type='uml:Class']");
            NodeList classes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < classes.getLength(); i++) {
                NamedNodeMap attributes = classes.item(i).getAttributes();
                Class _class = new Class();
                _class.setID(attributes.getNamedItem("xmi:id").getNodeValue());
                _class.setIsAbstract(Boolean.parseBoolean(attributes.getNamedItem("isAbstract").getNodeValue()));
                _class.setName(attributes.getNamedItem("name").getNodeValue());
                componentArray.add(_class);
                ArrayList<Operation> classOperations = getOperations(attributes.getNamedItem("xmi:id").getNodeValue());
                for (Operation operation : classOperations) {
                    componentArray.add(operation);
                }
                ArrayList<Attribute> classAttributes = getAttributes(attributes.getNamedItem("xmi:id").getNodeValue());
                for (Attribute attribute : classAttributes) {
                    componentArray.add(attribute);
                }
            }
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMI_Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return componentArray;
    }

    public ArrayList<Attribute> getAttributes(String className) throws XPathExpressionException {
        ArrayList<Attribute> attributeArray = new ArrayList();
        expr = xpath.compile("//packagedElement[@xmi:id='" + className + "']/ownedAttribute");
        NodeList classAttributes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < classAttributes.getLength(); i++) {
            NamedNodeMap attributes = classAttributes.item(i).getAttributes();
            Attribute attribute = new Attribute();
            attribute.setID(attributes.getNamedItem("xmi:id").getNodeValue());
            attribute.setName(attributes.getNamedItem("name").getNodeValue());
            attributeArray.add(attribute);
        }
        return attributeArray;
    }

    public ArrayList<Operation> getOperations(String className) throws XPathExpressionException {
        ArrayList<Operation> operationArray = new ArrayList();
        expr = xpath.compile("//packagedElement[@xmi:id='" + className + "']/ownedOperation");
        NodeList classOperations = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < classOperations.getLength(); i++) {
            NamedNodeMap attributes = classOperations.item(i).getAttributes();
            Operation operation = new Operation();
            operation.setID(attributes.getNamedItem("xmi:id").getNodeValue());
            operation.setName(attributes.getNamedItem("name").getNodeValue());
            operation.setParameters(extractParameters(attributes.getNamedItem("xmi:id").getNodeValue()));
            operationArray.add(operation);
        }
        return operationArray;
    }

    public ArrayList<Parameter> extractParameters(String operationID) throws XPathExpressionException {
        ArrayList<Parameter> parameterArray = new ArrayList();
        expr = xpath.compile("//ownedOperation[@xmi:id='" + operationID + "']/ownedParameter");
        NodeList operationParameters = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < operationParameters.getLength(); i++) {
            NamedNodeMap attributes = operationParameters.item(i).getAttributes();
            if (((Attr) attributes.getNamedItem("type")) != null) {
                String nodeValue = attributes.getNamedItem("type").getNodeValue();
                if (!nodeValue.equalsIgnoreCase("int_id")
                        && !nodeValue.equalsIgnoreCase("Integer_id")
                        && !nodeValue.equalsIgnoreCase("Float_id")
                        && !nodeValue.equalsIgnoreCase("ArrayList_id")
                        && !nodeValue.equalsIgnoreCase("Double_id")
                        && !nodeValue.equalsIgnoreCase("String_id")
                        && !nodeValue.equalsIgnoreCase("Boolean_id")
                        && !nodeValue.equalsIgnoreCase("void_id")) {
                    Parameter parameter = new Parameter();
                    parameter.setID(attributes.getNamedItem("xmi:id").getNodeValue());
                    parameter.setType(attributes.getNamedItem("type").getNodeValue());
                    parameterArray.add(parameter);
                }
            }
        }
        return parameterArray;
    }

    public ArrayList<Association> extractAssocations() throws XPathExpressionException {
        ArrayList<Association> classAssociations = new ArrayList();
        expr = xpath.compile("//packagedElement[@xmi:type='uml:Association']");
        NodeList assocations = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < assocations.getLength(); i++) {
            //<packagedElement isAbstract="false" isDerived="false" isLeaf="false" memberEnd="gEOpX0qGAqAABgcs wEOpX0qGAqAABgcv" xmi:id="f4OpX0qGAqAABgcq" xmi:type="uml:Association">
            NamedNodeMap attributes = assocations.item(i).getAttributes();
            Association association = new Association();
            association.setID(attributes.getNamedItem("xmi:id").getNodeValue());
            String[] split = attributes.getNamedItem("memberEnd").getNodeValue().split(" ");
            association.setSource((String) xpath.compile("(//*[@xmi:id='" + split[0] + "']/@type)[1]").evaluate(doc, XPathConstants.STRING));
            association.setTarget((String) xpath.compile("(//*[@xmi:id='" + split[1] + "']/@type)[1]").evaluate(doc, XPathConstants.STRING));
            classAssociations.add(association);
        }
        return classAssociations;
    }
}
