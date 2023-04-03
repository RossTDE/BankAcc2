import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.io.*;

class Bank extends ArrayList<Client> { //creating class Bank that will carry all methods

    public void GetUsers(String Filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(Filename))) {
            String line;
            int counter = 0; //counts what parameter is taken
            String[] info = new String[10]; //array of all 6 parameters
            
            while ((line = br.readLine()) != null) { //reads each line
                if(counter == 10) {
                    counter = 0; //returning counter back
                    this.add(new Client(Integer.parseInt(info[0])));
                } else if(line == '.') {
                    info[counter] = null;
                } else {
                    info[counter] = line; //taking parameter
                    counter++; //moving counter
                }
                
            }

        } catch (IOException e) { //if reading was not successful
            e.printStackTrace();
        }
    }

    public void UpdateUsers(String Filename) {
        try(FileWriter writer = new FileWriter(Filename)) {
            for(int i = 0; i < this.size(); i++) {
                writer.write(this.get(i).ToString()); //writes info to the file
                writer.append('.');

                writer.flush();
            }
        } catch(IOException ex) { 
            System.out.println(ex.getMessage()); //informs if try was not successfull
        } 
    }
    
    public int FindUser(int num) { //matches given id with database
        for(int i = 0; i < this.size(); i++) {
            if(this.get(i).getID() == num) {
                return i;
            }
        }

        return (-1); //if not found
    }

    public void PrintClients() { //method to print everything on the screen
        System.out.println("Our clients database: " + '\n');

        for(int i = 0; i < this.size(); i++) { //all info about all clients
            System.out.println(this.get(i).ToString());
        }
    }

    public void set_user(Scanner scan) { //method to create new client using user input
        String name, surname, adress, password;
        
        System.out.print("Your first name: "); //setting up each value
        name = scan.nextLine();
        System.out.print("Your last name: ");
        surname =scan.nextLine();
        System.out.print("Your adress: ");
        adress = scan.nextLine();
        System.out.print("Your adress: ");
        password = scan.nextLine();

        this.add(new Client(name, surname, adress, password)); //adding new client to the list
    }

    public int SignIn(Scanner scan) {
        int i;
        String pass;

        while(true) { //until user found
            System.out.print("Enter your bank ID: ");

            i = this.FindUser(scan.nextInt());
            if(i != -1) {
                break;
            }

            System.out.println("We do not have any account with this id!");
        }

            
        for(int t = 0; t < 5; t++) { //until password is correct(5 tries)
            System.out.print("Enter your password: ");
            scan.nextLine();
            pass = scan.next();
    
            if(pass.equals(this.get(i).getPassword())) {
                return i;
            }
    
            System.out.println("Password is not correct! You have " + (4-t) + " tries.");
        }

        return -1;
    }

    public int Welcome_Screen(Scanner scan) {
        int index = -1;

        while(true) {
            System.out.println("Welcome to the Scotia Bank\n" + 
                               "Enter 1 for \"Sign Up\" or 2 for \"Log In\". Other number to exit.");
            switch(scan.nextInt()) {
                case 1:
                    this.set_user(scan);
                    System.out.println("Your bank id is: " + this.get(this.size()).getID());
                    break;
                case 2:
                    index = this.SignIn(scan);
                    if(index != -1) {
                        return index;
                    }
                    break;
                default:
                    return -1;
                    break;
            }
        }
    }

    public void UI(Scanner scan, int index) {
        int option = 0;

        do {
            System.out.println("Choose your action: ");
            System.out.println("1 - Deposit.\n" + "2 - Withdrawal.\n" + "3 - My Banking.\n" + "4 - Exit\n");
            
            System.out.print("Enter option: ");
            option = scan.nextInt();
            switch(option) {
                case 1:
                    if(this.Deposit(scan, index)) { 
                        System.out.println("Deposit succeed!\nYour current balance: " + this.get(index).getBalance() + "\n");
                    } else {
                        System.out.println("Failed!"); //if Deposit returned false
                    }
                    break;
                case 2:
                    if(this.Withdrawal(scan, index)) { 
                        System.out.println("Withdrawal succeed!\nYour current balance: " + this.get(index).getBalance() + "\n");
                    } else {
                        System.out.println("Failed!"); //if Withdrawal returned false
                    }
                    break;
                case 3: 
                    if(this.ChangeInfo(scan, index)) { 
                        System.out.println("Info has been changed!"); //if info was changed
                    }
                    System.out.println();
                    break;
            }
        } while(option != 4); //when 4 is pressed - exit

        this.PrintClients(); //after exit printing all clients
    }

    public boolean Deposit(Scanner scan, int i) {
        System.out.print("Enter value to deposit: ");
        int money = scan.nextInt();

        if(money > 0) { //avoiding negative deposits
            this.get(i).setBalance(this.get(i).getBalance() + money);
            return true;
        }

        return false;
    }

    public boolean Withdrawal(Scanner scan, int i) {
        System.out.print("Enter ID to withdraw: ");
        int ID = scan.nextInt();
        int i2 = this.FindUser(ID);

        if(i2 == -1 || i2 == i) { //if such user is not fount or it is the same 
            return false;
        }

        
        System.out.print("Enter value you want to send: ");
        int money = scan.nextInt();

        if(money > 0 || this.get(i).getBalance() > money) { //cheking if we have enough money
            this.get(i2).setBalance(this.get(i2).getBalance() + money);
            this.get(i).setBalance(this.get(i).getBalance() - money);

            return true;
        }

        return false;
    }

    public boolean ChangeInfo(Scanner scan, int i) { //method to review and change info about this customer
        System.out.println(this.get(i).toString());

        System.out.println("To change information: ");
        System.out.println("1 - First Name\n" + "2 - Last Name.\n" + "3 - Adress.\n" + "4 - Exit\n");

        System.out.print("Enter option: ");
        int option = scan.nextInt();
        

        switch(option) { //menu to change information
            case 1:
                System.out.print("Enter your new first name: ");
                this.get(i).setFirstName(scan.next());
                break;
            case 2:
                System.out.print("Enter your new last name: ");
                this.get(i).setLastName(scan.next());
                break;
            case 3:
                System.out.print("Enter your new adress: ");
                this.get(i).setAdress(scan.next());
                break;
            case 4: return false; //exit menu with no changes
        }

        return true;
    }
}

