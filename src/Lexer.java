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

    public LinkedList<Token> start(BufferedReader bReader) throws IOException {
        LinkedList<Token> gatheredTokens = new LinkedList<Token>();
        String lineConstant;
        String tokenBuffor = "";
        Boolean chainFlag = false;
        Integer lineCounter = 0;
        Integer tmpStringLocation = null;
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
                        if (!chainFlag && !tokenBuffor.isEmpty()) {
                            checkWrongLexem(tokenBuffor, lineCounter, i);
                        }
                        Token t = new Token(TokenEnum.ZNAK_NAPISU, Main.tokens.get(TokenEnum.ZNAK_NAPISU));
                        chainFlag = true;
                        t.row = lineCounter;
                        t.column = i + 1;
                        tmpStringLocation = i + 2;
                        gatheredTokens.add(t);
                        continue;
                    } else {
                        if (!tokenBuffor.equals("")) {
                            Pattern p = Pattern.compile(Main.tokens.get(TokenEnum.ZNAKI));
                            Matcher m = p.matcher(tokenBuffor);
                            if (m.matches()) {
                                Token t = new Token(TokenEnum.ZNAKI, Main.tokens.get(TokenEnum.ZNAKI));
                                t.row = lineCounter;
                                t.column = tmpStringLocation;
                                tmpStringLocation = null;
                                gatheredTokens.add(t);
                                tokenBuffor = "";
                            } else {
                                System.out.println("NAPIS Error token is inappropriate character: " + c + " in line " + lineCounter + " and column " + (i + 1) + ".");
                                tokenBuffor = "";
                            }
                        }
                        Token t = new Token(TokenEnum.ZNAK_NAPISU, Main.tokens.get(TokenEnum.ZNAK_NAPISU));
                        t.row = lineCounter;
                        t.column = i + 1;
                        gatheredTokens.add(t);
                        chainFlag = false;
                        continue;
                    }
                }

                if (chainFlag) {
                    tokenBuffor += c;
                }

                if (!chainFlag) {
                    Pattern p = Pattern.compile("^[\\-\\.0-9]+$");
                    Matcher m = p.matcher(c.toString());
                    if (m.matches()) {
                        tokenBuffor += c;
                        if (i.equals(charsOfLine.length - 1)) {
                            Token t = new Token(TokenEnum.NUMER, Main.tokens.get(TokenEnum.NUMER));
                            t.row = lineCounter;
                            t.column = i - (tokenBuffor.length() - 1);
                            gatheredTokens.add(t);
                            tokenBuffor = "";
                            continue;
                        }
                    } else if (!(tokenBuffor.length() == 0)) {
                        Matcher tmpM = p.matcher(tokenBuffor);
                        if (tmpM.matches()) {
                            Token t = new Token(TokenEnum.NUMER, Main.tokens.get(TokenEnum.NUMER));
                            t.row = lineCounter;
                            t.column = i - (tokenBuffor.length() - 1);
                            gatheredTokens.add(t);
                            tokenBuffor = "";
                        }
                    }
                }

                if (tokenBuffor.length() > lengthFieldLimit) {
                    System.out.println("Too long string or number. Length limit is " + lengthFieldLimit + " signs.");
                }

                Token notChainSingleToken = null;
                if (c.equals(new Character('{'))) {
                    if (!chainFlag && !tokenBuffor.isEmpty()) {
                        checkWrongLexem(tokenBuffor, lineCounter, i);
                    }
                    notChainSingleToken = new Token(TokenEnum.POCZATEK_OBIEKTU, Main.tokens.get(TokenEnum.POCZATEK_OBIEKTU));
                }
                if (c.equals(new Character('}'))) {
                    if (!chainFlag && !tokenBuffor.isEmpty()) {
                        checkWrongLexem(tokenBuffor, lineCounter, i);
                    }
                    notChainSingleToken = new Token(TokenEnum.KONIEC_OBIEKTU, Main.tokens.get(TokenEnum.KONIEC_OBIEKTU));
                }
                if (c.equals(new Character('['))) {
                    if (!chainFlag && !tokenBuffor.isEmpty()) {
                        checkWrongLexem(tokenBuffor, lineCounter, i);
                    }
                    notChainSingleToken = new Token(TokenEnum.POCZATEK_TABLICY, Main.tokens.get(TokenEnum.POCZATEK_TABLICY));
                }
                if (c.equals(new Character(']'))) {
                    if (!chainFlag && !tokenBuffor.isEmpty()) {
                        checkWrongLexem(tokenBuffor, lineCounter, i);
                    }
                    notChainSingleToken = new Token(TokenEnum.KONIEC_TABLICY, Main.tokens.get(TokenEnum.KONIEC_TABLICY));
                }
                if (c.equals(new Character(':'))) {
                    if (!chainFlag && !tokenBuffor.isEmpty()) {
                        checkWrongLexem(tokenBuffor, lineCounter, i);
                    }
                    notChainSingleToken = new Token(TokenEnum.SEPARATOR_WARTOSCI, Main.tokens.get(TokenEnum.SEPARATOR_WARTOSCI));
                }
                if (c.equals(new Character(','))) {
                    if (!chainFlag && !tokenBuffor.isEmpty()) {
                        checkWrongLexem(tokenBuffor, lineCounter, i);
                    }
                    notChainSingleToken = new Token(TokenEnum.SEPARATOR_REKORDOW, Main.tokens.get(TokenEnum.SEPARATOR_REKORDOW));
                }
                if (notChainSingleToken != null) {
                    notChainSingleToken.row = lineCounter;
                    notChainSingleToken.column = i + 1;
                    gatheredTokens.add(notChainSingleToken);
                    tokenBuffor = "";
                    continue;
                }

                if (!chainFlag) {
                    tokenBuffor += c;

                    Token t = null;
                    if (tokenBuffor.equals("true")) {
                        t = new Token(TokenEnum.TRUE, Main.tokens.get(TokenEnum.TRUE));
                        t.column = i - 2;
                    }
                    if (tokenBuffor.equals("false")) {
                        t = new Token(TokenEnum.FALSE, Main.tokens.get(TokenEnum.FALSE));
                        t.column = i - 3;
                    }
                    if (tokenBuffor.equals("null")) {
                        t = new Token(TokenEnum.NULL, Main.tokens.get(TokenEnum.NULL));
                        t.column = i - 2;
                    }
                    if (t != null) {
                        t.row = lineCounter;
                        gatheredTokens.add(t);
                        tokenBuffor = "";
                        continue;
                    }
                }

                if (i.equals(charsOfLine.length - 1)) {
                    if (!chainFlag && !tokenBuffor.isEmpty()) {
                        checkWrongLexem(tokenBuffor, lineCounter, i);
                    }
                }
            }
        }
        return gatheredTokens;
    }

    private void checkWrongLexem(String tokenBuffor, Integer lineCounter, Integer i) {
        System.out.println("Wrong lexem: " + tokenBuffor + " in line: " + lineCounter + " and column: " + (i + 1));
        System.exit(0);
    }
}