package ua.com.hedgehogsoft.oculus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileRepository {
    private String fileFolderLocation;

    @Autowired
    public FileRepository(@Value("${report.folder}") String fileFolderLocation) {
        this.fileFolderLocation = fileFolderLocation;
    }

    public List<String> getFileNames() throws IOException {
        List<String> result;
        result = Files.list(Paths.get(fileFolderLocation))
                .filter(Files::isRegularFile)
                .sorted()
                .map(Path::toFile)
                .map(File::getName)
                .collect(Collectors.toList());
        return result;
    }

    public File getFileByNme(String name) throws IOException {
        return Files.list(Paths.get(fileFolderLocation))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .get();
    }
}
