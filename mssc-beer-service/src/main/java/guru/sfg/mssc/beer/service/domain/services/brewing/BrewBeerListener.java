//: guru.sfg.mssc.beer.service.domain.services.brewing.BrewBeerListener.java


package guru.sfg.mssc.beer.service.domain.services.brewing;


import guru.sfg.mssc.beer.service.config.JmsConfig;
import guru.sfg.mssc.beer.service.domain.model.Beer;
import guru.sfg.mssc.beer.service.domain.repositories.IBeerRepository;
import guru.sfg.mssc.beer.service.event.BrewBeerEvent;
import guru.sfg.mssc.beer.service.event.NewBeerInventoryEvent;
import guru.sfg.mssc.beer.service.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class BrewBeerListener {

    private final JmsTemplate jmsTemplate;
    private final IBeerRepository beerRepository;

    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE_NAME)
    public void listenToBrewBeerEvent(BrewBeerEvent brewBeerEvent) {

        BeerDto beerDto = brewBeerEvent.getBeerDto();
        Beer beer = this.beerRepository.getOne(beerDto.getId());

        // Brewing Beer ... ...

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        log.info(">>>>>>> Just brewed beer {} / Min on Hand: {} / QQH: {}",
                beerDto.getBeerName(), beer.getMinOnHand(),
                beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE_NAME,
                new NewBeerInventoryEvent(beerDto));
    }

}///:~