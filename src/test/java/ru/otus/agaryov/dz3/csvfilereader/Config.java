package ru.otus.agaryov.dz3.csvfilereader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import ru.otus.agaryov.dz3.service.AsciiCheckerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class Config {

    @Value("${csvfile}")
    private String csvfile;

    @Bean(name = "testChecker")
    AsciiCheckerService testAsciiChecker() {
        return new AsciiCheckerService();
    }

    @Component
    @TestPropertySource("test.yaml")
    //??? Как сделать чтобы он читал из test.yaml а не application.yaml???
    @EnableConfigurationProperties
    @ConfigurationProperties(prefix = "questions")
    public class MapConfig {
       private Map<String, String> ru = new HashMap<>();
       private Map<String, String> en = new HashMap<>();

        public Map<String, String> getRuMap() {
            return ru;
        }

        public Map<String, String> getEnMap() {
            return en;
        }

        public void setRu(Map<String, String> ru) {
            this.ru = ru;
        }

        public void setEn(Map<String, String> en) {
            this.en = en;
        }
    }

    @Bean(name = "ruCSVFileReader")
    CsvFileReader testRUCsvFileReader() {
        return new ImplCsvFileReader(csvfile,"ru");
    }

    @Bean(name = "enCSVFileReader")
    CsvFileReader testENCsvFileReader() {
        return new ImplCsvFileReader(csvfile,"en");
    }

}
