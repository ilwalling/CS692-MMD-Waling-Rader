import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class XMLGen {
    public String xmlFilePath = "test/TestInput008/Output/xmlOut.xml";
    public void generateXML(HashMap<Integer,HashMap<String,String>> modules,  HashMap<Integer, ArrayList<ModuleComparison>> fixedNearestComparisons, Integer removedCount, String identifier,
                                   ArrayList<String> normalHeaders, ArrayList<String>metricHeaders, ArrayList<String>xmlTagHeaders, String outputPath,String filename) throws Exception{
    try{
        xmlFilePath = outputPath + "xmlOut.xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        //root element
        Element rootMMD = document.createElement("mmd");
        document.appendChild(rootMMD);
        Element fileNameElement = document.createElement("filename");
        fileNameElement.appendChild(document.createTextNode(filename));
        rootMMD.appendChild(fileNameElement);
        for(int i = 0; i<modules.keySet().size()+ removedCount;i++){
            if(!modules.keySet().contains(i)){
                continue;
            }
            if(modules.get(i).containsKey("MetricID")){
                Element cloneGroup = document.createElement("clone_group");

                Element cloneData = document.createElement("clone_data");
                Element cloneId = document.createElement("clone_group_id");
                cloneId.appendChild(document.createTextNode(modules.get(i).get("MetricID")));
                Element cloneCount = document.createElement("count_of_clone_group");
                cloneCount.appendChild(document.createTextNode("2"));
                Element cloneProcess = document.createElement("clone_group_process_data");
                if(modules.get(i).containsKey("CloneVuln")){
                    int cloneVulnerability = Integer.valueOf(modules.get(i).get("CloneVuln"));
                    if(cloneVulnerability != 0){
                        Element cloneVulnStatus = document.createElement("clone_group_vulnerability_status");
                        Element cloneVulncategory = document.createElement("clone_group_vulnerability_category");
                        if(cloneVulnerability == 1){
                            cloneVulnStatus.appendChild(document.createTextNode("MIXED"));
                            cloneVulncategory.appendChild(document.createTextNode("MIXED"));
                        }
                        else if(cloneVulnerability > 1) {
                            cloneVulnStatus.appendChild(document.createTextNode("VULNERABLE"));
                            cloneVulncategory.appendChild(document.createTextNode("VULNERABLE"));
                        }
                        cloneProcess.appendChild(cloneVulnStatus);
                        cloneProcess.appendChild(cloneVulncategory);
                    }
                }

                cloneData.appendChild(cloneId);
                cloneData.appendChild(cloneCount);
                cloneData.appendChild(cloneProcess);
                cloneGroup.appendChild(cloneData);

                for(int j = 1; j<3; j++){
                    Element cloneModule = document.createElement("clone_module");
                    Element moduleID = document.createElement("module_id");
                    Element idElement = document.createElement(identifier.toLowerCase());
                    String moduleOrder = "Module" + j;
                    idElement.appendChild(document.createTextNode(modules.get(i).get(moduleOrder)));
                    moduleID.appendChild(idElement);
                    cloneModule.appendChild(moduleID);
                    Element moduleProcessData = document.createElement("module_process_data");
                    for (String header : normalHeaders) {
                        if(header.toLowerCase().equals(identifier.toLowerCase()) || header.toLowerCase().equals("metricclone")){
                            continue;
                        }
                        Element processDataHeader = document.createElement(header.toLowerCase());
                        processDataHeader.appendChild(document.createTextNode(modules.get(i).get(header)));
                        moduleProcessData.appendChild(processDataHeader);
                    }
                    cloneModule.appendChild(moduleProcessData);
                    cloneGroup.appendChild(cloneModule);
                }

                ArrayList<ModuleComparison> comparisons = fixedNearestComparisons.get(i);
                Element distanceConnections = document.createElement("distance_connections");
                for(int k = 0; k<comparisons.size(); k++){
                    String connectionIdentifier = i + String.valueOf(k);
                    Element connectionId = document.createElement("connection_id");
                    connectionId.appendChild(document.createTextNode(connectionIdentifier));
                    distanceConnections.appendChild(connectionId);
                }
                cloneGroup.appendChild(distanceConnections);
                rootMMD.appendChild(cloneGroup);
            }
            else{
                Element module = document.createElement("module");
                Element moduleID = document.createElement("module_id");
                Element idElement = document.createElement(identifier.toLowerCase());
                idElement.appendChild(document.createTextNode(modules.get(i).get(identifier)));
                moduleID.appendChild(idElement);
                module.appendChild(moduleID);
                Element moduleProcessData = document.createElement("module_process_data");
                for (String header : normalHeaders) {
                    if(header.toLowerCase().equals(identifier.toLowerCase()) || header.toLowerCase().equals("metricclone")){
                        continue;
                    }
                    Element processDataHeader = document.createElement(header.toLowerCase());
                    processDataHeader.appendChild(document.createTextNode(modules.get(i).get(header)));
                    moduleProcessData.appendChild(processDataHeader);
                }

                module.appendChild(moduleProcessData);
                ArrayList<ModuleComparison> comparisons = fixedNearestComparisons.get(i);
                Element distanceConnections = document.createElement("distance_connections");
                for(int k = 0; k<comparisons.size(); k++){
                    String connectionIdentifier = i + String.valueOf(k);
                    Element connectionId = document.createElement("connection_id");
                    connectionId.appendChild(document.createTextNode(connectionIdentifier));
                    distanceConnections.appendChild(connectionId);
                }
                module.appendChild(distanceConnections);
                rootMMD.appendChild(module);

            }
        }
        for(int i = 0; i<fixedNearestComparisons.keySet().size();i++){
            for(int j = 0; j<fixedNearestComparisons.get(i).size();j++){
                Element connection = document.createElement("connection");
                Element connectionID = document.createElement("connection_id");
                connectionID.appendChild(document.createTextNode(String.valueOf(i) + j));
                connection.appendChild(connectionID);
                Element listMetrics = document.createElement("list_of_metrics");
                for (String diffMetric:fixedNearestComparisons.get(i).get(j).differentMetrics) {
                    if(normalHeaders.contains(diffMetric) || xmlTagHeaders.contains(diffMetric) || metricHeaders.contains(diffMetric)){
                        Element metric = document.createElement("metric");
                        Element metricName = document.createElement("metric_name");
                        metricName.appendChild(document.createTextNode(diffMetric));
                        metric.appendChild(metricName);
                        Element metric1Val = document.createElement("value");
                        if(!fixedNearestComparisons.get(i).get(j).module1.containsKey("MetricID")){
                            metric1Val.setAttribute("module", fixedNearestComparisons.get(i).get(j).module1.get(identifier));
                        }
                        else{
                            metric1Val.setAttribute("module", fixedNearestComparisons.get(i).get(j).module1.get("MetricID"));
                        }
                        metric1Val.appendChild(document.createTextNode(fixedNearestComparisons.get(i).get(j).module1.get(diffMetric)));

                        Element metric1Val2 = document.createElement("value");
                        if(!fixedNearestComparisons.get(i).get(j).module2.containsKey("MetricID")){
                            metric1Val2.setAttribute("module", fixedNearestComparisons.get(i).get(j).module2.get(identifier));
                        }
                        else{
                            metric1Val2.setAttribute("module", fixedNearestComparisons.get(i).get(j).module2.get("MetricID"));
                        }
                        metric1Val2.appendChild(document.createTextNode(fixedNearestComparisons.get(i).get(j).module2.get(diffMetric)));

                        metric.appendChild(metric1Val);
                        metric.appendChild(metric1Val2);
                        listMetrics.appendChild(metric);
                    }
                }
                connection.appendChild(listMetrics);
                rootMMD.appendChild(connection);
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(xmlFilePath));
        transformer.transform(domSource, streamResult);
        System.out.println("Done creating XML File");
    }
    catch (ParserConfigurationException pce){
        throw new Exception("GENERAL ERROR: XML output");
    }
    catch (TransformerException tfe){
        throw new Exception("GENERAL ERROR: XML output");
    }
    }
}
