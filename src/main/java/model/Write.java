package model;

import java.io.FileWriter;
import java.io.IOException;

public class Write {
    private String fileName;
    private String content;

    public Write(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    public void writeOutput() {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
