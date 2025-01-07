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
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerApplication {

    private static final Logger log = LoggerFactory.getLogger(CustomerApplication.class);
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

        String topic = "customerContacts";

        try (Producer<String, Customer> producer = new KafkaProducer<>(props)) {
            for (int i = 0; i < 10_000; i++) {
                Customer customer = CustomerGenerator.getNext();

                log.info("Customer {}: {}", i, customer);

                ProducerRecord<String, Customer> record = new ProducerRecord<>(topic,
                    customer.getName(), customer);

                producer.send(record);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
