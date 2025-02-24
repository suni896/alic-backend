package com.eduhk.alic.alicbackend.service;

import org.springframework.stereotype.Service;

/**
 * @author FuSu
 * @date 2025/2/24 14:08
 */
//@Service
//@EnableKafka
//public class KafkaProducer {
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    //todo kafka config要改
//    @Value("${kafka.topic.chat}")
//    private String chatTopic;
//
//    // 发送消息到Kafka
//    public void sendMessage(String message) {
//        ProducerRecord<String, String> record = new ProducerRecord<>(chatTopic, message);
//        kafkaTemplate.send(record);
//    }
//}
