package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {

    private final Status status;
    private Map<String, String> headers = new HashMap<>();
    private String bodyText;
    private File bodyFile;

    public HttpResponse(Status status) {
        Objects.requireNonNull(status);
        this.status = status;
    }

    public void addHeader(String string, Object value) {
        this.headers.put(string, value.toString());
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public void setBodyFile(File bodyFile) {
        this.bodyFile = bodyFile;
    }

    /**
     * 文字列をレスポンスとして返す
     * @param out クライアントへのストリーム
     * @throws IOException
     */
    public void writeTextTo(OutputStream out) throws IOException {
        IOUtil.println(out, "HTTP/1.1 " + this.status);

        // レスポンスヘッダーを出力
        this.headers.forEach((key, value) -> {
            IOUtil.println(out, key + ": " + value);
        });

        if (this.bodyText != null) {
            IOUtil.println(out, "");
            IOUtil.print(out, this.bodyText);
        }
    }


    /**
     * ファイルの中身をレスポンスとして返す
     * @param out クライアントへのストリーム
     * @throws IOException
     */
    public void writeFileTo(OutputStream out) throws IOException {
        IOUtil.println(out, "HTTP/1.1 " + this.status);

        // レスポンスヘッダーを出力
        this.headers.forEach((key, value) -> {
            IOUtil.println(out, key + ": " + value);
        });

        if (this.bodyFile != null) {
            IOUtil.println(out, "");
            InputStreamReader reader = new InputStreamReader(new FileInputStream(bodyFile),"iso-8859-1");
            int ch = reader.read();
            while(ch != -1){
                IOUtil.print(out, (char)ch);
                System.out.print((char)ch);

                ch = reader.read();
            }
            reader.close();
        }
    }


}