class Client {
    private int ID;
    private Account chequing;
    private Account savings;
    private String password;
    private String first_name;
    private String last_name;
    private String adress;

    public Client(String frst, String lst, String adrs, String pswd, boolean acctype) {
        this.first_name = frst;
        this.last_name = lst;
        this.adress = adrs;
        this.password = pswd;

        Random rand = new Random();
        this.ID = rand.nextInt(1000000, 10000000);

        this.chequing = null;
        this.savings = null;

        switch(acctype) {
            case 0:
                this.chequing = new Account(rand.nextInt(1000000, 10000000), 0, 0);
                break;
            case 1:
                this.savings = new Account(rand.nextInt(1000000, 10000000), 1, 0);
                break;
        }
    }

    public Client(int id, String frst, String lst, String adrs, String pswd, 
                  int id_check, boolean acctype1, int blnc_check,
                  int id_save, boolean acctype2, int blnc_save) 
    {
        this.ID = id;
        this.first_name = frst;
        this.last_name = lst;
        this.adress = adrs;
        this.password = pswd;

        if(id_check == null) {
            this.chequing = null;
        } else if(id_save == null) {
            this.savings = null;
        } else {
            this.chequing = new Account(id_check, acctype1, blnc_check);
            this.savings = new Account(id_save, acctype2, blnc_save);
        }
        
    }

    /* GET METHODS */

    public int getID() { return this.ID; }

    public int getBalance() { return this.balance; }

    public String getFirstName() { return this.first_name; }

    public String getLastName() { return this.last_name; }

    public String getAdress() { return this.adress; }

    public String getPassword() { return this.password; }

    /* SET METHODS */

    public void setBalance(int blnc) { this.balance = blnc; }

    public void setFirstName(String frst) { this.first_name = frst; }

    public void setLastName(String lst) { this.last_name = lst; }

    public void setAdress(String adrs) { this.adress = adrs; }

    public void setPhone(String pswd) { this.password = pswd; }


    public String ToString() { //returns whole string of client information
        String str = this.ID+ " \n" +
        this.first_name + " " + this.last_name + " \n" + 
        this.adress + " \n" + 
        this.balance + " \n";

        return str;
    }

}

class Account {
    private int ID;
    private boolean acctype;
    private int balance;

    public Account(int id, boolean acc, int blnc) {
        this.ID = id;
        this.acctype = acc;
        this.balance = blnc;
    }
}

public class BankAccount2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Bank scotia = new Bank(); //Custom ArrayList of clients

        scotia.GetUsers("ScotiaBank.txt");
        scotia.PrintClients();
        scotia.UI(scan, scotia.SignIn(scan)); //calling bank

        scan.close();
    }
}