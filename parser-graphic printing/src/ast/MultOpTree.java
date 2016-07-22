package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class MultOpTree extends AST {
    private Symbol symbol;

/**
 *  @param tok contains the Symbol that indicates the specific multiplying operator
*/
    public MultOpTree(Token tok) {
        this.symbol = tok.getSymbol();
    }

    public Object accept(ASTVisitor v) {
        return v.visitMultOpTree(this);
    }

    /**
     *
     * @return
     */
    public Symbol getSymbol() {
        return symbol;
    }

}
