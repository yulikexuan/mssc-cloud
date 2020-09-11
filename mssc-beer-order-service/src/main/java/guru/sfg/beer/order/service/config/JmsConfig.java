//: guru.sfg.mssc.beer.service.config.JmsConfig.java


package guru.sfg.beer.order.service.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
public class JmsConfig {

    // The destination of validate-order
    public static final String ORDER_VALIDATION_QUEUE_NAME = "validate-order";
    public static final String ORDER_VALIDATION_RESULT_QUEUE_NAME =
            "validate-order-result";
    public static final String ORDER_ALLOCATION_QUEUE_NAME = "allocate-order";

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {

        MappingJackson2MessageConverter converter =
                new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);

        // This property needs to be set in order to allow for converting from
        // an incoming message to a Java object.
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);

        return converter;
    }

}///:~