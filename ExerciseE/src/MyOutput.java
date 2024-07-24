import java.util.ArrayList;
import java.util.regex.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

interface FormattedOutput {
    String getFormatted();
}

enum Actions {
    FORWARD, LEFT, REVERSE, RIGHT, START, STOP
}

enum Directions {
    E("East"), N("North"), NE("Northeast"), NW("Northwest"),
    S("South"), SE("Southeast"), SW("Southwest"), W("West");

    private final String fullName;

    Directions(String fullName) {
        this.fullName = fullName;
    }

    public String toString() {
        return fullName;
    }
}

class Movement implements Cloneable {
    private static final String REGEX = "\"\\s*([A-Z]+)\\s*-\\s*([A-Z]{1,2})\\s*\\(.*\\)\\s*\"";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private final Actions action;
    private final Directions direction;

    public Movement(String movement) {
        Matcher matcher = PATTERN.matcher(movement);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid movement format: " + movement);
        }
        this.action = Actions.valueOf(matcher.group(1));
        this.direction = Directions.valueOf(matcher.group(2));
    }

    public String getAction() {
        return action.toString();
    }

    public String getDirection() {
        return direction.toString();
    }

    @Override
    public Object clone() {
        return new Movement("\"" + action + " - " + direction + " (some sensor)\"");
    }
}

class Sensor implements Cloneable {
    private static final String REGEX = "\\(\\s*([a-zA-Z]+)\\s*\\)";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private final String sensor;

    public Sensor(String sensor) {
        Matcher matcher = PATTERN.matcher(sensor);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid sensor format: " + sensor);
        }
        this.sensor = matcher.group(1).trim(); // Remove any extra spaces
    }

    public String getSensor() {
        return sensor;
    }

    @Override
    public Object clone() {
        return new Sensor("(" + sensor + ")");
    }
}

class RobotDataLine implements Cloneable {
    private static final String DATE_REGEX = "\\[([0-9]{2})/([0-9]{2})/([0-9]{4})\\]";
    private static final Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);
    private static final String ROBOT_REGEX = "Robot\\s([0-9]{3}\\s[A-Z])";
    private static final Pattern ROBOT_PATTERN = Pattern.compile(ROBOT_REGEX);

    private final String dataLine;
    private final String robotID;
    private final Sensor sensor;
    private final Movement movement;
    private final LocalDate date;

    public RobotDataLine(String line) {
        this.dataLine = line;

        // Extract and validate date
        Matcher dateMatcher = DATE_PATTERN.matcher(line);
        if (!dateMatcher.find()) {
            throw new IllegalArgumentException("Invalid date format in line: " + line);
        }
        this.date = LocalDate.parse(dateMatcher.group(3) + "-" + dateMatcher.group(2) + "-" + dateMatcher.group(1),
                DateTimeFormatter.ISO_DATE);

        // Extract and validate robot ID
        Matcher robotMatcher = ROBOT_PATTERN.matcher(line);
        if (!robotMatcher.find()) {
            throw new IllegalArgumentException("Invalid robot ID format in line: " + line);
        }
        this.robotID = robotMatcher.group(1);

        // Extract and validate movement
        String movementStr = line.substring(line.indexOf("\""), line.lastIndexOf("\"") + 1);
        this.movement = new Movement(movementStr);

        // Extract and validate sensor
        String sensorStr = line.substring(line.indexOf("("), line.lastIndexOf(")") + 1).trim();
        this.sensor = new Sensor(sensorStr);
    }

    public String getRobotID() {
        return robotID;
    }

    public String getDataLine() {
        return dataLine;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public Movement getMovement() {
        return movement;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public Object clone() {
        return new RobotDataLine(dataLine);
    }
}

class RobotDataRecord implements Cloneable {
    private final ArrayList<RobotDataLine> log;

    public RobotDataRecord(String[] array) {
        this.log = new ArrayList<>();
        for (String line : array) {
            try {
                this.log.add(new RobotDataLine(line));
            } catch (IllegalArgumentException e) {
                System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            }
        }
    }

    public RobotDataLine getLine(int index) {
        if (index < 0 || index >= log.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + log.size());
        }
        return log.get(index);
    }

    public ArrayList<RobotDataLine> getDataRecord() {
        return new ArrayList<>(log);
    }

    @Override
    public Object clone() {
        RobotDataRecord cloned = new RobotDataRecord(new String[0]);
        for (RobotDataLine line : this.log) {
            cloned.log.add((RobotDataLine) line.clone());
        }
        return cloned;
    }
}

public class MyOutput {
    public static void main(String[] args) throws CloneNotSupportedException {
        String[] getSampleData = getSampleData();

        RobotDataRecord dataFile = new RobotDataRecord(getSampleData);
        RobotDataLine dataLine1 = dataFile.getLine(0);
        System.out.println("Data record 0: " + dataLine1.getDataLine());
        RobotDataLine dataLine2 = dataFile.getLine(6);
        System.out.println("Data record 6: " + dataLine2.getDataLine());

        String rID = dataFile.getLine(0).getRobotID();
        System.out.println("Robot ID: " + rID);

        LocalDate date = dataLine1.getDate();
        System.out.println("Date: " + date.toString());

        Movement mov = dataLine1.getMovement();
        System.out.println("Action: " + mov.getAction());
        System.out.println("Direction: " + mov.getDirection());

        Sensor sen = dataLine1.getSensor();
        System.out.println("Sensor: " + sen.getSensor());

        System.out.println();
        dataLine1 = dataFile.getLine(4);
        System.out.println("Log line 4: " + dataLine1.getDataLine());
        System.out.println("Action: " + dataLine1.getMovement().getAction());
        System.out.println("Direction: " + dataLine1.getMovement().getDirection());
        System.out.println("Sensor: " + dataLine1.getSensor().getSensor());

        System.out.println("\nTest all levels of cloning: ");
        RobotDataRecord dataFileCopy = (RobotDataRecord) dataFile.clone();
        System.out.println(dataFileCopy == dataFile);
        System.out.println(dataFileCopy.getDataRecord() == dataFile.getDataRecord());
        System.out.println(dataFileCopy.getLine(0) == dataFile.getLine(0));
        System.out.println(dataFileCopy.getLine(0).getMovement() == dataFile.getLine(0).getMovement());
        System.out.println(dataFileCopy.getLine(0).getSensor() == dataFile.getLine(0).getSensor());

        System.out.println("Uncomment below to test exceptions ...");
        // String[] badData = getBadData();
        // RobotDataRecord badDataFile = new RobotDataRecord(badData);
    }

    public static String[] getSampleData() {
        return new String[]{
            " Robot 890 A - - [02/03/2022] \" START - NE ( ultrasonic ) \" ",
            " Robot 890 A - - [02/03/2022] \" FORWARD - NE ( infrared ) \" ",
            " Robot 123 B - - [16/03/2022] \" REVERSE - SW ( lidar ) \" ",
            " Robot 793 B - - [29/03/2022] \" STOP - S ( temperature ) \" ",
            " Robot 405 A - - [10/04/2022] \" RIGHT - E ( light ) \" ",
            " Robot 561 C - - [21/04/2022] \" LEFT - SE ( ultrasonic ) \" ",
            " Robot 227 D - - [25/04/2022] \" FORWARD - N ( lidar ) \" "
        };
    }

    public static String[] getBadData() {
        return new String[]{
            " Robot 890 A - - [90/03/2022] \" START - NE ( ultra$onic ) \" ",
            " Robot 890 AZ - - [02/03/2022] \" BACKWARDS - NS ( infrared ) \" "
        };
    }
}