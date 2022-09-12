package dto;

public class TestStatistic {
    public int failTests;
    public int successTests;
    public int totalCountTests;

    @Override
    public String
    toString() {
        return "Статистика по тестируемуему классу {" +
                "Неуспешных тестов=" + failTests +
                ", Успешных тестов=" + successTests +
                ", Общее количество тестов=" + totalCountTests +
                '}';
    }
}
