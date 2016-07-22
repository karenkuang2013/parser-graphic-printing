package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class AddOpTree extends AST {
    private Symbol symbol;

    /**
     *
     * @param tok
     */
    public AddOpTree(Token tok) {
        this.symbol = tok.getSymbol();
    }

    public Object accept(ASTVisitor v) {
        return v.visitAddOpTree(this);
    }

    /**
     *
     * @return
     */
    public Symbol getSymbol() {
        return symbol;
    }

}
