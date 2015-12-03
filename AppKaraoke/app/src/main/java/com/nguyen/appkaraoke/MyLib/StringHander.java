package com.nguyen.appkaraoke.MyLib;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringHander {
    private String str;

    public StringHander(String str) {
        this.str = str.toLowerCase();
    }

    public String removeAccent(){
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
