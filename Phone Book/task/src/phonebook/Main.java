package phonebook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;



public class Main {
    final static String dataPath =
            "C:\\Users\\Oleg\\IdeaProjects\\Phone Book\\Phone Book\\directory.txt";
    final static String findPath =
            "C:\\Users\\Oleg\\IdeaProjects\\Phone Book\\Phone Book\\find.txt";
    final static String savePath =
            "C:\\Users\\Oleg\\IdeaProjects\\Phone Book\\Phone Book\\sorted_directory.txt";


    final static List<People> data = new ArrayList<>();
    final static List<People> search = new ArrayList<>();
    final static List<People> sortedData = new ArrayList<>();
    static int count = 0;
    public static void main(String[] args) {
        loadData(dataPath);
        loadSearch(findPath);
        System.out.println("Start searching (linear search)...");
        long linearSearchTime = linearSearch(data, search);
        printResult(linearSearchTime);
        System.out.println("Start searching (bubble sort + jump search)...");
        long bubbleSortTime = bubbleSort(data, linearSearchTime);
        if (bubbleSortTime > (linearSearchTime * 10)) {
           linearSearchTime = linearSearch(data, search);
           stopedBubbleSort (bubbleSortTime, linearSearchTime);
        } else {
            long jumpSearchTime = jumpSearch(sortedData, search);
            printResult(bubbleSortTime, jumpSearchTime);
            saveSortedToFile(sortedData);
        }
        System.out.println("Start searching (quick sort + binary search)...");
        long start = System.currentTimeMillis();
        quickSort(data, 0, data.size() - 1);
        long quickSortTime = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        count = 0;
        for (People man : search) {
            binarySearch(data, man.getName());
            count++;
        }
        long binarySearchTime = System.currentTimeMillis() - start;
        printResult(quickSortTime, binarySearchTime);
        saveSortedToFile(data);
        System.out.println("Start searching (hash table)...");
        start = System.currentTimeMillis();
        HashMap<String, Long> dataMap = createHashMap(data);
        long createHashMapTime = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        count = 0;
        for (People man : search) {
            count += searchInHashmap(dataMap, man.getName());
        }
        long searchInHashMapTime = System.currentTimeMillis() - start;
        printResultForHashmap(createHashMapTime, searchInHashMapTime);
    }

