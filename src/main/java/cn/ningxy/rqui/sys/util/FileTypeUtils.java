package cn.ningxy.rqui.sys.util;

import lombok.experimental.UtilityClass;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author ningxy
 */
@UtilityClass
public class FileTypeUtils {
    private static final Tika TIKA = new Tika();

    /**
     * 获取文件真实类型
     *
     * @param resource Resource
     * @return 文件类型（MimeType）
     * @throws IOException IOException
     */
    public static String getFileType(Resource resource) throws IOException {
        return TIKA.detect(resource.getInputStream());
    }

    /**
     * 验证文件类型是否在允许的范围内
     *
     * @param resource         Resource
     * @param allowedMediaType 允许的文件类型（MimeType）
     * @return 验证结果
     * @throws IOException IOException
     */
    public static boolean validateFileType(Resource resource, List<String> allowedMediaType) throws IOException {
        if (allowedMediaType == null || allowedMediaType.isEmpty()) {
            return false;
        }
        String fileType = getFileType(resource);
        return allowedMediaType.stream()
                .anyMatch(allowedType -> allowedType.equalsIgnoreCase(fileType));
    }

    /**
     * 获取文件扩展名
     *
     * @param path 文件路径
     * @return 文件扩展名
     */
    public static String getFileExtension(String path) {
        return StringUtils.getFilenameExtension(path);
    }
}
