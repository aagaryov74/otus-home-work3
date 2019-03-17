package ru.otus.agaryov.dz3.csvfilereader;

import java.util.Map;

public interface CsvFileReader {
    // Read csv file into a Map
    Map<String, String > readCsvIntoMap();
    // How many correct strings are in config file?
    Integer getReadedStrsCount();
    // change file if we need to change locale in runtime;
    void setCsvFile(String fileName);
}
