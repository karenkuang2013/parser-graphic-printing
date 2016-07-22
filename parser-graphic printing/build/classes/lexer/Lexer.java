package lexer;

import java.io.IOException;

/**
 * The Lexer class is responsible for scanning the source file which is a stream
 * of characters and returning a stream of tokens; each token object will
 * contain the string (or access to the string) that describes the token along
 * with an indication of its location in the source program to be used for error
 * reporting; we are tracking line numbers; white spaces are space, tab,
 * newlines
 */
public class Lexer {

    private boolean atEOF = false;
    private char ch;     // next character to process
    private SourceReader source;

    // positions in line of current token
    private int startPosition, endPosition;

    /**
     *
     * @param sourceFile
     * @throws Exception
     */
    public Lexer(String sourceFile) throws Exception {
        new TokenType();  // init token table
        source = new SourceReader(sourceFile);
        ch = source.read();
    }

    /**
     *
     * @param args
     */
    
    public static void main(String args[]) {
        Token tok;
        String fileName = args[0];
        try {
            Lexer lex = new Lexer(fileName);
            while (true) {
                tok = lex.nextToken();
                String p, str;
                p = String.format("Left: %3d Right: %3d | Token: %-15s",
                        tok.getLeftPosition(),
                        tok.getRightPosition(),
                        TokenType.tokens.get(tok.getKind()));

                if ((tok.getKind() == Tokens.Identifier)
                        || (tok.getKind() == Tokens.INTeger)
                        || (tok.getKind() == Tokens.Float)
                        || (tok.getKind() == Tokens.scientificN)
                        || (tok.getKind() == Tokens.Character)
                        || (tok.getKind() == Tokens.String)) {
                    str = String.format("%-10s", tok.toString());
                } else {
                    str = String.format("%-10s", " ");
                }
                p += str;
                System.out.printf("%s | %-18s | Line: %3d \n", p, tok.getKind(), lex.source.getLineno());
            }
        } catch (Exception e) {
        }
    }

