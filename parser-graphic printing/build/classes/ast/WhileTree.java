package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class WhileTree extends AST {

    /**
     *
     */
    public WhileTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitWhileTree(this);
    }
}

