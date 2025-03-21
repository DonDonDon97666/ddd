package basicDemo.demo1.ddd.domain;

public class AccountTransferService {
    public void transfer(Account sourceAccount, Account targetAccount, Money money) throws InsufficientMoneyException {
        sourceAccount.deposit(money);
        targetAccount.withdraw(money);
    }
}
