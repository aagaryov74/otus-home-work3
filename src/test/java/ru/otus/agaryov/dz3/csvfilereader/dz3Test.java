package ru.otus.agaryov.dz3.csvfilereader;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.agaryov.dz3.service.AsciiCheckerService;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = Config.class)
@TestPropertySource(locations = "/test.yaml")
public class dz3Test {


    @Qualifier("ruCSVFileReader")
    @Autowired
    CsvFileReader ruReader;

    @Qualifier("ruCSVFileReader")
    @Autowired
    CsvFileReader enReader;

    @Qualifier("testChecker")
    @Autowired
    AsciiCheckerService checker;
    @Autowired
    Config.MapConfig mapConfig;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testContext() {
        Assert.assertNotNull(applicationContext.getBean("ruCSVFileReader"));
        Assert.assertNotNull(applicationContext.getBean("enCSVFileReader"));
        Assert.assertNotNull(applicationContext.getBean(Config.MapConfig.class));
    }

    @Test
    public void testConfigMaps() {
        Assert.assertNotNull(mapConfig.getRuMap());
        Assert.assertNotNull(mapConfig.getEnMap());
        Assert.assertEquals(mapConfig.getRuMap().size(), 3);
        Assert.assertEquals(mapConfig.getEnMap().size(), 3);

    }

    @Test
    public void checkAnswersCount() {
        Map<String, String> ruQuest = ruReader.readCsvIntoMap();

        Assert.assertNotNull(ruQuest);

        Assert.assertEquals("There are must be 5 records in ru file",
                5, (long) ruReader.getReadedStrsCount());

        Map<String, String> enQuest = enReader.readCsvIntoMap();

        Assert.assertNotNull(enQuest);

        Assert.assertEquals("There are must be 5 records in en file",
                5, (long) enReader.getReadedStrsCount());

        //  CsvFileReader reader = spy(ruReader);

/*
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
*/
    }

}