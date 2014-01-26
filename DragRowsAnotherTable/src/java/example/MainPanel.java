package example;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.activation.*;
import javax.swing.*;
import javax.swing.table.*;

public class MainPanel extends JPanel {
    TransferHandler handler = new TableRowTransferHandler();
    String[] columnNames = {"String", "Integer", "Boolean"};
    Object[][] data = {
        {"AAA", 12, true}, {"aaa", 1, false},
        {"BBB", 13, true}, {"bbb", 2, false},
        {"CCC", 15, true}, {"ccc", 3, false},
        {"DDD", 17, true}, {"ddd", 4, false},
        {"EEE", 18, true}, {"eee", 5, false},
        {"FFF", 19, true}, {"fff", 6, false},
        {"GGG", 92, true}, {"ggg", 0, false}
    };
    private JTable makeDnDTable() {
        JTable t = new JTable(new DefaultTableModel(data, columnNames) {
            @Override public Class<?> getColumnClass(int column) {
                // ArrayIndexOutOfBoundsException:  0 >= 0
                // Bug ID: JDK-6967479 JTable sorter fires even if the model is empty
                // http://bugs.sun.com/view_bug.do?bug_id=6967479
                //return getValueAt(0, column).getClass();
                switch(column) {
                  case 0:
                    return String.class;
                  case 1:
                    return Number.class;
                  case 2:
                    return Boolean.class;
                  default:
                    return super.getColumnClass(column);
                }
            }
        });
        t.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        t.setTransferHandler(handler);
        t.setDropMode(DropMode.INSERT_ROWS);
        t.setDragEnabled(true);
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);

        //Disable row Cut, Copy, Paste
        ActionMap map = t.getActionMap();
        AbstractAction dummy = new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {}
        };
        map.put(TransferHandler.getCutAction().getValue(Action.NAME),   dummy);
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),  dummy);
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME), dummy);
        return t;
    }
    public MainPanel() {
        super(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(2,1));
        p.add(new JScrollPane(makeDnDTable()));
        p.add(new JScrollPane(makeDnDTable()));
        p.setBorder(BorderFactory.createTitledBorder("Drag & Drop JTable"));
        add(p);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setPreferredSize(new Dimension(320, 240));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | InstantiationException |
               IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class TableRowTransferHandler extends TransferHandler {
    private int[] rows    = null;
    private int addIndex  = -1;
    private int addCount  = 0;
    private final DataFlavor localObjectFlavor;
    private JComponent source = null;
    public TableRowTransferHandler() {
        super();
        localObjectFlavor = new ActivationDataFlavor(Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");
    }
    @Override protected Transferable createTransferable(JComponent c) {
        source = c;
        JTable table = (JTable) c;
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        ArrayList<Object> list = new ArrayList<Object>();
        rows = table.getSelectedRows();
        for(int i: rows) {
            list.add(model.getDataVector().elementAt(i));
        }
        Object[] transferedObjects = list.toArray();
        return new DataHandler(transferedObjects, localObjectFlavor.getMimeType());
    }
    @Override public boolean canImport(TransferSupport info) {
        JTable t = (JTable)info.getComponent();
        boolean b = info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
        //XXX bug?
        t.setCursor(b?DragSource.DefaultMoveDrop:DragSource.DefaultMoveNoDrop);
        return b;
    }
    @Override public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
    @Override public boolean importData(TransferSupport info) {
        JTable target = (JTable)info.getComponent();
        JTable.DropLocation dl  = (JTable.DropLocation)info.getDropLocation();
        DefaultTableModel model = (DefaultTableModel)target.getModel();
        int index = dl.getRow();
        int max = model.getRowCount();
        if(index<0 || index>max) {
            index = max;
        }
        addIndex = index;
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        try{
            Object[] values = (Object[])info.getTransferable().getTransferData(localObjectFlavor);
            if(source==target) { addCount = values.length; }
            for(int i=0;i<values.length;i++) {
                int idx = index++;
                model.insertRow(idx, (Vector)values[i]);
                target.getSelectionModel().addSelectionInterval(idx, idx);
            }
            return true;
        }catch(UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @Override protected void exportDone(JComponent c, Transferable t, int act) {
        cleanup(c, act == MOVE);
    }
    private void cleanup(JComponent src, boolean remove) {
        if(remove && rows != null) {
            JTable table = (JTable)src;
            src.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            if(addCount > 0) {
                for(int i=0;i<rows.length;i++) {
                    if(rows[i]>=addIndex) {
                        rows[i] += addCount;
                    }
                }
            }
            for(int i=rows.length-1;i>=0;i--) {
                model.removeRow(rows[i]);
            }
        }
        rows     = null;
        addCount = 0;
        addIndex = -1;
    }
}
