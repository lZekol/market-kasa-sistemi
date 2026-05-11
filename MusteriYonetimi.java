import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Müşteri kayıt ve yönetim sistemi.
 */
public class MusteriYonetimi {
    private final Map<Integer, Musteri> musteriler;

    public MusteriYonetimi() {
        this.musteriler = new HashMap<>();
    }

    public void ekle(Musteri m) {
        musteriler.put(m.getId(), m);
        System.out.printf("  ✅ Müşteri kaydedildi: [%d] %s%n", m.getId(), m.tamAd());
    }

    public Musteri bul(int id) {
        return musteriler.get(id);
    }

    /** Telefona göre bul */
    public Optional<Musteri> telefonIleBul(String telefon) {
        return musteriler.values().stream()
                .filter(m -> m.getTelefon().equals(telefon))
                .findFirst();
    }

    public void hepsiniListele() {
        if (musteriler.isEmpty()) {
            System.out.println("  Henüz müşteri yok.");
            return;
        }
        System.out.println("  ── MÜŞTERİ LİSTESİ ────────────────────────────────────────────────");
        musteriler.values().forEach(m -> System.out.println("  │ " + m));
        System.out.println("  ────────────────────────────────────────────────────────────────────");
    }

    public Collection<Musteri> getTumMusteriler() { return musteriler.values(); }
}
