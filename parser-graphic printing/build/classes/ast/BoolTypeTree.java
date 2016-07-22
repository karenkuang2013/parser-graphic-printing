package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class BoolTypeTree extends AST {

    /**
     *
     */
    public BoolTypeTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitBoolTypeTree(this);
    }

}

