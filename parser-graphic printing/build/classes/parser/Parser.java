package parser;

import java.util.*;
import lexer.*;
import ast.*;

/**
 *
 * @author qihongkuang
 */
public class Parser {
    private Token currentToken;
    private Lexer lex;
    private EnumSet<Tokens> relationalOps = 
    	EnumSet.of(Tokens.Equal,Tokens.NotEqual,Tokens.Less,Tokens.LessEqual);
    private EnumSet<Tokens> addingOps = 
    	EnumSet.of(Tokens.Plus,Tokens.Minus,Tokens.Or);    
    private EnumSet<Tokens> multiplyingOps = 
    	EnumSet.of(Tokens.Multiply,Tokens.Divide,Tokens.And);
    private EnumSet<Tokens> powerOps = EnumSet.of(Tokens.Power);
    private EnumSet<Tokens> notNegOps = EnumSet.of(Tokens.Not,Tokens.Minus);
    
/**
 *  Construct a new Parser; 
 *  @param sourceProgram - source file name
 *  @exception Exception - thrown for any problems at startup (e.g. I/O)
*/
    public Parser(String sourceProgram) throws Exception {
        try {
            lex = new Lexer(sourceProgram);
            scan();
        }
         catch (Exception e) {
            System.out.println("********exception*******"+e.toString());
            throw e;
         };
    }
    
