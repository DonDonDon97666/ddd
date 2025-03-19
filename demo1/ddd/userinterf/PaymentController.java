package demo1.ddd.userinterf;

import demo1.mvc.InvalidOperException;
import demo1.mvc.NoMoneyException;
import demo1.mvc.PaymentService;
import demo1.mvc.Result;

import java.math.BigDecimal;

public class PaymentController {
    private PaymentService paymentService;

    public Result pay(String merchantAcount, BigDecimal amount) throws InvalidOperException, NoMoneyException {
        //Long userId = (Long) session.getAttribute("userId");
        Long userId = 1L;
        return paymentService.pay(userId, merchantAcount, amount);
    }
}
