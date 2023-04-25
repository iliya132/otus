import exceptions.NotAuthorizedException;
import model.Cash;
import model.Nominal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AtmCellsServiceImpl;
import services.DummyBankServiceImpl;
import services.RedAtmServiceImpl;
import services.interfaces.AtmCellsService;
import services.interfaces.AtmService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static exceptions.NotEnoughMoneyException.NOT_ENOUGH_MONEY_MESSAGE;

public class RedAtmServiceImplTest {
    private AtmService atmService;
    private final List<String> messagesLog = new ArrayList<>();

    @BeforeEach
    public void setup() {
        AtmCellsService atmCellsService = new AtmCellsServiceImpl(Map.of(Nominal.FIFTY, 50, Nominal.TEN, 500));
        atmService = new RedAtmServiceImpl(new DummyBankServiceImpl(), atmCellsService, messagesLog::add);
        atmService.authorize("test-card");
        messagesLog.clear();
    }

    @Test
    public void atmServiceCanWithdrawMoney() {
        //test
        Cash result = atmService.withdraw(110);

        //assertions
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBanknotes().get(Nominal.FIFTY)).isEqualTo(2);
        Assertions.assertThat(result.getBanknotes().get(Nominal.TEN)).isEqualTo(1);
    }

    @Test
    public void atmServiceShouldPrintNotEnoughMoneyMessage() {
        //test
        Cash result = atmService.withdraw(10000000);

        //assertions
        Assertions.assertThat(result).isNull();
        Assertions.assertThat(messagesLog).contains(NOT_ENOUGH_MONEY_MESSAGE);
    }

    @Test
    public void atmServiceShouldThrowUnauthorizedException() {
        //do transaction
        atmService.withdraw(10);
        //Not authorized here
        Assertions.assertThatThrownBy(() -> atmService.withdraw(10)).isInstanceOf(NotAuthorizedException.class);
    }
}
