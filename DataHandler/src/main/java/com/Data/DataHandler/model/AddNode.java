package com.Data.DataHandler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNode {
    private String ip;
    private int priority;
    private List<Map<String, String>> replicas;
}
