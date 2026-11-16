import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarksImporter {

    private static final Logger LOGGER =
            Logger.getLogger(MarksImporter.class.getName());

    static class DuplicateStudentException
            extends RuntimeException {
        public DuplicateStudentException(String message) {
            super(message);
        }
    }

    static void importMarks(String filename) throws IOException {

        Set<String> seen = new HashSet<>();

        // Automatically closes the reader, even if an exception occurs.
        try (BufferedReader br =
                new BufferedReader(new FileReader(filename))) {

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                try {
                    String[] parts = line.split(",", -1);

                    if (parts.length != 2) {
                        throw new IllegalArgumentException(
                                "Expected exactly 2 fields.");
                    }

                    String id = parts[0].trim();
                    int mark = Integer.parseInt(parts[1].trim());

                    if (id.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Student ID cannot be empty.");
                    }

                    if (mark < 0 || mark > 100) {
                        throw new IllegalArgumentException(
                                "Mark must be between 0 and 100.");
                    }

                    if (!seen.add(id)) {
                        throw new DuplicateStudentException(
                                "Duplicate student ID: " + id);
                    }

                    System.out.println(
                            "ACCEPTED: " + id + " = " + mark);

                } catch (NumberFormatException e) {
                    System.out.println(
                            "INVALID: Non-numeric mark on line "
                            + lineNumber + ". Record skipped.");
                    LOGGER.log(Level.WARNING,
                            "Invalid numeric mark", e);

                } catch (DuplicateStudentException e) {
                    System.out.println(
                            "DUPLICATE: " + e.getMessage());
                    LOGGER.log(Level.WARNING,
                            "Duplicate student", e);

                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "INVALID: " + e.getMessage()
                            + " Record skipped.");
                    LOGGER.log(Level.WARNING,
                            "Invalid record", e);
                }
            }
        }
    }

    static void showTheoryAnswers() {
        System.out.println("\n===== ACTIVITY A =====");

        System.out.println(
                "1. parseInt(\"ABC\"): NumberFormatException, "
                + "unchecked. Validate or catch the input.");

        System.out.println(
                "2. name.length() when name is null: "
                + "NullPointerException, unchecked. "
                + "Check for null first.");

        System.out.println(
                "3. FileReader(\"missing.txt\"): "
                + "FileNotFoundException, checked. "
                + "Catch it or declare throws.");

        System.out.println(
                "OutOfMemoryError: The JVM may not have "
                + "enough memory to continue safely.");

        System.out.println("\n===== ACTIVITY D =====");

        System.out.println(
                "1. IOException is checked. "
                + "throws IOException declares it.");

        System.out.println(
                "2. throw raises an exception. "
                + "throws declares possible exceptions.");

        System.out.println(
                "3. Friendly messages help users; "
                + "technical logs help developers.");

        System.out.println(
                "4. Empty catch hides errors. "
                + "Catching Exception first makes a later "
                + "FileNotFoundException catch unreachable.");

        System.out.println(
                "\nRecovery strategy: Invalid records are "
                + "skipped and processing continues.");
    }

    public static void main(String[] args) {

        String filename = args.length > 0
                ? args[0] : "marks.txt";

        showTheoryAnswers();

        System.out.println("\n===== IMPORT RESULTS =====");

        try {
            importMarks(filename);

        } catch (FileNotFoundException e) {
            System.out.println(
                    "Marks file not found; check the file name.");
            LOGGER.log(Level.SEVERE,
                    "File not found", e);

        } catch (IOException e) {
            System.out.println(
                    "A file-reading problem occurred.");
            LOGGER.log(Level.SEVERE,
                    "I/O error", e);

        } finally {
            System.out.println(
                    "Import attempt finished");
        }
    }
}
