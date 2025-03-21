package basicDemo.demo1.ddd.app;

import basicDemo.demo1.ddd.domain.*;
import basicDemo.demo1.mvc.InvalidOperException;
import basicDemo.demo1.mvc.NoMoneyException;
import basicDemo.demo1.mvc.Result;

import java.math.BigDecimal;

/**
 * 变化被隔离，业务层只需要负责编排。
 * 编排好了之后，业务层一般不变？
 */
public class PaymentService {
    private AccountRepository accountRepository;
    private BusiSafeService busiSafeService;
    private AuditMessageProducer auditMessageProducer;
    private AccountTransferService accountTransferService;

    public Result pay(Long userId, String merchantAcount, BigDecimal amount) throws NoMoneyException, InvalidOperException, InsufficientMoneyException {
        // 参数校验
        Money money = new Money(amount);
        //加载数据
        Account clientAccount = accountRepository.find(userId);
        Account merchantAccount = accountRepository.find(merchantAcount);
        //交易检查
        Result result = busiSafeService.checkBus(clientAccount, merchantAccount, money);
        if (result != Result.SUCCESS) {
            return Result.REJECT;
        }
        //转账业务
        accountTransferService.transfer(clientAccount, merchantAccount, money);
        //保存数据
        accountRepository.save(clientAccount);
        accountRepository.save(merchantAccount);
        //发送审计信息
        AuditMessage auditMessage = new AuditMessage(clientAccount, merchantAccount, money);
        auditMessageProducer.send(auditMessage);
        return Result.SUCCESS;
    }
}
