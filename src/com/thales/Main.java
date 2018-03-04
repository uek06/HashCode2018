package com.thales;

import com.thales.models.Car;
import com.thales.models.Position;
import com.thales.models.Ride;
import javafx.geometry.Pos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static String FILENAME_A = "a_example.in";
    private static String FILENAME_B = "b_should_be_easy.in";
    private static String FILENAME_C = "c_no_hurry.in";
    private static String FILENAME_D = "d_metropolis.in";
    private static String FILENAME_E = "e_high_bonus.in";
    private static String OUTPUTNAME = "example.out";

    private static int bonus;
    private static int nbCol;
    private static int nbRow;
    private static int nbCar;
    private static int nbSteps;
    private static int nbRides;
    private static List<Ride> rides;
    private static List<Car> cars;

    public static void main(String[] args) {
        try {
            InputStreamReader is = new InputStreamReader(
                    Main.class.getClassLoader().getResourceAsStream(Main.FILENAME_E));
            BufferedReader bf = new BufferedReader(is);
            String line = bf.readLine();
            String[] constants = line.split(" ");
            nbRow = Integer.parseInt(constants[0]);
            nbCol = Integer.parseInt(constants[1]);
            nbCar = Integer.parseInt(constants[2]);
            nbRides = Integer.parseInt(constants[3]);
            bonus = Integer.parseInt(constants[4]);
            nbSteps = Integer.parseInt(constants[5]);
            rides = new ArrayList<>();
            cars = new ArrayList<>();
            // Loop on inputs rides
            for (int i = 0; i < nbRides; i++){
                String currentLine = bf.readLine();
                String[] lineSplited = currentLine.split(" ");
                Ride ride = new Ride();
                ride.setId(i);
                ride.setStartPosition(new Position(Integer.parseInt(lineSplited[0]), Integer.parseInt(lineSplited[1])));
                ride.setEndPosition(new Position(Integer.parseInt(lineSplited[2]), Integer.parseInt(lineSplited[3])));
                ride.setEarliestStart(Integer.parseInt(lineSplited[4]));
                ride.setLatestFinish(Integer.parseInt(lineSplited[5]));
                ride.initialize();
                rides.add(ride);
            }

            // Sort by distance then earliest start
            rides = rides.stream().sorted((r1, r2) -> {
                if(r1.getDistance() < r2.getDistance()) {
                    return -1;
                } else if(r1.getDistance() > r2.getDistance()) {
                    return 1;
                } else {
                    if(r1.getEarliestStart() < r2.getEarliestStart()) {
                        return -1;
                    } else if(r1.getEarliestStart() > r2.getEarliestStart()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }).collect(Collectors.toList());

            // Create cars
            for (int i = 0; i < nbCar; i++){
                Car car = new Car();
                car.setId(i);
                cars.add(car);
            }

            // Run simulation
            runSimulation();


            saveOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveOutput() {
        StringBuilder sb = new StringBuilder("");

        // Output rides
        for (Car car : cars) {
            if(car.getRideIds().size() > 0) {
                sb.append(car.getRideIds().size());

                for (Integer rideId : car.getRideIds()) {
                    sb.append(" ");
                    sb.append(rideId);
                }
                sb.append("\n");
            }
        }

        try {
            PrintWriter out = new PrintWriter(Main.OUTPUTNAME);
            out.write(sb.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runSimulation() {
        for(int currentStep = 0; currentStep < nbSteps ; currentStep++){
            for(Car car: cars){
                updateCarStatus(car);
                if(car.getCurrentRide() == null) {
                    if(!rides.isEmpty()) {
                        Ride chosenRide = lookForNextRide(car, currentStep);
                        car.setCurrentRide(chosenRide);
                        car.setPosition(chosenRide.getStartPosition());
                        rides.remove(chosenRide);
                    }
                }
                updatePosition(car, currentStep);
            }
        }
    }

    private static void updateCarStatus(Car car) {
        if(isCarArrived(car) && car.getCurrentRide() != null){
            car.getRideIds().add(car.getCurrentRide().getId());
            car.setCurrentRide(null);
        }
    }

    private static boolean isCarArrived(Car car){
        if((car.getCurrentRide() == null)) {
            return true;
        }
        return car.getPosition().equals(car.getCurrentRide().getEndPosition());
    }

    private static Ride lookForNextRide(Car car, int currentStep) {
        Ride bestRide = rides.get(0);

        for (Ride ride: rides) {
            if(currentStep + ride.getDistance() < ride.getLatestFinish()){
                if(Position.Distance(car.getPosition(), ride.getStartPosition()) - Position.Distance(car.getPosition(), bestRide.getStartPosition()) == 0){
                    if(Position.Distance(car.getPosition(), ride.getStartPosition()) < Position.Distance(car.getPosition(), bestRide.getStartPosition())){
                        bestRide = ride;
                    }
                } else if(Position.Distance(car.getPosition(), ride.getStartPosition()) < Position.Distance(car.getPosition(), bestRide.getStartPosition())){
                    bestRide = ride;
                }
            }
        }
        return bestRide;

    }

    private static void updatePosition(Car car, int currentStep) {

        if(car.getCurrentRide() != null) {

            Position endPosition = car.getCurrentRide().getEndPosition();

            if (currentStep >= car.getCurrentRide().getEarliestStart()) {
                if (car.getPosition().getX() < endPosition.getX()) {
                    car.getPosition().setX(car.getPosition().getX() + 1);
                } else if (car.getPosition().getY() < endPosition.getY()) {
                    car.getPosition().setY(car.getPosition().getY() + 1);
                } else if (car.getPosition().getX() > endPosition.getX()) {
                    car.getPosition().setX(car.getPosition().getX() - 1);
                } else if (car.getPosition().getY() > endPosition.getY()) {
                    car.getPosition().setY(car.getPosition().getY() - 1);
                }
            }
        }
    }
}
