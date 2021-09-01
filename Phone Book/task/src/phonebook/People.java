package phonebook;

public class People implements Comparable<People> {
    private long number;
    private String name;

    public People (long number, String name) {
        this.number = number;
        this.name = name;
    }

    public People (String name) {
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(People people) {

        return people.getName().compareTo(name);

    }
}
