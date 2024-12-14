package com.Data.DataHandler.Service;

import com.Data.DataHandler.model.ServerNode;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;


public class ConsistantHashingService {

    private final int numberOfReplicas=5;
    private final SortedMap<Integer, ServerNode> circle = new TreeMap<>();


    // Adds a node to the consistent hashing circle
    public void add(ServerNode node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            String key = node.getNodeid() + "-replica-" + i; // You can customize this based on your node properties
            int hash = hash(key);
            circle.put(hash, node);
        }
    }

    // Gets the node for a specific key (chunkIdentifier)
    public ServerNode getNode(String chunkIdentifier) {
        if (circle.isEmpty()) {
            return null; // or throw an exception
        }

        int hash = hash(chunkIdentifier);
        // Get the first node whose hash is greater than or equal to the hash of the chunkIdentifier
        SortedMap<Integer, ServerNode> tailMap = circle.tailMap(hash);
        Integer targetHash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        return circle.get(targetHash);
    }

    // Computes the hash for a given key using SHA-256
    private int hash(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getBytes());
            return hashToInt(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Converts the byte array to an integer (to fit into the hash ring)
    private int hashToInt(byte[] hash) {
        int result = 0;
        for (int i = 0; i < 4; i++) { // Use first 4 bytes for hash
            result = (result << 8) + (hash[i] & 0xff);
        }
        return Math.abs(result); // Ensure non-negative
    }
}

