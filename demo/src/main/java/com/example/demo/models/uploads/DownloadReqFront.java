package com.example.demo.models.uploads;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadReqFront {
    @NotBlank
    private String fileId;
    private String downloadType;
}
