package me.marco.Utility;

import java.util.ArrayList;
import java.util.List;

public class UtilString {

    public static String fixItemCapitalisation(String itemName) {
        String toSplit = itemName.replace("_", " ");
        String[] nameSplit = toSplit.split(" ");
        List<String> newList = new ArrayList<String>();
        for (String parts : nameSplit) {
            newList.add(parts.charAt(0) + parts.toLowerCase().substring(1));
        }
        return String.join(" ", newList);
    }

}
