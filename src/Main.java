import javax.swing.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        // Try with resources för att strömmarna ska stängas per automatik
        try (BufferedReader buff = new BufferedReader(new FileReader("customers.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("trackingclients.txt", true));) {

            // Variabler som används i gymsystemet
            String input = JOptionPane.showInputDialog("Skriv in namn eller personnummer:");
            String line = buff.readLine().trim();
            String dateString = "";
            String[] person;
            Date date;

            // Variabel som kontrollerar om användaren hittas i systemet eller ej
            boolean identity = false;

            // Fortsätter så länge den inlästa raden inte är tom
            while ( !line.equalsIgnoreCase("") ) {
                person = line.split(", ");

                // Läser in datumsträngen
                dateString = buff.readLine();

                // Kontrollerar om användaren finns i systemet
                if (input.equalsIgnoreCase(person[0]) || input.equalsIgnoreCase(person[1])) {
                    identity = true;
                    System.out.println("[" + person[0] + "] " + person[1] + " finns i systemet.");

                    date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

                    Date today = cal.getTime();
                    cal.add(Calendar.YEAR, -1);
                    Date lastYear = cal.getTime();

                    // Kontrollerar om användaren är en aktiv medlem eller en gammal kund
                    if (lastYear.compareTo(date) < 0) {
                        String str = person[0] + " " + person[1] + " " + formatter.format(today);

                        writer.write(str);
                        writer.newLine();

                        System.out.println("Nuvarande medlem");
                    }
                    else {
                        System.out.println("Föredetta kund");
                    }

                }

                // Läsa in nästa rad
                line = buff.readLine().trim();
            }

            // Om användaren inte fanns i systemet
            if(!identity){
                System.out.println("Du finns inte i systemet och är obehörig till gymmet.");

            }
        }

        // Catch-satser för att ta emot flera typer av fel som kastas
        catch (FileNotFoundException fnfe){
            System.out.println("Cannot read from file.");
        }
        catch (IOException ex) {
            System.out.println("Could not close file.");
        }
        catch (ParseException pe) {
            System.out.println("Could not parse date.");
        }
        catch (NullPointerException npex) {
            System.out.println("No information entered.");
        }
    }
}
