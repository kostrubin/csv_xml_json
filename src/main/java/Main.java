import com.google.gson.*;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String csvFileName = "data.csv";
        String xmlFileName = "data.xml";

        // task 1
        List<Employee> csvList = parseCSV(columnMapping, csvFileName);
        String jsonFromCsv = listToJson(csvList);
        writeString(jsonFromCsv, "data.json");

        // task 2
        List<Employee> xmlList = parseXML(xmlFileName);
        String jsonFromXml = listToJson(xmlList);
        writeString(jsonFromXml, "data2.json");
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                .withMappingStrategy(strategy)
                .build();

            return csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        return gson.toJson(list);
    }

    private static void writeString(String json, String outputName) {
        try (FileWriter file = new FileWriter(outputName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String file) {
        List<Employee> resultList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(file));
            doc.getDocumentElement();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    resultList.add(new Employee(
                        Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent()),
                        element.getElementsByTagName("firstName").item(0).getTextContent(),
                        element.getElementsByTagName("lastName").item(0).getTextContent(),
                        element.getElementsByTagName("country").item(0).getTextContent(),
                        Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent())
                    ));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
