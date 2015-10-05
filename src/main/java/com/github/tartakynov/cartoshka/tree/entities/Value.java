package com.github.tartakynov.cartoshka.tree.entities;

import com.github.tartakynov.cartoshka.tree.entities.literals.MultiLiteral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Value extends Expression {
    private final Collection<Expression> expressions;

    private final boolean isDynamic;

    public Value(Collection<Expression> expressions) {
        this.expressions = expressions;
        this.isDynamic = hasDynamicExpression(expressions);
    }

    @Override
    public Literal ev() {
        if (expressions.size() == 1) {
            return expressions.iterator().next().ev();
        }

        List<Literal> literals = new ArrayList<>();
        for (Expression expression : expressions) {
            literals.add(expression.ev());
        }

        return new MultiLiteral(literals);
    }

    @Override
    public boolean isDynamic() {
        return isDynamic;
    }
}