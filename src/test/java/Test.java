import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Test {
    private static final String OUTPUT_FILE_NAME = "test.json";

    private List<Employee> getExpectedList() {
        List<Employee> expectedList = new ArrayList<>();
        expectedList.add(new Employee(1, "John", "Smith", "USA", 25));
        expectedList.add(new Employee(2, "Ivan", "Petrov", "RU", 23));

        return expectedList;
    };

    private Employee[] getExpectedArray() {
        List<Employee> employeeList = getExpectedList();
        Employee[] employees = employeeList.toArray(new Employee[employeeList.size()]);

        return employees;
    };

    private String getExpectedJson() {
        String formattedJson = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Smith\",\n" +
                "    \"country\": \"USA\",\n" +
                "    \"age\": 25\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"firstName\": \"Ivan\",\n" +
                "    \"lastName\": \"Petrov\",\n" +
                "    \"country\": \"RU\",\n" +
                "    \"age\": 23\n" +
                "  }\n" +
                "]";
        return formattedJson
                .replace("\n","")
                .replace(" ", "");
    };

    @org.junit.jupiter.api.Test
    public void testParsingCsvToJavaClasses() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String csvFileName = "data.csv";

        List<Employee> result = Main.parseCSV(columnMapping, csvFileName);
        Employee[] resultArray = result.toArray(new Employee[result.size()]);

        Assertions.assertArrayEquals(getExpectedArray(), resultArray);
    }

    @org.junit.jupiter.api.Test
    public void testParsingXmlToJavaClasses() {
        String xmlFileName = "data.xml";

        List<Employee> result = Main.parseXML(xmlFileName);
        Employee[] resultArray = result.toArray(new Employee[result.size()]);

        Assertions.assertArrayEquals(getExpectedArray(), resultArray);
    }

    @org.junit.jupiter.api.Test
    public void testParsingJavaClassesToJson() {
        String expected = getExpectedJson();
        String result = Main.listToJson(getExpectedList());

        Assertions.assertEquals(expected, result);
    }

    @org.junit.jupiter.api.Test
    public void testFileCreation() {
        Main.writeString(getExpectedJson(), OUTPUT_FILE_NAME);

        File expectedFile = new File(OUTPUT_FILE_NAME);

        Assertions.assertTrue(expectedFile.exists());
    }

    @AfterAll
    public static void removeTestFile() {
        File file = new File(OUTPUT_FILE_NAME);
        file.delete();
    }
}
