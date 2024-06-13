package org.example;

import java.sql.*;
import java.util.Scanner;

public class jdbc_bank_application {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bank";
        String username = "root";
        String password = "Anu123@#";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            Scanner sc = new Scanner(System.in);
            System.out.println(" 1. Create a new account  \n 2. Create More Than One Account\n 3. Check Balance\n 4. Deposit the amount \n 5. Withdraw the amount  \n 6. Watch demo account \n 7. Exit  \n ");
            boolean  choices;
            do{
                System.out.println("Select the options");
                int choice = sc.nextInt();
                switch (choice)
                {
                    case 1:
                        try {
                            System.out.println("Enter the account number");
                            int accno = sc.nextInt();
                            if (String.valueOf(accno).length() != 5) {
                                System.out.println("Error: Account number must be exactly 5 digits.");
                                break;
                            }

                            System.out.println("Enter the name");
                            String name = sc.next();
                            System.out.println("Enter the  acc_type");
                            String acc_type = sc.next();
                            System.out.println("Enter the balance");
                            String balance = sc.next();
                            int rowCount = statement.executeUpdate("insert into bankdatabase values(" + accno + ",'" + name + "','" + acc_type + "','" + balance + "')");
                            if (rowCount > 0) {
                                System.out.println("data inserted :");
                            } else {
                                System.out.println("data insertion failed");
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {


                            String sql = "INSERT INTO bankdatabase (accno, name, acc_type, balance) VALUES (?, ?, ?, ?)";

                            PreparedStatement preparedStatement = connection.prepareStatement(sql);


                            System.out.println("Enter the number of records to insert:");
                            int numberOfRecords = sc.nextInt();

                            for (int i = 0; i < numberOfRecords; i++) {
                                System.out.println("Enter details for record " + (i + 1) + ":");
                                System.out.println("Account number:");
                                int accno = sc.nextInt();
                                if (String.valueOf(accno).length() != 5) {
                                    System.out.println("Error: Account number must be exactly 5 digits.");
                                }

                                System.out.println("Name:");
                                String name = sc.next();
                                System.out.println("Account type:");
                                String acc_type = sc.next();
                                System.out.println("Balance:");
                                int balance = sc.nextInt();

                                preparedStatement.setInt(1, accno);
                                preparedStatement.setString(2, name);
                                preparedStatement.setString(3, acc_type);
                                preparedStatement.setInt(4, balance);

                                preparedStatement.addBatch();
                            }

                            int[] batchResult = preparedStatement.executeBatch();

                            int totalRowCount = 0;
                            for (int i : batchResult) {
                                if (i == Statement.SUCCESS_NO_INFO || i > 0) {
                                    totalRowCount += i;
                                }
                            }

                            System.out.println("Total " + totalRowCount + " rows inserted.");

                            preparedStatement.close();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            System.out.println("Enter the accno for balance checking");
                            int a = sc.nextInt();

                            // Execute the SELECT query to check if the account exists and get its balance
                            ResultSet resultSet = statement.executeQuery("SELECT balance FROM bankdatabase WHERE accno=" + a);

                            // Check if any rows were returned by the SELECT query
                            if (resultSet.next()) {
                                // Account exists
                                System.out.println("Data found");
                                int balance = resultSet.getInt("balance");
                                System.out.println("AVAILABLE BALANCE IN ACCOUNT NUMBER " + a + " is : " + balance);
                            } else {
                                // Account does not exist
                                System.out.println("Account number " + a + " is not found");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        break;

                    case 4:
                        try {
                            System.out.println("Enter the accno for deposit");
                            int a = sc.nextInt(); // Using int instead of Integer

                            // Execute the SELECT query to check if the account exists
                            ResultSet resultSet = statement.executeQuery("SELECT balance FROM bankdatabase WHERE accno=" + a);

                            // Check if any rows were returned by the SELECT query
                            if (resultSet.next()) {
                                // Account exists
                                System.out.println("Data found");
                                int c = resultSet.getInt("balance");
                                System.out.println("AVAILABLE BALANCE IN ACCOUNT NUMBER " + a + " is : " + c);
                                System.out.println("Enter the amount to be deposited");
                                int deposit = sc.nextInt();
                                c += deposit;
                                String d = Integer.toString(c);
                                long rowCount = statement.executeLargeUpdate("UPDATE bankdatabase SET balance='" + d + "' WHERE accno=" + a);

                                if (rowCount > 0) {
                                    System.out.println("Money deposited successfully, actual balance is " + c);
                                } else {
                                    System.out.println("Money deposit failed");
                                }
                            } else {
                                // Account does not exist
                                System.out.println("Account number " + a + " does not exist");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try {
                            System.out.println("Enter the accno for withdraw");
                            int a = sc.nextInt(); // Using int instead of Integer

                            ResultSet resultSet = statement.executeQuery("SELECT balance FROM bankdatabase WHERE accno=" + a);
                            if (resultSet.next()) {
                                System.out.println("Data found");
                                int balance = resultSet.getInt("balance");
                                System.out.println("AVAILABLE BALANCE IN ACCOUNT NUMBER " + a + " is : " + balance);

                                System.out.println("Enter the amount to be withdrawn");
                                int withdrawAmount = sc.nextInt();

                                if (withdrawAmount <= balance) {
                                    balance -= withdrawAmount;
                                    statement.executeUpdate("UPDATE bankdatabase SET balance='" + balance + "' WHERE accno=" + a);
                                    System.out.println("Money withdrawal successful. Actual balance is " + balance);
                                } else {
                                    System.out.println("Not Enough Amount");
                                }
                            } else {
                                System.out.println("Account number " + a + " does not exist");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;

                    case 6:
                        System.out.println("Demo Account");
                        System.out.println("Name of the account holder :: demouser");
                        System.out.println("Account number             :: 87541");
                        System.out.println("Account Type               :: Saving");
                        System.out.println("Balance                    :: 848484");
                        break;
                    case 7:
                        break;
                }
                System.out.println("Enter Your choice ");
                choices=sc.nextBoolean();
            }while (choices);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
