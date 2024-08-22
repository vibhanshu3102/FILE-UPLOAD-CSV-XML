package com.Penske.CsvUpload.Controller;

import com.Penske.CsvUpload.Model.UploadEntity;
import com.Penske.CsvUpload.Service.UploadService;
import com.Penske.CsvUpload.Util.FileUploadRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSVData(@RequestBody FileUploadRequest fileUploadRequest) {
        try {
            // Read users from the provided file path
            String file = fileUploadRequest.getPath() + fileUploadRequest.getFileName();
            List<UploadEntity> users = uploadService.readUsersFromFile(file);
            if(users.isEmpty()){
                return ResponseEntity.ok("Empty CSV file");
            }
            return ResponseEntity.ok("Data from CSV file saved successfully.");
        } catch (IOException e) {
            // Handle errors related to file processing
            return ResponseEntity.status(500).body("Failed to process the file.");
        }
    }

    @GetMapping("/getData")
    public List<UploadEntity> getAllData(){
        return uploadService.getData();
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        List<UploadEntity> fileData = uploadService.saveUploadedFileData(multipartFile);
        if (fileData.isEmpty()){
            return ResponseEntity.ok("Empty Data");
        }
        return ResponseEntity.ok("Data Saved Successfully");
    }

    @PostMapping("/uploadXML")
    public ResponseEntity<String> uploadXmlFile(@RequestParam("XmlFile") MultipartFile multipartFile) throws IOException {
        List<UploadEntity> fileData = uploadService.getUploadedXmlData(multipartFile);
        if (fileData.isEmpty()){
            return ResponseEntity.ok("Empty Data");
        }
        return ResponseEntity.ok("Data Saved Successfully");
    }

    @GetMapping("/GetAllXmlData")
    public List<UploadEntity> getAllXmlFileData(){
        return uploadService.getAllXmlData();
    }

    @GetMapping("/downloadCsv")
    public ResponseEntity<String> downloadCsvFile(@RequestParam("id") String id , HttpServletResponse response) {
        try {
            uploadService.downloadCsvById(id , response);
            return ResponseEntity.ok("Downloaded");
        } catch (IOException e) {
            return ResponseEntity.ok("Problem...");
        }
    }

}
