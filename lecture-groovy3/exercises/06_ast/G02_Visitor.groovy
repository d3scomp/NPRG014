abstract class Expression {
    public <T> T acceptVisitor(Visitor<T> v) {
        v.visit(this)
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
                                           
abstract class Visitor<T> {
    abstract T visit(IntegerConstant expr)
    abstract T visit(PlusExpr expr)
    abstract T visit(MultExpr expr)
}

class PrintingVisitor extends Visitor<Void> {
    Void visit(IntegerConstant expr) {
        print expr.value
    }
    Void visit(PlusExpr expr) {
        expr.left.acceptVisitor(this)
        print ' ' + expr.getOperator() + ' '
        expr.right.acceptVisitor(this)
    }
    Void visit(MultExpr expr) {
        expr.left.acceptVisitor(this)
        print ' ' + expr.getOperator() + ' '
        expr.right.acceptVisitor(this)
    }    
}
expr.acceptVisitor(new PrintingVisitor())

//TASK add a visitor that will compute the value of the expression

//println ""
//println expr.acceptVisitor(new ComputingVisitor())