package ru.otus.agaryov.dz3.csvfilereader;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CsvFileReaderImpl implements CsvFileReader {
    private static final Logger logger = LoggerFactory.getLogger(CsvFileReader.class);
    // Config File with questions
    private String csvFile;
    // Counter of strings that have been read
    private Integer readStrCounter;

    @Autowired
    public CsvFileReaderImpl(@Value("${config.csvfile}") String csvFile) {
        this.readStrCounter = 0;
        this.csvFile = csvFile + "_" +
                java.util.Locale.getDefault().getLanguage() + ".csv";
        logger.info("csvfile is {}",this.csvFile);
    }

    public CsvFileReaderImpl(@Value("${config.csvfile}") String csvFile,
                             String localeLanguage) {
        this.readStrCounter = 0;
        this.csvFile = csvFile + "_" +
                localeLanguage + ".csv";
        logger.info("csvfile is {}",this.csvFile);
    }

    @Override
    public Map<String,String> readCsvIntoMap() {
        CSVReader reader;
        this.readStrCounter=0;
        Map<String,String> qaMap = new LinkedHashMap<>();
        try {
            reader = new CSVReader(new FileReader(this.csvFile));
            String[] line;
            while ((line = reader.readNext()) != null) {
                    qaMap.put(line[0], line[1]);
                    this.readStrCounter++;
            }
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error at reading config file  " + csvFile + ": "
                    + e.getMessage());
            return null;
        }
        return qaMap;
    }

    @Override
    public Map<String,String> setCsvFile(String fileName) {
        this.csvFile = fileName;
        return readCsvIntoMap();
    }

    @Override
    public Integer getReadedStrsCount() {
        return readStrCounter;
    }

}
