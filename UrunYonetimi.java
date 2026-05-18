import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ürün envanteri yönetimi.
 */
public class UrunYonetimi {
    private final Map<Integer, Urun> urunler;

    public UrunYonetimi() {
        this.urunler = new HashMap<>();
    }

    public void ekle(Urun u) {
        urunler.put(u.getId(), u);
        System.out.printf("  ✅ Ürün eklendi: [%d] %s%n", u.getId(), u.getAd());
    }

    public boolean sil(int id) {
        if (!urunler.containsKey(id)) {
            System.out.println("  ❌ Ürün bulunamadı: " + id);
            return false;
        }
        System.out.println("  🗑️  Silindi: " + urunler.get(id).getAd());
        urunler.remove(id);
        return true;
    }

    public Urun bul(int id) {
        return urunler.get(id);
    }

    /** Ada göre arama (büyük/küçük harf duyarsız) */
    public List<Urun> ara(String anahtar) {
        String k = anahtar.toLowerCase();
        return urunler.values().stream()
                .filter(u -> u.getAd().toLowerCase().contains(k)
                        || u.getKategori().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    /** Kategoriye göre listele */
    public List<Urun> kategoriFiltre(String kategori) {
        return urunler.values().stream()
                .filter(u -> u.getKategori().equalsIgnoreCase(kategori))
                .collect(Collectors.toList());
    }

    /** Stok uyarısı — stok < esik olan ürünler */
    public List<Urun> kritikStok(int esik) {
        return urunler.values().stream()
                .filter(u -> u.getStok() <= esik)
                .collect(Collectors.toList());
    }

    public void hepsiniListele() {
        if (urunler.isEmpty()) {
            System.out.println("  Henüz ürün yok.");
            return;
        }
        System.out.println(" ÜRÜN LİSTESİ ");
        urunler.values().forEach(u -> System.out.println("  │ " + u));
        System.out.println(" ");
    }

    public Map<Integer, Urun> getUrunler() { return urunler; }
    public int toplamUrunSayisi()           { return urunler.size(); }
}

