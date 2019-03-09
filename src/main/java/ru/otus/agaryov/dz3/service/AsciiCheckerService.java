package ru.otus.agaryov.dz3.service;

import org.springframework.stereotype.Service;

@Service
public class AsciiCheckerService {

        // to check if a string only contains US-ASCII code point
        public boolean isASCII(String checkStr) {
            boolean isASCII = true;
            for (int i = 0; i < checkStr.length(); i++) {
                int c = checkStr.charAt(i);
                if (c > 0x7F) {
                    isASCII = false;
                    break;
                }
            }
            return isASCII;
        }
}
