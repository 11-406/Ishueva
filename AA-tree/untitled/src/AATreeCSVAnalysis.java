import java.io.*;
import java.util.*;

public class AATreeCSVAnalysis {

    private static class Node {
        int key;
        int level;
        Node left, right;

        Node(int key) {
            this.key = key;
            this.level = 1;
            this.left = this.right = null;
        }
    }

    private Node root;
    private long insertOperations;
    private long searchOperations;
    private long deleteOperations;

    public AATreeCSVAnalysis() {
        root = null;
        resetCounters();
    }

    private void resetCounters() {
        insertOperations = 0;
        searchOperations = 0;
        deleteOperations = 0;
    }

    // Основные операции
    //вставка
    public void insert(int key) {
        root = insert(root, key);
    }

    //поиск
    public boolean search(int key) {
        return search(root, key) != null;
    }

    //удаление
    public void delete(int key) {
        root = delete(root, key);
    }

    // Вспомогательные методы
    private Node search(Node node, int key) {
        searchOperations++;
        if (node == null || node.key == key) {
            return node;
        }
        searchOperations++;
        if (key < node.key) {
            return search(node.left, key);
        } else {
            return search(node.right, key);
        }
    }

    private Node insert(Node node, int key) {
        insertOperations++;
        if (node == null) {
            return new Node(key);
        }

        insertOperations++;
        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        } else {
            return node;
        }

        node = skew(node);
        node = split(node);

