package model;

public class Produk {
    private int id;
    private String nama;
    private int harga;
    private String foto;

    public Produk(int id, String nama, int harga, String foto) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.foto = foto;
    }

    public Produk(String nama, int harga, String foto) {
        this(0, nama, harga, foto);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}