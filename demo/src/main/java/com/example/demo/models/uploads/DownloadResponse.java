package com.example.demo.models.uploads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadResponse {
    private boolean success;
    private String file;
    private String status;
}
