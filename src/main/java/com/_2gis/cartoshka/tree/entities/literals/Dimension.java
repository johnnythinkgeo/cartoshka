package com._2gis.cartoshka.tree.entities.literals;

import com._2gis.cartoshka.CartoshkaException;
import com._2gis.cartoshka.Location;
import com._2gis.cartoshka.scanner.TokenType;
import com._2gis.cartoshka.tree.Visitor;
import com._2gis.cartoshka.tree.entities.Literal;

public class Dimension extends Literal {
    private final double value;

    private final String unit;

    private final boolean hasDot;

    public Dimension(Location location, double value, String unit, boolean hasDot) {
        super(location);
        this.value = value;
        this.unit = unit;
        this.hasDot = hasDot;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public Literal operate(TokenType operator) {
        if (operator == TokenType.SUB) {
            return new Dimension(getLocation(), -value, unit, hasDot);
        }

        return super.operate(operator);
    }

    @Override
    public Literal operate(TokenType operator, Literal operand) {
        if (operand.isDimension() || operand.isNumeric()) {
            Double left = this.getValue();
            Double right = null;
            if (operand.isDimension()) {
                Dimension dimension = (Dimension) operand;
                if (unit.equals(dimension.getUnit())) {
                    right = dimension.getValue();
                }
            } else {
                right = operand.toNumber();
            }

            if (right != null) {
                switch (operator) {
                    case ADD:
                        return new Dimension(Location.min(getLocation(), operand.getLocation()), left + right, unit, hasDot || operand.hasDot());
                    case SUB:
                        return new Dimension(Location.min(getLocation(), operand.getLocation()), left - right, unit, hasDot || operand.hasDot());
                    case MUL:
                        return new Dimension(Location.min(getLocation(), operand.getLocation()), left * right, unit, hasDot || operand.hasDot());
                    case DIV:
                        return new Dimension(Location.min(getLocation(), operand.getLocation()), left / right, unit, hasDot || operand.hasDot());
                    case MOD:
                        return new Dimension(Location.min(getLocation(), operand.getLocation()), left % right, unit, hasDot || operand.hasDot());
                }
            }
        }

        return super.operate(operator, operand);
    }

    @Override
    public boolean isDimension() {
        return true;
    }

    @Override
    public Double toNumber() {
        if (unit.equals("%")) {
            return value / 100.0;
        }

        return super.toNumber();
    }

    @Override
    public boolean hasDot() {
        return hasDot;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDimension(this);
    }

    @Override
    public String toString() {
        if (hasDot) {
            return String.format("%s%s", Double.toString(Math.round(value * 100d) / 100d), unit);
        }

        return String.format("%s%s", Integer.toString((int) value), unit);
    }

    @Override
    public int compareTo(Literal o) {
        if (o.isDimension()) {
            Dimension other = (Dimension) o;
            if (other.getUnit().equals(unit)) {
                return Double.compare(value, other.getValue());
            }
        }

        throw CartoshkaException.incorrectComparison(getLocation());
    }
}