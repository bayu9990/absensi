
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;


public class rekapbulan extends javax.swing.JFrame {

    private final Connection conn; 

    public rekapbulan() {
        initComponents();
        conn = koneksi.getKoneksi();
        setTitle("Rekap Bulanan");
        setTanggalPertemuan(pertemuan);
        setKelas(kelas);
        setNama(nama);
    }
    private void setTanggalPertemuan(JComboBox<String> comboBox) {
    comboBox.addItem("--Pilih--"); // Add the default selection item
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();

    for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = sdf.format(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = sdf.format(cal.getTime());

        String dateRange = startDate + " - " + endDate;
        comboBox.addItem(dateRange);
    }
}

    private void setKelas(JComboBox<String> comboBox) {
    comboBox.addItem("--Pilih--"); // Add the default selection item
    String sql = "SELECT DISTINCT kelas FROM siswa WHERE kelas IS NOT NULL";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String kelas = rs.getString("kelas");
            comboBox.addItem(kelas);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void setNama(JComboBox<String> comboBox) {
    comboBox.addItem("--Pilih--"); // Add the default selection item
    String sql = "SELECT DISTINCT nama FROM siswa WHERE nama IS NOT NULL";

    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String nama = rs.getString("nama");
            comboBox.addItem(nama);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    
    private void tampilkanDataAbsensi(String selectedKelas, String selectedDateRange) {
    String[] dateRange = selectedDateRange.split(" - ");
    String startDate = dateRange[0];
    String endDate = dateRange[1];

    String sql = "SELECT s.nis, s.nama, "
               + "SUM(CASE WHEN a.ket = 'hadir' THEN 1 ELSE 0 END) AS Hadir, "
               + "SUM(CASE WHEN a.ket = 'izin' THEN 1 ELSE 0 END) AS Izin, "
               + "SUM(CASE WHEN a.ket = 'sakit' THEN 1 ELSE 0 END) AS Sakit, "
               + "SUM(CASE WHEN a.ket = 'alpha' THEN 1 ELSE 0 END) AS Alpha "
               + "FROM siswa s "
               + "LEFT JOIN absen a ON s.nis = a.nis "
               + "LEFT JOIN pertemuan p ON a.id_pertemuan = p.id_pertemuan "
               + "WHERE s.kelas = ? AND p.tgl BETWEEN ? AND ? "
               + "GROUP BY s.nis, s.nama";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, selectedKelas);
        ps.setString(2, startDate);
        ps.setString(3, endDate);

        ResultSet rs = ps.executeQuery();
        DefaultTableModel model = (DefaultTableModel) table2.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            Object[] row = {
                rs.getString("nis"),
                rs.getString("nama"),
                rs.getInt("Hadir"),
                rs.getInt("Izin"),
                rs.getInt("Sakit"),
                rs.getInt("Alpha")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void tampilkanDataAbsensiPerKelas(String selectedKelas, String selectedDateRange) {
    String[] dateRange = selectedDateRange.split(" - ");
    String startDate = dateRange[0];
    String endDate = dateRange[1];

    String sql = "SELECT a.ket AS Keterangan, COUNT(a.ket) AS JumlahTotal "
               + "FROM absen a "
               + "JOIN siswa s ON a.nis = s.nis "
               + "JOIN pertemuan p ON a.id_pertemuan = p.id_pertemuan "
               + "WHERE s.kelas = ? AND p.tgl BETWEEN ? AND ? "
               + "GROUP BY a.ket";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, selectedKelas);
        ps.setString(2, startDate);
        ps.setString(3, endDate);

        ResultSet rs = ps.executeQuery();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Clear existing data

        while (rs.next()) {
            Object[] row = {
                rs.getString("Keterangan"),
                rs.getInt("JumlahTotal")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void tampilkanDataAbsensiPerSiswa(String selectedNama, String selectedKelas, String selectedDateRange) {
    String[] dateRange = selectedDateRange.split(" - ");
    String startDate = dateRange[0];
    String endDate = dateRange[1];

    String sql = "SELECT s.nis, s.nama, "
               + "SUM(CASE WHEN a.ket = 'hadir' THEN 1 ELSE 0 END) AS Hadir, "
               + "SUM(CASE WHEN a.ket = 'izin' THEN 1 ELSE 0 END) AS Izin, "
               + "SUM(CASE WHEN a.ket = 'sakit' THEN 1 ELSE 0 END) AS Sakit, "
               + "SUM(CASE WHEN a.ket = 'alpha' THEN 1 ELSE 0 END) AS Alpha "
               + "FROM siswa s "
               + "LEFT JOIN absen a ON s.nis = a.nis "
               + "LEFT JOIN pertemuan p ON a.id_pertemuan = p.id_pertemuan "
               + "WHERE s.nama = ? AND s.kelas = ? AND p.tgl BETWEEN ? AND ? "
               + "GROUP BY s.nis, s.nama";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, selectedNama);
        ps.setString(2, selectedKelas);
        ps.setString(3, startDate);
        ps.setString(4, endDate);

        ResultSet rs = ps.executeQuery();
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); // Clear existing data

        while (rs.next()) {
            Object[] row = {
                rs.getString("nis"),
                rs.getString("nama"),
                rs.getInt("Hadir"),
                rs.getInt("Izin"),
                rs.getInt("Sakit"),
                rs.getInt("Alpha")
            };
            model.addRow(row);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    private void setTable1ColumnsForClassAggregation() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setColumnIdentifiers(new Object[] { "Keterangan", "Jumlah Total" });
    }
private void setTable1ColumnsForStudentDetails() {
    DefaultTableModel model = (DefaultTableModel) table1.getModel();
    model.setColumnIdentifiers(new Object[] { "NIS", "Nama", "Hadir", "Izin", "Sakit", "Alpha" });
}


    private void clearTable1() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
    }

    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pertemuan = new javax.swing.JComboBox<>();
        submit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        kelas = new javax.swing.JComboBox<>();
        nama = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        submit1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pertemuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pertemuanActionPerformed(evt);
            }
        });

