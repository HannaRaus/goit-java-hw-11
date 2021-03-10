import java.util.*;

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
    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(15);
        fizzBuzz.play();
    }
}

class FizzBuzz {
    private final int end;
    private volatile int currentNumber;
    private volatile StringJoiner result = new StringJoiner(", ", "", ".");

    public FizzBuzz(int end) {
        this.end = end;
        this.currentNumber = 1;
    }

    public void play() {

        new Thread(() -> number(), "Just numbers").start();

        new Thread(() -> fizz(), "Fizz numbers").start();

        new Thread(() -> buzz(), "Buzz numbers").start();

        new Thread(() -> fizzbuzz(), "FizzBuzz numbers").start();

        System.out.println("Result - " + result);
    }

    public synchronized void number() {
        while (currentNumber <= end) {
            addNumberToResult(String.valueOf(currentNumber));
            if (currentNumber % 3 == 0 || currentNumber % 5 == 0) {
                isWaiting();
            }
        }
    }

    public synchronized void fizz() {
        while (currentNumber <= end) {
            if (currentNumber % 3 == 0 && currentNumber % 5 != 0) {
                addNumberToResult("fizz");
            } else isWaiting();
        }
    }

    public synchronized void buzz() {
        while (currentNumber <= end) {
            if (currentNumber % 5 == 0 && currentNumber % 3 != 0) {
                addNumberToResult("buzz");
            } else isWaiting();
        }
    }

    public synchronized void fizzbuzz() {
        while (currentNumber <= end) {
            if (currentNumber % 15 == 0) {
                addNumberToResult("fizzbuzz");
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

    private synchronized void addNumberToResult(String numberIs) {
            result.add(String.valueOf(numberIs));
            currentNumber++;
            notifyAll();
    }

}


