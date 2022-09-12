import annotations.After;
import annotations.Before;
import annotations.Test;

class SimpleJUnitTest {

    @Before
    public void setUp() {
        System.out.println("Работает метод setUp с аннотацией @Before");
    }

    @Test
    public void firstTest() {
        System.out.println("Работает success метод firstTest с аннотацией @Test");
    }

    @Test
    public void secondTest() {
        System.out.println("Работает success метод secondTest с аннотацией @Test");
    }

    @Test
    public void thirdTest() {
        System.out.println("Работает fail метод thirdTest с аннотацией @Test");
        throw new RuntimeException("fail");
    }

    @Test
    public void forthTest() {
        System.out.println("Работает success метод forthTest с аннотацией @Test");
    }

    @After
    public void tearDown() {
        System.out.println("Работает метод tearDown с аннотацией @After");
    }

    public void methodWithoutAnnotation() {
        System.out.println("НЕ ДОЛЖЕН ВЫЗЫВАТЬСЯ!!!");
    }

}