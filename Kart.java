/**
 * Kredi/Banka kartı ödeme yöntemi.
 */
public class Kart implements Odeme {
    private String kartNo;
    private String kartSahibi;
    private double limit;
    private double harcanan;
    private boolean krediKarti; // true = kredi, false = banka

    public Kart(String kartNo, String kartSahibi, double limit, boolean krediKarti) {
        if (kartNo.length() != 16) throw new IllegalArgumentException("Kart numarası 16 haneli olmalı!");
        this.kartNo = kartNo;
        this.kartSahibi = kartSahibi;
        this.limit = limit;
        this.krediKarti = krediKarti;
        this.harcanan = 0;
    }

    @Override
    public boolean odemeYap(double tutar) {
        double kalanLimit = limit - harcanan;
        if (kalanLimit >= tutar) {
            harcanan += tutar;
            return true;
        }
        System.out.printf("  ❌ Yetersiz limit! Kalan limit: %.2f₺ | Gereken: %.2f₺%n",
                kalanLimit, tutar);
        return false;
    }

    @Override
    public String odemeYontemi() {
        return krediKarti ? "KREDİ KARTI" : "BANKA KARTI";
    }

    @Override
    public String ozet() {
        String maskeliNo = "** ** ** " + kartNo.substring(12);
        return String.format("%s | %s | %s | Harcanan: %.2f₺",
                odemeYontemi(), maskeliNo, kartSahibi, harcanan);
    }

    public double getKalanLimit() { return limit - harcanan; }
    public double getHarcanan()   { return harcanan; }
}
