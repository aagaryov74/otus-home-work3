package ru.otus.agaryov.dz3.exam;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.agaryov.dz3.csvfilereader.CsvFileReader;
import ru.otus.agaryov.dz3.results.ResultChecker;
import ru.otus.agaryov.dz3.service.AsciiCheckerServiceImpl;
import ru.otus.agaryov.dz3.service.IOService;
import ru.otus.agaryov.dz3.service.LocalizatorService;

import java.io.IOException;

@Service
public class ExamExecutorImpl implements ExamExecutor {
    private final CsvFileReader csvFileReader;
    private final ResultChecker resultChecker;
    private final AsciiCheckerServiceImpl asciiCheckerServiceImpl;
    private final IOService ioService;
    private final LocalizatorService localizatorService;

    @Autowired
    public ExamExecutorImpl(@Qualifier("csvFileReaderImpl") CsvFileReader csvFileReader,
                            ResultChecker resultChecker,
                            IOService ioService,
                            AsciiCheckerServiceImpl asciiCheckerServiceImpl, LocalizatorService localizatorService) {
        this.csvFileReader = csvFileReader;
        this.resultChecker = resultChecker;
        this.ioService = ioService;
        this.asciiCheckerServiceImpl = asciiCheckerServiceImpl;
        this.localizatorService = localizatorService;
    }

    public void doExam() {
        try {
            if (resultChecker.getQuestions() != null) {
                String consoleLanguage = ioService.getLocaleLang();
                ioService.printToConsole("enterFio");

                String studentFIO = ioService.readFromConsole();
                if (!(consoleLanguage.equalsIgnoreCase("en")&&(asciiCheckerServiceImpl.isASCII(studentFIO))
                )) {
                    ioService.printToConsole("doyouwanttochangelocale");
                    String yesOrNo = ioService.readFromConsole();
                    if (yesOrNo.trim().equalsIgnoreCase("y")) {
                        String language = ioService.getLanguage("enterlanguage");
                        if (localizatorService.setLanguage(language)) {
                            resultChecker.setMap(csvFileReader.readCsvIntoMap());
                            if (resultChecker.getQuestions() == null) throw new IOException();
                        } else {
                            System.err.println("cannot set language");
                        }
                    }
                }
                ioService.printFToConsole("welcome",
                        studentFIO, csvFileReader.getReadedStrsCount());

                for (int i = 0; i < resultChecker.getQuestions().length; i++) {
                    String question = resultChecker.getQuestions()[i].toString();
                    ioService.printFToConsole("question",
                            i + 1, question);
                    String input = ioService.readFromConsole();
                    resultChecker.checkAnswer(question, input);
                }
                ioService.printFToConsole("results", resultChecker.getResult());
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            ioService.printToConsole("iowarning");
        }
    }
}
