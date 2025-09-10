package com.gdblab.algebra.parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import com.gdblab.algebra.parser.error.ErrorDetails;
import com.gdblab.algebra.parser.error.SyntaxErrorException;

public class RPQErrorListener extends BaseErrorListener{
    
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        if (msg.length() > 0) {
            msg = msg.substring(0, 1).toUpperCase() + msg.substring(1) + ".";
        } else {
            msg = "Syntax error. No message available.";
        }
        ErrorDetails details = new ErrorDetails(line, charPositionInLine, msg);
        throw new SyntaxErrorException(details);
    }
}
