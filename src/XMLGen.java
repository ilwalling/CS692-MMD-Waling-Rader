import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class XMLGen {

    public void generateXML(){
    try{
        DocumentBuilderFactory docFactory = new DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        //root element
        Element rootMMD = document.createElement("mmd");
        document.appendChild(rootMMD);


    }
    catch (ParserConfigurationException pce){
        pce.printStackTrace();
    }
    catch (TransformerException tfe){
        tfe.printStackTrace();
    }
    }
}
