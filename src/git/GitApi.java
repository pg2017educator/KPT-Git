package git;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public abstract class GitApi {
    private String owner = "pg2017educator";
    private String token = "99fe834d28" + "f86c7c5c6a" + "0bb1be3845" + "59fecd7a93";
    private String repos = "kpt-git-db";

    static {
        try {
            // ホスト名の検証を行なわない
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession ses) {
                    return true;
                }
            });

            // 証明書の検証を行わない
            TrustManager[] tm = {
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };
            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, tm, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected String getBasicAuthHeadder() {
        return "Basic " + Base64.getEncoder().encodeToString(new String(owner + ":" + token).getBytes());
    }

    protected String getBaseUrl() {
        return "https://api.github.com/repos/" + owner + "/" + repos;
    }

    /**
     * リクエストのBody部をサーバーに送る
     * @param client
     * @param param
     * @throws IOException
     */
    protected void sendReqBody(HttpsURLConnection client, StringBuffer param) throws IOException {
        OutputStream os = client.getOutputStream();
        os.write(param.toString().getBytes());
        os.close();
    }

    protected String encodeContent(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }
}
