package com.bofa.handlenullInjson;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonNullCheck {

	public static void main(final String[] args) throws Exception {

		String jsonString = "{\"id\":1 , \"data\":null, \"data2\":\"\", "
				+ "\"data3\":{\"id\":1 , \"data\":null, \"data2\":\"\" } , " + " \"array\":[\"n1\",\"n2\" , null],"
				+ " \"array2\":[{\"id\":1 , \"data\":null, \"data2\":\"\" }]" + "}";

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);

		final JsonNode jsonNode = objectMapper.readTree(jsonString);
		// JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);
		ObjectNode clearedNode = removeEmptyFields(jsonNode);

		String outputStr = objectMapper.writeValueAsString(clearedNode);

		System.out.println(outputStr);
		
		//Output : {"id":1,"data3":{"id":1},"array":["n1","n2"],"array2":[{"id":1}]}


	}

	/**
	 * Removes empty fields from the given JSON object node.
	 * 
	 * @param an object node
	 * @return the object node with empty fields removed
	 */
	public static ObjectNode removeEmptyFields(final JsonNode jsonNode) {
		ObjectNode ret = new ObjectMapper().createObjectNode();
		Iterator<Entry<String, JsonNode>> iter = jsonNode.fields();

		while (iter.hasNext()) {
			Entry<String, JsonNode> entry = iter.next();
			String key = entry.getKey();
			JsonNode value = entry.getValue();

			if (!value.isNull()) {
				if (value instanceof ObjectNode) {
					Map<String, ObjectNode> map = new HashMap<String, ObjectNode>();
					map.put(key, removeEmptyFields((ObjectNode) value));
					ret.setAll(map);
				} else if (value instanceof ArrayNode) {
					ret.set(key, removeEmptyFields((ArrayNode) value));
				} else if (value.asText() != null && !value.asText().isEmpty()) {
					ret.set(key, value);
				}
			}
		}

		return ret;
	}

	/**
	 * Removes empty fields from the given JSON array node.
	 * 
	 * @param an array node
	 * @return the array node with empty fields removed
	 */
	public static ArrayNode removeEmptyFields(ArrayNode array) {
		ArrayNode ret = new ObjectMapper().createArrayNode();
		Iterator<JsonNode> iter = array.elements();

		while (iter.hasNext()) {
			JsonNode value = iter.next();

			if (!value.isNull()) {
				if (value instanceof ArrayNode) {
					ret.add(removeEmptyFields((ArrayNode) (value)));
				} else if (value instanceof ObjectNode) {
					ret.add(removeEmptyFields((ObjectNode) (value)));
				} else if (value != null && !value.textValue().isEmpty()) {
					ret.add(value);
				}
			}
		}

		return ret;
	}

}
