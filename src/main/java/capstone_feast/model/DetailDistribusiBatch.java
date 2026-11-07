package capstone_feast.model;

public class DetailDistribusiBatch {
    private String idDetailDistribusi;
    private int jumlahDiambil;

    public DetailDistribusiBatch(String idDetailDistribusi, int jumlahDiambil) {
        this.idDetailDistribusi = idDetailDistribusi;
        this.jumlahDiambil = jumlahDiambil;
    }

    // Getter dan Setter
    public String getIdDetailDistribusi() { return idDetailDistribusi; }
    public void setIdDetailDistribusi(String idDetailDistribusi) { this.idDetailDistribusi = idDetailDistribusi; }

    public int getJumlahDiambil() { return jumlahDiambil; }
    public void setJumlahDiambil(int jumlahDiambil) { this.jumlahDiambil = jumlahDiambil; }
}