package com._2gis.cartoshka.tree.entities;

import com._2gis.cartoshka.Feature;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.Visitor;
import com._2gis.cartoshka.tree.entities.literals.MultiLiteral;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Value extends Expression {
    private Collection<Expression> expressions;

    public Value(Location location, Collection<Expression> expressions) {
        super(location);
        this.expressions = expressions;
    }

    @Override
    public <R, P> R accept(Visitor<R, P> visitor, P params) {
        return visitor.visitValueExpression(this, params);
    }

    @Override
    public String toString() {
        return collectionToString(expressions, ", ");
    }

    public Collection<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(Collection<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Literal ev(Feature feature) {
        if (expressions.size() == 1) {
            return expressions.iterator().next().ev(feature);
        }

        List<Literal> literals = new ArrayList<>();
        for (Expression expression : expressions) {
            literals.add(expression.ev(feature));
        }

        return new MultiLiteral(getLocation(), literals);
    }
}