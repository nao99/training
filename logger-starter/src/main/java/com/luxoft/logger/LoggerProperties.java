package com.luxoft.logger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * LoggerProperties class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-08-09
 */
@ConfigurationProperties(prefix = "logger")
@ConstructorBinding
public class LoggerProperties {
    private final String fileName;
    private final String directoryToSave;

    public LoggerProperties(String fileName, String directoryToSave) {
        this.fileName = fileName;
        this.directoryToSave = directoryToSave;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDirectoryToSave() {
        return directoryToSave;
    }
}
