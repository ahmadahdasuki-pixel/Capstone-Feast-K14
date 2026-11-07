package capstone_feast.model;

public class Produk {
    private int idProduk;
    private int idAdmin;
    private String namaProduk;
    private String satuanProduk;
    private double hargaJual;
    private String statusProduk;

    public Produk() {
    }

    public Produk(int idProduk, int idAdmin, String namaProduk, String satuanProduk, double hargaJual, String statusProduk) {
        this.idProduk = idProduk;
        this.idAdmin = idAdmin;
        this.namaProduk = namaProduk;
        this.satuanProduk = satuanProduk;
        this.hargaJual = hargaJual;
        this.statusProduk = statusProduk;
    }

    // Getter dan Setter
    public int getIdProduk() { return idProduk; }
    public void setIdProduk(int idProduk) { this.idProduk = idProduk; }

    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public String getSatuanProduk() { return satuanProduk; }
    public void setSatuanProduk(String satuanProduk) { this.satuanProduk = satuanProduk; }

    public double getHargaJual() { return hargaJual; }
    public void setHargaJual(double hargaJual) { this.hargaJual = hargaJual; }

    public String getStatusProduk() { return statusProduk; }
    public void setStatusProduk(String statusProduk) { this.statusProduk = statusProduk; }

    @Override
    public String toString() {
        return namaProduk;
    }
}