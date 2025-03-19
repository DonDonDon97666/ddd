package demo1.ddd.app;

import demo1.ddd.domain.Account;
import demo1.ddd.domain.Money;
import demo1.ddd.infra.RiskCheckService;
import demo1.ddd.infra.RiskCode;
import demo1.mvc.Result;

public class BusiSafeService {
    private RiskCheckService riskCheckService;

    public Result checkBus(Account userId, Account merchantAccountId, Money amount) {
        RiskCode riskCode = riskCheckService.checkPayment();
        if ("0000".equals(riskCode)) {
            return Result.SUCCESS;
        }

        return Result.REJECT;
    }
}
