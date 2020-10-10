//: guru.sfg.mssc.beer.service.config.JmsConfig.java


package guru.sfg.beer.inventory.service.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
public class JmsConfig {

    public static final String NEW_INVENTORY_QUEUE_NAME = "new-inventory";

    public static final String ORDER_ALLOCATION_QUEUE_NAME = "allocate-order";

    public static final String ORDER_ALLOCATION_RESPONSE_QUEUE_NAME =
            "allocate-order-response";

    public static final String ORDER_DEALLOCATION_QUEUE_NAME =
            "deallocate-order";

    public static final String ORDER_DEALLOCATION_RESPONSE_QUEUE_NAME =
            "deallocate-order-response";

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMaper) {

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);

        // This property needs to be set in order to allow for converting from
        // an incoming message to a Java object.
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMaper);

        return converter;
    }

}///:~