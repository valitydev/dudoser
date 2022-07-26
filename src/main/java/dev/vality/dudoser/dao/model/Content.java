package dev.vality.dudoser.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    private String type;
    private byte[] data;

    public String getDataValue() {
        if (data != null) {
            return new String(data, StandardCharsets.UTF_8);
        }
        return "";
    }

}
