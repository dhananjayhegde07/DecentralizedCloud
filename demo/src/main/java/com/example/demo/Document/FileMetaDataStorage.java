package com.example.demo.Document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaDataStorage {

    @Id
    private String fileid;
    private String uploadedBy;
    private LocalDate uploadDate;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String description;

}
