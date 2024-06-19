package com.example.jjgrego;

import com.example.jjgrego.entities.Beer;
import com.example.jjgrego.entities.Customer;
import com.example.jjgrego.model.BeerCSV;
import com.example.jjgrego.model.BeerStyle;
import com.example.jjgrego.repositories.BeerRepository;
import com.example.jjgrego.repositories.CustomerRepository;
import com.example.jjgrego.services.BeerServiceCSV;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Component
public class BootstrapData implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;

    private final BeerServiceCSV beerServiceCSV;

    public BootstrapData(BeerRepository br, CustomerRepository cr, BeerServiceCSV beerServiceCSV){
        this.beerRepository = br;
        this.customerRepository = cr;
        this.beerServiceCSV = beerServiceCSV;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        loadCSVData();
        loadBeers();
        loadCustomers();
    }


    private void loadCustomers(){
        List<Customer> customers = List.of(
                Customer.builder().name("Jen Gregory").createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build(),
                Customer.builder().name("John Gregory").createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build(),
                Customer.builder().name("Henry Gregory").createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build(),
                Customer.builder().name("Nathan Gregory").createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build()
        );
        customerRepository.saveAll(customers);
        log.info("inserted:  " + customerRepository.count() + " customers for shoppintg");


    }

    private void loadBeers(){

        List<Beer> beers = List.of(
                Beer.builder().beerName("Spaten Oktoberfest")
                        .beerStyle(BeerStyle.LAGER)
                        .price(new BigDecimal("2.75"))
                        .quantityOnHand(544)
                        .upc("upc1")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build(),
                Beer.builder().beerName("Guinness")
                        .beerStyle(BeerStyle.STOUT)
                        .price(new BigDecimal("3.75"))
                        .quantityOnHand(1000)
                        .upc("upc2")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build(),
                Beer.builder().beerName("Voodoo Ranger")
                        .beerStyle(BeerStyle.PALE_ALE)
                        .price(new BigDecimal("2.25"))
                        .quantityOnHand(466)
                        .upc("upc3")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build(),
                Beer.builder().beerName("Hot Britches")
                        .beerStyle(BeerStyle.IPA)
                        .price(new BigDecimal("3.45"))
                        .quantityOnHand(600)
                        .upc("upc14")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build(),
                Beer.builder().beerName("Spaten")
                        .beerStyle(BeerStyle.LAGER)
                        .price(new BigDecimal("1.99"))
                        .quantityOnHand(760)
                        .upc("upc15")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                        .build()
        );

        beerRepository.saveAll(beers);
        log.info("inserted:  " + beerRepository.count() + " beers for consumption");


    }

    private void loadCSVData() throws FileNotFoundException {
            if (beerRepository.count() < 10){
                File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

                List<BeerCSV> recs = beerServiceCSV.convertCSV(file);

                log.info("xxxxxxxxxxxxxx loaded " + recs.size() + " from csv file");
                recs.forEach(beerCSVRecord -> {
                    BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                        case "American Pale Lager" -> BeerStyle.LAGER;
                        case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                                BeerStyle.ALE;
                        case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                        case "American Porter" -> BeerStyle.PORTER;
                        case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                        case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                        case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                        case "English Pale Ale" -> BeerStyle.PALE_ALE;
                        default -> BeerStyle.PILSNER;
                    };

                    beerRepository.save(Beer.builder()
                            .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                            .beerStyle(beerStyle)
                            .price(BigDecimal.TEN)
                            .upc(beerCSVRecord.getRow().toString())
                            .quantityOnHand(beerCSVRecord.getCount())
                            .build());
                });
            }
        }

}
