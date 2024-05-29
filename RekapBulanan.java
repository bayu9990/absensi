/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.swing.JOptionPane;

public class RekapBulanan extends javax.swing.JFrame {

    /**
     * Creates new form ViewAllRecord
     */
    DefaultTableModel model;

    public RekapBulanan() {
        initComponents();
       
        
        setTitle("Rekap Bulanan"); // Menambahkan judul aplikasi
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID", "No Absen", "Nama", "Kelas", "Jenis Kelamin", "Keterangan", "Pertemuan"});
        tbl_siswa.setModel(model);
       TampilData(model);
       countAndDisplayStatus(model);
        
    }
    
    private void TampilData(DefaultTableModel model) {
        Connection connection = koneksi.getKoneksi();
        String query = "SELECT * FROM siswa";
        Statement st;
        ResultSet rs;
        
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                String id = rs.getString("id");
                String noAbsen = rs.getString("no_absen");
                String nama = rs.getString("nama");
                String kelas = rs.getString("kelas");
                String jenisKelamin = rs.getString("jenis_kelamin");
                String keterangan = rs.getString("keterangan");
                String pertemuan = rs.getString("pertemuan");
                
                model.addRow(new Object[]{id, noAbsen, nama, kelas, jenisKelamin, keterangan, pertemuan});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
public void clearTable() {
    DefaultTableModel model = (DefaultTableModel) tbl_siswa.getModel();
    model.setRowCount(0); // Clear existing table data
}

private void countAndDisplayStatus(DefaultTableModel model) {
        int hadirCount = 0; // Counter for "Hadir"
        int alphaCount = 0; // Counter for "Alpha"
        int izinCount = 0; // Counter for "Izin"

        for (int i = 0; i < model.getRowCount(); i++) {
            String keterangan = (String) model.getValueAt(i, 5); // Assuming "keterangan" is the 6th column

            if ("Hadir".equalsIgnoreCase(keterangan)) {
                hadirCount++;
            } else if ("Alpha".equalsIgnoreCase(keterangan)) {
                alphaCount++;
            } else if ("Izin".equalsIgnoreCase(keterangan)) {
                izinCount++;
            }
        }

        lbl_siswaHadir.setText(String.valueOf(hadirCount));
        lbl_siswaAlpha.setText(String.valueOf(alphaCount));
        lbl_siswaIzin.setText(String.valueOf(izinCount));
    }


// Fungsi search() dengan pesan
public void search() {
        Date uFromDate = tgl_awal.getDate();
        Date uToDate = tgl_akhir.getDate();

        if (uFromDate != null && uToDate != null) {
            if (uFromDate.compareTo(uToDate) > 0) {
                JOptionPane.showMessageDialog(this, "Tanggal Akhir tidak boleh lebih lambat dari Tanggal Awal");
            } else {
                try {
                    long time11 = uFromDate.getTime();
                    long time12 = uToDate.getTime();

                    java.sql.Date sFromDate = new java.sql.Date(time11);
                    java.sql.Date sToDate = new java.sql.Date(time12);

                    Connection con = koneksi.getKoneksi();
                    String sql = "SELECT * FROM siswa WHERE pertemuan BETWEEN ? AND ?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setDate(1, sFromDate);
                    pst.setDate(2, sToDate);

                    ResultSet rs = pst.executeQuery();

                    // Clear the table before adding new data
                    DefaultTableModel model = (DefaultTableModel) tbl_siswa.getModel();
                    model.setRowCount(0);

                    int hadirCount = 0; // Counter for "Hadir"
                    int alphaCount = 0; // Counter for "Alpha"
                    int izinCount = 0; // Counter for "Izin"

                    while (rs.next()) {
                        String id = rs.getString("id");
                        String noAbsen = rs.getString("no_absen");
                        String nama = rs.getString("nama");
                        String kelas = rs.getString("kelas");
                        String jenisKelamin = rs.getString("jenis_kelamin");
                        String keterangan = rs.getString("keterangan");
                        String pertemuan = rs.getString("pertemuan");

                        // Count the different keterangan values
                        if ("Hadir".equalsIgnoreCase(keterangan)) {
                            hadirCount++;
                        } else if ("Alpha".equalsIgnoreCase(keterangan)) {
                            alphaCount++;
                        } else if ("Izin".equalsIgnoreCase(keterangan)) {
                            izinCount++;
                        }

                        model.addRow(new Object[]{id, noAbsen, nama, kelas, jenisKelamin, keterangan, pertemuan});
                    }

                    countAndDisplayStatus(model);

         
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select both Tanggal Awal and Tanggal Akhir.");
        }
    }

    private void reset() {
            clearTable();
            TampilData(model);
            countAndDisplayStatus(model);
            tgl_awal.setDate(null);
             tgl_akhir.setDate(null);
            
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
        jdl_username = new javax.swing.JLabel();
        jdl_username3 = new javax.swing.JLabel();
        SearchIssue = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        lbl_siswaHadir = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        lbl_siswaIzin = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_siswa = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        lbl_siswaAlpha = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        SearchIssue1 = new javax.swing.JButton();
        tgl_awal = new com.toedter.calendar.JDateChooser();
        tgl_akhir = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(1366, 768));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 768));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jdl_username.setBackground(new java.awt.Color(255, 255, 255));
        jdl_username.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jdl_username.setText("Tanggal Awal :");
        jPanel1.add(jdl_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, -1, -1));

