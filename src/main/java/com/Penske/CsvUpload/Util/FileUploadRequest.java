package com.Penske.CsvUpload.Util;

import lombok.Data;

@Data
public class FileUploadRequest {
    private String path;
    private String fileName;
}
