import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Kampanya ve indirim kodu yönetimi.
 */
public class KampanyaYonetimi {
    private final Map<String, Kampanya> kampanyalar;

    public KampanyaYonetimi() {
        this.kampanyalar = new HashMap<>();
        // Varsayılan kampanyalar
        varsayilanKampanyalarEkle();
    }

    private void varsayilanKampanyalarEkle() {
        ekle(new Kampanya("HOSGELDIN", "Yeni müşteri %10 indirim",
                Kampanya.KampanyaTipi.YUZDE_INDIRIM, 10, 0, 1));
        ekle(new Kampanya("YUZELLI",   "150₺ üzeri 20₺ indirim",
                Kampanya.KampanyaTipi.MIN_TUTAR_INDIRIM, 20, 150, 0));
        ekle(new Kampanya("YUZDE20",   "%20 indirim (min 200₺)",
                Kampanya.KampanyaTipi.YUZDE_INDIRIM, 20, 200, 0));
    }

    public void ekle(Kampanya k) {
        kampanyalar.put(k.getKod(), k);
    }

    public Kampanya bul(String kod) {
        return kampanyalar.get(kod.toUpperCase());
    }

    public void hepsiniListele() {
        if (kampanyalar.isEmpty()) {
            System.out.println("  Aktif kampanya yok.");
            return;
        }
        System.out.println("  ┌── KAMPANYALAR ─────────────────────────────────────────────────────┐");
        kampanyalar.values().forEach(k -> System.out.println("  │ " + k));
        System.out.println("  └────────────────────────────────────────────────────────────────────┘");
    }

    public Collection<Kampanya> getTumKampanyalar() { return kampanyalar.values(); }
}
