/*
Паттерн "Наблюдатель" (Observer) - это поведенческий паттерн проектирования,
который определяет зависимость один-ко-многим между объектами таким образом,
что при изменении состояния одного объекта все зависящие от него объекты уведомляются и обновляются автоматически.
Это позволяет объектам наблюдать за изменениями в других объектах и реагировать на эти изменения.

Основные компоненты паттерна "Наблюдатель":
    Субъект (Subject): Предоставляет интерфейс для добавления, удаления и уведомления наблюдателей.
        Субъект содержит список наблюдателей и оповещает их об изменениях в своем состоянии.

    Наблюдатель (Observer): Определяет интерфейс для обновления,
        который используется субъектом для уведомления наблюдателей об изменениях.
        Когда состояние субъекта изменяется, он оповещает всех своих наблюдателей.

    Конкретный субъект (Concrete Subject): Реализует интерфейс субъекта и содержит информацию,
        состояние которой может изменяться.
        Когда состояние конкретного субъекта изменяется, он уведомляет своих наблюдателей.

    Конкретный наблюдатель (Concrete Observer): Реализует интерфейс наблюдателя и хранит ссылку на конкретный субъект.
        Конкретный наблюдатель реагирует на уведомления, которые он получает от субъекта,
        обновляя своё состояние в соответствии с состоянием субъекта.
*/

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {

        ixBet bet142 = new ixBet(142);
        List<Sheeple> sheeples = new ArrayList<Sheeple>();
        int n = 4;

        for (int i = 0; i < n; ++i) {
            sheeples.add(new Sheeple(i));
        }

        for (Sheeple sheeple : sheeples) {
            bet142.RegisterObserver(sheeple);
        }

        bet142.PlayBet();

        for (Sheeple sheeple : sheeples) {
            bet142.RemoveObserver(sheeple);
        }
    }
}

interface IObserver {
    public void Update(Object info);
}

interface IObservable {
    public void RegisterObserver(IObserver observer);

    public void RemoveObserver(IObserver observer);

    public void NotifyObservers();
}

class ixBet implements IObservable {
    private int betId;
    private String result;
    private List<IObserver> observers;

    public ixBet(int betId) {
        this.betId = betId;
        observers = new ArrayList<IObserver>();
    }

    public void PlayBet() {
        result = (ThreadLocalRandom.current().nextInt(0, 2) == 1) ? "won" : "lost";
        NotifyObservers();
    }

    @Override
    public void RegisterObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void RemoveObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void NotifyObservers() {
        String[] info = new String[2];
        info[0] = result;
        info[1] = Integer.valueOf(betId).toString();

        try {
            for (IObserver observer : observers) {
                observer.Update(info);
            }
        } catch (NullPointerException e) {
            System.out.println("Наблюдатель был null");
        }
    }
}

class Sheeple implements IObserver {
    private int id;

    public Sheeple(int id) {
        this.id = id;
    }

    @Override
    public void Update(Object info) {
        try {
            String isWinner = ((String[]) info)[0];
            String betId = ((String[]) info)[1];
            System.out.println("Player id: " + id + " " + isWinner + " bet " + betId);
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("Ошибка индексирования");
        } catch (ClassCastException exception) {
            System.out.println("Ошибка преобразования типов");
        }
    }
}