package ast;

import visitor.*;

/**
 *
 * @author qihongkuang
 */
public class ProgramTree extends AST {

    /**
     *
     */
    public ProgramTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitProgramTree(this);
    }

}

