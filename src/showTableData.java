import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class showTableData {
    public void simpleMessage(){
        //Input fields for username and password
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        //Creating a panel for the fields
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(new BoxLayout(panelLogin, BoxLayout.Y_AXIS));
        panelLogin.add(new JLabel("Username: "));
        panelLogin.add(usernameField);
        panelLogin.add (new JLabel("Password: "));
        panelLogin.add(passwordField);

        //Show dialog box
        int option = JOptionPane.showConfirmDialog(null, panelLogin, "Enter MySQL database username and password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION){
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String url = "jdbc:mysql://localhost:3306/login_schema";

            try(Connection connection = DriverManager.getConnection(url, username, password)){

                JOptionPane.showMessageDialog(null, "Connection successful");

//                //Add frame for button
//                JFrame queryButtonFrame = new JFrame("Query Button");
//                queryButtonFrame.setSize(200, 75);
//
//                //Add query button for frame
//                JButton queryButton = new JButton("Run query");
//                queryButtonFrame.add(queryButton);
//
//                queryButton.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        queryMySQL();//<-method to run
//                    }
//                });



                String query = "SELECT * FROM login_schema.users";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                JTable table = new JTable(showTableData(resultSet));

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(400, 400));

                JOptionPane.showMessageDialog(null, scrollPane, "Query result", JOptionPane.PLAIN_MESSAGE);

            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,"Connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }else{
            JOptionPane.showMessageDialog(null, "Login canceled.");
        }
    }
    public static DefaultTableModel showTableData(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        //How to create column headers
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        //How to populate the table
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getObject(i);
            }
            model.addRow(rowData);
        }
        return model;
    }
}
