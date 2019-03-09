package ru.otus.agaryov.dz3.csvfilereader;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.TestPropertySource;
import ru.otus.agaryov.dz3.service.AsciiCheckerService;

@Configuration
@TestPropertySource(locations = "/test.properties")
public class Config {

    @Bean
    AsciiCheckerService testAsciiChecker() {
        return new AsciiCheckerService();
    }

    @Bean
    CsvFileReader testFileReader() {
        return new ImplCsvFileReader("QuestionsAndAnswers_en.csv");
    }

    @Bean
    public MessageSource testMessageSource() {
        ReloadableResourceBundleMessageSource ms
                = new ReloadableResourceBundleMessageSource();
        ms.setBasename("test");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }
}
