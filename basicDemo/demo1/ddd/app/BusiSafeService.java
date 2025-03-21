package basicDemo.demo1.ddd.app;

import basicDemo.demo1.ddd.domain.Account;
import basicDemo.demo1.ddd.domain.Money;
import basicDemo.demo1.ddd.infra.RiskCheckService;
import basicDemo.demo1.ddd.infra.RiskCode;
import basicDemo.demo1.mvc.Result;

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
