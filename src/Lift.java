import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javafx.application.Platform;

class Lift implements Runnable {
    private final int id;
    private int currentFloor;
    private int direction; // -1 вниз, 1 вверх, 0 - стоит
    private final Set<Integer> stops;
    private final Set<Integer> queue;
    private final LiftView view;

    public Lift(int id, int startFloor, LiftView view) {
        this.id = id;
        this.currentFloor = startFloor;
        this.direction = 0;
        this.stops = ConcurrentHashMap.newKeySet();
        this.queue = ConcurrentHashMap.newKeySet();
        this.view = view;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2500);
                synchronized (this) {
                    if (!stops.isEmpty()) {
                        currentFloor += direction;
                        //System.out.println("Лифт " + id + " на этаже " + currentFloor); //вместо этого сейчас интерфейс
                        updateView();
                        if (needsToStop()) {
                            stopAtFloor();
                        }
                    } else {
                        direction = 0;
                        updateView();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void callLift(int floor) {
        if (stops.isEmpty()) {
            setDirection(floor);
            stops.add(floor);
        } else if (direction * (floor - currentFloor) > 0) {
            stops.add(floor);
        } else {
            queue.add(floor);
        }
    }

    private synchronized void setDirection(int targetFloor) {
        if (targetFloor > currentFloor) {
            direction = 1;
        } else if (targetFloor < currentFloor) {
            direction = -1;
        }
    }

    private synchronized boolean needsToStop() {
        return stops.contains(currentFloor);
    }

    private synchronized void stopAtFloor() {
        stops.remove(currentFloor);
        System.out.println("Лифт " + id + " остановился на этаже " + currentFloor);
        updateView();
        if (stops.isEmpty() && !queue.isEmpty()) {
            stops.addAll(queue);
            queue.clear();
            setDirection(stops.iterator().next());
        }
    }

    private void updateView() {
        Platform.runLater(() -> view.updateLift(id, currentFloor, direction));
    }

    public synchronized boolean isIdle() {
        return stops.isEmpty() && direction == 0;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getDirection() {
        return direction;
    }

    public int getId() {
        return id;
    }
}