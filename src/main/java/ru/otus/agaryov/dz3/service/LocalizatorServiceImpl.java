package ru.otus.agaryov.dz3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import ru.otus.agaryov.dz3.csvfilereader.CsvFileReader;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

@Service
public class LocalizatorServiceImpl implements LocalizatorService {
    private final String csvFile;

    @Value("${config.messages}")
    private String messagesFile;

    private final IOService ioService;
    private final CsvFileReader csvFileReader;
    private String language;

    public LocalizatorServiceImpl(@Value("${config.csvfile}") String csvFile,
                                  IOService ioService, CsvFileReader csvFileReader) {
        this.csvFile = csvFile;
        this.ioService = ioService;
        this.csvFileReader = csvFileReader;
        this.language = Locale.getDefault().getLanguage();
    }

    @Override
    public Boolean setLanguage(String language) {
        if (isLangOk(language)) {
            this.language = language;
            ioService.setLocale(getLocale());
            csvFileReader.setCsvFile(getCSVFile());
            return true;
        }
        return false;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public String getCSVFile() {
        String fileName = csvFile + "_" +
                this.language.toLowerCase() + ".csv";
        File qFile = new File(fileName);
        if (qFile.canRead()) {
            return fileName;
        } else {
            return null;
        }
    }

    @Override
    public Locale getLocale() {
        try {
            Locale locale = new Locale.Builder().
                    setLanguage(this.language.toLowerCase()).
                    setRegion(this.language.toUpperCase()).build();
            if (locale != null) {
                return locale;
            }
        } catch (Exception e) {
            System.err.println("Cannot set locale to "+ this.language);
        }
        return null;
    }

    private Boolean isLangOk(String language) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream(messagesFile);
            Map<String, Map<String, String>> maps = yaml.load(inputStream);
            if (maps.get(language.toLowerCase()).size() > 0) {
                File qFile = new File(csvFile + "_" + language + ".csv");
                if (qFile.canRead()) {
                    Locale locale = new Locale.Builder().
                            setLanguage(language.toLowerCase()).
                            setRegion(language.toUpperCase()).build();
                    if (locale != null) {
                        return true;
                    }
                } else {
                    System.err.println("There are no csv file with questions with this Language");
                }
            } else {
                System.err.println("There are no messages with this Language in config file");
            }
        } catch (Exception e) {
            System.err.println("exception was at messages reading");
            return false;
        }
        return false;
    }
}
