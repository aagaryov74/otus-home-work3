package ru.otus.agaryov.dz3.results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.agaryov.dz3.csvfilereader.CsvFileReader;

import java.util.Map;

@Component
public class ResultCheckerImpl implements ResultChecker {
    private Integer result;
    private Map<String, String> qaMap;

    @Autowired
    public ResultCheckerImpl(@Qualifier("csvFileReaderImpl") CsvFileReader csvFileReader){
        this.result = 0;
        if (csvFileReader != null) this.qaMap = csvFileReader.readCsvIntoMap();
    }

    @Override
    public Object[] getQuestions() {
        if (this.qaMap != null) return this.qaMap.keySet().toArray();
        return null;
    }


    @Override
    public void checkAnswer(String question, String answer) {
        if (qaMap != null && qaMap.get(question) != null) {
            if (qaMap.get(question).contentEquals(answer.trim())) this.result++;
        }
    }

    @Override
    public Integer getResult() {
        return this.result;
    }

    @Override
    public void setMap(Map<String, String> aMap) {
        this.qaMap = aMap;
    }
}
