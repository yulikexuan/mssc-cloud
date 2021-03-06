//: guru.sfg.mssc.beer.service.config.JmsConfig.java


package guru.sfg.mssc.beer.service.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
public class JmsConfig {

    public static final String BREWING_REQUEST_QUEUE_NAME = "brewing-request";
    public static final String NEW_INVENTORY_QUEUE_NAME = "new-inventory";
    public static final String ORDER_VALIDATION_QUEUE_NAME = "validate-order";
    public static final String ORDER_VALIDATION_RESULT_QUEUE_NAME =
            "validate-order-result";

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);

        // This property needs to be set in order to allow for converting from
        // an incoming message to a Java object.
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);

        return converter;
    }

}///:~