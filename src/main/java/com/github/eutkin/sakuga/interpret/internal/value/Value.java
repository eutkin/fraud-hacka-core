package com.github.eutkin.sakuga.interpret.internal.value;

import java.util.Objects;

public class Value {

    public static Value FALSE_VALUE = fromBoolean(false);
    public static Value TRUE_VALUE = fromBoolean(true);
    public static Value NULL = fromObject(null);

    private final Object value;
    private boolean error = false;

    private Value(Object value) {
        this.value = value;
    }

    private Value(Object value, boolean error) {
        this.value = value;
        this.error = error;
    }

    public static Value fromString(String value) {
        return value != null ? new Value(value.toLowerCase()) : Value.NULL;
    }

    public static Value fromBoolean(Boolean value) {
        return new Value(value);
    }

    public static Value fromObject(Object value) {
        return switch (value) {
            case String str -> Value.fromString(str);
            case null, default -> new Value(value);
        };
    }

    public static Value fromNumber(Number value) {
        return value != null ? new Value(value) : Value.NULL;
    }

    public static Value fromError(Object message) {
        return new Value(message, true);
    }

    public boolean isError() {
        return this.error;
    }

    public boolean isNull() {
        return !this.error && this.value == null;
    }

    public boolean isTrue() {
        return !this.error && Boolean.TRUE == this.value;
    }

    public boolean isFalse() {
        return !this.error && Boolean.FALSE == this.value;
    }

    public boolean isString() {
        return !this.error && this.value instanceof String;
    }

    @Override
    public String toString() {
        return Objects.toString(this.value);
    }

    public boolean isNumber() {
        return !this.error && this.value instanceof Number;
    }

    public Number toNumber() {
        return (Number) this.value;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return Objects.equals(value, value1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    public Value and(Value other) {
        if (this.isError() || other.isError()) {
            return Value.fromError(String.join(";",
                    Objects.toString(this.value),
                    Objects.toString(other.value))
            );
        }
        if (this.isNull() || other.isNull()) {
            return FALSE_VALUE;
        }
        return Value.fromBoolean(Boolean.logicalAnd(this.isTrue(), other.isTrue()));
    }

    public Value or(Value other) {
        if (this.isError() || other.isError()) {
            return Value.fromError(String.join(";",
                    Objects.toString(this.value),
                    Objects.toString(other.value))
            );
        }
        if (this.isNull() || other.isNull()) {
            return FALSE_VALUE;
        }
        return Value.fromBoolean(Boolean.logicalOr(this.isTrue(), other.isTrue()));
    }

    public Value eq(Value other) {
        if (this.isError() || other.isError()) {
            return Value.fromError(String.join(";",
                    Objects.toString(this.value),
                    Objects.toString(other.value))
            );
        }
        if (this.isNull() || other.isNull()) {
            return FALSE_VALUE;
        }
        if (this.isNumber()) {
            return Value.fromBoolean(this.toNumber().floatValue() == other.toNumber().floatValue());
        }
        return Objects.equals(this, other) ? TRUE_VALUE : FALSE_VALUE;
    }

    public Value notEq(Value other) {
        if (this.isError() || other.isError()) {
            return Value.fromError(String.join(";",
                    Objects.toString(this.value),
                    Objects.toString(other.value))
            );
        }
        if (this.isNull() || other.isNull()) {
            return FALSE_VALUE;
        }
        return Objects.equals(this, other) ? FALSE_VALUE : TRUE_VALUE;
    }


    public Value gt(Value other) {
        if (this.isError() || other.isError()) {
            return Value.fromError(String.join(";",
                    Objects.toString(this.value),
                    Objects.toString(other.value))
            );
        }
        if (this.isNull() || other.isNull()) {
            return FALSE_VALUE;
        }
        if (this.isString()) {
            return Value.fromBoolean(this.toString().compareToIgnoreCase(other.toString()) > 0);
        }
        if (this.isNumber()) {
            return Value.fromBoolean(this.toNumber().doubleValue() > other.toNumber().doubleValue());
        }
        return FALSE_VALUE;
    }

    public Value lt(Value other) {
        if (this.isError() || other.isError()) {
            return Value.fromError(String.join(";",
                    Objects.toString(this.value),
                    Objects.toString(other.value))
            );
        }
        if (this.isNull() || other.isNull()) {
            return FALSE_VALUE;
        }
        if (this.isString()) {
            return Value.fromBoolean(this.toString().compareToIgnoreCase(other.toString()) < 0);
        }
        if (this.isNumber()) {
            return Value.fromBoolean(this.toNumber().floatValue() < other.toNumber().floatValue());
        }
        return FALSE_VALUE;
    }


    public Value gte(Value other) {
        Value eq = this.eq(other);
        return eq.isFalse() ? this.gt(other) : eq;
    }

    public Value lte(Value other) {
        Value eq = this.eq(other);
        return eq.isFalse() ? this.lt(other) : eq;
    }
}
