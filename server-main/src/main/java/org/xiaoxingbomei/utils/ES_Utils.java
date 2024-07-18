package org.xiaoxingbomei.utils;

public class ES_Utils
{
    /**
     * es的检索key
     */
    public static String getKeyWord(String field) {
        return field + ".keyword";
    }
}
