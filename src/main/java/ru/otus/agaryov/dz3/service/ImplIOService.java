package ru.otus.agaryov.dz3.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import static java.lang.System.out;

@Service
public class ImplIOService implements  IOService {
    private MessageSource messageSource;
    private Locale locale;
    private BufferedReader bufferedReader;

    public ImplIOService(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        locale = Locale.getDefault();
    }
    @Override
    public void printToConsole(String propertyParam) {
        out.println(messageSource.getMessage(locale.getLanguage() +
                "." + propertyParam,null,locale));

    }
    @Override
    public void printFToConsole(String propertyParam, Object ... args) {
        out.printf(messageSource.getMessage(locale.getLanguage() +
                "." + propertyParam,null,locale),args);
    }
    @Override
    public String readFromConsole() throws IOException {
        return bufferedReader.readLine();
    }

    public String getMessage(String propertyParam) {
        return messageSource.getMessage(propertyParam,null,locale);
    }
    @Override
    public String getLocaleLang() {
        return locale.getLanguage();
    }

    @Override
    public void setLocaleLang(String language) {
        if (language.equalsIgnoreCase("en")) {
            this.locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        } else {
            this.locale = new Locale.Builder().setLanguage(language).setRegion(language.toUpperCase()).build();
        }
    }

    @Override
    public String getLanguage(String prompt) throws IOException{
        String lang;
        do {
            printToConsole(prompt);
            lang = readFromConsole();
        } while (!lang.matches("^[a-zA-z]{2}$"));
        return lang;
    }
}
