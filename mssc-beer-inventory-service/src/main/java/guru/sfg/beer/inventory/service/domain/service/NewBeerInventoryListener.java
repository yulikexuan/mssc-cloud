//: guru.sfg.beer.inventory.service.domain.service.NewInventoryListener.java


package guru.sfg.beer.inventory.service.domain.service;


import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.mssc.commons.dto.BeerDto;
import guru.sfg.mssc.commons.event.NewBeerInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NewBeerInventoryListener {

    private final IBeerInventoryService beerInventoryService;

    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE_NAME)
    public void listenToNewInventoryEvent(NewBeerInventoryEvent newBeerInventoryEvent) {

        BeerDto beerDto = newBeerInventoryEvent.getBeerDto();

        log.debug(">>>>>>> New inventory for beer {} - QQH: {}",
                beerDto.getBeerName(), beerDto.getQuantityOnHand());

        this.beerInventoryService.saveNewInventory(beerDto);
    }

}///:~