package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class BlockTree extends AST {

    /**
     *
     */
    public BlockTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitBlockTree(this);
    }

}

