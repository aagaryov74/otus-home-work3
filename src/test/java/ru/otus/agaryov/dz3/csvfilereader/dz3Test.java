package ru.otus.agaryov.dz3.csvfilereader;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.agaryov.dz3.results.ResultCheckerImpl;
import ru.otus.agaryov.dz3.service.AsciiCheckerServiceImpl;
import ru.otus.agaryov.dz3.service.IOServiceImpl;
import ru.otus.agaryov.dz3.service.LocalizatorServiceImpl;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@DisplayName("Тесты классов программы тестирования студентов")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Config.class)
@TestPropertySource(locations = "/test.yaml")
class dz3Test {

    @Autowired
    Config.MapConfig mapConfig;

    @SpyBean
    private AsciiCheckerServiceImpl asciiCheckerService;

    @SpyBean
    private CsvFileReaderImpl csvFileReader;

    @SpyBean
    private LocalizatorServiceImpl localizatorService;

    @SpyBean
    private IOServiceImpl ioService;

    @SpyBean
    private ResultCheckerImpl resultChecker;

    @DisplayName("Тестируем asciiChecker")
    @Test
    void testAsciiCheckerService() {
        assertFalse(asciiCheckerService.isASCII("Привет"));
        assertTrue(asciiCheckerService.isASCII("this is ascii only"));
    }

    @DisplayName("Проверяем что при переключении на несуществующие консоли не бросаются исключения")
    @Test
    void testThatLocalizatorDoentThowExcentions() {
        assertThatCode(() -> localizatorService.setLanguage("yy")).doesNotThrowAnyException();
    }

    @DisplayName("Проверяем что csvFileReader не бросает исключений при несуществующих файлах")
    @Test
    void testThatCsvFileReader() {
        assertThatCode(() -> csvFileReader.setCsvFile("unexisted file")).doesNotThrowAnyException();
    }

    @DisplayName("Проверяем что в тестовом файле testquestions.yaml с вопросами есть по 3 вопроса на англ и русском")
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
        csvFileReader.setCsvFile(localizatorService.getCSVFile());


        Map<String, String> ruQuiz = csvFileReader.readCsvIntoMap();
        assertNotNull(ruQuiz);

        assertEquals(5, (int) csvFileReader.getReadedStrsCount());

        csvFileReader.setCsvFile(localizatorService.getCSVFile());

        Map<String, String> enQuiz = csvFileReader.readCsvIntoMap();

        assertNotNull(enQuiz);

        assertEquals(5, (int) csvFileReader.getReadedStrsCount());

    }

    @DisplayName("Тест Класса ResultChecker с подкладкой из вопросов из тестового testquestions.yaml")
    @Test
    void testResultChecker() {

        for (String lang : mapConfig.getLanguages()) {
            resultChecker.setMap(mapConfig.getMapByLang(lang));

            for (String question : mapConfig.getMapByLang(lang).keySet()
            ) {
                resultChecker.checkAnswer(question, mapConfig.getMapByLang(lang).get(question));
            }
            assertEquals(3, (int) resultChecker.getResult());
            if (lang.contentEquals("en")) {
                verify(resultChecker, times(1)).
                        checkAnswer("How many legs does elephant have", "4");
            }
            if (lang.contentEquals("ru")) {
                verify(resultChecker, times(1)).
                        checkAnswer("Сколько ног у слона", "4");
            }
            verify(resultChecker, never()).
                    checkAnswer("Сколько деревьев в саду", "21");

            int qCount = resultChecker.getQuestions().length;

            assertEquals(3, qCount);

        }

    }

}