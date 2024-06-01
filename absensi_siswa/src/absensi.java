
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.LocalDate;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.TableCellEditor;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author muhro
 */
public class absensi extends javax.swing.JFrame {

    private final Connection conn;
    private final LocalDate tanggal = LocalDateTime.now().atZone(ZoneId.of("GMT+7")).toLocalDate();
    private JComboBox ket;
    private final String[] ketDesc = {"Hadir", "Izin", "Sakit", "Alfa"};

    public absensi() {
        initComponents();
        conn = koneksi.getKoneksi();
        loadClasses();
        date.setText(Days(tanggal.getDayOfWeek()) + ", " + tanggal.format(DateTimeFormatter.ofPattern("dd - MM - yyyy")));
        ket = new JComboBox(ketDesc);
    }

    private String Days(DayOfWeek dayOfWeek) {

        Map<DayOfWeek, String> hariMap = new HashMap<>();
        hariMap.put(DayOfWeek.MONDAY, "Senin");
        hariMap.put(DayOfWeek.TUESDAY, "Selasa");
        hariMap.put(DayOfWeek.WEDNESDAY, "Rabu");
        hariMap.put(DayOfWeek.THURSDAY, "Kamis");
        hariMap.put(DayOfWeek.FRIDAY, "Jumat");
        hariMap.put(DayOfWeek.SATURDAY, "Sabtu");
        hariMap.put(DayOfWeek.SUNDAY, "Minggu");

        return hariMap.get(dayOfWeek);
    }

