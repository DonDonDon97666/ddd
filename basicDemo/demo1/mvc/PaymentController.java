package basicDemo.demo1.mvc;

import java.math.BigDecimal;

public class PaymentController {
    private PaymentService paymentService;

    public Result pay(String merchantAcount, BigDecimal amount) throws InvalidOperException, NoMoneyException {
        //Long userId = (Long) session.getAttribute("userId");
        Long userId = 1L;
        return paymentService.pay(userId, merchantAcount, amount);
    }
}
