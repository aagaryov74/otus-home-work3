package ru.otus.agaryov.dz3.exam;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.agaryov.dz3.csvfilereader.CsvFileReader;
import ru.otus.agaryov.dz3.results.ResultChecker;
import ru.otus.agaryov.dz3.service.AsciiCheckerService;
import ru.otus.agaryov.dz3.service.IOService;

import java.io.IOException;

@Service
public class ExamExecutor {
    private CsvFileReader csvFile;
    private ResultChecker checker;
    private AsciiCheckerService asciiCheckerService;
    private IOService ioService;

    @Value("${csvfile}")
    private String csvFilePrefix;

    @Autowired
    public ExamExecutor(@Qualifier("implCsvFileReader") CsvFileReader csvFileReader,
                        ResultChecker resultChecker,
                        IOService ioService,
                        AsciiCheckerService asciiCheckerService) {
        this.csvFile = csvFileReader;
        this.checker = resultChecker;
        this.ioService = ioService;
        this.asciiCheckerService = asciiCheckerService;
    }

    public void doExam() {
        try {
            if (checker.getQuestions() != null) {
                String consoleLanguage = ioService.getLocaleLang();
                ioService.printToConsole("enterFio");
                String studentFIO = ioService.readFromConsole();
                if (!(consoleLanguage.equalsIgnoreCase("en")&&(asciiCheckerService.isASCII(studentFIO))
                )) {
                    ioService.printToConsole("doyouwanttochangelocale");
                    String yesOrNo = ioService.readFromConsole();
                    if (yesOrNo.trim().equalsIgnoreCase("y")) {
                        String lang = ioService.getLanguage("enterlanguage");
                        checker.setMap(csvFile.setCsvFile((csvFilePrefix +
                                "_" + lang.toLowerCase() + ".csv")));
                        ioService.setLocaleLang(lang);
                        if (checker.getQuestions() == null) throw new IOException();

                    }
                }
                ioService.printFToConsole("welcome",
                        studentFIO, csvFile.getReadedStrsCount());

                for (int i = 0; i < checker.getQuestions().length; i++) {
                    String question = checker.getQuestions()[i].toString();
                    ioService.printFToConsole("question",
                            i + 1, question);
                    String input = ioService.readFromConsole();
                    checker.checkAnswer(question, input);
                }
                ioService.printFToConsole("results", checker.getResult());
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            ioService.printToConsole("iowarning");
        }
    }
}
