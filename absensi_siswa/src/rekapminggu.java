import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ASUS
 */
public class rekapminggu extends javax.swing.JFrame {

    private final Connection conn; 
    
    public rekapminggu() {
        initComponents();
        conn = koneksi.getKoneksi();
        setTitle("Rekap Mingguan");
        setTanggalPertemuan();
        setKelas(kelas);
        setNama(nama);
    }

    private void setTanggalPertemuan() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_YEAR, -7);
        String startDate = sdf.format(cal.getTime());
        String endDate = sdf.format(Calendar.getInstance().getTime());
        String dateRange = "Periode: " + startDate + " - " + endDate;

        tgl.setText(dateRange);
    }

    private void setKelas(JComboBox<String> comboBox) {
        comboBox.addItem("--Pilih--");
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
            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
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
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
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
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
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
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setColumnIdentifiers(new Object[] { "Keterangan", "Jumlah Total" });
    }

    private void setTable1ColumnsForStudentDetails() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setColumnIdentifiers(new Object[] { "NIS", "Nama", "Hadir", "Izin", "Sakit", "Alpha" });
    }

    private void clearTable1() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        kelas = new javax.swing.JComboBox<>();
        nama = new javax.swing.JComboBox<>();
        submit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        tgl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(830, 544));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Nama");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 74, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Kelas");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(661, 74, -1, -1));

        kelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kelasActionPerformed(evt);
            }
        });
        getContentPane().add(kelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(628, 105, 98, 38));

        nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaActionPerformed(evt);
            }
        });
        getContentPane().add(nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 105, 98, 38));

        submit.setText("Submit");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });
        getContentPane().add(submit, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, 98, 39));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 743, 123));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NIS", "Nama", "Hadir", "Izin", "Alpha"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 743, 110));

        jPanel1.setBackground(new java.awt.Color(71, 110, 110));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REKAP MINGGUAN");

        jButton1.setBackground(new java.awt.Color(56, 87, 87));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Kembali");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(188, 188, 188)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 821, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Pertemuan Minggu");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(337, 74, -1, -1));

        tgl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tgl.setText("Minggu");
        getContentPane().add(tgl, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        // TODO add your handling code here:
            String selectedKelas = (String) kelas.getSelectedItem();
            String selectedNama = (String) nama.getSelectedItem();

            clearTable1(); // Clear the table first

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.DAY_OF_YEAR, -7);
            String startDate = sdf.format(cal.getTime());
            String endDate = sdf.format(Calendar.getInstance().getTime());
            String dateRange = startDate + " - " + endDate;

            tampilkanDataAbsensi(selectedKelas, dateRange);
            if (selectedNama != null && selectedKelas != null) {
                setTable1ColumnsForStudentDetails();
                tampilkanDataAbsensiPerSiswa(selectedNama, selectedKelas, dateRange);
            } else if (selectedKelas != null) {
                setTable1ColumnsForClassAggregation();
                tampilkanDataAbsensiPerKelas(selectedKelas, dateRange);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Silakan pilih nama kelas atau siswa.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_submitActionPerformed
    
    private void namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_namaActionPerformed

    private void kelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kelasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kelasActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
            java.util.logging.Logger.getLogger(rekapminggu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(rekapminggu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(rekapminggu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(rekapminggu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new rekapminggu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JComboBox<String> kelas;
    private javax.swing.JComboBox<String> nama;
    private javax.swing.JButton submit;
    private javax.swing.JLabel tgl;
    // End of variables declaration//GEN-END:variables
}
