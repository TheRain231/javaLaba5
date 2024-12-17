import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class LiftController {
    private final List<Lift> lifts;

    public LiftController(int liftCount, LiftView view) {
        lifts = new ArrayList<>();
        for (int i = 0; i < liftCount; i++) {
            lifts.add(new Lift(i + 1, 1, view));
        }
        for (Lift lift : lifts) {
            new Thread(lift).start();
        }
    }

    public void callLift(int floor) {
        Lift bestLift = findBestLift(floor);
        bestLift.callLift(floor);
        System.out.println("Вызов лифта на этаж " + floor + ", назначен лифт " + bestLift.getId());
    }

    private Lift findBestLift(int floor) {
        Lift bestLift = null;
        int minDistance = Integer.MAX_VALUE;

        for (Lift lift : lifts) {
            if (lift.isIdle()) {
                int distance = Math.abs(lift.getCurrentFloor() - floor);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestLift = lift;
                }
            } else if (lift.getDirection() * (floor - lift.getCurrentFloor()) > 0) {
                int distance = Math.abs(lift.getCurrentFloor() - floor);
                if (distance < minDistance) {
                    minDistance = distance;
                    bestLift = lift;
                }
            }
        }

        if (bestLift == null) {
            bestLift = lifts.stream()
                    .min(Comparator.comparingInt(lift -> Math.abs(lift.getCurrentFloor() - floor)))
                    .orElseThrow();
        }

        return bestLift;
    }
}