    /**
     *
     * @return
     */
    public Lexer getLex() { return lex; }
    
/**
 *  Execute the parse command
 *  @return the AST for the source program
 *  @exception Exception - pass on any type of exception raised
*/
    public AST execute() throws Exception {
        try {
            return rProgram();
        }catch (SyntaxError e) {
            e.print();
            throw e;
            }
    }
    
    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rProgram() throws SyntaxError {
        // note that rProgram actually returns a ProgramTree; we use the 
        // principle of substitutability to indicate it returns an AST
       AST t = new ProgramTree();
        expect(Tokens.Program);
        t.addKid(rBlock());
        return t;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rBlock() throws SyntaxError {

        expect(Tokens.LeftBrace);

        AST t = new BlockTree();

        while (startingDecl()) {  // get decls

            t.addKid(rDecl());
        }

        while (startingStatement()) {  // get statements

            t.addKid(rStatement());
        }

        expect(Tokens.RightBrace);

        return t;
    }

    boolean startingDecl() {

        if (isNextTok(Tokens.Int) || isNextTok(Tokens.BOOLean) || isNextTok(Tokens.Float)
                || isNextTok(Tokens.Character) || isNextTok(Tokens.String)) {

            return true;

        }

        return false;

    }

    boolean startingStatement() {

        if (isNextTok(Tokens.If) || isNextTok(Tokens.While) || isNextTok(Tokens.Return)
                || isNextTok(Tokens.LeftBrace) || isNextTok(Tokens.Identifier)
                || isNextTok(Tokens.Do) || isNextTok(Tokens.For)) {

            return true;
        }
        return false;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rDecl() throws SyntaxError {
        AST t,t1;
        t = rType();
        t1 = rName();
        if (isNextTok(Tokens.LeftParen)) { // function
            t = (new FunctionDeclTree()).addKid(t).addKid(t1);
            t.addKid(rFunHead());
            t.addKid(rBlock());
            return t;
        }
        t = (new DeclTree()).addKid(t).addKid(t1);
        return t;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rType() throws SyntaxError {
        AST t;
        if (isNextTok(Tokens.Int)) {
            t = new IntTypeTree();
            scan();
            
        } else if(isNextTok(Tokens.Float)){
            t = new FloatType();
            scan();
            
        }else if(isNextTok(Tokens.Character)){
            t = new charTypeTree();
            scan();
            
        }else if(isNextTok(Tokens.String)){
            t = new StringTypeTree();
            scan();
            
        }
        else {
            expect(Tokens.BOOLean);
            t = new BoolTypeTree();
            
        }
        return t;
        
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rFunHead() throws SyntaxError {
        AST t = new FormalsTree();
        expect(Tokens.LeftParen);
        if (!isNextTok(Tokens.RightParen)) {
            do {
                t.addKid(rDecl());
                if (isNextTok(Tokens.Comma)) {
                    scan();
                } else {
                    break;
                }
            } while (true);
        }
        expect(Tokens.RightParen);
        return t;
    }
    
    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rEhead() throws SyntaxError{
        AST t = new EheadTree();
        expect(Tokens.LeftParen);
        if(!isNextTok(Tokens.semiColon)){
            t.addKid(rAssign());
        }
        expect(Tokens.semiColon);
        if(!isNextTok(Tokens.semiColon)){
            t.addKid(rExpr());
        }
        expect(Tokens.semiColon);
        if(!isNextTok(Tokens.semiColon)){
            t.addKid(rAssign());
        }
        expect(Tokens.RightParen);
        return t;
    }
    
    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rAssign() throws SyntaxError{
        AST t = new AssignTree();
        t.addKid(rName());
        //AST kid = rName();
        //AST t = new AssignTree().addKid(kid);
        expect(Tokens.Assign);
        t.addKid(rExpr());
        return t;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rStatement() throws SyntaxError {
        AST t;
        if (isNextTok(Tokens.If)) {
            scan();
            t = new IfTree();
            t.addKid(rExpr());
            expect(Tokens.Then);
            t.addKid(rBlock());
            
            if (isNextTok(Tokens.Else)) {
                scan();
                t.addKid(rBlock());
                
            } 
            
            return t;
            
        }
        if (isNextTok(Tokens.While)) {
            scan();
            t = new WhileTree();
            t.addKid(rExpr());
            t.addKid(rBlock());
            return t;
        }
        if (isNextTok(Tokens.Return)) {
            scan();
            t = new ReturnTree();
            t.addKid(rExpr());
            return t;
        }
        if(isNextTok(Tokens.Do)){
            scan();
            t = new DoTree();
            t.addKid(rBlock());
            expect(Tokens.While);
            t.addKid(rExpr());
            return t;
        }
        if(isNextTok(Tokens.For)){
            scan();
            t = new ForTree();
            t.addKid(rEhead());
            t.addKid(rBlock());
            return t;
        }
        
        if (isNextTok(Tokens.LeftBrace)) {
            return rBlock();
        }
        t = rAssign();
        /*
        t = rName();
        t = (new AssignTree()).addKid(t);
        expect(Tokens.Assign);
        t.addKid(rExpr());
        */
        return t;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rExpr() throws SyntaxError {
        AST t, kid = rSimpleExpr();
        t = getRelationTree();
        if (t == null) {
            return kid;
        }
        t.addKid(kid);
        t.addKid(rSimpleExpr());
        return t;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rSimpleExpr() throws SyntaxError {
        AST t, kid = rTerm();
        while ( (t = getAddOperTree()) != null) {
            t.addKid(kid);
            t.addKid(rTerm());
            kid = t;
        }
        return kid;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rTerm() throws SyntaxError {
        AST t, kid = rFactor();
        while ( (t = getMultOperTree()) != null) {
            t.addKid(kid);
            t.addKid(rFactor());
            kid = t;
        }
        return kid;
    }
    
    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rFactor() throws SyntaxError{
        AST t, kid = rFF();
        t = getPowerTree();
        if(t == null){
            return kid;
        }
        t.addKid(kid);
        t.addKid(rFactor());
        return t;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rFF() throws SyntaxError {
        AST t;
        if (isNextTok(Tokens.LeftParen)) { // -> (e)
            scan();
            t = rExpr();
            expect(Tokens.RightParen);
            return t;
        }
        if (isNextTok(Tokens.INTeger)) {  //  -> <int>
            t = new IntTree(currentToken);
            scan();
            return t;
        }
        if(isNextTok(Tokens.Float)){
            t = new FloatTree(currentToken);
            scan();
            return t;
        }
        if(isNextTok(Tokens.Character)){
            t = new CharTree(currentToken);
            scan();
            return t;
        }
        if(isNextTok(Tokens.String)){
            t = new StringTree(currentToken);
            scan();
            return t;
        }
        if(isNextTok(Tokens.scientificN)){
            t = new SciNTree(currentToken);
            scan();
            return t;
        }
        if(isNextTok(Tokens.Not)){
            t = new NotTree();
            scan();
            t.addKid(rExpr());
            return t;
        }
        
        if(isNextTok(Tokens.Minus)){
            t = new NegTree();
            scan();
            t.addKid(rExpr());
            return t;
        } 
        
        t = rName();
        if (!isNextTok(Tokens.LeftParen)) {  //  -> name
            return t;
        }
        scan();     // -> name '(' (e list ',')? ) ==> call
        t = (new CallTree()).addKid(t);    
        if (!isNextTok(Tokens.RightParen)) {
            do {
                t.addKid(rExpr());
                if (isNextTok(Tokens.Comma)) {
                    scan();
                } else {
                    break;
                }
            } while (true);
        }
        expect(Tokens.RightParen);
        return t;
    }

    /**
     *
     * @return
     * @throws SyntaxError
     */
    public AST rName() throws SyntaxError {
        AST t;
        if (isNextTok(Tokens.Identifier)) {
            t = new IdTree(currentToken);
            scan();
            return t;
        }
        throw new SyntaxError(currentToken,Tokens.Identifier);
    }
    
    AST getRelationTree() {  // build tree with current token's relation
    	Tokens kind = currentToken.getKind();
    	if (relationalOps.contains(kind)) {
    		AST t = new RelOpTree(currentToken);
    		scan();
    		return t;
    	} else {
    		return null;
    	}
     }
    
    private AST getAddOperTree() {
    	Tokens kind = currentToken.getKind();
    	if (addingOps.contains(kind)) {
    		AST t = new AddOpTree(currentToken);
    		scan();
    		return t;
    	} else {
    		return null;
    	}
    }

    private AST getMultOperTree() {
    	Tokens kind = currentToken.getKind();
       	if (multiplyingOps.contains(kind)) {
    		AST t = new MultOpTree(currentToken);
    		scan();
    		return t;
    	} else {
    		return null;
    	}
    }
    
    private AST getPowerTree(){
        Tokens kind = currentToken.getKind();
        if(powerOps.contains(kind)){
            AST t = new powerTree(currentToken);
            scan();
            return t;
        }else{
            return null;
        }
    }
    
    private boolean isNextTok(Tokens kind) {
        if ((currentToken == null) || (currentToken.getKind() != kind)) {
            return false;
        }
        return true;
    }

    private void expect(Tokens kind) throws SyntaxError {
        if (isNextTok(kind)) {
            scan();
            return;
        }
        throw new SyntaxError(currentToken,kind);
    }

    private void scan() {
        currentToken = lex.nextToken();
        if (currentToken != null) {
            currentToken.print();   // debug printout
        }
        return;
    }

    
}

class SyntaxError extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Token tokenFound;
    private Tokens kindExpected;
    
/**
 *  record the syntax error just encountered
 *  @param tokenFound is the token just found by the parser
 *  @param kindExpected is the token we expected to find based on 
 *  the current context
*/
    public SyntaxError(Token tokenFound, Tokens kindExpected) {
        this.tokenFound = tokenFound;
        this.kindExpected = kindExpected;
    }
    
    void print() {
        System.out.println("Expected: " + 
           kindExpected);
        return;
    }
}
