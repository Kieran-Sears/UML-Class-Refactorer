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
import DataTypes.Class.OwnedEnd;
import DataTypes.Component;
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

    public ArrayList<Component> extractComponents() {
        ArrayList<Component> componentArray = new ArrayList();
        try {
            expr = xpath.compile("//packagedElement[@xmi:type='uml:Class']");
            NodeList classes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < classes.getLength(); i++) {
                NamedNodeMap attributes = classes.item(i).getAttributes();
                Class _class = new Class();
                _class.setID(attributes.getNamedItem("xmi:id").getNodeValue());
                _class.setIsAbstract(Boolean.parseBoolean(attributes.getNamedItem("isAbstract").getNodeValue()));
                _class.setName(attributes.getNamedItem("name").getNodeValue());
                _class.setVisibility(attributes.getNamedItem("visibility").getNodeValue());
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
            // <ownedAttribute aggregation="none" isDerived="false" isDerivedUnion="false" isID="false"
            // isLeaf="false" isReadOnly="false" isStatic="false" name="cardNumber" 
            //        visibility="private" xmi:id="ilwpX0qGAqAABgbW" xmi:type="uml:Property">
            NamedNodeMap attributes = classAttributes.item(i).getAttributes();
            Attribute attribute = new Attribute();
            attribute.setAggregation(attributes.getNamedItem("aggregation").getNodeValue());
            attribute.setIsDerived(Boolean.parseBoolean(attributes.getNamedItem("isDerived").getNodeValue()));
            attribute.setIsDerivedUnion(Boolean.parseBoolean(attributes.getNamedItem("isDerivedUnion").getNodeValue()));
            attribute.setIsID(Boolean.parseBoolean(attributes.getNamedItem("isID").getNodeValue()));
            attribute.setIsLeaf(Boolean.parseBoolean(attributes.getNamedItem("isLeaf").getNodeValue()));
            attribute.setIsReadOnly(Boolean.parseBoolean(attributes.getNamedItem("isReadOnly").getNodeValue()));
            attribute.setIsStatic(Boolean.parseBoolean(attributes.getNamedItem("isStatic").getNodeValue()));
            attribute.setID(attributes.getNamedItem("xmi:id").getNodeValue());
            attribute.setName(attributes.getNamedItem("name").getNodeValue());
            attribute.setVisibility(attributes.getNamedItem("visibility").getNodeValue());
            attributeArray.add(attribute);
        }
        return attributeArray;
    }

    public ArrayList<Operation> getOperations(String className) throws XPathExpressionException {
        ArrayList<Operation> operationArray = new ArrayList();
        expr = xpath.compile("//packagedElement[@xmi:id='" + className + "']/ownedOperation");
        NodeList classOperations = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < classOperations.getLength(); i++) {
            /*
             <ownedOperation isAbstract="false" isLeaf="false" isOrdered="false" isQuery="false" 
             isStatic="false" isUnique="true" name="doMenu" visibility="public" xmi:id="AWzxX0qGAqAABgZo" 
             xmi:type="uml:Operation">
             <ownedParameter direction="return" xmi:id="AWzxX0qGAqAABgZo_return" xmi:type="uml:Parameter"/>
             </ownedOperation>
             */
            NamedNodeMap attributes = classOperations.item(i).getAttributes();
            Operation operation = new Operation();
            operation.setIsAbstract(Boolean.parseBoolean(attributes.getNamedItem("isAbstract").getNodeValue()));
            operation.setIsLeaf(Boolean.parseBoolean(attributes.getNamedItem("isLeaf").getNodeValue()));
            operation.setIsOrdered(Boolean.parseBoolean(attributes.getNamedItem("isOrdered").getNodeValue()));
            operation.setIsQuery(Boolean.parseBoolean(attributes.getNamedItem("isQuery").getNodeValue()));
            operation.setIsStatic(Boolean.parseBoolean(attributes.getNamedItem("isStatic").getNodeValue()));
            operation.setIsUnique(Boolean.parseBoolean(attributes.getNamedItem("isUnique").getNodeValue()));
            operation.setID(attributes.getNamedItem("xmi:id").getNodeValue());
            operation.setName(attributes.getNamedItem("name").getNodeValue());
            operation.setVisibility(attributes.getNamedItem("visibility").getNodeValue());
            operation.setBehaviourFeature(extractParameters(className));
            operationArray.add(operation);
        }
        return operationArray;
    }

  
    public ArrayList<Parameter> extractParameters(String className) throws XPathExpressionException {
        ArrayList<Parameter> parameterArray = new ArrayList();
        expr = xpath.compile("//packagedElement[@xmi:id='" + className + "']/ownedOperation/ownedParameter");
        NodeList operationParameters = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < operationParameters.getLength(); i++) {
            NamedNodeMap attributes = operationParameters.item(i).getAttributes();
            if (((Attr) attributes.getNamedItem("type")) != null) {
                Parameter parameter = new Parameter();
                parameter.setID(attributes.getNamedItem("xmi:id").getNodeValue());
                parameter.setType(attributes.getNamedItem("type").getNodeValue());
                parameterArray.add(parameter);
            }
//            expr = xpath.compile("//packagedElement[@xmi:id='" + className + "']/ownedOperation/ownedParameter/type");
//            NodeList parameterHRefs = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//            for (int j = 0; j < parameterHRefs.getLength(); j++) {
//                NamedNodeMap attributes1 = parameterHRefs.item(j).getAttributes();
//                parameter.setDataType(attributes1.getNamedItem("href").getNodeValue().substring(attributes1.getNamedItem("href").getNodeValue().indexOf("#")+1));
//            }
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
            association.setMemberEnd(attributes.getNamedItem("memberEnd").getNodeValue());
            association.setXmi_type(attributes.getNamedItem("xmi:type").getNodeValue());
            association.setIsDerived(Boolean.parseBoolean(attributes.getNamedItem("isDerived").getNodeValue()));
            association.setIsLeaf(Boolean.parseBoolean(attributes.getNamedItem("isLeaf").getNodeValue()));
            association.setIsAbstract(Boolean.parseBoolean(attributes.getNamedItem("isAbstract").getNodeValue()));
            association.setID(attributes.getNamedItem("xmi:id").getNodeValue());
            expr = xpath.compile("//packagedElement[@xmi:id='" + association.getID() + "']/ownedEnd");
            NodeList ownedEnds = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            OwnedEnd[] ends = new OwnedEnd[ownedEnds.getLength()];
            for (int j = 0; j < ownedEnds.getLength(); j++) {
                /*
                 <ownedEnd aggregation="none" association="f4OpX0qGAqAABgcq" isDerived="false" isDerivedUnion="false" 
                 isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" type="k4iBX0qGAqAABgXy" 
                 xmi:id="gEOpX0qGAqAABgcs" xmi:type="uml:Property">
                 */
                NamedNodeMap attributes1 = ownedEnds.item(j).getAttributes();
                OwnedEnd end = new OwnedEnd();
                end.setAggregation(attributes1.getNamedItem("aggregation").getNodeValue());
                end.setAssociation(attributes1.getNamedItem("association").getNodeValue());
                end.setIsDerived(Boolean.parseBoolean(attributes1.getNamedItem("isDerived").getNodeValue()));
                end.setIsDerivedUnion(Boolean.parseBoolean(attributes1.getNamedItem("isDerivedUnion").getNodeValue()));
                end.setIsLeaf(Boolean.parseBoolean(attributes1.getNamedItem("isLeaf").getNodeValue()));
                end.setIsNavigable(Boolean.parseBoolean(attributes1.getNamedItem("isNavigable").getNodeValue()));
                end.setIsReadOnly(Boolean.parseBoolean(attributes1.getNamedItem("isReadOnly").getNodeValue()));
                end.setIsStatic(Boolean.parseBoolean(attributes1.getNamedItem("isStatic").getNodeValue()));
                end.setType(attributes1.getNamedItem("type").getNodeValue());
                end.setID(attributes1.getNamedItem("xmi:id").getNodeValue());
                ends[j] = end;
            }
            association.setOwnedEnds(ends);
            classAssociations.add(association);
        }

        return classAssociations;
    }
}
