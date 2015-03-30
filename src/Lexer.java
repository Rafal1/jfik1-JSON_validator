import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rafał Zawadzki
 */
public class Lexer {
    private static Integer lengthFieldLimit = 500000;

    public LinkedList<String> start(BufferedReader bReader) throws IOException {
        LinkedList<String> gatheredTokens = new LinkedList<String>();
        String lineConstant;
        String tokenBuffor = "";
        Boolean chainFlag = false;
        Integer lineCounter = 0;
        while ((lineConstant = bReader.readLine()) != null) {
            lineCounter++;
            char[] charsOfLine = lineConstant.toCharArray();
            for (Integer i = 0; i < charsOfLine.length; i++) {
                Character c = charsOfLine[i];
                if (c.equals(new Character(' '))) {
                    continue;
                }

                if (c.equals('\"')) {
                    if (!chainFlag) {
                        chainFlag = true;
                        gatheredTokens.add("ZNAK_NAPISU");
                    } else {
                        if (!tokenBuffor.equals("")) {
                            Pattern p = Pattern.compile(".*");
                            Matcher m = p.matcher(tokenBuffor);
                            if (m.matches()) {
                                gatheredTokens.add("ZNAKI");
                                tokenBuffor = "";
                            } else {
                                System.out.println("NAPIS Error token is inappropriate character: " + c + " in line " + lineCounter + " and column " + i + ".");
                                tokenBuffor = "";
                            }
                        }
                        gatheredTokens.add("ZNAK_NAPISU");
                        chainFlag = false;
                    }
                    continue;
                }
                tokenBuffor += c;

                if (!chainFlag) {
                    if (tokenBuffor.equals("true")) {
                        gatheredTokens.add("TRUE");
                        tokenBuffor = "";
                        continue;
                    }

                    if (tokenBuffor.equals("false")) {
                        gatheredTokens.add("FALSE");
                        tokenBuffor = "";
                        continue;
                    }

                    if (tokenBuffor.equals("null")) {
                        gatheredTokens.add("NULL");
                        tokenBuffor = "";
                        continue;
                    }

                    Pattern p = Pattern.compile("^[\\-\\.0-9]+$");
                    Matcher m = p.matcher(tokenBuffor);
                    if (m.matches()) {
                        continue;
                    } else {
                        if (!(tokenBuffor.length() == 1 | tokenBuffor.length() == 0)) {
                            String tmpTokenBuffor;
                            tmpTokenBuffor = tokenBuffor.substring(0, tokenBuffor.length() - 1);
                            Matcher tmpM = p.matcher(tmpTokenBuffor);
                            if (tmpM.matches()) {
                                gatheredTokens.add("NUMER");
                                tokenBuffor = "";
                            }
                        }
                    }
                }

                if (tokenBuffor.length() > lengthFieldLimit) {
                    System.out.println("Too long string or number. Length limit is " + lengthFieldLimit + " signs.");
                }

                if (c.equals(new Character('{'))) {
                    gatheredTokens.add("POCZATEK_OBIEKTU");
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character('}'))) {
                    gatheredTokens.add("KONIEC_OBIEKTU");
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character('['))) {
                    gatheredTokens.add("POCZATEK_TABLICY");
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(']'))) {
                    gatheredTokens.add("KONIEC_TABLICY");
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(':'))) {
                    gatheredTokens.add("SEPARATOR_WARTOSCI");
                    tokenBuffor = "";
                    continue;
                }

                if (c.equals(new Character(','))) {
                    gatheredTokens.add("SEPARATOR_REKORDOW");
                    tokenBuffor = "";
                    continue;
                }
            }

        }
        return gatheredTokens;
    }

    public static Integer getLengthFieldLimit() {
        return lengthFieldLimit;
    }

    public static void setLengthFieldLimit(Integer lengthFieldLimit) {
        Lexer.lengthFieldLimit = lengthFieldLimit;
    }

}
