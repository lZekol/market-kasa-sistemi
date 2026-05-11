/**
 * Soyut temel sınıf — tüm ürünlerin ortak özelliklerini tanımlar.
 */
public abstract class Urun {
    private static int sayac = 1000;

    private final int id;
    private String ad;
    private double fiyat;
    private int stok;
    private String kategori;
    private double indirimYuzdesi; // 0-100 arası

    public Urun(String ad, double fiyat, int stok, String kategori) {
        this.id = ++sayac;
        this.ad = ad;
        this.fiyat = fiyat;
        this.stok = stok;
        this.kategori = kategori;
        this.indirimYuzdesi = 0;
    }

    // Soyut metot: her alt sınıf kendi açıklamasını verir
    public abstract String detayliAciklama();

    // İndirimli fiyat hesapla
    public double indirimliFilyat() {
        return fiyat * (1 - indirimYuzdesi / 100.0);
    }

    public boolean stokDus(int miktar) {
        if (stok >= miktar) {
            stok -= miktar;
            return true;
        }
        return false;
    }

    public void stokEkle(int miktar) {
        if (miktar > 0) stok += miktar;
    }

    // Getters & Setters
    public int getId()                        { return id; }
    public String getAd()                     { return ad; }
    public void setAd(String ad)              { this.ad = ad; }
    public double getFiyat()                  { return fiyat; }
    public void setFiyat(double fiyat)        { if (fiyat >= 0) this.fiyat = fiyat; }
    public int getStok()                      { return stok; }
    public String getKategori()               { return kategori; }
    public void setKategori(String k)         { this.kategori = k; }
    public double getIndirimYuzdesi()         { return indirimYuzdesi; }
    public void setIndirimYuzdesi(double y)   { if (y >= 0 && y <= 100) this.indirimYuzdesi = y; }

    @Override
    public String toString() {
        String indirimInfo = indirimYuzdesi > 0
                ? String.format(" [%%%.0f İNDİRİM → %.2f₺]", indirimYuzdesi, indirimliFilyat())
                : "";
        return String.format("[%d] %-25s | %8.2f₺%s | Stok: %d | %s",
                id, ad, fiyat, indirimInfo, stok, kategori);
    }
}
