package ru.volkov.guest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LogOperator {
    private final Logger logger;
    private boolean console;
    private boolean info;
    private boolean error;

    public LogOperator(Class<?> beanType) {
        logger = LoggerFactory.getLogger(beanType);
        console = true;
        info = true;
        error = true;
    }

    public void console(String... args) {
        if (console) {
            System.out.println(getMessage(args));
        }
    }

    public void info(String... args) {
        if (info) {
            logger.info(getMessage(args));
        }
    }

    public void error(String... args) {
        if (error) {
            logger.error(getMessage(args));
        }
    }

    private String getMessage(String... args) {
        return String.format("<<< %s(%s) >>>", getCallMethodName(), convertArgs(args));
    }

    private String convertArgs(String... args) {
        if (args.length == 0) {
            return "-";
        }
        return Arrays.stream(args).map(val -> val + "").collect(Collectors.joining(" , "));
    }

    private String getCallMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[4].getMethodName();
    }

    public LogOperator onConsole(boolean console) {
        this.console = console;
        return this;
    }

    public LogOperator onInfo(boolean info) {
        this.info = info;
        return this;
    }

    public LogOperator onError(boolean error) {
        this.error = error;
        return this;
    }
}
