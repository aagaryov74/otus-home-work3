package ru.otus.agaryov.dz3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Locale;
@Service
public class LocalizatorServiceImpl implements LocalizatorService {
    private final String csvFile;

    public LocalizatorServiceImpl(@Value("${config.csvfile}") String csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public String getCSVFileByLang(String language) {
        String fileName = csvFile + "_" + language.toLowerCase() + ".csv";
        File qFile = new File(fileName);
        if (qFile.canRead()) {
            return fileName;
        } else {
            return null;
        }
    }

    @Override
    public Locale getLocaleByLang(String language) {
        try {
            Locale locale = new Locale.Builder().
                    setLanguage(language.toLowerCase()).
                    setRegion(language.toUpperCase()).build();
            if (locale != null) {
                return locale;
            }
        } catch (Exception e) {
            System.err.println("Cannot set locale to "+
                    language);
        }
        return null;
    }
}
