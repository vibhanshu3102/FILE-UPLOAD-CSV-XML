package com.Penske.CsvUpload.Model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "uploadData")
public class UploadEntity {

    private String name;
    private String email;
    private String designation;
}
