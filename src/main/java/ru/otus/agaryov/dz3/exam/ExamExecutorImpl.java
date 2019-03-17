package ru.otus.agaryov.dz3.exam;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.agaryov.dz3.csvfilereader.CsvFileReader;
import ru.otus.agaryov.dz3.results.ResultChecker;
import ru.otus.agaryov.dz3.service.AsciiCheckerServiceImpl;
import ru.otus.agaryov.dz3.service.IOService;

import java.io.IOException;

@Service
public class ExamExecutorImpl implements ExamExecutor {
    private final CsvFileReader csvFile;
    private final ResultChecker checker;
    private final AsciiCheckerServiceImpl asciiCheckerServiceImpl;
    private final IOService ioService;

    @NonNull
    @Value("${config.csvfile}")
    private String csvFilePrefix;

    @Autowired
    public ExamExecutorImpl(@Qualifier("csvFileReaderImpl") CsvFileReader csvFileReader,
                            ResultChecker resultChecker,
                            IOService ioService,
                            AsciiCheckerServiceImpl asciiCheckerServiceImpl) {
        this.csvFile = csvFileReader;
        this.checker = resultChecker;
        this.ioService = ioService;
        this.asciiCheckerServiceImpl = asciiCheckerServiceImpl;
    }

    public void doExam() {
        try {
            if (checker.getQuestions() != null) {
                String consoleLanguage = ioService.getLocaleLang();
                ioService.printToConsole("enterFio");
                String studentFIO = ioService.readFromConsole();
                if (!(consoleLanguage.equalsIgnoreCase("en")&&(asciiCheckerServiceImpl.isASCII(studentFIO))
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
