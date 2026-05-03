import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Alışveriş sepeti — ürün + miktar eşleşmesini tutar.
 */
public class Sepet {
    // Ürün ID → [Urun, miktar]
    private final Map<Integer, int[]> urunler; // int[0]=miktar, Map value için wrapper
    private final Map<Integer, Urun>  urunDetay;
    private Kampanya uygulanenKampanya;

    public Sepet() {
        this.urunler   = new LinkedHashMap<>();
        this.urunDetay = new LinkedHashMap<>();
    }

    /**
     * Sepete ürün ekle
     */
    public boolean ekle(Urun urun, int miktar) {
        if (miktar <= 0) return false;
        if (urun.getStok() < miktar) {
            System.out.printf("  ⚠️  Yetersiz stok! '%s' için stok: %d, İstenen: %d%n",
                    urun.getAd(), urun.getStok(), miktar);
            return false;
        }
        int mevcutMiktar = urunler.containsKey(urun.getId())
                ? urunler.get(urun.getId())[0] : 0;
        int yeniToplamMiktar = mevcutMiktar + miktar;
        if (urun.getStok() < yeniToplamMiktar) {
            System.out.printf("  ⚠️  Sepetteki + yeni miktar stoğu aşıyor! Max eklenebilir: %d%n",
                    urun.getStok() - mevcutMiktar);
            return false;
        }
        urunler.put(urun.getId(), new int[]{yeniToplamMiktar});
        urunDetay.put(urun.getId(), urun);
        return true;
    }

    /**
     * Sepetten ürün çıkar (tamamını)
     */
    public boolean cikar(int urunId) {
        if (!urunler.containsKey(urunId)) return false;
        urunler.remove(urunId);
        urunDetay.remove(urunId);
        return true;
    }

    /**
     * Kampanya uygula
     */
    public void kampanyaUygula(Kampanya k) {
        if (k.gecerliMi(araToplam())) {
            this.uygulanenKampanya = k;
            System.out.printf("  ✅ Kampanya uygulandı: %s (%.2f₺ indirim)%n",
                    k.getKod(), k.indirimHesapla(araToplam()));
        } else {
            System.out.println("  ❌ Kampanya bu sepet için geçerli değil.");
        }
    }

    public void kampanyaKaldir() {
        this.uygulanenKampanya = null;
    }

    /** İndirim öncesi toplam */
    public double araToplam() {
        double toplam = 0;
        for (Map.Entry<Integer, int[]> e : urunler.entrySet()) {
            Urun u = urunDetay.get(e.getKey());
            toplam += u.indirimliFilyat() * e.getValue()[0];
        }
        return toplam;
    }

    /** Kampanya indirimi */
    public double kampanyaIndirimi() {
        if (uygulanenKampanya == null) return 0;
        return uygulanenKampanya.indirimHesapla(araToplam());
    }

    /** Ödenecek nihai tutar */
    public double genelToplam() {
        return araToplam() - kampanyaIndirimi();
    }

    public boolean bosmu() {
        return urunler.isEmpty();
    }

    public void temizle() {
        urunler.clear();
        urunDetay.clear();
        uygulanenKampanya = null;
    }

    public Map<Integer, int[]>  getUrunler()   { return urunler; }
    public Map<Integer, Urun>   getUrunDetay() { return urunDetay; }
    public Kampanya getUygulanenKampanya()     { return uygulanenKampanya; }

    public void yazdir() {
        if (bosmu()) {
            System.out.println("  Sepet boş.");
            return;
        }
        System.out.println("  ┌─────────────────────────────────────────────────────┐");
        System.out.println("  │                      SEPETİNİZ                      │");
        System.out.println("  ├─────────────────────────────────────────────────────┤");
        for (Map.Entry<Integer, int[]> e : urunler.entrySet()) {
            Urun u   = urunDetay.get(e.getKey());
            int  adet = e.getValue()[0];
            double satirToplam = u.indirimliFilyat() * adet;
            System.out.printf("  │ %-28s x%2d = %8.2f₺       │%n",
                    u.getAd(), adet, satirToplam);
        }
        System.out.println("  ├─────────────────────────────────────────────────────┤");
        System.out.printf("  │ Ara Toplam:                        %10.2f₺       │%n", araToplam());
        if (uygulanenKampanya != null) {
            System.out.printf("  │ Kampanya (%s):               -%9.2f₺       │%n",
                    uygulanenKampanya.getKod(), kampanyaIndirimi());
        }
        System.out.printf("  │ GENEL TOPLAM:                      %10.2f₺       │%n", genelToplam());
        System.out.println(" ");
    }
}
