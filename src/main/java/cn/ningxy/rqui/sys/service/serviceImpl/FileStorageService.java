package cn.ningxy.rqui.sys.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import cn.ningxy.rqui.sys.util.FileTypeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ningxy
 */
@Slf4j
@Service
public class FileStorageService {

    private static String uploadBasePath;

    /**
     * 拼接 base 上传路径和 bucket 路径
     *
     * @param bucketString bucket 路径
     * @return 拼接后的路径
     */
    private static Path genPathWithBase(String bucketString) {
        bucketString = (bucketString == null) ? "" : bucketString;
        return Paths.get(uploadBasePath, bucketString);
    }

    /**
     * 保存文件
     *
     * @param bucketPath       bucket 路径
     * @param filename         文件名
     * @param multipartFile    文件
     * @param allowedMediaType 支持的文件类型（MIME TYPE）
     */
    public static String saveFile(String bucketPath,
                                  String filename,
                                  MultipartFile multipartFile,
                                  List<String> allowedMediaType) throws IOException {
        if (!FileTypeUtils.validateFileType(multipartFile.getResource(), allowedMediaType)) {
            throw new RuntimeException("文件类型不受支持");
        }

        Path saveDirPath = genPathWithBase(bucketPath);

        if (!Files.exists(saveDirPath)) {
            try {
                Files.createDirectories(saveDirPath);
            } catch (IOException e) {
                log.error(e.toString());
                throw new RuntimeException("创建文件夹失败。");
            }
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = saveDirPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Could not save file: " + filename);
            log.error(e.toString());
            throw new RuntimeException("文件保存出错。");
        }
        return saveDirPath.resolve(filename).toString();
    }

    /**
     * 加载单个文件
     *
     * @param fileUriString 文件路径（包含文件名）
     * @return Resource
     */
    public static Resource loadFile(String fileUriString) {
        Path filePath = genPathWithBase(fileUriString);
        try {
            Resource resource = new FileSystemResource(filePath.toAbsolutePath());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MalformedURLException();
            }
        } catch (MalformedURLException e) {
            log.error(e.toString());
            throw new RuntimeException("下载失败，可能是找不到文件。");
        }
    }

    /**
     * 加载多个文件
     *
     * @param fileUriStringList 文件路径（包含文件名）
     * @return Resource
     */
    public static List<Resource> loadFiles(List<String> fileUriStringList) {
        return fileUriStringList.stream()
                .map(FileStorageService::loadFile)
                .collect(Collectors.toList());
    }

    @Value("${custom.file.upload-path}")
    public void setUploadFilePath(String uploadFilePath) {
        FileStorageService.uploadBasePath = StringUtils.cleanPath(uploadFilePath);
    }
}
