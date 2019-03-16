package ru.otus.agaryov.dz3.csvfilereader;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.agaryov.dz3.exam.ExamExecutor;
import ru.otus.agaryov.dz3.results.ImplResultChecker;
import ru.otus.agaryov.dz3.results.ResultChecker;
import ru.otus.agaryov.dz3.service.AsciiCheckerService;

import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = Config.class)
@TestPropertySource(locations = "/test.yaml")
public class dz3Test {

    @Qualifier("AsciiChecker")
    @Autowired
    private AsciiCheckerService testAsciiChecker;

    @Qualifier("ruCSVFileReader")
    @Autowired
    private CsvFileReader ruReader;

    @Qualifier("ruCSVFileReader")
    @Autowired
    private CsvFileReader enReader;

    @Autowired
    Config.MapConfig mapConfig;
    @Autowired
    private ApplicationContext applicationContext;
/*
    @Autowired
    private ResultChecker resultChecker;

    @Autowired
    private ExamExecutor examExecutor;
*/
    @Test
    public void testContext() {
        Assert.assertNotNull(applicationContext.getBean("ruCSVFileReader"));
        Assert.assertNotNull(applicationContext.getBean("enCSVFileReader"));
        Assert.assertNotNull(applicationContext.getBean(Config.MapConfig.class));
    }

    @Test
    public void testAscii() {
        Assert.assertFalse(testAsciiChecker.isASCII("Привет"));
        Assert.assertTrue(testAsciiChecker.isASCII("this is ascii only"));

    }

    @Test
    public void testConfigMaps() {
        Assert.assertNotNull(mapConfig.getRuMap());
        Assert.assertNotNull(mapConfig.getEnMap());
        Assert.assertEquals(mapConfig.getRuMap().size(), 3);
        Assert.assertEquals(mapConfig.getEnMap().size(), 3);

    }

    @Test
    public void checkQuestions() {
        Map<String, String> ruQuiz = ruReader.readCsvIntoMap();

        Assert.assertNotNull(ruQuiz);

        Assert.assertEquals("There are must be 5 records in ru questions file",
                5, (long) ruReader.getReadedStrsCount());

        Map<String, String> enQuiz = enReader.readCsvIntoMap();

        Assert.assertNotNull(enQuiz);

        Assert.assertEquals("There are must be 5 records in en questions file",
                5, (long) enReader.getReadedStrsCount());

    }

    @Test
    public void testSpyQuiz() {

        CsvFileReader reader = Mockito.spy(ruReader);

        // Не написали еще чтение из файла в мапу, но уже хотим проверить, как ответопроверятель работает
        when(reader.readCsvIntoMap()).thenReturn(mapConfig.getEnMap());

        // Ответопроверятель
        ResultChecker resChecker = new ImplResultChecker(reader);
        // Прокладка
        ResultChecker sChecker = Mockito.spy(resChecker);

        for (String question : mapConfig.getEnMap().keySet()
        ) {
            //System.out.println("question = "+ question+ " Answer = " +
            //        mapConfig.getEnMap().get(question));
            sChecker.checkAnswer(question, mapConfig.getEnMap().get(question));
        }

        int res = sChecker.getResult();
        Assert.assertEquals(3, res);

        verify(sChecker, times(1)).
                checkAnswer("How many legs does elephant have", "4");

        verify(sChecker, never()).
                checkAnswer("Сколько деревьев в саду", "21");

        int qCount = sChecker.getQuestions().length;

        Assert.assertEquals(3, qCount);

    }

}