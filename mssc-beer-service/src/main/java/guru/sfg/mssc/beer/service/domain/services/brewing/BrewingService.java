//: guru.sfg.mssc.beer.service.domain.services.brewing.BrewingService.java


package guru.sfg.mssc.beer.service.domain.services.brewing;


import guru.sfg.mssc.beer.service.config.JmsConfig;
import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository;
import guru.sfg.mssc.beer.service.domain.services.IBeerSpecification;
import guru.sfg.mssc.commons.event.BrewBeerEvent;
import guru.sfg.mssc.beer.service.web.mapper.IBeerMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService implements IBrewingService {

    private final JmsTemplate jmsTemplate;
    private final IBeerMapper beerMapper;
    private final IBeerRepository beerRepository;
    private final IBeerSpecification beerLowInventorySpecification;

    @Override
    @Scheduled(initialDelay = 10000L, fixedDelay = 60000L) // Every 60 Sec.
    public void checkForLowInventory() {

        List<Beer> allBeers = this.beerRepository.findAll();

        allBeers.stream()
                .filter(this.beerLowInventorySpecification::isSatisfied)
                .forEach(this::sendBrewBeerEvent);
    }

    private void sendBrewBeerEvent(@NonNull Beer beer) {
        jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE_NAME,
                BrewBeerEvent.of(beerMapper.beerToBeerDto(beer)));
    }

}///:~