/*
 * Copyright 2025 Seoryeong Min.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.kafkapractice.serializer;

import java.util.Map;
import java.util.Properties;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericAvroCustomerApplication {

    private static final Logger log = LoggerFactory.getLogger(GenericAvroCustomerApplication.class);
    private static final Map<String, String> env = System.getenv();

    public static void main(String[] args) {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.get("KAFKA_BOOTSTRAP_SERVERS"));
        // props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.put("schema.registry.url", env.get("KAFKA_SCHEMA_REGISTRY_URL"));
        // props.put("schema.registry.url", "localhost:8081");

        String schemaString = "{\"namespace\":\"customerManagement.avro\","
            + "\"type\":\"record\","
            + "\"name\":\"Customer\","
            + "\"fields\":["
            + "{\"name\":\"id\",\"type\":\"int\"},"
            + "{\"name\":\"name\",\"type\":\"string\"},"
            + "{\"name\":\"email\",\"type\":[\"null\",\"string\"], \"default\":\"null\"}"
            + "]}";

        try (Producer<String, GenericRecord> producer = new KafkaProducer<>(props)) {
            Schema.Parser parser = new Schema.Parser();
            Schema schema = parser.parse(schemaString);

            for (int i = 0; i < 10_000; i++) {
                String name = "customer" + i;
                String email = "email" + i + "@domain.com";

                GenericRecord customer = new Record(schema);

                customer.put("id", i);
                customer.put("name", name);
                customer.put("email", email);

                ProducerRecord<String, GenericRecord> record = new ProducerRecord<>(
                    "customerContacts", name, customer);

                producer.send(record);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
