package basicDemo.demo1.ddd.app;

import basicDemo.demo1.ddd.domain.*;
import basicDemo.demo1.mvc.InvalidOperException;
import basicDemo.demo1.mvc.NoMoneyException;
import basicDemo.demo1.mvc.Result;

import java.math.BigDecimal;

/**
 * �仯�����룬ҵ���ֻ��Ҫ������š�
 * ���ź���֮��ҵ���һ�㲻�䣿
 */
public class PaymentService {
    private AccountRepository accountRepository;
    private BusiSafeService busiSafeService;
    private AuditMessageProducer auditMessageProducer;
    private AccountTransferService accountTransferService;

    public Result pay(Long userId, String merchantAcount, BigDecimal amount) throws NoMoneyException, InvalidOperException, InsufficientMoneyException {
        // ����У��
        Money money = new Money(amount);
        //��������
        Account clientAccount = accountRepository.find(userId);
        Account merchantAccount = accountRepository.find(merchantAcount);
        //���׼��
        Result result = busiSafeService.checkBus(clientAccount, merchantAccount, money);
        if (result != Result.SUCCESS) {
            return Result.REJECT;
        }
        //ת��ҵ��
        accountTransferService.transfer(clientAccount, merchantAccount, money);
        //��������
        accountRepository.save(clientAccount);
        accountRepository.save(merchantAccount);
        //���������Ϣ
        AuditMessage auditMessage = new AuditMessage(clientAccount, merchantAccount, money);
        auditMessageProducer.send(auditMessage);
        return Result.SUCCESS;
    }
}
