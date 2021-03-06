/*
Напишите программу, которая каждую секунду отображает на экране данные о времени, прошедшем от начала сессии (запуска программы).

Другой ее поток выводит каждые 5 секунд сообщение "Прошло 5 секунд".
Предусмотрите возможность ежесекундного оповещения потока, воспроизводящего сообщение, потоком, отсчитывающим время.
 */
public class TaskOne {
    private static Clock clock = new Clock(30);

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                clock.startSession();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                clock.everyFiveSeconds();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }
}

class Clock {
    private int start = 1;
    private int end;

    public Clock(int end) {
        this.end = end;
    }

    public synchronized void startSession() throws InterruptedException {
        System.out.println("Таймер запущено");
        while (end > 0) {
            Thread.sleep(1000);
            System.out.println(start + " sec");
            start++;
            end--;
            notifyAll();
            while (start % 5 == 0) {
                if (end <= 0) { break; }
                wait();
            }
        }
        System.out.println("Таймер зупинено");
    }

    public synchronized void everyFiveSeconds() throws InterruptedException {
        while (end > 0) {
            while (start % 5 != 0) {
                if (end <= 0) { return; }
                wait();
            }
            Thread.sleep(1000);
            System.out.println("Пройшло 5 секунд");
            start++;
            end--;
            notifyAll();
        }
    }
}