package basicDemo.demo1.ddd.domain;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private String accountNumber;
    private BigDecimal available;

    public void withdraw(Money money) {
        BigDecimal bigDecimal = money.toBigDecimal();
        available = available.add(bigDecimal);
    }

    public void deposit(Money money) throws InsufficientMoneyException {
        BigDecimal bigDecimal = money.toBigDecimal();
        if (available.compareTo(bigDecimal) < 0) {
            throw new InsufficientMoneyException();
        }
        available = available.subtract(bigDecimal);
    }
}
