//: guru.sfg.mssc.beer.service.config.JmsConfig.java


package guru.sfg.mssc.beer.service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
public class JmsConfig {

    public static final String BREWING_REQUEST_QUEUE_NAME = "brewing-request";

    @Bean
    public MessageConverter messageConverter() {

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);

        // This property needs to be set in order to allow for converting from
        // an incoming message to a Java object.
        converter.setTypeIdPropertyName("_type");

        return converter;
    }

}///:~