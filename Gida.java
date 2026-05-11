/**
  Gıda ürünlerini temsil eder.
 */
public class Gida extends Urun {
    private String sonKullanmaTarihi;
    private boolean glutensiz;
    private boolean vegan;

    public Gida(String ad, double fiyat, int stok, String sonKullanmaTarihi) {
        super(ad, fiyat, stok, "Gıda");
        this.sonKullanmaTarihi = sonKullanmaTarihi;
        this.glutensiz = false;
        this.vegan = false;
    }

    public Gida(String ad, double fiyat, int stok, String sonKullanmaTarihi,
                boolean glutensiz, boolean vegan) {
        this(ad, fiyat, stok, sonKullanmaTarihi);
        this.glutensiz = glutensiz;
        this.vegan = vegan;
    }

    @Override
    public String detayliAciklama() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🛒 GIDA | %s | SKT: %s | Fiyat: %.2f₺",
                getAd(), sonKullanmaTarihi, getFiyat()));
        if (glutensiz) sb.append(" | GLUTENSİZ");
        if (vegan)     sb.append(" | VEGAN");
        return sb.toString();
    }

    public String getSonKullanmaTarihi() { return sonKullanmaTarihi; }
    public void setSonKullanmaTarihi(String t) { this.sonKullanmaTarihi = t; }
    public boolean isGlutensiz() { return glutensiz; }
    public void setGlutensiz(boolean g) { this.glutensiz = g; }
    public boolean isVegan() { return vegan; }
    public void setVegan(boolean v) { this.vegan = v; }
}
