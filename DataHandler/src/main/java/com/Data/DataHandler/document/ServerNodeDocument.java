package com.Data.DataHandler.document;


import com.Data.DataHandler.model.ServerNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerNodeDocument {
    List<ServerNode> ips;
}
