/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visitor;

import ast.AST;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Qihong Kuang
 */
public class CountVisitor extends ASTVisitor {

    private int[] nCount = new int[100];
    private int depth = 0;
    private int maxDepth = 0;
    private int[] nNextAvailX = new int[100];
    private boolean shift = false;
    private int shiftStep;

    private void count(AST t) {
        if (!shift) {
            nCount[depth] ++;
            if (depth > maxDepth) {
                maxDepth = depth;
            }
            if (t.kidCount() == 0) {
                t.setxLoc(nNextAvailX[depth]);
                nNextAvailX[depth] = (t.getxLoc() + 2);
                return;
            }
            depth ++;
            visitKids(t);
            depth --;
            int leftChildX = t.getKid(1).getxLoc();
            int rightChildX = t.getKid(t.kidCount()).getxLoc();
            int tempTX = (leftChildX + rightChildX) / 2;
            if (tempTX >= nNextAvailX[depth]) {
                t.setxLoc(tempTX);
            } else {
                shiftStep = (nNextAvailX[depth] - tempTX);
                shift = true;
                depth += 1;
                visitKids(t);
                depth -= 1;
                shift = false;

                t.setxLoc(nNextAvailX[depth]);
            }
            nNextAvailX[depth] = (t.getxLoc() + 2);
            return;
        }
        int originalX = t.getxLoc();
        originalX += shiftStep;
        t.setxLoc(originalX);
        nNextAvailX[depth] = (t.getxLoc() + 2);
        depth ++;
        visitKids(t);
        depth --;
    }

    /**
     *
     * @return
     */
    public int[] getCount() {
        int[] count = new int[maxDepth + 1];

        for (int i = 0; i <= maxDepth; i++) {
            count[i] = nCount[i];
        }

        return count;
    }

    /**
     *
     * @return
     */
    public int[] getMaxX() {
        int[] xMax = new int[this.maxDepth + 1];
        for (int i = 0; i <= this.maxDepth; i++) {
            xMax[i] = this.nNextAvailX[i];
        }
        return xMax;
    }

    /**
     *
     */
    public void printCount() {
        for (int i = 0; i <= maxDepth; i++) {
            System.out.println("Depth: " + i + " Nodes: " + nCount[i]);
        }
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitProgramTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitBlockTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFunctionDeclTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitCallTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitDeclTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIntTypeTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitBoolTypeTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFormalsTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitActualArgsTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIfTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitWhileTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitReturnTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitAssignTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIntTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitIdTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitRelOpTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitAddOpTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitMultOpTree(AST t) {
        count(t);
        return null;
    }

    //new methods here

    /**
     *
     * @param t
     * @return
     */
    public Object visitFloatTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitFloatTypeTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    public Object visitRepeatTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitCharTypeTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitCharTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitStringTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitSciNTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitDoTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitForTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitEheadTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitStringTypeTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitNegOpTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitNotTree(AST t) {
        count(t);
        return null;
    }

    /**
     *
     * @param t
     * @return
     */
    @Override
    public Object visitPowTree(AST t) {
        count(t);
        return null;
    }

}
