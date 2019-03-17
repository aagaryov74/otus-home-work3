package ru.otus.agaryov.dz3.service;

import java.io.IOException;
import java.util.Locale;

public interface IOService {

    void printToConsole(String propertyParam);
    void printFToConsole(String propertyParam, Object ... args);
    String readFromConsole() throws IOException;
    String getLocaleLang();
    void setLocale(Locale locale);
    String getLanguage(String prompt) throws IOException;

}