    static int searchInHashmap (HashMap<String, Long> dataMap, String name) {
        if (dataMap.containsKey(name)) {
            return 1;
        }
        return 0;
    }
    static HashMap<String, Long> createHashMap (List<People> data) {
        HashMap<String, Long> map = new HashMap<>();
        for (People man : data) {
            map.put(man.getName(), man.getNumber());
        }
        return map;
    }
    static void printResultForHashmap(long createTime, long searchTime) {
        long minutesBubble =  createTime / (60 * 1000) % 60;
        long secondsBubble = createTime / 1000 % 60;
        long msecBubble = createTime / 1000;

        long minutesJump = searchTime / (60 * 1000) % 60;
        long secondsJump = searchTime / 1000 % 60;
        long msecJump = searchTime / 1000;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms." + "\n",
                count, search.size(), minutesJump + minutesBubble, secondsJump + secondsBubble , msecJump + msecBubble);
        System.out.printf("Creating time: %d min. %d sec. %d ms." + "\n",
                minutesBubble,secondsBubble, msecBubble);
        System.out.printf("Searching time: %d min. %d sec. %d ms."  + "\n",
                minutesJump, secondsJump, msecJump);
    }
    static int binarySearch(List<People> array, String elem) {
        int left = 0;
        int right = array.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (array.get(mid).getName().toUpperCase().trim().compareTo(elem.toUpperCase().trim()) == 0) {
                return mid;
            } else if (array.get(mid).getName().toUpperCase().trim().compareTo(elem.toUpperCase().trim()) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    static <T> void swap(List <T> arr, int from, int to) {
        T temp = arr.get(from);
        arr.set(from, arr.get(to));
        arr.set(to, temp);
    }
    static void quickSort(List<People> array, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(array, left, right);
            quickSort(array, left, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, right);
        }
    }
    private static int partition(List<People> array, int left, int right) {
        People pivot = array.get(right);
        int partitionIndex = left;
        for (int i = left; i < right; i++) {
            if (array.get(i).compareTo(pivot) < 0) {
                swap(array, i, partitionIndex);
                partitionIndex++;
            }
        }
        swap(array, partitionIndex, right); //
        return partitionIndex;
    }
    static void loadData(String path)  {
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNext()) {
                long number = scanner.nextLong();
                String name = scanner.nextLine().trim();
                data.add(new People(number, name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void loadSearch(String path)  {
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNext()) {
                search.add(new People(scanner.nextLine().trim()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static long linearSearch (List<People> data, List<People> search) {
        count = 0;
        long start = System.currentTimeMillis();
        for (People searchName : search) {
            for (People dataName : data){
               if (searchName.getName().equalsIgnoreCase(dataName.getName())) {
                   count++;
                   break;
               }
            }
        }

        long interval = System.currentTimeMillis() - start;

        return interval;
    }
    static void printResult(long linearSearchTime) {
        long minutes = linearSearchTime / (60 * 1000) % 60;
        long seconds = linearSearchTime / 1000 % 60;
        long msec = linearSearchTime / 1000;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms." + "\n",
                count, search.size(), minutes, seconds , msec);
    }
    static void printResult(long bubbleSortTime, long jumpSearchTime) {
        long minutesBubble =  bubbleSortTime / (60 * 1000) % 60;
        long secondsBubble = bubbleSortTime / 1000 % 60;
        long msecBubble = bubbleSortTime / 1000;

        long minutesJump = jumpSearchTime / (60 * 1000) % 60;
        long secondsJump = jumpSearchTime / 1000 % 60;
        long msecJump = jumpSearchTime / 1000;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms." + "\n",
                count, search.size(), minutesJump + minutesBubble, secondsJump + secondsBubble , msecJump + msecBubble);
        System.out.printf("Sorting time: %d min. %d sec. %d ms." + "\n",
                minutesBubble,secondsBubble, msecBubble);
        System.out.printf("Searching time: %d min. %d sec. %d ms."  + "\n",
                minutesJump, secondsJump, msecJump);
    }
    static void stopedBubbleSort(long bubbleSortTime, long linearSearchTime) {
        long minutesBubble =  bubbleSortTime / (60 * 1000) % 60;
        long secondsBubble = bubbleSortTime / 1000 % 60;
        long msecBubble = bubbleSortTime / 1000;

        long minutesLinear = linearSearchTime / (60 * 1000) % 60;
        long secondsLinear = linearSearchTime / 1000 % 60;
        long msecLinear = linearSearchTime / 1000;

        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms." + "\n",
                count, search.size(), minutesLinear + minutesBubble,
                secondsLinear + secondsBubble, msecLinear + msecBubble);
        System.out.printf("Sorting time: %d min. %d sec. %d ms. - STOPPED, moved to linear search" + "\n",
                minutesBubble,secondsBubble, msecBubble);
        System.out.printf("Searching time: %d min. %d sec. %d ms."  + "\n",
                minutesLinear, secondsLinear, msecLinear);
    }
    static long bubbleSort (List<People> data, long linearSearchTime) {
        sortedData.addAll(data);
        long start = System.currentTimeMillis();
        long interval = 0;
        for (int i = 0; i < sortedData.size() - 1; i++) {
            for (int j = 0; j < sortedData.size() - i - 1; j++) {
                if (sortedData.get(j).getName().compareTo(sortedData.get(j + 1).getName()) > 0) {
                    People temp = sortedData.get(j);
                    sortedData.set(j, data.get(j + 1));
                    sortedData.set(j + 1, temp);
                }

            }
            interval = System.currentTimeMillis() - start;
            if (interval > (linearSearchTime * 10)) {
                break;
            }
        }
        return interval;
    }
    static long jumpSearch(List<People> sortedData, List<People> search) {
        count = 0;
        long start = System.currentTimeMillis();
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If array is empty, the element is not found */
        if (sortedData.size() == 0) {
            return System.currentTimeMillis() - start;
        }
        for (People man : search) {

            /* Check the first element */
            if (sortedData.get(currentRight).getName().equalsIgnoreCase(man.getName())) {
                count++;
            }

            /* Calculating the jump length over array elements */
            int jumpLength = (int) Math.sqrt(sortedData.size());

            /* Finding a block where the element may be present */
            while (currentRight < sortedData.size() - 1) {

                /* Calculating the right border of the following block */
                currentRight = Math.min(sortedData.size() - 1, currentRight + jumpLength);

                if (sortedData.get(currentRight).getName().compareTo(man.getName()) > 0) {
                    break; // Found a block that may contain the target element
                }

                prevRight = currentRight; // update the previous right block border
            }

        /* Doing linear search in the found block */
        for (int i = currentRight; i > prevRight; i--) {
            if (sortedData.get(i).getName().equalsIgnoreCase(man.getName()) ) {
                count++;
                }
            }
        }
        return System.currentTimeMillis() - start;
    }
    static void saveSortedToFile(List<People> sortedData) {
        File file = new File(savePath);

        try (FileWriter writer = new FileWriter(file, true)) {
            for (People man:sortedData) {
                writer.write(man.getName() + " " + man.getNumber());
            }
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }
    }

}
