package ru.otus.agaryov.dz3.csvfilereader;


import junit.framework.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.agaryov.dz3.results.ResultChecker;
import ru.otus.agaryov.dz3.results.ResultCheckerImpl;
import ru.otus.agaryov.dz3.service.AsciiCheckerServiceImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("Тесты классов программы тестирования студентов")
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties
@ContextConfiguration(classes = Config.class)
@TestPropertySource(locations = "/test.yaml")
class dz3Test {

    @Autowired
    Config.MapConfig mapConfig;

    @SpyBean
    private AsciiCheckerServiceImpl asciiCheckerService;

    @SpyBean
    private CsvFileReaderImpl csvFileReader;

    @DisplayName("Тестируем asciiChecker")
    @Test
    void testAsciiCheckerService() {
        assertFalse(asciiCheckerService.isASCII("Привет"));
        assertTrue(asciiCheckerService.isASCII("this is ascii only"));
    }


    @DisplayName("Проверяем что в тестовом файле с вопросами есть по 3 вопроса на англ и русском")
    @Test
    void testConfigMaps() {
        assertNotNull(mapConfig.getLanguages());
        assertNotNull(mapConfig.getMapByLang("ru"));
        assertNotNull(mapConfig.getMapByLang("en"));
        assertEquals(mapConfig.getMapByLang("ru").size(), 3);
        assertEquals(mapConfig.getMapByLang("en").size(), 3);

    }


    @DisplayName("Проверяем что класс - читатель из csv файла читает по 5 вопросов из них на русском и английском")
    @Test
    void checkQuestions() {
        Map<String, String> Quiz = csvFileReader.readCsvIntoMap();
        assertNotNull(Quiz);

        assertEquals(5, (int) csvFileReader.getReadedStrsCount());

        Map<String, String> enQuiz = csvFileReader.readCsvIntoMap();

        assertNotNull(enQuiz);

        assertEquals(5, (int) csvFileReader.getReadedStrsCount());

    }

    @Test
    void testSpyQuiz() {

        // Ответопроверятель
        ResultChecker resChecker;
        // Прокладка
        ResultChecker sChecker;

        for (String lang : mapConfig.getLanguages()) {
            CsvFileReader reader =
                    mock(CsvFileReaderImpl.class);
            // Не написали еще чтение из файла в мапу, но уже хотим проверить, как ответопроверятель работает
            when(reader.readCsvIntoMap()).thenReturn(mapConfig.getMapByLang(lang));
            when(reader.getReadedStrsCount()).thenReturn(mapConfig.getMapByLang(lang).size());
            resChecker = new ResultCheckerImpl(reader);
            sChecker = Mockito.spy(resChecker);

            for (String question : mapConfig.getMapByLang(lang).keySet()
            ) {
                sChecker.checkAnswer(question, mapConfig.getMapByLang(lang).get(question));
//                System.out.println("q = "+ question + " a = " +
//                        mapConfig.getMapByLang(lang).get(question) +
//                        " counter in checker " + sChecker.getResult());
            }
            assertNotNull(sChecker);

            int res = sChecker.getResult();

            assertEquals(3, res);

            if (lang.contentEquals("en")) {
                verify(sChecker, times(1)).
                        checkAnswer("How many legs does elephant have", "4");
            }
            if (lang.contentEquals("ru")) {
                verify(sChecker, times(1)).
                        checkAnswer("Сколько ног у слона", "4");
            }
            verify(sChecker, never()).
                    checkAnswer("Сколько деревьев в саду", "21");

            int qCount = sChecker.getQuestions().length;

            assertEquals(3, qCount);

        }

    }

}