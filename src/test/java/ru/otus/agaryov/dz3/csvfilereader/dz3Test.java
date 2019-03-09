package ru.otus.agaryov.dz3.csvfilereader;


import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.agaryov.dz3.results.ImplResultChecker;
import ru.otus.agaryov.dz3.results.ResultChecker;
import ru.otus.agaryov.dz3.service.AsciiCheckerService;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(locations = "/test.properties")
public class dz3Test {

    @Autowired
    Config config;

    @Autowired
    @Qualifier("testAsciiChecker")
    AsciiCheckerService asciiCheckerService;

    @Autowired
    @Qualifier("testMessageSource")
    MessageSource ms;

    @Autowired
    @Qualifier("testFileReader")
    CsvFileReader reader;

    @Value("#{${questions}}")
    private LinkedHashMap<String,String> cMap;

    @Value("${question.one}")
    private String q1;



    @Test
    public void checkAnswers() {

        Locale ruLocale = new Locale.Builder().setLanguage("ru").setRegion("RU").build();

        CsvFileReader reader = mock(CsvFileReader.class);

        // Не написали еще чтение из файла в мапу, но уже хотим проверить, как ответопроверятель работает
        when(reader.readCsvIntoMap()).thenReturn(cMap);

        // Ответопроверятель
        ResultChecker resChecker = new ImplResultChecker(reader);
        // Прокладка
        ResultChecker sChecker = Mockito.spy(resChecker);

        sChecker.checkAnswer(q1, "4");
        sChecker.checkAnswer(ms.getMessage("question.one",null,ruLocale), "4");
        sChecker.checkAnswer(ms.getMessage("question.two",null,Locale.ENGLISH), "4");
        sChecker.checkAnswer(ms.getMessage("question.three",null,ruLocale), "12");

        int res = sChecker.getResult();
        Assert.assertEquals(2,res);

        verify(sChecker, times(1)).
                checkAnswer("How many legs does elephant have", "4");

        verify(sChecker, never()).
                checkAnswer("Сколько деревьев в саду", "21");

        int qCount = sChecker.getQuestions().length;

        Assert.assertEquals(3,qCount);

    }

    @Test
    public void checkAsciiChecker() {
        System.out.println("Ascii Checker test");
        Assert.assertFalse(asciiCheckerService.isASCII("Кириллица"));
    }


    @Test
    public void checkFileReader() {
        Map<String,String> chMap = reader.readCsvIntoMap();
        Assert.assertEquals(chMap.size(),5);
    }
}