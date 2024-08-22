package com.Penske.CsvUpload.Service;

import com.Penske.CsvUpload.Model.UploadEntity;
import com.Penske.CsvUpload.Model.XmlRecords;
import com.Penske.CsvUpload.Repository.UploadRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadService {

    @Autowired
    private UploadRepository uploadRepository;

    public List<UploadEntity> readUsersFromFile(String filePath) throws IOException {
        List<UploadEntity> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                UploadEntity user = new UploadEntity();
                user.setName(csvRecord.get("name"));
                user.setEmail(csvRecord.get("email"));
                user.setDesignation(csvRecord.get("designation"));
                users.add(user);
            }
        }
        uploadRepository.saveAll(users);
        return users;
    }

    public List<UploadEntity> getData() {
        return uploadRepository.findAll();
    }

    public List<UploadEntity> saveUploadedFileData(MultipartFile multipartFile) throws IOException {

        //CREATING LIST TO CHECK THE DETAILS AFTER SAVING ARE EMPLTY OR NOT
        List<UploadEntity> uploadEntityList = new ArrayList<>();

        //THIS WILL CONVERT ALL THE DATA FROM FILE TO INPUT STREAM
        InputStream inputStream = multipartFile.getInputStream();

        //READER IS THE ARGUMENT PASSED IN PARSER AND IT HELPS IN READING THE STREAM OR FILE
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));

        //PARSER FINALLY HELPS IN CONVERTING THEM INTO FILE RECORDS
        CSVParser parser = new CSVParser(reader , CSVFormat.DEFAULT.withFirstRecordAsHeader());

        for (CSVRecord csvRecord : parser) {
            UploadEntity user = new UploadEntity();
            user.setName(csvRecord.get("name"));
            user.setEmail(csvRecord.get("email"));
            user.setDesignation(csvRecord.get("designation"));
            uploadRepository.save(user);
            uploadEntityList.add(user);
        }
        return uploadEntityList;
    }

    public List<UploadEntity> getUploadedXmlData(MultipartFile multipartFile) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        XmlRecords xmlRecords = xmlMapper.readValue(multipartFile.getInputStream(), XmlRecords.class);
        List<UploadEntity> uploadEntities = xmlRecords.getUploadEntities();
        return uploadRepository.saveAll(uploadEntities);
    }

    public List<UploadEntity> getAllXmlData() {
        return uploadRepository.findAll();
    }

    public void downloadCsvById(String id, HttpServletResponse response) throws IOException {

        UploadEntity entity = uploadRepository.findById(id).orElseThrow(() -> new RuntimeException("Data not found"));


        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");


        try (OutputStream outputStream = response.getOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT.withHeader("Name", "Email", "Designation"))) {

            // Write data to CSV
            csvPrinter.printRecord(entity.getName(), entity.getEmail(), entity.getDesignation());
            csvPrinter.flush();
        }
    }
}

