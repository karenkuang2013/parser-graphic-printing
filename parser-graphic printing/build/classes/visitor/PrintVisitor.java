package visitor;

import ast.*;

/**
 *  PrintVisitor is used to visit an AST and print it using
 *  appropriate indentation:<br>
 *  <pre>
 *  1. root
 *  2.   Kid1
 *  3.   Kid2
 *  4.     Kid21
 *  5.     Kid22
 *  6.     Kid23
 *  7.   Kid3
 *  </pre>
*/
public class PrintVisitor extends ASTVisitor {
    private int indent = 0;

    private void printSpaces(int num) {
        String s = "";
        for (int i = 0; i < num; i++) {
            s += ' ';
        }
        System.out.print(s);
    }

/**
 *  Print the tree
 *  @param s is the String for the root of t
 *  @param t is the tree to print - print the information
 *  in the node at the root (e.g. decoration) and its kids
 *  indented appropriately
*/
    public void print(String s,AST t) {
        // assume less than 1000 nodes; no problem for csc 413
        int num = t.getNodeNum();
        AST decoration = t.getDecoration();
        int decNum = (decoration == null)? -1 : decoration.getNodeNum();
        String spaces = "";
        if (num < 100) spaces += " ";
        if (num < 10) spaces += " ";
        System.out.print(num + ":" + spaces);
        printSpaces(indent);
        if (decNum != -1) {
            s += "           Dec: " + decNum;
        }
        String lab = t.getLabel();
        if ( lab.length() > 0 ) {
            s += "  Label: "+t.getLabel();
        }
        if (t.getClass() == IdTree.class) {
            int offset = ((IdTree)t).getFrameOffset();
            if (offset >= 0) {
                s += "  Addr: " + offset;
            }
        }
        System.out.println(s);
        indent += 2;
        visitKids(t);
        indent -= 2;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitProgramTree(AST t) { print("Program",t);  return null; }
    
    /**
     *
     * @param t
     * @return
     */
    public Object visitBlockTree(AST t) { print("Block",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFunctionDeclTree(AST t) { print("FunctionDecl",t);  return null; }
    
    /**
     *
     * @param t
     * @return
     */
    public Object visitCallTree(AST t) { print("Call",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitDeclTree(AST t) { print("Decl",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIntTypeTree(AST t) { print("IntType",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitBoolTypeTree(AST t) { print("BoolType",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFormalsTree(AST t) { print("Formals",t);  return null; }
  
    /**
     *
     * @param t
     * @return
     */
    public Object visitActualArgsTree(AST t) { print("ActualArgs",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIfTree(AST t) { print("If",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitWhileTree(AST t) { print("While",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitReturnTree(AST t) { print("Return",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitAssignTree(AST t) { print("Assign",t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIntTree(AST t) { print("Int: "+((IntTree)t).getSymbol().toString(),t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIdTree(AST t) { print("Id: "+((IdTree)t).getSymbol().toString(),t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitRelOpTree(AST t) { print("RelOp: "+((RelOpTree)t).getSymbol().toString(),t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitAddOpTree(AST t) { print("AddOp: "+((AddOpTree)t).getSymbol().toString(),t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitMultOpTree(AST t) { print("MultOp: "+((MultOpTree)t).getSymbol().toString(),t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitFloatTypeTree(AST t) {
        print("FloatType",t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitCharTypeTree(AST t) {
        print("CharType",t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitStringTypeTree(AST t) {
        print("StringType",t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitFloatTree(AST t) {
        print("float: "+((FloatTree)t).getSymbol().toString(),t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitCharTree(AST t) {
        print("char: "+((CharTree)t).getSymbol().toString(),t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitStringTree(AST t) {
        print("String: "+((StringTree)t).getSymbol().toString(),t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitSciNTree(AST t) {
        print("scientificN: "+((SciNTree)t).getSymbol().toString(),t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitDoTree(AST t) {
        print("do ", t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitNotTree(AST t) {
        print("invert ",t);  
        return null;
    }
    
    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitNegOpTree(AST t) {
        print("Negative ",t);  
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitForTree(AST t) {
        print("for ",t);  
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitEheadTree(AST t) {
        print("Ehead ",t);  
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitPowTree(AST t) {
        print("power: "+((powerTree)t).getSymbol().toString(),t);  
        return null;
    }
    
    
    
}