//: guru.sfg.mssc.beer.service.domain.services.brewing.BrewBeerListener.java


package guru.sfg.mssc.beer.service.domain.services.brewing;


import guru.sfg.mssc.beer.service.config.JmsConfig;
import guru.sfg.mssc.beer.service.domain.services.IBeerService;
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
    private final IBeerService beerService;

    /*
     * @Transactional :
     * Use BeerService here other than adding Transactional annotation
     * One more thing should be adding a new method to BeerService::getQuantitytoBrew
     */
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE_NAME)
    public void listenToBrewBeerEvent(BrewBeerEvent brewBeerEvent) {

        BeerDto beerDto = brewBeerEvent.getBeerDto();
        int quantityToBrew = this.beerService.getQuantityToBrew(beerDto.getId());

        // Brewing Beer ... ...

        beerDto.setQuantityOnHand(quantityToBrew);

        log.info(">>>>>>> Just brewed beer {} - QQH: {}",
                beerDto.getBeerName(), beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE_NAME,
                new NewBeerInventoryEvent(beerDto));
    }

}///:~