    private void loadClasses() {
        try {
            String query = "SELECT DISTINCT kelas FROM siswa";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                kelas.addItem(rs.getString("kelas"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kelas Belum Ada");
        }
    }

    private int getSiswaId(String kelas, int absen) {
        int ids = 0;
        try {
            String query = "SELECT id FROM siswa WHERE kelas = ? AND no_absen = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, kelas);
            stmt.setInt(2, absen);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ids = rs.getInt("id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data : " + ex.getMessage());
        }
        return ids;
    }

    private ResultSet loadKeterangan(Date dates) {
        ResultSet rsAbsen = null;
        try {
            String query = "SELECT id_pertemuan,tgl FROM pertemuan WHERE tgl = ?";
            PreparedStatement smt = conn.prepareStatement(query);
            smt.setDate(1, dates);
            ResultSet rs = smt.executeQuery();
            if (rs.next()) {
                String absen = "SELECT nis,ket FROM absen WHERE id_pertemuan = ?";
                PreparedStatement smtAbsen = conn.prepareStatement(absen, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                smtAbsen.setInt(1, rs.getInt("id_pertemuan"));
                rsAbsen = smtAbsen.executeQuery();
                return rsAbsen;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data : " + ex.getMessage());
        }
        return rsAbsen;
    }

    private void loadDataByClass(String kelas) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        jTable1.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(ket));

        try {
            String query = "SELECT nis,no_absen,nama, kelas FROM siswa WHERE kelas = ? ORDER BY no_absen";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, kelas);
            ResultSet rs = stmt.executeQuery();
            ResultSet rsPertemuan = loadKeterangan(Date.valueOf(tanggal));
            if (rsPertemuan != null) {
                while (rs.next()) {
                    int id = rs.getInt("nis");
                    String keterangan = "";

                    rsPertemuan.beforeFirst();
                    while (rsPertemuan.next()) {
                        if (rsPertemuan.getInt("nis") == id) {
                            keterangan = rsPertemuan.getString("ket");
                            break;
                        }
                    }
                    model.addRow(new Object[]{
                        rs.getInt("nis"),
                        rs.getString("no_absen"),
                        rs.getString("nama"),
                        rs.getString("kelas"),
                        keterangan,});

                }
            } else {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("nis"),
                        rs.getString("no_absen"),
                        rs.getString("nama"),
                        rs.getString("kelas")
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data : " + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kelas = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        submit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pertemuan_tanggal = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        kelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Pilih-" }));
        kelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kelasActionPerformed(evt);
            }
        });

        jLabel2.setText("Kelas");

        submit.setText("Submit");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nis", "No absen", "Nama", "Kelas", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        pertemuan_tanggal.setFont(new java.awt.Font("Gill Sans MT", 1, 12)); // NOI18N
        pertemuan_tanggal.setText("Pertemuan Tanggal :");

        date.setFont(new java.awt.Font("Gill Sans MT", 1, 18)); // NOI18N
        date.setText("Tanggal");

        jButton1.setText("Kembali");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pertemuan_tanggal)
                    .addComponent(date))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(submit))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 1009, Short.MAX_VALUE))
                        .addGap(21, 21, 21))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pertemuan_tanggal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submit)
                    .addComponent(kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        String insert = "INSERT INTO pertemuan (tgl) VALUES (?) ON DUPLICATE KEY UPDATE tgl = VALUES(tgl)";
        String lastID = "SELECT id_pertemuan FROM pertemuan WHERE tgl = ?";
        String verif = "SELECT * FROM absen WHERE id_pertemuan = ? AND nis = ?";
        String insertAbsen = "INSERT INTO absen (nis, id_pertemuan, ket) VALUES (?, ?, ?)";
        String updateAbsen = "UPDATE absen SET nis = ?,ket = ? WHERE id_pertemuan = ? AND nis = ?";

        try {
            try (PreparedStatement stmtPertemuan = conn.prepareStatement(insert)) {
                stmtPertemuan.setDate(1, Date.valueOf(tanggal));
                stmtPertemuan.executeUpdate();
            }

            int idPertemuan = 0;
            try (PreparedStatement stmtLastID = conn.prepareStatement(lastID)) {
                stmtLastID.setDate(1, Date.valueOf(tanggal));
                try (ResultSet rs = stmtLastID.executeQuery()) {
                    if (rs.next()) {
                        idPertemuan = rs.getInt("id_pertemuan");
                    }
                }
            }

            for (int row = 0; row < model.getRowCount(); row++) {
                String nis = model.getValueAt(row, 0).toString();

                PreparedStatement stmtverif = conn.prepareStatement(verif);
                stmtverif.setInt(1, idPertemuan);
                stmtverif.setInt(2, Integer.parseInt(nis));
                ResultSet rsverif = stmtverif.executeQuery();

                if (!rsverif.next()) {
                    PreparedStatement stmtAbsen = conn.prepareStatement(insertAbsen);

                    TableCellEditor editor = jTable1.getColumnModel().getColumn(4).getCellEditor();
                    Component editorComponent = editor.getTableCellEditorComponent(jTable1, jTable1.getValueAt(row, 4), false, row, 4);

                    if (editorComponent instanceof JComboBox) {
                        JComboBox<String> comboBox = (JComboBox<String>) editorComponent;
                        String selectedValue = (String) comboBox.getSelectedItem();

                        stmtAbsen.setInt(1, Integer.parseInt(nis));
                        stmtAbsen.setString(3, selectedValue);
                        stmtAbsen.setInt(2, idPertemuan);

                        stmtAbsen.executeUpdate();
                    }
                } else {
                    PreparedStatement stmtAbsen = conn.prepareStatement(updateAbsen);

                    TableCellEditor editor = jTable1.getColumnModel().getColumn(4).getCellEditor();
                    Component editorComponent = editor.getTableCellEditorComponent(jTable1, jTable1.getValueAt(row, 4), false, row, 4);

                    if (editorComponent instanceof JComboBox) {
                        JComboBox<String> comboBox = (JComboBox<String>) editorComponent;
                        String selectedValue = (String) comboBox.getSelectedItem();

                        stmtAbsen.setInt(1, Integer.parseInt(nis));
                        stmtAbsen.setInt(3, idPertemuan);
                        stmtAbsen.setString(2, selectedValue);
                        stmtAbsen.setInt(4, Integer.parseInt(nis));

                        stmtAbsen.executeUpdate();
                    }
                }

            }

            JOptionPane.showMessageDialog(this, "Absen pada tanggal " + tanggal + " Disimpan");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error menyimpan absen : " + ex.getMessage());
        }
    }//GEN-LAST:event_submitActionPerformed

    private void kelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kelasActionPerformed
        // TODO add your handling code here:
        String selectedClass = kelas.getSelectedItem().toString();
        if (!selectedClass.equals("-Pilih-")) {
            loadDataByClass(selectedClass);
        }
    }//GEN-LAST:event_kelasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        absen absen = new absen();
        absen.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(absensi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new absensi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel date;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> kelas;
    private javax.swing.JLabel pertemuan_tanggal;
    private javax.swing.JButton submit;
    // End of variables declaration//GEN-END:variables
}