        submit.setText("Submit");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nama");

        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIS", "Nama", "Hadir", "Izin", "Sakit", "Alpha"
            }
        ));
        jScrollPane2.setViewportView(table2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Kelas");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Pertemuan");

        kelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kelasActionPerformed(evt);
            }
        });

        nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(71, 110, 110));
        jPanel1.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REKAP BULANAN");

        submit1.setBackground(new java.awt.Color(56, 87, 87));
        submit1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submit1.setForeground(new java.awt.Color(255, 255, 255));
        submit1.setText("Kembali");
        submit1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submit1MouseClicked(evt);
            }
        });
        submit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(submit1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(340, 340, 340))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(submit1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel2))
                            .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(kelas, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(74, 74, 74)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(113, 113, 113)
                                .addComponent(pertemuan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(37, 37, 37)))))
                .addGap(110, 110, 110))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(410, 410, 410))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pertemuan, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(kelas, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pertemuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pertemuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pertemuanActionPerformed

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
     String selectedKelas = (String) kelas.getSelectedItem();
    String selectedNama = (String) nama.getSelectedItem();
    String selectedDateRange = (String) pertemuan.getSelectedItem();

    if ("--Pilih--".equals(selectedDateRange)) {
        selectedDateRange = null;
    }
    if ("--Pilih--".equals(selectedKelas)) {
        selectedKelas = null;
    }
    if ("--Pilih--".equals(selectedNama)) {
        selectedNama = null;
    }

    clearTable1(); // Clear the table first

    if (selectedDateRange != null) {
        tampilkanDataAbsensi(selectedKelas, selectedDateRange);
        if (selectedNama != null && selectedKelas != null) {
            setTable1ColumnsForStudentDetails();
            tampilkanDataAbsensiPerSiswa(selectedNama, selectedKelas, selectedDateRange);
        } else if (selectedKelas != null) {
            setTable1ColumnsForClassAggregation();
            tampilkanDataAbsensiPerKelas(selectedKelas, selectedDateRange);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih nama kelas atau siswa.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih rentang tanggal.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_submitActionPerformed

    private void kelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kelasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kelasActionPerformed

    private void namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaActionPerformed

    private void submit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submit1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_submit1ActionPerformed

    private void submit1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submit1MouseClicked
         absen home = new absen();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_submit1MouseClicked

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
            java.util.logging.Logger.getLogger(rekapbulan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(rekapbulan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(rekapbulan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(rekapbulan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new rekapbulan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> kelas;
    private javax.swing.JComboBox<String> nama;
    private javax.swing.JComboBox<String> pertemuan;
    private javax.swing.JButton submit;
    private javax.swing.JButton submit1;
    private javax.swing.JTable table1;
    private javax.swing.JTable table2;
    // End of variables declaration//GEN-END:variables
}
