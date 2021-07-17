package com.stc.thamquan.configs;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 4/11/21
 * Time      : 14:19
 * Filename  : KafkaTopicConfig
 */
@Configuration
public class KafkaTopicConfig {
    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.topic.email-no-template}")
    private String emailWithoutTemplateTopic;

    @Value(value = "${kafka.topic.email-with-template}")
    private String emailWithTemplateTopic;

    @Value(value = "${kafka.topic.email-with-attachment}")
    private String emailWithAttachmentTopic;


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic emailTopicWithoutTemplate() {
        return new NewTopic(emailWithoutTemplateTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic emailTopicWithTemplate() {
        return new NewTopic(emailWithTemplateTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic emailTopicWithAttachment() {
        return new NewTopic(emailWithAttachmentTopic, 1, (short) 1);
    }
}
