package com.Penske.CsvUpload.Repository;

import com.Penske.CsvUpload.Model.UploadEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends MongoRepository<UploadEntity, String> {
}
