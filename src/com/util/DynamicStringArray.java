package com.util;
import java.util.ArrayList;

public class DynamicStringArray {
	public String[] stringarray = {};
    public ArrayList <String> dynamicstringarray = new ArrayList<String>();    
    
    public void addString(String string) {
        dynamicstringarray.add(string);
    }

    public String[] getStringArray() {
        stringarray = (String[]) dynamicstringarray.toArray(stringarray);
        return stringarray;
    }

    public void printStringArray() {
        stringarray = (String[]) dynamicstringarray.toArray(stringarray);
        for(int i=0; i<stringarray.length; i++) {
            System.out.println(stringarray[i]);
        }
    }
}