        jdl_username3.setBackground(new java.awt.Color(255, 0, 0));
        jdl_username3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jdl_username3.setText("Tanggal Akhir :");
        jPanel1.add(jdl_username3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 100, -1, -1));

        SearchIssue.setBackground(new java.awt.Color(255, 51, 51));
        SearchIssue.setForeground(new java.awt.Color(255, 255, 255));
        SearchIssue.setText("SEARCH");
        SearchIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchIssueActionPerformed(evt);
            }
        });
        jPanel1.add(SearchIssue, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 130, 140, 40));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(96, 123, 179)));

        lbl_siswaHadir.setFont(new java.awt.Font("Segoe UI Black", 1, 50)); // NOI18N
        lbl_siswaHadir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lbl_siswaHadir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_siswaHadir, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 140, 130));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Jumlah Siswa Hadir");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 150, 30));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(96, 123, 179)));

        lbl_siswaIzin.setFont(new java.awt.Font("Segoe UI Black", 1, 50)); // NOI18N
        lbl_siswaIzin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lbl_siswaIzin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_siswaIzin, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 220, 140, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Jumlah Siswa Izin");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 190, 170, 30));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbl_siswa.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tbl_siswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "No Absen", "Nama", "Kelas", "Jenis Kelamin", "Keterangan", "Tgl. Pertemuan"
            }
        ));
        jScrollPane1.setViewportView(tbl_siswa);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 720, 290));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 1370, 400));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("Jumlah Siswa Alpha");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 190, 170, 30));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(96, 123, 179)));

        lbl_siswaAlpha.setFont(new java.awt.Font("Segoe UI Black", 1, 50)); // NOI18N
        lbl_siswaAlpha.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lbl_siswaAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_siswaAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 220, 140, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("REKAP BULANAN");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, -1, -1));

        SearchIssue1.setBackground(new java.awt.Color(0, 51, 255));
        SearchIssue1.setForeground(new java.awt.Color(255, 255, 255));
        SearchIssue1.setText("RESET");
        SearchIssue1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchIssue1ActionPerformed(evt);
            }
        });
        jPanel1.add(SearchIssue1, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 200, 140, 40));
        jPanel1.add(tgl_awal, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 130, 140, 40));
        jPanel1.add(tgl_akhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 130, 140, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 770));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 900, 320));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SearchIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchIssueActionPerformed
        Date uFromDate = tgl_awal.getDate();
        Date uToDate = tgl_akhir.getDate();

        if (uFromDate == null || uToDate == null) {
            JOptionPane.showMessageDialog(this, "Pilih Tanggal Awal and Tanggal Akhir.");
            return;
        }

        if (uFromDate.compareTo(uToDate) > 0) {
            JOptionPane.showMessageDialog(this, "Tanggal Akhir tidak boleh lebih lambat dari Tanggal Awal");
            return;
        }

        clearTable();
        search();
        
    }//GEN-LAST:event_SearchIssueActionPerformed

    private void tbl_issueBookDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_issueBookDetailsMouseClicked
        
    }//GEN-LAST:event_tbl_issueBookDetailsMouseClicked

    private void SearchIssue1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchIssue1ActionPerformed
        reset();
    }//GEN-LAST:event_SearchIssue1ActionPerformed

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
            java.util.logging.Logger.getLogger(RekapBulanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RekapBulanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RekapBulanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RekapBulanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RekapBulanan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SearchIssue;
    private javax.swing.JButton SearchIssue1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jdl_username;
    private javax.swing.JLabel jdl_username3;
    private javax.swing.JLabel lbl_siswaAlpha;
    private javax.swing.JLabel lbl_siswaHadir;
    private javax.swing.JLabel lbl_siswaIzin;
    private javax.swing.JTable tbl_siswa;
    private com.toedter.calendar.JDateChooser tgl_akhir;
    private com.toedter.calendar.JDateChooser tgl_awal;
    // End of variables declaration//GEN-END:variables
}




