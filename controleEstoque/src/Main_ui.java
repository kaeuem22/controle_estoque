import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import static application.Queries.queryItem;
import static application.Queries.querySpecificItem;

public class Main_ui extends JFrame{
    private JTextField id_input;
    private JButton button1;
    private JLabel label_id;
    private JPanel panelMain;
    private JButton button2;
    private JTable table1;


    public Main_ui() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(button1, querySpecificItem(Integer.parseInt(id_input.getText())));
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

    }

    public JPanel getRootPanel(){
        return panelMain;
    };

    private void createTable(){
        Object[][]data = queryItem();
        table1.setModel(new DefaultTableModel(
                data,
                new String[]{"id","nome","preco","quantidade"}
        ));


        TableColumnModel columns = table1.getColumnModel();
        columns.getColumn(0).setMinWidth(200);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(1).setCellRenderer(centerRender);
        columns.getColumn(2).setCellRenderer(centerRender);
        columns.getColumn(3).setCellRenderer(centerRender);
    }

    public static void main(String[] args) {
        Main_ui m =new Main_ui();
        m.setContentPane(m.panelMain);
        m.setTitle("pesquisa");
        m.setBounds(800,300,400,400);
        m.setVisible(true);
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
