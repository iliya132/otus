package services;

import exceptions.AtmException;
import exceptions.NotAuthorizedException;
import model.Callback;
import model.Cash;
import services.interfaces.AtmCellsService;
import services.interfaces.AtmService;
import services.interfaces.BankService;
import services.interfaces.UiOutputService;

import java.util.function.Supplier;

public class RedAtmServiceImpl implements AtmService {

    private final BankService bankService;
    private final AtmCellsService atmCellsService;
    private final UiOutputService uiService;
    private String cardNumber;

    public RedAtmServiceImpl(BankService bankService,
                             AtmCellsService atmCellsService,
                             UiOutputService uiService) {
        this.bankService = bankService;
        this.atmCellsService = atmCellsService;
        this.uiService = uiService;
    }

    @Override
    public void authorize(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public Cash withdraw(int amount) throws NotAuthorizedException {
        Cash cash = withAtmSession(() -> tryWithdrawInternal(amount));
        uiService.printMessage("Given cash:\n" + cash);
        return cash;
    }

    private void printMessage(String message) {
        uiService.printMessage(message);
    }

    private Cash tryWithdrawInternal(int amount) {
        try {
            bankService.withdraw(amount);
            return atmCellsService.withdraw(amount);
        } catch (AtmException ex) {
            rollbackOperation(ex.getUiMessage(), amount);
            return null;
        }
    }

    private void rollbackOperation(String uiMessage, int operationAmount) {
        bankService.refund(operationAmount);
        printMessage(uiMessage);
    }

    @Override
    public void deposit(Cash banknotes) throws NotAuthorizedException {
        withAtmSession(() -> doDeposit(banknotes));
    }

    private void doDeposit(Cash banknotes) {
        atmCellsService.deposit(banknotes);
        bankService.deposit(banknotes.getAmount());
    }

    private void withAtmSession(Callback callback) throws NotAuthorizedException {
        ensureAuthorizedOrThrow();
        callback.run();
        finishAtmSession();
    }

    private <T> T withAtmSession(Supplier<T> callback) throws NotAuthorizedException {
        ensureAuthorizedOrThrow();
        var res = callback.get();
        finishAtmSession();
        return res;
    }

    private void ensureAuthorizedOrThrow() throws NotAuthorizedException {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new NotAuthorizedException();
        }
    }

    private void finishAtmSession() {
        this.cardNumber = null;
    }
}
