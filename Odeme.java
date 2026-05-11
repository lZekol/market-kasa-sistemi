/**
 * Ödeme yöntemlerinin uyması gereken sözleşme.
 */
public interface Odeme {
    /**
     * Ödemeyi gerçekleştirir.
     * @param tutar ödenecek miktar
     * @return işlem başarılıysa true
     */
    boolean odemeYap(double tutar);

    /**
     * Ödeme yöntemi adı (makbuz için)
     */
    String odemeYontemi();

    /**
     * Kısa özet bilgi
     */
    String ozet();
}
