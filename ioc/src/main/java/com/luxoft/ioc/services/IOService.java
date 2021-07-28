package com.luxoft.ioc.services;

public interface IOService {
    void out(String message);
    String readLn(String prompt);
    int readInt(String prompt);
}
