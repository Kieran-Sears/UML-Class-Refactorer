/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author Kieran
 */
   
   public class XMI_NameSpace implements NamespaceContext {

    @Override
    public String getNamespaceURI(String prefix) {
    
        if (prefix == null) {
            throw new NullPointerException("No prefix");
        } else if ("UML".equals(prefix)) {
            return "org.omg.xmi.namespace.UML";
        } else if ("xml".equals(prefix)) {
            return XMLConstants.XML_NS_URI;
        }else if ("xmi".equals(prefix)) {
            return "http://schema.omg.org/spec/XMI/2.1";
        }
        return XMLConstants.NULL_NS_URI;
    }

    @Override
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }
    
}
