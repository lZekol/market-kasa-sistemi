/**
 * Nakit ödeme yöntemi.
 * Interface implementasyonu
 */
public class Nakit implements Odeme {
    private double verilenPara;
    private double para;        // kasa bakiyesi
    private double ustluk;

    public Nakit(double verilenPara) {
        this.verilenPara = verilenPara;
        this.para = verilenPara;
        this.ustluk = 0;
    }

    @Override
    public boolean odemeYap(double tutar) {
        if (para >= tutar) {
            ustluk = para - tutar;
            para = 0;
            return true;
        }
        System.out.printf("  ❌ Yetersiz nakit! Gereken: %.2f₺ | Verilen: %.2f₺%n", tutar, para);
        return false;
    }

    @Override
    public String odemeYontemi() { return "NAKİT"; }

    @Override
    public String ozet() {
        return String.format("Nakit | Verilen: %.2f₺ | Üstlük: %.2f₺", verilenPara, ustluk);
    }

    public double getUstluk()       { return ustluk; }
    public double getVerilenPara()  { return verilenPara; }
}
