package demo1.ddd.app;

import demo1.ddd.domain.Account;
import demo1.ddd.domain.AccountBuilder;
import demo1.mvc.AccountDO;
import demo1.mvc.AccountDao;

public class AccountRepository {
    private AccountDao accountDao;

    public Account find(Long id) {
        AccountDO accountDO = accountDao.selectByUserId(id);
        return AccountBuilder.toAccount(accountDO);
    }

    public Account find(String accountNumber) {
        AccountDO accountDO = accountDao.selectByAccountNumber(accountNumber);
        return AccountBuilder.toAccount(accountDO);
    }

    public Account save(Account account) {
        AccountDO accountDO = AccountBuilder.fromAccount(account);
        if (accountDO.getId() == null) {
            accountDao.insert(accountDO);
        } else {
            accountDao.update(accountDO);
        }
        return AccountBuilder.toAccount(accountDO);
    }
}
