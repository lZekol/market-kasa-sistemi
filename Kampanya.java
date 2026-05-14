public class Kampanya {
    public enum KampanyaTipi {
        YUZDE_INDIRIM,      // % indirim
        SABIT_INDIRIM,      // sabit TL indirim
        UCRETSIZ_URUN,      // 3 al 2 öde vb.
        MIN_TUTAR_INDIRIM   // X₺ üzeri Y₺ indirim
    }

    private final String kod;
    private final String aciklama;
    private final KampanyaTipi tip;
    private final double deger;          // yüzde veya TL miktarı
    private final double minTutar;       // minimum sepet tutarı (0 = yok)
    private boolean aktif;
    private int kullanimSayisi;
    private final int maxKullanim;       // 0 = sınırsız

    public Kampanya(String kod, String aciklama, KampanyaTipi tip,
                    double deger, double minTutar, int maxKullanim) {
        this.kod = kod.toUpperCase();
        this.aciklama = aciklama;
        this.tip = tip;
        this.deger = deger;
        this.minTutar = minTutar;
        this.aktif = true;
        this.kullanimSayisi = 0;
        this.maxKullanim = maxKullanim;
    }

    /**
      Kampanyayı sepet tutarına uygula.
     */
    public double indirimHesapla(double sepetTutari) {
        if (!aktif) return 0;
        if (maxKullanim > 0 && kullanimSayisi >= maxKullanim) return 0;
        if (sepetTutari < minTutar) return 0;

        switch (tip) {
            case YUZDE_INDIRIM:
                return sepetTutari * (deger / 100.0);
            case SABIT_INDIRIM:
                return Math.min(deger, sepetTutari); // sepetten fazla indirim olmaz
            case MIN_TUTAR_INDIRIM:
                return sepetTutari >= minTutar ? deger : 0;
            default:
                return 0;
        }
    }

    public void kullan() {
        kullanimSayisi++;
    }

    public boolean gecerliMi(double sepetTutari) {
        return aktif
                && (maxKullanim == 0 || kullanimSayisi < maxKullanim)
                && sepetTutari >= minTutar;
    }

    public String getKod()          { return kod; }
    public String getAciklama()     { return aciklama; }
    public boolean isAktif()        { return aktif; }
    public void setAktif(boolean a) { this.aktif = a; }
    public int getKullanimSayisi()  { return kullanimSayisi; }

    @Override
    public String toString() {
        String durumu = aktif ? "✅ AKTİF" : "❌ PASİF";
        String limitInfo = maxKullanim > 0
                ? String.format("(%d/%d kez)", kullanimSayisi, maxKullanim)
                : "(Sınırsız)";
        return String.format("[%s] %s | %s | Min: %.0f₺ | %s %s",
                kod, aciklama, tipAcikla(), minTutar, durumu, limitInfo);
    }

    private String tipAcikla() {
        switch (tip) {
            case YUZDE_INDIRIM:     return String.format("%%%.0f indirim", deger);
            case SABIT_INDIRIM:     return String.format("%.0f₺ indirim", deger);
            case MIN_TUTAR_INDIRIM: return String.format("%.0f₺ indirim", deger);
            default:                return "Özel kampanya";
        }
    }
}
