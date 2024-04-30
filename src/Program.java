import java.sql.*;
import java.util.Scanner;

public class Program{
    private final static String url = "jdbc:mysql://localhost:3306/saloondb";
    private final static String username = "root";
    private final static String password = "Lapka";
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException  e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }

        while (true) {
            System.out.println("1 - выбрать таблицу room\n2 - выбрать таблиу master\n\n 0 - выход");
            String choice = scan.next();
            if (choice.equals("0")) {
                try {
                    System.out.println("Мы попали в закрытие");
                    connection.close();
                    statement.close();
                    scan.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
                break;
            }
            if (choice.equals("1") || choice.equals("2")){
                // выбрали одну из таблиц
                System.out.println("1 - добавить запись\n2 - удалить запись\n3 - редактировать запись\n4 - показать таблицу\n\n0 - назад");
                String choice2 = scan.next();
                switch (choice2) {
                    case ("1"):     // добавляем запись
                        if (choice.equals("1")){    // если выбрали таблицу room
                            // добавляем запись в room
                            // берем данные
                            System.out.println("введите название зала: ");
                            String roomName = scan.next();

                            // добавить запись
                            try {
                                String sql = "INSERT INTO Room (Name) VALUES (?)";
                                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setString(1, roomName);
                                int rows = preparedStatement.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("запись успешно добавлена!");
                                } else {
                                    System.out.println("не удалось добавить запись.");
                                }
                            } catch (SQLException e) {
                                System.err.println("ошибка при добавлении записи в таблицу room" + e.getMessage());
                            }
                            break;
                        }
                        else {
                            // добавляем запись в master
                            System.out.println("введите зарплату и номер зала, в котором он работает: ");
                            int salary = scan.nextInt();
                            int roomId = scan.nextInt();


                            // добавить запись
                            try {
                                String sql = "INSERT INTO master (Salary, RoomId) VALUES (?,?)";
                                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                                preparedStatement.setInt(1, salary);
                                preparedStatement.setInt(2, roomId);

                                // проверка что существует зал
                                String checkRoomSQL = "SELECT COUNT(*) FROM room WHERE Id = ?";
                                PreparedStatement checkRoomStatement = connection.prepareStatement(checkRoomSQL);
                                checkRoomStatement.setInt(1, roomId);
                                ResultSet resultSet = checkRoomStatement.executeQuery();
                                resultSet.next();
                                int roomCount = resultSet.getInt(1);

                                if (roomCount == 0) {
                                    System.out.println("указанный зал не существует. невозможно добавить мастера.");
                                    break;
                                }
                                int rows = preparedStatement.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("запись успешно добавлена!");
                                } else {
                                    System.out.println("не удалось добавить запись.");
                                }
                            } catch (SQLException e) {
                                System.err.println("ошибка при добавлении записи в таблицу master" + e.getMessage());
                            }
                            break;
                        }
                    case ("2"):     // удаляем запись
                        if (choice.equals("1")){
                            // удаляем запись из room
                            System.out.println("введите номер зала, который хотите удалить: ");
                            int roomToDelete = scan.nextInt();

                            try {
                                String deleteRoomSQL = "DELETE FROM room WHERE Id = ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(deleteRoomSQL);
                                preparedStatement.setInt(1, roomToDelete);
                                int rows = preparedStatement.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("запись успешно удалена!");
                                } else {
                                    System.out.println("не удалось удалить запись.");
                                }
                            } catch (SQLException e) {
                                System.err.println("ошибка при удалении записи из таблицы room" + e.getMessage());
                            }
                            break;
                        }
                        else {
                            // удаляем запись из master
                            System.out.println("введите номер мастера, которого хотите удалить: ");
                            int masterToDelete = scan.nextInt();

                            try {
                                String deleteMasterSQL = "DELETE FROM master WHERE Id = ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(deleteMasterSQL);
                                preparedStatement.setInt(1, masterToDelete);
                                int rows = preparedStatement.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("запись успешно удалена!");
                                } else {
                                    System.out.println("не удалось удалить запись.");
                                }
                            } catch (Exception e) {
                                System.err.println("ошибка при удалении записи из таблицы master" + e.getMessage());
                            }
                            break;
                        }
                    case ("3"): // редактируем запись
                        if (choice.equals("1")){
                            // редактируем в room
                            System.out.println("введите номер зала, который хотите отредактировать: ");
                            int roomToUpdate = scan.nextInt();

                            System.out.println("введите новое название зала: ");
                            String newRoomName = scan.next();

                            try {
                                String updateRoomSQL = "UPDATE room SET Name = ? WHERE Id = ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(updateRoomSQL);
                                preparedStatement.setString(1, newRoomName);
                                preparedStatement.setInt(2, roomToUpdate);
                                int rows = preparedStatement.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("запись успешно обновлена!");
                                } else {
                                    System.out.println("не удалось обновить запись.");
                                }
                            } catch (SQLException e) {
                                System.err.println("ошибка при обновлении записи в таблице room" + e.getMessage());
                            }
                            break;
                        }
                        else {
                            // редактируем в master
                            System.out.println("введите номер мастера, которого хотите отредактировать: ");
                            int masterToUpdate = scan.nextInt();

                            System.out.println("введите новую зарплату мастера: ");
                            int newSalary = scan.nextInt();

                            System.out.println("введите новый номер зала, в котором работает мастер: ");
                            int newRoomId = scan.nextInt();

                            try {
                                String updateMasterSQL = "UPDATE master SET Salary = ?, RoomId = ? WHERE Id = ?";
                                PreparedStatement preparedStatement = connection.prepareStatement(updateMasterSQL);
                                preparedStatement.setInt(1, newSalary);
                                preparedStatement.setInt(2, newRoomId);
                                preparedStatement.setInt(3, masterToUpdate);
                                int rows = preparedStatement.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("запись успешно обновлена!");
                                } else {
                                    System.out.println("не удалось обновить запись.");
                                }
                            } catch (SQLException e) {
                                System.err.println("ошибка при обновлении записи в таблице master" + e.getMessage());
                            }
                            break;
                        }
                    case ("4"): // показываем таблицу
                        if (choice.equals("1")){  // показываем таблицу room
                            try {
                                String selectRoomSQL = "SELECT * FROM room";
                                PreparedStatement preparedStatement = connection.prepareStatement(selectRoomSQL);
                                ResultSet resultSet = preparedStatement.executeQuery();

                                // вывод результата
                                System.out.println("содержимое таблицы room:");
                                while (resultSet.next()){
                                    int roomId = resultSet.getInt("Id");
                                    String roomName = resultSet.getString("Name");
                                    System.out.println("ID: " + roomId + ", Зал: " + roomName);
                                }
                            } catch (SQLException e) {
                                System.err.println("ошибка при показе таблицы room" + e.getMessage());
                            }

                        } else {    // показываем таблицу master
                            try {
                                String selectMasterSQL = "SELECT * FROM master";
                                PreparedStatement preparedStatement = connection.prepareStatement(selectMasterSQL);
                                ResultSet resultSet = preparedStatement.executeQuery();

                                // вывод результата
                                System.out.println("Номер   Зарплата    Зал");
                                while (resultSet.next()){
                                    int masterId = resultSet.getInt("Id");
                                    int masterSalary = resultSet.getInt("Salary");
                                    int masterRoomId = resultSet.getInt("RoomId");
                                    System.out.println(masterId+"\t"+ masterSalary+"\t\t"+masterRoomId);
                                }
                            } catch (SQLException e) {
                                System.err.println("ошибка при показе таблицы master" + e.getMessage());
                            }
                        }
                        break;
                    case ("0"):     // выход
                        break;
                    default:
                        System.out.println("неизвестная команда. попробуйте еще раз.");
                }
            }
        }
    }
}
