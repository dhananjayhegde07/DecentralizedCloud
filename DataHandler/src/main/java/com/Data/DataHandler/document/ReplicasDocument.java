package com.Data.DataHandler.document;


import com.Data.DataHandler.model.ServerNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplicasDocument {
    @Id
    private String id;

    private String nodeid;

    private String ip;

    private int priority;
}
