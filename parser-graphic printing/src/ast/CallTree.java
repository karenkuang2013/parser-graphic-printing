package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class CallTree extends AST {

    /**
     *
     */
    public CallTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitCallTree(this);
    }

}

