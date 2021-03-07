import java.util.StringJoiner;

/*
Напишите программу, которая выводит в консоль строку, состоящую из чисел от 1 до n, но с заменой некоторых значений:

если число делится на 3 - вывести "fizz"
если число делится на 5 - вывести "buzz"
если число делится на 3 и на 5 - вывести "fizzbuzz"
Например, для n = 15, ожидаемый результат:
1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz.

Программа должна быть многопоточной, работать с 4 потоками:

Поток A вызывает fizz() чтобы проверить делимость на 3 и вывести fizz.
Поток B вызывает buzz() чтобы проверить делимость на 5 и вывести buzz.
Поток C вызывает fizzbuzz() чтобы проверить делимость на 3 и 5 и вывести fizzbuzz.
Поток D вызывает number() чтобы вывести число.
 */
public class TaskTwo {
    private static FizzBuzz fizzBuzz = new FizzBuzz(15);

    public static void main(String[] args) {
        fizzBuzz.run();
    }
}

class FizzBuzz {
    private final int end;
    private volatile int number;
    private StringJoiner result;

    public FizzBuzz(int end) {
        this.end = end;
        this.number = 1;
        result = new StringJoiner(", ");
    }

    public void run() {
        new Thread(() -> number(), "Just numbers").start();

        new Thread(() -> fizz(), "Fizz numbers").start();

        new Thread(() -> buzz(), "Buzz numbers").start();

        new Thread(() -> fizzbuzz(), "FizzBuzz numbers").start();

    }


    private synchronized void number() {
        while (number <= end) {
            System.out.print(number + ", ");
            result.add(String.valueOf(number));
            number++;
            notifyAll();
            if (number % 3 == 0 || number % 5 == 0) {
                isWaiting();
            }
        }
    }

    private synchronized void fizz() {
        while (number <= end) {
            if (number % 3 == 0 && number % 5 != 0) {
                System.out.print("fizz, ");
                number++;
                notifyAll();
            } else isWaiting();
        }
    }

    private synchronized void buzz() {
        while (number <= end) {
            if (number % 5 == 0 && number % 3 != 0) {
                System.out.print("buzz, ");
                number++;
                notifyAll();
            } else isWaiting();
        }
    }

    private synchronized void fizzbuzz() {
        while (number <= end) {
            if (number % 15 == 0) {
                System.out.print("fizzbuzz, ");
                number++;
                notifyAll();
            } else isWaiting();
        }
    }

    private void isWaiting() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


