package org.hubay.byteandbuy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hubay.byteandbuy.persistence.snapshot.GameSnapshot;
import org.springframework.stereotype.Service;

/**
 * Zabespecuje serializaciu a deserializaciu GameSnapshot objektu.
 */
@Service
public class SnapshotSerializer {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Prevedie GameSnapshot na JSON pre ulozenie do DB.
     */
    public String toJson(GameSnapshot snapshot) {
        try {
            return mapper.writeValueAsString(snapshot);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obnovi GameSnapshot z JSON nacitaneho z DB.
     */
    public GameSnapshot fromJson(String json) {
        try {
            return mapper.readValue(json, GameSnapshot.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
