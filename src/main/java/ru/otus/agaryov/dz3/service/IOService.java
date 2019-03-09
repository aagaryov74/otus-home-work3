package ru.otus.agaryov.dz3.service;

import java.io.IOException;

public interface IOService {

    void printToConsole(String propertyParam);
    void printFToConsole(String propertyParam, Object ... args);
    String readFromConsole() throws IOException;
    String getLocaleLang();
    void setLocaleLang(String language);
    String getLanguage(String prompt) throws IOException;

}
