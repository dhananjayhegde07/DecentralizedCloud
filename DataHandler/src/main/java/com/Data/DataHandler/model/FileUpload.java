package com.Data.DataHandler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUpload {
    private String fileId;
    private String fileContent;
}
