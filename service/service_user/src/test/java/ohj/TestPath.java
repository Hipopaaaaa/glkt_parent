package ohj;

public class TestPath {
    public static void main(String[] args) {
        String url="http://hipopaaaaqian.gz2vip.91tunnel.com/course/1";
        int index = url.indexOf("com/");
        System.out.println(url.substring(index + 4));
    }
}
