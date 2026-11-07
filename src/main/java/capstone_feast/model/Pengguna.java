
package capstone_feast.model;

public abstract class Pengguna {

    private int id;
    private String namaPengguna;
    private String username;
    private String statusAkun;
    private String nomorTelepon;

    // Constructor
    public Pengguna(int id, String namaPengguna, String username, String statusAkun, String nomorTelepon) {
        this.id = id;
        this.namaPengguna = namaPengguna;
        this.username = username;
        this.statusAkun = statusAkun;
        this.nomorTelepon = nomorTelepon;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatusAkun() {
        return statusAkun;
    }

    public void setStatusAkun(String statusAkun) {
        this.statusAkun = statusAkun;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public abstract String getRole();
}
