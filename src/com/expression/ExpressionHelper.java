package com.expression;

import com.model.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionHelper {
    private static Map<ExpressionEnums, String> mapRegex;
    public ExpressionHelper() {
        Map<ExpressionEnums, String> mapRegex = new HashMap<>();
        String concat = "\\$concat\\(\\[(\\w|\\s)+\\]\\,\\s+\\\".*\\\"\\)\\s*";
        String substring = "\\$substring\\(\\[(\\w|\\s)+\\]\\,\\s*(\\d)+\\,\\s*(\\d)+\\)\\s*";
        String toUpperCase = "\\$toUpperCase\\(\\[(\\w|\\s)+\\]\\)\\s*";
        String toLowerCase = "\\$toLowerCase\\(\\[(\\w|\\s)+\\]\\)\\s*";
        String trim = "\\$trim\\(\\[(\\w|\\s)+\\]\\)\\s*";
        String numeric = "\\[(\\w|\\s)+\\]\\s+(\\+|\\*|\\-|\\\\)\\s*(\\d)+\\s*";
        String pair  = "\\$concat\\(\\[(\\w|\\s)+\\]\\,( )*\\[(\\w|\\s)+\\]\\,( )*\\\"(.)*\\\"\\)\\s*";
        mapRegex.put(ExpressionEnums.CONCAT, concat);
        mapRegex.put(ExpressionEnums.SUBSTRING, substring);
        mapRegex.put(ExpressionEnums.TO_UPPER_CASE, toUpperCase);
        mapRegex.put(ExpressionEnums.TO_LOWER_CASE, toLowerCase);
        mapRegex.put(ExpressionEnums.TRIM, trim);
        mapRegex.put(ExpressionEnums.NUMERIC, numeric);
        mapRegex.put(ExpressionEnums.PAIR, pair);
        this.mapRegex = mapRegex;
    }

    public ExpressionEnums checkRegex(String text) {
        final ExpressionEnums[] match = {ExpressionEnums.NONE};
        for (Map.Entry<ExpressionEnums, String> entry : mapRegex.entrySet()) {
            if (text.matches(entry.getValue())) {
                match[0] = entry.getKey();
            }
        }
        return match[0];
    }

    public static String getColumnName(String expression) {
        String name = expression.substring(expression.indexOf('[') + 1, expression.indexOf(']'));
        return name;
    }

    public static List<String> getListColumnNameInExpress(String expression) {
        List<String> listColumnNameInExpress = new ArrayList<>();
        if (expression.matches(mapRegex.get(ExpressionEnums.PAIR))) {
            String regex = "\\$concat\\(\\[(?<name1>(\\w|\\s)+)\\]\\,( )*\\[(?<name2>(\\w|\\s)+)\\]\\,( )*\\\"(.)*\\\"\\)\\s*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(expression);
            while (matcher.find()) {
                listColumnNameInExpress.add(matcher.group("name1"));
                listColumnNameInExpress.add(matcher.group("name2"));
            }
        } else {
            String name = expression.substring(expression.indexOf('[') + 1, expression.indexOf(']'));
            listColumnNameInExpress.add(name);
        }
        return listColumnNameInExpress;
    }

    public static Map<String, String> executeExpression(Map<String, String> mapOneRow, Column column) {
        String expression = column.getExpression();
        String inputData = mapOneRow.get(column.getLinearId());

        Map<String, String> resultMap = new HashMap<>();
        for (Map.Entry<String, String> entry : mapOneRow.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue());
        }

        if (expression.matches(mapRegex.get(ExpressionEnums.CONCAT))) {
            String regex = "\\$concat\\(\\[(\\w|\\s)+\\]\\,\\s+\\\"(?<text>.*)\\\"\\)\\s*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(expression);
            String text = "";
            while (matcher.find()) {
                text = matcher.group("text");
                break;
            }
            resultMap.put(column.getId(), inputData.concat(text));
        }

        if (expression.matches(mapRegex.get(ExpressionEnums.SUBSTRING))) {
            String regex = "\\$substring\\(\\[(\\w|\\s)+\\]\\,\\s*(?<start>(\\d)+)\\,\\s*(?<end>(\\d)+)\\)\\s*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(expression);
            int start = 0;
            int end = 0;
            while (matcher.find()) {
                start = Integer.valueOf(matcher.group("start"));
                end = Integer.valueOf(matcher.group("end"));
                break;
            }
            resultMap.put(column.getId(), inputData.substring(start, end));
        }

        if (expression.matches(mapRegex.get(ExpressionEnums.TRIM))) {
            resultMap.put(column.getId(), inputData.trim());
        }

        if (expression.matches(mapRegex.get(ExpressionEnums.TO_UPPER_CASE))) {
            resultMap.put(column.getId(), inputData.toUpperCase());
        }

        if (expression.matches(mapRegex.get(ExpressionEnums.TO_LOWER_CASE))) {
            resultMap.put(column.getId(), inputData.toLowerCase()) ;
        }

        if (expression.matches(mapRegex.get(ExpressionEnums.PAIR))) {
            String regex = "\\$concat\\(\\[(?<name1>(\\w|\\s)+)\\]\\,( )*\\[(?<name2>(\\w|\\s)+)\\]\\,( )*\\\"(?<text>(.)*)\\\"\\)\\s*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(expression);
            String text = "";
            while (matcher.find()) {
                text = matcher.group("text");
                break;
            }
            String [] linearIds = column.getLinearId().split("\\|");
            resultMap.put(column.getId(), mapOneRow.get(linearIds[0]) + text + mapOneRow.get(linearIds[1]) );
        }

        return resultMap;
    }

}
