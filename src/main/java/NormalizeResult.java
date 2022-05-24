import java.util.Optional;

public class NormalizeResult {
    /**
     * 都道府県
     */
    public String pref;
    /**
     * 市区町村
     */
    public String city;
    /**
     * 町丁目
     */
    public String town;

    /**
     * 正規化後の住所文字列
     */
    public String addr;
    /**
     * 緯度。データが存在しない場合は null
     */
    public Optional<Double> lat;
    /**
     * 軽度。データが存在しない場合は null
     */
    public Optional<Double> lng;
    /**
     * 住所文字列をどこまで判別できたかを表す正規化レベル
     * - 0 - 都道府県も判別できなかった。
     * - 1 - 都道府県まで判別できた。
     * - 2 - 市区町村まで判別できた。
     * - 3 - 町丁目まで判別できた。
     */
    public int level;

    NormalizeResult(String pref, String city, String town, String addr, Optional<Double> lat, Optional<Double> lng, int level) {
        this.pref = pref;
        this.city = city;
        this.town = town;
        this.addr = addr;
        this.lat = lat;
        this.lng = lng;
        this.level = level;
    }
}
