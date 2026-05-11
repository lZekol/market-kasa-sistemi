/**
  Elektronik ürünlerini temsil eder.
 */
public class Elektronik extends Urun {
    private String marka;
    private int garantiAy;
    private String model;

    public Elektronik(String ad, double fiyat, int stok, String marka, int garantiAy) {
        super(ad, fiyat, stok, "Elektronik");
        this.marka = marka;
        this.garantiAy = garantiAy;
        this.model = "";
    }

    public Elektronik(String ad, double fiyat, int stok, String marka, int garantiAy, String model) {
        this(ad, fiyat, stok, marka, garantiAy);
        this.model = model;
    }

    @Override
    public String detayliAciklama() {
        return String.format("📱 ELEKTRONİK | %s %s (%s) | Fiyat: %.2f₺ | Garanti: %d ay",
                marka, getAd(), model, getFiyat(), garantiAy);
    }

    public String getMarka()            { return marka; }
    public void setMarka(String m)      { this.marka = m; }
    public int getGarantiAy()           { return garantiAy; }
    public void setGarantiAy(int g)     { this.garantiAy = g; }
    public String getModel()            { return model; }
    public void setModel(String m)      { this.model = m; }
}
