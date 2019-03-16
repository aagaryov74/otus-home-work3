package ru.otus.agaryov.dz3.csvfilereader;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import ru.otus.agaryov.dz3.service.AsciiCheckerService;

import java.io.InputStream;
import java.util.*;

@Configuration
public class Config {

    @Value("${csvfile}")
    private String csvfile;

    @Bean(name = "AsciiChecker")
    AsciiCheckerService testAsciiChecker() {
        return new AsciiCheckerService();
    }


    @Component
    public class MapConfig {
        private Map<String,Map<String, String>> maps;
        public MapConfig() {

            Yaml yaml = new Yaml();
            InputStream inputStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("testquestions.yaml");
            this.maps = yaml.load(inputStream);
        }

        Map<String, String> getRuMap() {
            return maps.get("ru");
        }

        Map<String, String> getEnMap() {
            return maps.get("en");
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
