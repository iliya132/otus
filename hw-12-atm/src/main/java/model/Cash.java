package model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cash {
    private final Map<Nominal, Integer> banknotes;

    public Cash(Map<Nominal, Integer> banknotes) {
        this.banknotes = banknotes;
    }

    public Cash() {
        this.banknotes = new HashMap<>();
    }

    public void append(Nominal nominal, int count) {
        banknotes.merge(nominal, count, Integer::sum);
    }

    public Map<Nominal, Integer> getBanknotes() {
        return Map.copyOf(banknotes);
    }

    public int getAmount() {
        return banknotes.entrySet().stream()
                .mapToInt(it -> it.getKey().getAmount() * it.getValue())
                .sum();
    }

    public void append(Cash other) {
        other.banknotes.forEach((key, value) -> this.banknotes.merge(key, value, Integer::sum));
    }

    @Override
    public String toString() {
        return this.banknotes
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue() + " : [" +
                        entry.getKey().getAmount() * entry.getValue() + "р]")
                .collect(Collectors.joining("\n")) + "\nTOTAL: [" + getAmount() + "р]";
    }
}
