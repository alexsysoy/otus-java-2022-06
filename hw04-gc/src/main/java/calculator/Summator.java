package calculator;

import java.util.ArrayList;
import java.util.List;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    private final List<Data> listValues = new ArrayList<>();

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);

        int size = listValues.size();
        int value = data.getValue();

        if (size % 6_600_000 == 0) {
            listValues.clear();
        }
        sum += value;

        sumLastThreeValues = value + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = value;

        int expressionOne = (sumLastThreeValues * sumLastThreeValues / (value + 1) - sum);

        for (var idx = 0; idx < 3; idx++) {
            someValue += expressionOne;
            someValue = Math.abs(someValue) + size;
        }
    }

    public Integer getSum() {
        return sum;
    }

    public Integer getPrevValue() {
        return prevValue;
    }

    public Integer getPrevPrevValue() {
        return prevPrevValue;
    }

    public Integer getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public Integer getSomeValue() {
        return someValue;
    }
}
