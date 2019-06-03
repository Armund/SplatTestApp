import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TextSearcher {
    public String directoryPath = "D:/ALEX/Proging/SPLATapp"; //TODO заменить на File
    public String extension = ".log";
    public String textToSearch;
    ArrayList<File> files = new ArrayList<File>();


    public void setPath(String path) {
        directoryPath = path;
    }

    public void setExtension(String exp) {
        extension = exp;
    }

    public void processFilesFromFolder(File folder) {
        for (File entry : folder.listFiles()) {
            if (entry.isDirectory()) {
                processFilesFromFolder(entry);
                continue;
            }
            if (entry.getPath().endsWith(extension)) { //TODO изменить условие
                files.add(entry);
                System.out.println(entry);
            }
        }
    }

    public void searchText(String text) {
        textToSearch = text;
        textToSearch = textToSearch.replace("\n", "\r\n");
        System.out.println(textToSearch);
        File folder = new File(directoryPath);
        files.clear();
        processFilesFromFolder(folder);
        String content = null;
        for (File file : files) {
            try {
                content = new String(Files.readAllBytes(Paths.get(file.getPath())), "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (content.contains(textToSearch)) {
                System.out.println(content);
            }
        }
    }

    public String filesToString() {
        String string = "";
        for (File file : files) {
            string += file.getPath() + "\n";
        }
        return string;
    }
}