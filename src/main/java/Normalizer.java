import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Normalizer {
    /**
     * 住所データAPIのエンドポイント
     */
    public String endpoint = "https://geolonia.github.io/japanese-addresses/api/ja";
    /**
     * 都道府県のコンパイル済み正規表現
     */
    private PrefectureRegex[] prefectureRegexes;

    Normalizer(String endpoint) {
        this.endpoint = endpoint;
        this.prefectureRegexes = this.getPrefectureRegexes();
    }

    /**
     * 住所を正規化します
     * @param address 住所文字列
     * @return 正規化の結果のオブジェクト
     */
    //public NormalizeResult normalize(String address) {
    public void normalize(String address) {
        // 1.まずはじめに表記の正規化を行う
        // 全角スペース→半角スペース
        address = address.replaceAll("　", " ");
        // 連続する半角スペース→半角スペース
        address = address.replaceAll(" +", " ");
        // 全角英数字→半角英数字
        char[] characters = address.toCharArray();
        for (int index = 0; index < characters.length; index++) {
            int codePoint = (int)characters[index];
           if ((0xFF10 <= codePoint && codePoint <= 0xFF19) || (0xFF21 <= codePoint && codePoint <= 0xFF3A) || (0xFF41 <= codePoint && codePoint <= 0xFF5A)) {
               codePoint = codePoint - 0xFEE0;
               characters[index] = (char)codePoint;
           }
        }
        address = characters.toString();
        // 数字の後にくる横棒はハイフンに統一する
        address = address.replaceAll("([0-9０-９一二三四五六七八九〇十百千])([-－﹣−‐⁃‑‒–—﹘―⎯⏤ーｰ─━])", "$1-");
        // 町丁目名以前のスペースはすべて削除
        address = address.replaceAll("(.+)( +)(丁目?|番(町|地|丁)|条|軒|線|(の|ノ)町|地割)", "$1$3");

        // 2.次に都道府県名の正規化を行う
        // 158行目まで読んだ https://github.com/geolonia/normalize-japanese-addresses/blob/08ac91969714ae231da464f845284f99f2505650/src/normalize.ts#L158
    }

    private PrefectureRegex[] getPrefectureRegexes() {
        PrefectureRegex[] prefectureRegexes = new PrefectureRegex[47];
        int index = 0;
        JsonNode json = Utils.fetchJson(this.endpoint + ".json");
        Iterator<String> iterPref = json.fieldNames();
        while (iterPref.hasNext()) {
            String prefName = iterPref.next();
            String prefNameWithoutPrefix = prefName.replaceAll("(都|道|府|県)$", "");
            String regex = String.format("^%s(都|道|府|県)?", prefNameWithoutPrefix);
            prefectureRegexes[index] = new PrefectureRegex(prefName, Pattern.compile(regex));
            index++;
        }
        return prefectureRegexes;
    }
}
