package git;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ContentsApi extends GitApi {

    /**
     * 指定したパスのコンテンツを取得する
     * @param path パス
     * @return 指定したパスのコンテンツ。
     */
    public Contents getContents(String path) {
        try {
            URL url = new URL(getBaseUrl() + "/contents/" + path);
            System.out.println(url);

            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();


            client.setRequestMethod("GET");
            client.setRequestProperty("Authorization", getBasicAuthHeadder());
            client.connect();

            // pathで指定したファイルが存在しない場合
            if (client.getResponseCode() == 404) {
            	//空のファイルを作成
            	createFile(path, "{}");

            	client = (HttpsURLConnection) url.openConnection();

                client.setRequestMethod("GET");
                client.setRequestProperty("Authorization", getBasicAuthHeadder());
                client.connect();

            }

            System.out.println("Response Code:" + client.getResponseCode());
            Map<String, List<String>> headers = client.getHeaderFields();
            System.out.println(headers);

            StringBuffer resBody = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                resBody.append(line);
                System.out.println(line);
            }
            reader.close();

            return new Contents(resBody.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 指定したパスに、ファイルを保存する。パスが存在しなければ新規の作成し、パスが存在していれば上書きする。
     * @param path パス
     * @param content ファイルの内容
     */
    public void saveFile(String path, String content) {
		System.out.println("call saveFileApi");
        Contents file = getContents(path);
        if (file.isExist()) {
        		System.out.println("exist");
            updateFile(path, content, file.getSha());
        } else {
        	System.out.println("nonfile");
            createFile(path, content);
        }

    }

    /**
     * 指定したパスに、ファイルを新規に作成する。
     * @param path パス
     * @param content ファイルの内容
     */
    public void createFile(String path, String content) {
        StringBuffer param = new StringBuffer();
        param.append("\n{");
        param.append("\"message\":\"" + "create" + "\","); // コミットメッセージ
        param.append("\"content\":\"" + encodeContent(content) + "\"");
        param.append("}");

        System.out.println(param);

        try {
            URL url = new URL(getBaseUrl() + "/contents/" + path);

            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
            client.setRequestMethod("PUT");
            client.setRequestProperty("Authorization", getBasicAuthHeadder());
            client.addRequestProperty("Content-Type", "application/json");
            client.setDoOutput(true);
            client.setDoInput(true);
            client.connect();

            sendReqBody(client, param);

            System.out.println("Response Code:" + client.getResponseCode());
            Map<String, List<String>> headers = client.getHeaderFields();
            System.out.println(headers);

            StringBuffer resBody = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                resBody.append(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 指定したハッシュのファイルを、パス名で更新する。
     * @param path パス
     * @param content ファイルの内容
     * @param sha 既存ファイルのハッシュ
     */
    public void updateFile(String path, String content, String sha) {
        StringBuffer param = new StringBuffer();
        param.append("\n{");
        param.append("\"message\":\"" + "update" + "\","); // コミットメッセージ
        param.append("\"content\":\"" + encodeContent(content) + "\",");
        param.append("\"sha\":\"" + sha + "\"");
        param.append("}");

        System.out.println(param);

        try {
            URL url = new URL(getBaseUrl() + "/contents/" + path);

            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
            client.setRequestMethod("PUT");
            client.setRequestProperty("Authorization", getBasicAuthHeadder());
            client.addRequestProperty("Content-Type", "application/json");
            client.setDoOutput(true);
            client.setDoInput(true);
            client.connect();

            sendReqBody(client, param);

            System.out.println("Response Code:" + client.getResponseCode());
            Map<String, List<String>> headers = client.getHeaderFields();
            System.out.println(headers);

            StringBuffer resBody = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                resBody.append(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace(  );
        }


    }

}
