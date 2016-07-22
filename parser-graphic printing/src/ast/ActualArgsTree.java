package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class ActualArgsTree extends AST {

    /**
     *
     */
    public ActualArgsTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitActualArgsTree(this);
    }

}

