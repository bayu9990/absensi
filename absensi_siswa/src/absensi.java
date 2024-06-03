
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
                    String keterangan = "Hadir";

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
                        rs.getString("kelas"),
                        "Hadir"
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data : " + ex.getMessage());
        }
    }

 private void presensi() {
    PreparedStatement psHadir = null;
    PreparedStatement psTotal = null;
    PreparedStatement psInsert = null;
    ResultSet rsHadir = null;
    ResultSet rsTotal = null;

    try {
        String queryHadir = "SELECT a.nis, COUNT(*) AS jumlah_hadir "
                            + "FROM absen a "
                            + "WHERE a.KET = 'hadir' "
                            + "GROUP BY a.nis";
        psHadir = conn.prepareStatement(queryHadir);
        rsHadir = psHadir.executeQuery();

        while (rsHadir.next()) {
            int nis = rsHadir.getInt("nis");
            int jumlahHadir = rsHadir.getInt("jumlah_hadir");

            String queryTotal = "SELECT COUNT(*) AS jumlah_total "
                                + "FROM absen "
                                + "WHERE nis = ? AND KET IN ('hadir', 'izin', 'alpha')";
            psTotal = conn.prepareStatement(queryTotal);
            psTotal.setInt(1, nis);
            rsTotal = psTotal.executeQuery();

            if (rsTotal.next()) {
                int jumlahTotal = rsTotal.getInt("jumlah_total");

                if (jumlahTotal > 0) {
                    double persentaseHadir = (double) jumlahHadir / jumlahTotal * 100;

                    String siswaQuery = "SELECT nama, kelas FROM siswa WHERE nis = ?";
                    PreparedStatement psSiswa = conn.prepareStatement(siswaQuery);
                    psSiswa.setInt(1, nis);
                    ResultSet rsSiswa = psSiswa.executeQuery();

                    String nama = "";
                    String kelas = "";
                    if (rsSiswa.next()) {
                        nama = rsSiswa.getString("nama");
                        kelas = rsSiswa.getString("kelas");
                    }

                    String insertQuery = "INSERT INTO persentase (nis, kelas, nama, persentase) "
                                        + "VALUES (?, ?, ?, ?) "
                                        + "ON DUPLICATE KEY UPDATE persentase = VALUES(persentase)";
                    psInsert = conn.prepareStatement(insertQuery);
                    psInsert.setInt(1, nis);
                    psInsert.setString(2, kelas);
                    psInsert.setString(3, nama);
                    psInsert.setDouble(4, persentaseHadir);
                    psInsert.executeUpdate();

                    if (rsSiswa != null) rsSiswa.close();
                    if (psSiswa != null) psSiswa.close();
                }
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rsHadir != null) rsHadir.close();
            if (rsTotal != null) rsTotal.close();
            if (psHadir != null) psHadir.close();
            if (psTotal != null) psTotal.close();
            if (psInsert != null) psInsert.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        kelas = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        submit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        date = new javax.swing.JLabel();
        pertemuan_tanggal = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(248, 249, 249));

        jPanel1.setBackground(new java.awt.Color(248, 249, 249));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Kelas");

        kelas.setBackground(new java.awt.Color(255, 255, 255));
        kelas.setForeground(new java.awt.Color(51, 51, 51));
        kelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Pilih-" }));
        kelas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        kelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        kelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kelasActionPerformed(evt);
            }
        });

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nis", "No absen", "Nama", "Kelas", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
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
        jTable1.setOpaque(false);
        jTable1.setRowHeight(25);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(5);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        submit.setBackground(new java.awt.Color(71, 110, 110));
        submit.setForeground(new java.awt.Color(255, 255, 255));
        submit.setText("Absen");
        submit.setBorder(null);
        submit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(71, 110, 110));

        date.setBackground(new java.awt.Color(255, 255, 255));
        date.setFont(new java.awt.Font("Gill Sans MT", 1, 18)); // NOI18N
        date.setForeground(new java.awt.Color(255, 255, 255));
        date.setText("Tanggal");

        pertemuan_tanggal.setBackground(new java.awt.Color(255, 255, 255));
        pertemuan_tanggal.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        pertemuan_tanggal.setForeground(new java.awt.Color(255, 255, 255));
        pertemuan_tanggal.setText("Pertemuan Tanggal");

        jButton1.setBackground(new java.awt.Color(56, 87, 87));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Kembali");
        jButton1.setBorder(null);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(256, 256, 256)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pertemuan_tanggal)
                    .addComponent(date))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pertemuan_tanggal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 812, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kelas, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
            presensi();
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> kelas;
    private javax.swing.JLabel pertemuan_tanggal;
    private javax.swing.JButton submit;
    // End of variables declaration//GEN-END:variables
}
