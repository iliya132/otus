package services;

import exceptions.CantProcessOperationException;
import exceptions.NotEnoughMoneyException;
import model.AtmCell;
import model.Cash;
import model.Nominal;
import services.interfaces.AtmCellsService;

import java.util.*;

public class AtmCellsServiceImpl implements AtmCellsService {
    private final Map<Nominal, AtmCell> cells = new HashMap<>(Nominal.values().length);

    public AtmCellsServiceImpl(Map<Nominal, Integer> banknotesInCells) {
        initializeCells();
        for (var entry : banknotesInCells.entrySet()) {
            cells.get(entry.getKey()).put(entry.getValue());
        }
    }

    private void initializeCells() {
        for (var nominal : Nominal.values()) {
            cells.put(nominal, new AtmCell(nominal, 0));
        }
    }

    @Override
    public Cash withdraw(int amount) throws CantProcessOperationException, NotEnoughMoneyException {
        if (amount % 10 != 0) {
            throw new CantProcessOperationException();
        }

        if (amount > getTotalMoney()) {
            throw new NotEnoughMoneyException();
        }

        List<Nominal> sortedNominals = Arrays.stream(Nominal.values())
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Nominal::getAmount)))
                .toList();
        var notGivenAmount = amount;
        Cash result = new Cash();
        for (var nominal : sortedNominals) {
            var givenCache = withdrawMax(nominal, notGivenAmount);
            if (givenCache.getAmount() != 0) {
                result.append(givenCache);
                notGivenAmount -= givenCache.getAmount();
            }
        }
        if (notGivenAmount != 0) {
            throw new CantProcessOperationException();
        }
        return result;
    }

    private int getTotalMoney() {
        return cells.values().stream().mapToInt(AtmCell::getTotalAmount).sum();
    }

    private Cash withdrawMax(Nominal nominal, int amount) {
        var cell = cells.get(nominal);
        var givenAmount = 0;
        while (calcAmount(nominal, givenAmount) < amount) {
            try {
                if (calcAmount(nominal, givenAmount) + nominal.getAmount() > amount) {
                    break;
                }
                cell.take(1);
                givenAmount += 1;
            } catch (NotEnoughMoneyException ex) {
                break;
            }
        }
        return new Cash(Map.of(nominal, givenAmount));

    }

    private int calcAmount(Nominal nominal, int count) {
        return nominal.getAmount() * count;
    }

    @Override
    public void deposit(Cash banknotes) {
        banknotes.getBanknotes().forEach((key, value) -> cells.get(key).put(value));
    }
}
