/**
 * Temizlik ürünlerini temsil eder.
 */
public class Temizlik extends Urun {
    private String icerik;       // kimyasal içerik uyarısı
    private double hacimMl;
    private boolean ekolojik;

    public Temizlik(String ad, double fiyat, int stok, double hacimMl) {
        super(ad, fiyat, stok, "Temizlik");
        this.hacimMl = hacimMl;
        this.icerik = "Standart";
        this.ekolojik = false;
    }

    public Temizlik(String ad, double fiyat, int stok, double hacimMl,
                    String icerik, boolean ekolojik) {
        this(ad, fiyat, stok, hacimMl);
        this.icerik = icerik;
        this.ekolojik = ekolojik;
    }

    @Override
    public String detayliAciklama() {
        return String.format("🧹 TEMİZLİK | %s | %.0f ml | İçerik: %s%s | Fiyat: %.2f₺",
                getAd(), hacimMl, icerik, ekolojik ? " | EKOLOJİK" : "", getFiyat());
    }

    public double getHacimMl()          { return hacimMl; }
    public void setHacimMl(double h)    { this.hacimMl = h; }
    public String getIcerik()           { return icerik; }
    public void setIcerik(String i)     { this.icerik = i; }
    public boolean isEkolojik()         { return ekolojik; }
    public void setEkolojik(boolean e)  { this.ekolojik = e; }
}
