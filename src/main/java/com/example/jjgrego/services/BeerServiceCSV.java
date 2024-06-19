package com.example.jjgrego.services;

import com.example.jjgrego.model.BeerCSV;

import java.io.File;
import java.util.List;

public interface BeerServiceCSV {
    List<BeerCSV> convertCSV(File f);
}
