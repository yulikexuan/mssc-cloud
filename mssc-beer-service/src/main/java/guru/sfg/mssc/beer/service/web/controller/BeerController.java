//: guru.sfg.mssc.beer.service.web.controller.BeerController.java


package guru.sfg.mssc.beer.service.web.controller;


import guru.sfg.mssc.beer.service.domain.services.IBeerService;
import guru.sfg.mssc.beer.service.web.model.BeerDto;
import guru.sfg.mssc.beer.service.web.model.BeerPagedList;
import guru.sfg.mssc.beer.service.web.model.BeerStyleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class BeerController {

    static final Integer DEFAULT_PAGE_NUMBER = 0;
    static final Integer DEFAULT_PAGE_SIZE = 10;

    private final IBeerService beerService;

    @Autowired
    public BeerController(IBeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping(path = "/beer", produces = {"application/json"})
    public ResponseEntity<BeerPagedList> listBeers(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
            @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle,
                PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }

    @GetMapping("/beer/{id}")
    public ResponseEntity<BeerDto> getBeerById(
            @PathVariable("id") UUID id,
            @RequestParam(value = "showInventoryOnHand", required = false)
                    Boolean showInventoryOnHand) {

        if (Objects.isNull(showInventoryOnHand)) {
            showInventoryOnHand = false;
        }

        BeerDto beerDto = this.beerService.getById(id, showInventoryOnHand);
        return new ResponseEntity<>(beerDto, HttpStatus.OK);
    }

    @GetMapping("/upcbeer/{upc}")
    public ResponseEntity<BeerDto> getBeerByUpc(
            @PathVariable("upc") String upc,
            @RequestParam(value = "showInventoryOnHand", required = false)
                    Boolean showInventoryOnHand) {

        if (Objects.isNull(showInventoryOnHand)) {
            showInventoryOnHand = false;
        }

        BeerDto beerDto = this.beerService.getByUpc(upc, showInventoryOnHand);
        return new ResponseEntity<>(beerDto, HttpStatus.OK);
    }

    @PostMapping(path = "/beer")
    public ResponseEntity saveNewBeer(@Valid @RequestBody BeerDto beerDto) {

        return new ResponseEntity(this.beerService.saveNewBeer(beerDto),
                HttpStatus.CREATED);
    }

    @PutMapping("/beer/{id}")
    public ResponseEntity updateBeerById(
            @PathVariable("id") UUID id, @Valid @RequestBody BeerDto beerDto) {

        return new ResponseEntity(this.beerService.updateBeer(id, beerDto),
                HttpStatus.NO_CONTENT);
    }

}///:~