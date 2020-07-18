package guru.sfg.beer.inventory.service.web.controllers;


import guru.sfg.beer.inventory.service.domain.service.IBeerInventoryService;
import guru.sfg.beer.inventory.service.web.model.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


/**
 * Created by jt on 2019-05-31.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerInventoryController {

    private final IBeerInventoryService beerInventoryService;

    @GetMapping("api/v1/beer/{beerId}/inventory")
    List<BeerInventoryDto> listBeersById(@PathVariable UUID beerId){
        return beerInventoryService.getAllInventoriesByBeerId(beerId);
    }
}
