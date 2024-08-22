package com.Penske.CsvUpload.Model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


import java.util.List;

@Data
@JacksonXmlRootElement(localName = "records")
public class XmlRecords {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "record")
    private List<UploadEntity> uploadEntities;
}
