package demo1.mvc;

import java.math.BigDecimal;

/**
 * ddd : data driven design
 * <p>
 * ���ڵ����⣺
 * 1. �������
 * 1.1 AccountDO ��Ӧ���ݿ�һ�ű�������ݿ��ֶη����仯������Ҳ��Ҫ�仯����ôPaymentService���ܻ�����仯������ҵ���߼�����Ҫ�仯
 * 1.2 ������̱�̣����ҵ���߼������仯������������Ҫ�ı�
 * 1.3 �ⲿ����û�и��룬����Ҳ���kafka�ˣ���Ҫ�Ĵ���
 * 1.4 ����ҵ���߼���ø��ӣ��������������ά��
 * 1.5 ��һ����룬����Ҫ���²����������
 * 1.6 ����ҵ���ݽ�������Խ��Խ���ӣ�Խ��Խ����ά����ֱ���ع�
 * <p>
 * 2. ϵͳ����
 * 2.1 ֱ�������˵ײ����ݽṹ������AccountDO��������ݽṹ�����仯����Ӱ�����
 * 2.2 AccountDao ֱ������ jdbc����� orm ��ܴ� hibernate �仯Ϊ mybatis����Ӱ�����
 * 2.3 ������������� RiskCheckService �� kafka �仯����Ӱ�����
 * 2.5 ��Ԫ��������
 */
public class PaymentService {
    private AccountDao accountDao; // �������ݿ�
    private KafkaTemplate kafkaTemplate; // ����kafka
    private RiskCheckService riskCheckService; // ���΢����ӿ�

    public Result pay(Long userId, String merchantAcount, BigDecimal amount) throws NoMoneyException, InvalidOperException {
        //1.�����ݶ�ȡ����
        AccountDO clientDO = accountDao.selectByUserId(userId);
        AccountDO merchantDO = accountDao.selectByAccountNumber(merchantAcount);
        //2.ҵ�����У��
        if (amount.compareTo(clientDO.getAvailable()) > 0) {
            throw new NoMoneyException();
        }
        //3.���÷��΢����
        RiskCode riskCode = riskCheckService.checkPayment();
        //4.��齻�׺Ϸ���
        if (!"0000".equals(riskCode)) {
            throw new InvalidOperException();
        }
        //5.������ֵ�����Ҹ����ֶ�
        BigDecimal newSource = clientDO.getAvailable().subtract(amount);
        BigDecimal newTarget = merchantDO.getAvailable().add(amount);
        clientDO.setAvailable(newSource);
        merchantDO.setAvailable(newTarget);
        //6.���µ����ݿ�
        accountDao.update(clientDO);
        accountDao.update(merchantDO);
        //7.���������Ϣ
        String message = userId + "," + merchantAcount + "," + amount;
        kafkaTemplate.send("AUDIT_MSG", message);
        return Result.SUCCESS;
    }
}
