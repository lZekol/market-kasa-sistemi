import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Kasa sistemi — ödeme alır, fiş üretir, günlük rapor tutar.
 */
public class Kasa {
    private static int fisNo = 1000;

    private final int kasaNo;
    private final List<String> gunlukFisler;
    private double gunlukCiro;
    private int gunlukIslemSayisi;

    public Kasa(int kasaNo) {
        this.kasaNo = kasaNo;
        this.gunlukFisler = new ArrayList<>();
        this.gunlukCiro = 0;
        this.gunlukIslemSayisi = 0;
    }

    /**
     * Ana ödeme ve fatura işlemi.
     * Polimorfizm: Odeme arayüzü sayesinde Nakit veya Kart fark etmez.
     */
    public boolean odemeAl(Sepet sepet, Odeme odeme, Musteri musteri) {
        if (sepet.bosmu()) {
            System.out.println("  ❌ Sepet boş!");
            return false;
        }

        double tutar = sepet.genelToplam();

        // Puan indirimi (müşteri varsa)
        double puanIndirimi = 0;
        if (musteri != null) {
            System.out.printf("  💳 %s — Mevcut puan: %d%n",
                    musteri.tamAd(), musteri.getSadakatPuani());
            // Bu noktada konsol menüsü puan kullanımını sorabilir
        }

        double odenecek = tutar - puanIndirimi;

        if (!odeme.odemeYap(odenecek)) {
            System.out.println("  ❌ Ödeme başarısız!");
            return false;
        }

        // Stokları güncelle
        for (java.util.Map.Entry<Integer, int[]> e : sepet.getUrunler().entrySet()) {
            Urun u = sepet.getUrunDetay().get(e.getKey());
            u.stokDus(e.getValue()[0]);
        }

        // Kampanya kullanım sayısını artır
        if (sepet.getUygulanenKampanya() != null) {
            sepet.getUygulanenKampanya().kullan();
        }

        // Fiş oluştur
        String fis = fisDuzenle(sepet, odeme, musteri, odenecek);
        gunlukFisler.add(fis);
        gunlukCiro += odenecek;
        gunlukIslemSayisi++;

        // Müşteri güncelle
        if (musteri != null) {
            musteri.alisverisYap(odenecek, "Fiş #" + fisNo);
        }

        System.out.println(fis);

        // Nakit üstlük
        if (odeme instanceof Nakit) {
            Nakit n = (Nakit) odeme;
            if (n.getUstluk() > 0) {
                System.out.printf("  💰 Üstlük: %.2f₺%n", n.getUstluk());
            }
        }

        sepet.temizle();
        return true;
    }

    private String fisDuzenle(Sepet sepet, Odeme odeme, Musteri musteri, double odenecek) {
        fisNo++;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("\n  ╔═══════════════════════════════════════════════════════╗\n");
        sb.append(  "  ║              MARKET KASA SİSTEMİ — FİŞ               ║\n");
        sb.append(  "  ╠═══════════════════════════════════════════════════════╣\n");
        sb.append(String.format("  ║  Fiş No: %-10d  Kasa: %-5d  Tarih: %-16s║%n",
                fisNo, kasaNo, LocalDateTime.now().format(fmt)));
        if (musteri != null) {
            sb.append(String.format("  ║  Müşteri: %-45s║%n", musteri.tamAd()));
        }
        sb.append(  "  ╠═══════════════════════════════════════════════════════╣\n");
        for (java.util.Map.Entry<Integer, int[]> e : sepet.getUrunler().entrySet()) {
            Urun u    = sepet.getUrunDetay().get(e.getKey());
            int adet  = e.getValue()[0];
            double st = u.indirimliFilyat() * adet;
            sb.append(String.format("  ║  %-30s x%2d  %7.2f₺  ║%n", u.getAd(), adet, st));
        }
        sb.append(  "  ╠═══════════════════════════════════════════════════════╣\n");
        sb.append(String.format("  ║  Ara Toplam: %42.2f₺  ║%n", sepet.araToplam()));
        if (sepet.kampanyaIndirimi() > 0) {
            sb.append(String.format("  ║  Kampanya İndirimi: %35.2f₺- ║%n", sepet.kampanyaIndirimi()));
        }
        sb.append(String.format("  ║  ÖDENEN TUTAR: %39.2f₺  ║%n", odenecek));
        sb.append(String.format("  ║  Ödeme: %-47s║%n", odeme.ozet()));
        sb.append(  "  ╚═══════════════════════════════════════════════════════╝");
        return sb.toString();
    }

    /** Günlük satış raporu */
    public void gunlukRaporYazdir() {
        System.out.println("\n  ╔════════════════════════════════════╗");
        System.out.println(  "  ║         GÜNLÜK SATIŞ RAPORU        ║");
        System.out.println(  "  ╠════════════════════════════════════╣");
        System.out.printf(   "  ║  Tarih      : %-20s║%n",
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        System.out.printf(   "  ║  Kasa No    : %-20d║%n", kasaNo);
        System.out.printf(   "  ║  İşlem Sayısı: %-19d║%n", gunlukIslemSayisi);
        System.out.printf(   "  ║  Günlük Ciro: %-19.2f₺║%n", gunlukCiro);
        System.out.println(  "  ╚════════════════════════════════════╝");
    }

    public int getKasaNo()              { return kasaNo; }
    public double getGunlukCiro()       { return gunlukCiro; }
    public int getGunlukIslemSayisi()   { return gunlukIslemSayisi; }
    public List<String> getGunlukFisler() { return gunlukFisler; }
}
