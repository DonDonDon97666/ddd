package basicDemo.demo1.ddd.infra;

import java.math.BigDecimal;

public class AccountDO {
    private BigDecimal available;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }
}
