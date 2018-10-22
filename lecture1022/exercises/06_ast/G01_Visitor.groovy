abstract class Expression {
    public void acceptVisitor(Visitor v) {
        //TASK implement this method to make the visitor pattern work
    }
}

abstract class BinaryExpression extends Expression {
    Expression left, right
    abstract String getOperator()
}

class IntegerConstant extends Expression {
    int value
}

class PlusExpr extends BinaryExpression {
    String getOperator() {"+"}
}

class MultExpr extends BinaryExpression {
    String getOperator() {"*"}
}

def expr = new PlusExpr(left: new IntegerConstant(value: 10),
                        right: new MultExpr(left: new IntegerConstant(value: 30),
                                            right: new IntegerConstant(value: 8)))
                                           
abstract class Visitor {
    abstract void visit(IntegerConstant expr)
    abstract void visit(PlusExpr expr)
    abstract void visit(MultExpr expr)
}

class PrintingVisitor extends Visitor {
    void visit(IntegerConstant expr) {
        print expr.value
    }
    void visit(PlusExpr expr) {
        expr.left.acceptVisitor(this)
        print ' ' + expr.getOperator() + ' '
        expr.right.acceptVisitor(this)
    }
    void visit(MultExpr expr) {
        expr.left.acceptVisitor(this)
        print ' ' + expr.getOperator() + ' '
        expr.right.acceptVisitor(this)
    }    
}
expr.acceptVisitor(new PrintingVisitor())
