package az.edadi.back.service;

import java.io.File;

public interface FileService {

    String saveFile(String key, File file, String folder);

    String update(String key, File file);

    void deleteFile(String key, String folder);
}
