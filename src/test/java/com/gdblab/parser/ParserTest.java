package com.gdblab.parser;

import com.gdblab.parser.impl.*;
import com.gdlab.parser.RPQGrammarLexer;
import com.gdlab.parser.RPQGrammarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private RPQExpression parse(final String rpq) {
        RPQGrammarLexer lexer = new RPQGrammarLexer(CharStreams.fromString(rpq));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RPQGrammarParser parser = new RPQGrammarParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        RPQGrammarListener listener = new RPQGrammarListener();
        walker.walk(listener, parser.query());

        return listener.getRoot();
    }

    @Test
    public void rpqParserTest() {
        String rpq = "x|y";
        RPQExpression exp = parse(rpq);
        assertEquals(new AlternativePathExpression(new LabelExpression("x"), new LabelExpression("y")), exp);
    }

    @Test
    public void simplePrecedenceTest() {
        String rpq = "a.b|c";
        RPQExpression exp = parse(rpq);
        assertEquals(new AlternativePathExpression(new ConcatenationExpression(new LabelExpression("a"),
                new LabelExpression("b")), new LabelExpression("c")), exp);
    }

    @Test
    public void simpleParenthesisTest() {
        String rpq = "a.(b|c)";
        RPQExpression exp = parse(rpq);
        assertEquals(new ConcatenationExpression(new LabelExpression("a"),
                new AlternativePathExpression(new LabelExpression("b"), new LabelExpression("c"))), exp);
    }

    @Test
    public void starPlusAndOptionalTest() {
        String rpq = "a+.b*.c?";
        RPQExpression exp = parse(rpq);
        assertEquals(new ConcatenationExpression(new ConcatenationExpression(new OneOrMoreExpression(new LabelExpression("a")),
                new ZeroOrMoreExpression(new LabelExpression("b"))), new ZeroOrOneExpression(new LabelExpression("c"))),
                exp);
    }

    @Test
    public void alternativeAndStarTest(){
        String rpq = "a.(b|c)*";
        RPQExpression exp = parse(rpq);
        assertEquals(new ConcatenationExpression(new LabelExpression("a"),
                new ZeroOrMoreExpression(new AlternativePathExpression(new LabelExpression("b"), new LabelExpression("c")))),
                exp);
    }

}
