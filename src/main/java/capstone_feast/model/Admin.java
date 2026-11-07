
package capstone_feast.model;

import java.sql.Date;

public class Admin extends Pengguna {

    private Date tanggalDiangkat;

    public Admin(int id, String namaPengguna, String username, String statusAkun, String nomorTelepon, Date tanggalDiangkat) {
        super(id, namaPengguna, username, statusAkun, nomorTelepon);
        this.tanggalDiangkat = tanggalDiangkat;
    }

    // Getter dan Setter
    public Date getTanggalDiangkat() {
        return tanggalDiangkat;
    }

    public void setTanggalDiangkat(Date tanggalDiangkat) {
        this.tanggalDiangkat = tanggalDiangkat;
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
