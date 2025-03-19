package demo1.mvc;

import java.math.BigDecimal;

/**
 * ddd : data driven design
 * <p>
 * 存在的问题：
 * 1. 代码层面
 * 1.1 AccountDO 对应数据库一张表，如果数据库字段发生变化，对象也需要变化，那么PaymentService可能会产生变化，整个业务逻辑就需要变化
 * 1.2 面向过程编程，如果业务逻辑发生变化，整个方法需要改变
 * 1.3 外部依赖没有隔离，如果我不用kafka了，需要改代码
 * 1.4 随着业务逻辑变得复杂，整个代码会难以维护
 * 1.5 改一块代码，就需要重新测试整块代码
 * 1.6 随着业务演进，代码越来越复杂，越来越难以维护，直到重构
 * <p>
 * 2. 系统层面
 * 2.1 直接依赖了底层数据结构，比如AccountDO，如果数据结构发生变化，会影响代码
 * 2.2 AccountDao 直接依赖 jdbc，如果 orm 框架从 hibernate 变化为 mybatis，会影响代码
 * 2.3 如果第三方服务 RiskCheckService 、 kafka 变化，会影响代码
 * 2.5 单元测试困难
 */
public class PaymentService {
    private AccountDao accountDao; // 操作数据库
    private KafkaTemplate kafkaTemplate; // 操作kafka
    private RiskCheckService riskCheckService; // 风控微服务接口

    public Result pay(Long userId, String merchantAcount, BigDecimal amount) throws NoMoneyException, InvalidOperException {
        //1.从数据读取数据
        AccountDO clientDO = accountDao.selectByUserId(userId);
        AccountDO merchantDO = accountDao.selectByAccountNumber(merchantAcount);
        //2.业务参数校验
        if (amount.compareTo(clientDO.getAvailable()) > 0) {
            throw new NoMoneyException();
        }
        //3.调用风控微服务
        RiskCode riskCode = riskCheckService.checkPayment();
        //4.检查交易合法性
        if (!"0000".equals(riskCode)) {
            throw new InvalidOperException();
        }
        //5.计算新值，并且更新字段
        BigDecimal newSource = clientDO.getAvailable().subtract(amount);
        BigDecimal newTarget = merchantDO.getAvailable().add(amount);
        clientDO.setAvailable(newSource);
        merchantDO.setAvailable(newTarget);
        //6.更新到数据库
        accountDao.update(clientDO);
        accountDao.update(merchantDO);
        //7.发送审计信息
        String message = userId + "," + merchantAcount + "," + amount;
        kafkaTemplate.send("AUDIT_MSG", message);
        return Result.SUCCESS;
    }
}
