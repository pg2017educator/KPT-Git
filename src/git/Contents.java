package git;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contents {

    private String contents;

    public Contents(String contents) {
        this.contents = contents;
    }

    public boolean isFile() {
        return contents.startsWith("{");
    }

    public boolean isDir() {
        return contents.startsWith("[");
    }

    public String getContent() {
        Pattern p = Pattern.compile("\"content\":\"([^\"]*)\"");
        Matcher m = p.matcher(contents);
        if (m.find()) {
            String content = m.group(1).replace("\\n", "");
            System.out.println(content);
            return new String(Base64.getDecoder().decode(content));
        }
        return null;
    }

    public String getSha() {
        Pattern p = Pattern.compile("\"sha\":\"([^\"]*)\"");
        Matcher m = p.matcher(contents);
        if (m.find()) {
            String sha = m.group(1).replace("\\n", "");
            System.out.println(sha);
            return sha;
        }
        return null;
    }

    public boolean isExist() {
        return contents != null;
    }
}
