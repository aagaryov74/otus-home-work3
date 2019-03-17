package ru.otus.agaryov.dz3.service;

import java.util.Locale;

 public interface LocalizatorService {
    String getCSVFileByLang(String language);
    Locale getLocaleByLang(String language);
}
