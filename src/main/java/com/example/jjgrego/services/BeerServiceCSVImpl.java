package com.example.jjgrego.services;

import com.example.jjgrego.model.BeerCSV;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BeerServiceCSVImpl implements BeerServiceCSV{

    @Override
    public List<BeerCSV> convertCSV(File f) {
        try {
            List<BeerCSV> beerCSVRecords = new CsvToBeanBuilder<BeerCSV>(new FileReader(f))
                    .withType(BeerCSV.class)
                    .build().parse();
            return beerCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
