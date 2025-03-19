package demo1.ddd.domain;

import java.math.BigDecimal;

public class Money {
    public Money(BigDecimal amount) {

    }

    public BigDecimal toBigDecimal() {
        return new BigDecimal("100.00");
    }
}
