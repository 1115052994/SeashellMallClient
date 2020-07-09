package com.netmi.baselibrary.data.base;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 类描述：文件上传工具
 * 创建人：Simple
 * 创建时间：2017/9/5 14:02
 * 修改备注：
 */
public class FilesUploadUtils {


    /**
     * 构建文件上传  List<MultipartBody.Part>
     * 注： 如果出现content-length：0, 可能图片正在写入，此时读取会失败
     *
     * @param filePath
     * @return
     */
    public static MultipartBody.Part multiPartFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            return MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        }
        return null;
    }

    /**
     * 构建多文件上传  Map<String, RequestBody>
     *
     * @param files
     * @param fileTypes
     * @return
     */
    public static Map<String, RequestBody> filesToMapRequestBody(List<File> files, List<String> fileTypes) {

        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i) != null && files.get(i).exists()) {
                if (files.get(i).isFile()) {
                    map.put("file" + i + "\";filename=\"" + files.get(i).getName(), RequestBody.create(MediaType.parse(fileTypes.get(i)), files.get(i)));
                }
            }
        }
        return map;
    }
}
