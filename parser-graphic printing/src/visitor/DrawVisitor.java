/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visitor;

import ast.AST;
import ast.AddOpTree;
import ast.CharTree;
import ast.FloatTree;
//import ast.FloatTree;
import ast.IdTree;
import ast.IntTree;
import ast.MultOpTree;
import ast.RelOpTree;
import ast.SciNTree;
import ast.StringTree;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Qihong Kuang
 */
public class DrawVisitor extends ASTVisitor {

    private final int nodew = 80;
    private final int nodeh = 25;
    private final int vertSep = 40;
    private final int horizSep = 10;

    private int width;
    private int height;

    private int[] nCount;
    private int[] progress;
    private int depth = 0;
    private BufferedImage bimg;
    private Graphics2D g2;

    /**
     *
     * @param nCount
     */
    public DrawVisitor(int[] nCount) {
        this.nCount = nCount;
        this.progress = new int[this.nCount.length];

        width = max(nCount) * (nodew + horizSep);
        height = nCount.length * (nodeh + vertSep);

        g2 = createGraphics2D(width+400, height);
    }

    private int max(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     *
     * @param s
     * @param t
     */
    public void draw(String s, AST t) {
        int hstep = 45;

        int vstep = 50;

        int x = t.getxLoc() * hstep;

        int y = depth * vstep;

        g2.setColor(Color.black);
        g2.drawOval(x, y, nodew, nodeh);
        g2.setColor(Color.BLACK);
        g2.drawString(s, x + 10, y + 2 * nodeh / 3);

        int startx = x + nodew / 2;
        int starty = y + nodeh;
        int endx;
        int endy;

        this.g2.setColor(Color.black);
        for (int i = 1; i <= t.kidCount(); i++) {
            endx = t.getKid(i).getxLoc() * hstep + 40;
            endy = (depth + 1) * vstep;
            g2.drawLine(startx, starty, endx, endy);
        }
        progress[depth]++;
        depth++;
        visitKids(t);
        depth--;
    }

    private Graphics2D createGraphics2D(int w, int h) {

        Graphics2D g2;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        }
        
        
        g2 = bimg.createGraphics();
        
        g2.setBackground(Color.WHITE);
       
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        
       
        
        return g2;
    }

    /**
     *
     * @return
     */
    public BufferedImage getImage() {
        
        return bimg;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitProgramTree(AST t) {
        draw("Program", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitBlockTree(AST t) {
        draw("Block", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFunctionDeclTree(AST t) {
        draw("FunctionDecl", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitCallTree(AST t) {
        draw("Call", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitDeclTree(AST t) {
        draw("Decl", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIntTypeTree(AST t) {
        draw("IntType", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitBoolTypeTree(AST t) {
        draw("BoolType", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFormalsTree(AST t) {
        draw("Formals", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitActualArgsTree(AST t) {
        draw("ActualArgs", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIfTree(AST t) {
        draw("If", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitWhileTree(AST t) {
        draw("While", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitReturnTree(AST t) {
        draw("Return", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitAssignTree(AST t) {
        draw("Assign", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIntTree(AST t) {
        draw("Int: " + ((IntTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIdTree(AST t) {
        draw("Id: " + ((IdTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitRelOpTree(AST t) {
        draw("RelOp: " + ((RelOpTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitAddOpTree(AST t) {
        draw("AddOp: " + ((AddOpTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitMultOpTree(AST t) {
        draw("MultOp: " + ((MultOpTree) t).getSymbol().toString(), t);
        return null;
    }

    //new methods here
    //public Object visitFloatTree(AST t) { draw("Float: "+((FloatTree)t).getSymbol().toString(),t);  return null; }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFloatTypeTree(AST t) {
        draw("FloatType", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitRepeatTree(AST t) {
        draw("Repeat", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitCharTypeTree(AST t) {
        draw("CharType", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitFloatTree(AST t) {
        draw("Float: " + ((FloatTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitCharTree(AST t) {
        draw("Char: " + ((CharTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitStringTree(AST t) {
        draw("String: " + ((StringTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitSciNTree(AST t) {
        draw("ScientificN: " + ((SciNTree) t).getSymbol().toString(), t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitDoTree(AST t) {
        draw("do: ", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitForTree(AST t) {
        draw("for: ", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitEheadTree(AST t) {
        draw("Ehead: ", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitStringTypeTree(AST t) {
        draw("StringType: ", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitNegOpTree(AST t) {
        draw("Negative: ", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitNotTree(AST t) {
        draw("Not: ", t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitPowTree(AST t) {
        draw("power: ", t);
        return null;
    }

}
