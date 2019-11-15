package com.sky.CatBreeds.utils;

public class LogUtils {
    public static int line(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null || trace.length == 0)
            return -1; //
        return trace[0].getLineNumber();
    }
    public static String fun(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null)
            return ""; //
        return trace[0].getMethodName();
    }
    public static String filename(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        if (trace == null)
            return ""; //
        return trace[0].getFileName();
    }
    public static String funAndLine(Exception e) {
    	StackTraceElement[] trace = e.getStackTrace();
    	if (trace == null || trace.length == 0)
        return ""; //
    	return  trace[0].getMethodName()+"|"+trace[0].getLineNumber()+"|";
    }
}