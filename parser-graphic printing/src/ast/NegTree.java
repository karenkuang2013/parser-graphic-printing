package ast;

import lexer.Symbol;
import lexer.Token;
import visitor.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author qihongkuang
 */
public class NegTree extends AST{  
    
    /**
     *
     */
    public NegTree(){
        
    }
    @Override
    public Object accept(ASTVisitor v) {
        return v.visitNegOpTree(this);
    }
    
    
}
