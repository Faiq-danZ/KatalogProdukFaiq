package controller;

import data.ProdukRepository;
import db.Database;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import model.Produk;

public class KatalogController {

    @FXML private TextField namaField;
    @FXML private TextField hargaField;
    @FXML private ComboBox<String> fotoBox;
    @FXML private Button simpanButton;
    @FXML private Button batalButton;
    @FXML private Label jumlahLabel;
    @FXML private TilePane katalogPane;

    private final ProdukRepository repository = new ProdukRepository();
    private Produk produkSedangDiedit = null;

    @FXML
    public void initialize() {
        Database.init();

        // Mengisi daftar pilihan gambar sesuai file di folder resources/image
        fotoBox.getItems().addAll("Kopi.png", "Roti.jpg", "Sabun.jpg", "Sampo.jpg", "Susu.jpg");
        fotoBox.getSelectionModel().selectFirst();

        katalogPane.setHgap(14);
        katalogPane.setVgap(14);
        katalogPane.setStyle("-fx-padding: 16; -fx-background-color: #CBD5E1;");

        loadKatalog();
    }

    @FXML
    private void onSimpan() {
        String nama = namaField.getText().trim();
        String hargaText = hargaField.getText().trim();
        String foto = fotoBox.getValue();

        if (nama.isEmpty() || hargaText.isEmpty() || foto == null) {
            tampilkanPeringatan("Input Belum Lengkap", "Nama produk, harga, dan foto wajib diisi.");
            return;
        }

        int harga;
        try {
            harga = Integer.parseInt(hargaText);
            if (harga < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            tampilkanPeringatan("Harga Tidak Valid", "Masukkan harga berupa angka bulat positif.");
            return;
        }

        // Cek apakah sedang mode tambah data baru atau update data lama
        if (produkSedangDiedit == null) {
            repository.insert(new Produk(nama, harga, foto));
        } else {
            produkSedangDiedit.setNama(nama);
            produkSedangDiedit.setHarga(harga);
            produkSedangDiedit.setFoto(foto);
            repository.update(produkSedangDiedit);
        }

        resetForm();
        loadKatalog();
    }

    @FXML
    private void onBatal() {
        resetForm();
    }

    private void resetForm() {
        namaField.clear();
        hargaField.clear();
        fotoBox.getSelectionModel().selectFirst();
        produkSedangDiedit = null;

        simpanButton.setText("Simpan");
        simpanButton.setStyle("-fx-background-color: #0f766e; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
    }

    private void loadKatalog() {
        katalogPane.getChildren().clear();
        List<Produk> daftarProduk = repository.findAll();

        for (Produk p : daftarProduk) {
            katalogPane.getChildren().add(buatKartu(p));
        }

        jumlahLabel.setText("Jumlah Produk: " + daftarProduk.size());
    }

    // Membuat komponen kartu produk secara dinamis untuk dimasukkan ke TilePane
    private VBox buatKartu(Produk p) {
        VBox kartu = new VBox(8);
        kartu.setPrefWidth(185);
        kartu.setAlignment(Pos.TOP_LEFT);
        kartu.setStyle("-fx-background-color: white; -fx-padding: 12; -fx-background-radius: 10; "
                     + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);");

        ImageView imageView = new ImageView();
        InputStream stream = getClass().getResourceAsStream("/image/" + p.getFoto());
        if (stream != null) {
            imageView.setImage(new Image(stream));
        }
        imageView.setFitWidth(160);
        imageView.setFitHeight(105);
        imageView.setPreserveRatio(true);

        Label namaLbl = new Label(p.getNama());
        namaLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1e293b;");
        namaLbl.setWrapText(true);

        Label hargaLbl = new Label("Rp" + p.getHarga());
        hargaLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");

        Label idLbl = new Label("ID: " + p.getId());
        idLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #94a3b8;");

        Button btnEdit = new Button("Edit");
        btnEdit.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        btnEdit.setOnAction(e -> {
            produkSedangDiedit = p;
            namaField.setText(p.getNama());
            hargaField.setText(String.valueOf(p.getHarga()));
            fotoBox.setValue(p.getFoto());

            simpanButton.setText("Update");
            simpanButton.setStyle("-fx-background-color: #d97706; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
            namaField.requestFocus();
        });

        Button btnHapus = new Button("Hapus");
        btnHapus.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        btnHapus.setOnAction(e -> {
            Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
            konfirmasi.setTitle("Konfirmasi Hapus");
            konfirmasi.setHeaderText(null);
            konfirmasi.setContentText("Yakin ingin menghapus produk \"" + p.getNama() + "\"?");

            Optional<ButtonType> hasil = konfirmasi.showAndWait();
            if (hasil.isPresent() && hasil.get() == ButtonType.OK) {
                repository.delete(p.getId());
                if (produkSedangDiedit != null && produkSedangDiedit.getId() == p.getId()) {
                    resetForm();
                }
                loadKatalog();
            }
        });

        HBox barisTombol = new HBox(8, btnEdit, btnHapus);
        kartu.getChildren().addAll(imageView, namaLbl, hargaLbl, idLbl, barisTombol);
        return kartu;
    }

    private void tampilkanPeringatan(String judul, String pesan) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}