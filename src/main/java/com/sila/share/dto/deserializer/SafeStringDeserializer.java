package com.sila.share.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SafeStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        try {
            String value = p.getValueAsString();

            if (value == null) return null;

            value = value.trim();

            if (value.isEmpty()) return null;

            // handle literal "string"
            if ("string".equalsIgnoreCase(value)) return null;

            return value;

        } catch (Exception e) {
            return null;
        }
    }
}