    /**
     * newIdTokens are either ids or reserved words; new id's will be inserted
     * in the symbol table with an indication that they are id's
     *
     * @param id is the String just scanned - it's either an id or reserved word
     * @param startPosition is the column in the source file where the token
     * begins
     * @param endPosition is the column in the source file where the token ends
     * @return the Token; either an id or one for the reserved words
     */
    public Token newIdToken(String id, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(id, Tokens.Identifier));
    }

    /**
     * number tokens are inserted in the symbol table; we don't convert the
     * numeric strings to numbers until we load the bytecodes for interpreting;
     * this ensures that any machine numeric dependencies are deferred until we
     * actually run the program; i.e. the numeric constraints of the hardware
     * used to compile the source program are not used
     *
     * @param number is the int String just scanned
     * @param startPosition is the column in the source file where the int
     * begins
     * @param endPosition is the column in the source file where the int ends
     * @return the int Token
     */
    public Token newNumberToken(String number, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition,
                Symbol.symbol(number, Tokens.INTeger));
    }

    /**
     *
     * @param floatNo
     * @param startPosition
     * @param endPosition
     * @return
     */
    public Token newFloatToken(String floatNo, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(floatNo, Tokens.Float));
    }

    /**
     *
     * @param sciNo
     * @param startPosition
     * @param endPosition
     * @return
     */
    public Token newSciToken(String sciNo, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(sciNo, Tokens.scientificN));
    }

    /**
     *
     * @param ch
     * @param startPosition
     * @param endPosition
     * @return
     */
    public Token newCharToken(String ch, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(ch, Tokens.Character));
    }

    /**
     *
     * @param st
     * @param startPosition
     * @param endPosition
     * @return
     */
    public Token newStringToken(String st, int startPosition, int endPosition) {
        return new Token(startPosition, endPosition, Symbol.symbol(st, Tokens.String));
    }

    /**
     * build the token for operators (+ -) or separators (parens, braces) filter
     * out comments which begin with two slashes
     *
     * @param s is the String representing the token
     * @param startPosition is the column in the source file where the token
     * begins
     * @param endPosition is the column in the source file where the token ends
     * @return the Token just found
     */
    public Token makeToken(String s, int startPosition, int endPosition) {
        if (s.equals("//")) {  // filter comment
            try {
                int oldLine = source.getLineno();
                do {
                    ch = source.read();
                } while (oldLine == source.getLineno());
            } catch (Exception e) {
                atEOF = true;
            }
            return nextToken();
        }
        Symbol sym = Symbol.symbol(s, Tokens.BogusToken); // be sure it's a valid token
        if (sym == null) {
            System.out.println("******** illegal character: " + s);
            atEOF = true;
            return nextToken();
        }
        return new Token(startPosition, endPosition, sym);
    }

    /**
     * @return the next Token found in the source file
     */
    public Token nextToken() { // ch is always the next char to process
        if (atEOF) {
            if (source != null) {
                source.close();
                source = null;
            }
            return null;
        }
        try {
            while (Character.isWhitespace(ch)) {  // scan past whitespace
                ch = source.read();
            }
        } catch (Exception e) {
            atEOF = true;
            return nextToken();
        }

        startPosition = source.getPosition(); //start from the last position process
        endPosition = startPosition - 1; //return the finished last position

        if (Character.isJavaIdentifierStart(ch)) {
            // return tokens for ids and reserved words

            String id = "";
            try {
                do {
                    endPosition++;
                    id += ch;
                    ch = source.read();
                } while (Character.isJavaIdentifierPart(ch));
            } catch (Exception e) {
                atEOF = true;
            }
            return newIdToken(id, startPosition, endPosition);

        }

        int numOfDot = 0;
        int numOfE = 0;

        if (Character.isDigit(ch)) {
            // return number tokens
            String number = "";
            String tempNum;

            try {

                do {
                    number += ch;
                    ch = source.read();
                    endPosition++;
                    char temp = ch;

                    if (ch == '.' && numOfDot == 0) {
                        endPosition++;
                        numOfDot = 1;
                        number += ch;
                        ch = source.read();
                    }

                    if (ch == 'E' || ch == 'e') {

                        if (number.charAt(0) != '0') {
                            if (number.length() == 1 || number.charAt(1) == '.') {
                                endPosition++;
                                ch = source.read();
                                char tempChar2 = source.read();
                                source.readBack();
                                
                                if (Character.isDigit(tempChar2) && (ch == '+' || ch == '-')) {
                                    number += temp;
                                    numOfE = 1;
                                } else if (Character.isDigit(ch)) {
                                    number += temp;
                                    numOfE = 1;
                                } 
                                else if (Character.isJavaIdentifierPart(tempChar2)) {
                                    source.readBack();
                                    source.readBack();                             
                                    endPosition = endPosition - 2;
                                    ch = source.read();
                                }
                            }
                        }
                    }

                } while (Character.isDigit(ch) || ch == '+' || ch == '-');

            } catch (Exception e) {
                atEOF = true;
            }
            if (numOfE == 1) {
                return newSciToken(number, startPosition, endPosition);
            } else if (numOfDot != 0) {
                return newFloatToken(number, startPosition, endPosition);
            } else {
                return newNumberToken(number, startPosition, endPosition);
            }
        }

        //recognize float with format: d*.d+
        int check = 0;
        if (ch == '.') {
            check = 0;
            String floatNum = "";
            try {
                do {
                    floatNum += ch;
                    ch = source.read();
                    endPosition++;
                    if (check == 0 && (Character.isJavaIdentifierStart(ch) || ch == '.' || Character.isWhitespace(ch))) {
                        atEOF = true;
                        System.out.println("Line: " + source.getLineno() + " For a float number, the dot must be followed by a digit.");
                        return nextToken();
                    } else {
                        check = 1;
                    }
                } while (Character.isDigit(ch));

            } catch (Exception ex) {
                atEOF = true;
            }
            return newFloatToken(floatNum, startPosition, endPosition);

        }
        //recognize a new token char<char>
        if (ch == '\'') {
            check = 0;
            String newCh = "";
            newCh += ch;
            endPosition++;
            try {

                do {
                    ch = source.read();
                    if(ch == '\''){
                        System.out.println("Line: "+source.getLineno() + " \'\' is not a valid character.");
                        System.out.print("Charater Format: " + "\'" + "single character/only one whitespace" + "\'" + "(e.g.'a' or ' ')");
                        return nextToken();
                    }
                    if (check == 0) {
                        endPosition++;
                        newCh += ch;
                        ch = source.read();
                        check = 1;
                        //newCh should only contain one character
                    } else if (ch != '\'' && check != 0) {
                        atEOF = true;
                        System.out.println("Error happens in line: " + source.getLineno());
                        System.out.print("Charater Format: " + "\'" + "single character/only one whitespace" + "\'" + "(e.g.'a' or ' ')");
                        ch = source.read();
                        return nextToken();
                    }

                } while (ch != '\'');
                if (ch == '\'' && check != 0) {
                    endPosition++;
                    newCh += ch;
                    ch = source.read();
                }

            } catch (Exception ex) {
                atEOF = true;
            }
            return newCharToken(newCh, startPosition, endPosition);
        }

        //recognize a new token String <string>
        if (ch == '\"') {
            String str;

            try {
                str = source.getThisString();
                //include the left double quote
                int nextDoubleQuote = str.indexOf('\"', 1);

                if (nextDoubleQuote != -1) {
                    
                    for (int i = 0; i <= nextDoubleQuote; i++) {
                        str += ch;
                        ch = source.read();
                    }
                    endPosition = source.getPosition() - 1;
                    return newStringToken(str, startPosition, endPosition);
                } else {
                    throw new NumberFormatException(" This String is missing a quote.  " + "Line: " + source.getLineno());
                }
                
                

            } catch (IOException ex) {
                atEOF = true;
                System.out.println("IOException in line: " + source.getLineno() + ex.getMessage());
                System.exit(1);
            } catch (NumberFormatException ex) {
                atEOF = true;
                System.out.println("NumberFormatException in line: " + source.getLineno() + ex.getMessage());
                System.exit(1);
            }
        }

        // At this point the only tokens to check for are one or two
        // characters; we must also check for comments that begin with
        // 2 slashes
        String charOld = "" + ch;
        String op = charOld;
        Symbol sym;
        try {
            endPosition++;
            ch = source.read();
            op += ch;
            // check if valid 2 char operator; if it's not in the symbol
            // table then don't insert it since we really have a one char
            // token
            sym = Symbol.symbol(op, Tokens.BogusToken);
            if (sym == null) {  // it must be a one char token
                return makeToken(charOld, startPosition, endPosition);
            }
            endPosition++;
            ch = source.read();
            return makeToken(op, startPosition, endPosition);
        } catch (Exception e) {
        }
        atEOF = true;
        if (startPosition == endPosition) {
            op = charOld;
        }
        return makeToken(op, startPosition, endPosition);
    }
}
