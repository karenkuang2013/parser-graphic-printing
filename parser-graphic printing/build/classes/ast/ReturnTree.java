package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class ReturnTree extends AST {

    /**
     *
     */
    public ReturnTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitReturnTree(this);
    }

}

