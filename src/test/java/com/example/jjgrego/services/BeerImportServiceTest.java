package com.example.jjgrego.services;

import com.example.jjgrego.model.BeerCSV;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class BeerImportServiceTest {

    BeerServiceCSV beerServiceCsv = new BeerServiceCSVImpl();

    @Test
    void importBeerCsv() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beer.csv");

        List<BeerCSV> records = beerServiceCsv.convertCSV(file);

        assertThat(records.size() > 0);

    }
}
