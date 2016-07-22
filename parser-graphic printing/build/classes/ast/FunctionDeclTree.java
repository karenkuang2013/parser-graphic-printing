package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class FunctionDeclTree extends AST {

    /**
     *
     */
    public FunctionDeclTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitFunctionDeclTree(this);
    }

}

