//: guru.sfg.mssc.beer.service.domain.services.inventory.IBeerInventoryService.java


package guru.sfg.mssc.beer.service.domain.services.inventory;


import java.util.UUID;


public interface IBeerInventoryService {

    Integer getOnhandInventory(UUID beerId);

}///:~