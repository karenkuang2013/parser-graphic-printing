package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class DeclTree extends AST {

    /**
     *
     */
    public DeclTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitDeclTree(this);
    }

}