        return node;
    }

    private Node delete(Node node, int key) {
        deleteOperations++;
        if (node == null) {
            return null;
        }

        deleteOperations++;
        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null && node.right == null) {
                return null;
            } else if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Node minNode = findMin(node.right);
                node.key = minNode.key;
                node.right = delete(node.right, minNode.key);
            }
        }

        node = decreaseLevel(node);
        node = skew(node);
        node.right = skew(node.right);
        if (node.right != null) {
            node.right.right = skew(node.right.right);
        }
        node = split(node);
        node.right = split(node.right);

        return node;
    }

    // Операции балансировки
    private Node skew(Node node) {
        if (node == null) return null;
        if (node.left != null && node.left.level == node.level) {
            Node leftChild = node.left;
            node.left = leftChild.right;
            leftChild.right = node;
            return leftChild;
        }
        return node;
    }

    private Node split(Node node) {
        if (node == null) return null;
        if (node.right != null && node.right.right != null &&
                node.right.right.level == node.level) {
            Node rightChild = node.right;
            node.right = rightChild.left;
            rightChild.left = node;
            rightChild.level++;
            return rightChild;
        }
        return node;
    }

    private Node decreaseLevel(Node node) {
        if (node == null) return null;
        int expectedLevel = Math.min(
                node.left != null ? node.left.level : node.level - 1,
                node.right != null ? node.right.level : node.level - 1
        ) + 1;

        if (expectedLevel < node.level) {
            node.level = expectedLevel;
            if (node.right != null && node.right.level > expectedLevel) {
                node.right.level = expectedLevel;
            }
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Генерация тестовых данных
    private static int[] generateRandomArray(int size, int min, int max) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }
        return array;
    }

    // Сохранение в CSV
    private static void saveToCSV(String filename, List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.println(line);
            }
            System.out.println("Файл " + filename + " успешно создан");
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла: " + e.getMessage());
        }
    }

    // Генерация CSV-данных
    private static List<String> generateCSVData(String operation, List<Long> times, List<Long> ops) {
        List<String> lines = new ArrayList<>();
        // Заголовок
        lines.add("Operation,Index,Time(ns),Operations");

        // Данные
        for (int i = 0; i < times.size(); i++) {
            lines.add(String.format("%s,%d,%d,%d",
                    operation, i+1, times.get(i), ops.get(i)));
        }

        // Статистика
        double avgTime = times.stream().mapToLong(Long::longValue).average().orElse(0);
        double avgOps = ops.stream().mapToLong(Long::longValue).average().orElse(0);
        lines.add(String.format("%s Summary,,%.2f,%.2f", operation, avgTime, avgOps));

        return lines;
    }

    public static void main(String[] args) {
        // 1. Генерация случайного массива из 10000 чисел
        int[] data = generateRandomArray(10000, 0, 100000);
        AATreeCSVAnalysis tree = new AATreeCSVAnalysis();

        // 2. Тестирование вставки
        List<Long> insertTimes = new ArrayList<>();
        List<Long> insertOps = new ArrayList<>();

        System.out.println("Тестирование вставки...");
        for (int key : data) {
            tree.resetCounters();
            long startTime = System.nanoTime();
            tree.insert(key);
            long endTime = System.nanoTime();
            insertTimes.add(endTime - startTime);
            insertOps.add(tree.insertOperations);
        }
        saveToCSV("insert_results.csv", generateCSVData("Insert", insertTimes, insertOps));

        // 3. Тестирование поиска (100 случайных элементов)
        List<Long> searchTimes = new ArrayList<>();
        List<Long> searchOps = new ArrayList<>();
        Random random = new Random();

        System.out.println("Тестирование поиска...");
        for (int i = 0; i < 100; i++) {
            int index = random.nextInt(data.length);
            int key = data[index];

            tree.resetCounters();
            long startTime = System.nanoTime();
            boolean found = tree.search(key);
            long endTime = System.nanoTime();

            searchTimes.add(endTime - startTime);
            searchOps.add(tree.searchOperations);
        }
        saveToCSV("search_results.csv", generateCSVData("Search", searchTimes, searchOps));

        // 4. Тестирование удаления (1000 случайных элементов)
        List<Long> deleteTimes = new ArrayList<>();
        List<Long> deleteOps = new ArrayList<>();
        Set<Integer> removedIndices = new HashSet<>();

        System.out.println("Тестирование удаления...");
        for (int i = 0; i < 1000; i++) {
            int index;
            do {
                index = random.nextInt(data.length);
            } while (removedIndices.contains(index));

            removedIndices.add(index);
            int key = data[index];

            tree.resetCounters();
            long startTime = System.nanoTime();
            tree.delete(key);
            long endTime = System.nanoTime();

            deleteTimes.add(endTime - startTime);
            deleteOps.add(tree.deleteOperations);
        }
        saveToCSV("delete_results.csv", generateCSVData("Delete", deleteTimes, deleteOps));

        // 5. Сводная статистика
        List<String> summaryLines = new ArrayList<>();
        summaryLines.add("Operation,Avg Time(ns),Avg Ops,Max Time,Min Time");

        summaryLines.add(String.format("Insert,%.2f,%.2f,%d,%d",
                average(insertTimes), average(insertOps),
                Collections.max(insertTimes), Collections.min(insertTimes)));

        summaryLines.add(String.format("Search,%.2f,%.2f,%d,%d",
                average(searchTimes), average(searchOps),
                Collections.max(searchTimes), Collections.min(searchTimes)));

        summaryLines.add(String.format("Delete,%.2f,%.2f,%d,%d",
                average(deleteTimes), average(deleteOps),
                Collections.max(deleteTimes), Collections.min(deleteTimes)));

        saveToCSV("summary_stats.csv", summaryLines);

        // 6. Вывод в консоль
        System.out.println("\n=== Сводная статистика ===");
        System.out.println("Операция\tСр. время\tСр. операции\tМакс время\tМин время");
        System.out.printf("Вставка\t\t%.2f\t%.2f\t%d\t%d\n",
                average(insertTimes), average(insertOps),
                Collections.max(insertTimes), Collections.min(insertTimes));
        System.out.printf("Поиск\t\t%.2f\t%.2f\t%d\t%d\n",
                average(searchTimes), average(searchOps),
                Collections.max(searchTimes), Collections.min(searchTimes));
        System.out.printf("Удаление\t%.2f\t%.2f\t%d\t%d\n",
                average(deleteTimes), average(deleteOps),
                Collections.max(deleteTimes), Collections.min(deleteTimes));
    }

    private static double average(List<Long> list) {
        return list.stream().mapToLong(Long::longValue).average().orElse(0);
    }
}