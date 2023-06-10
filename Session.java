import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Session {
    void setSession(String username) {
        try {
            FileWriter myWriter = new FileWriter("session.txt");
            myWriter.write(username);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    String getSessionUser() {
        try {
            File myObj = new File("session.txt");
            try (Scanner myReader = new Scanner(myObj)) {
                String data = myReader.nextLine();
                return data;
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
}
