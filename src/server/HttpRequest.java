package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class HttpRequest {
	private final HttpHeader header;
	private final String bodyText;

	public HttpRequest(InputStream input) {
		try {
			this.header = new HttpHeader(input);
			this.bodyText = this.readBody(input);

			System.out.println("header:" + header.getText());
			System.out.println("body:" + bodyText);

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private String readBody(InputStream in) throws IOException {
		final int contentLength = this.header.getContentLength();

		if (contentLength <= 0) {
			return null;
		}

		byte[] buffer = new byte[contentLength];
		in.read(buffer);

		return IOUtil.toString(buffer);
	}

	public String getHeaderText() {
		return this.header.getText();
	}

	public String getBodyText() {
		return this.bodyText;
	}

	public HttpHeader getHeader() {
		return this.header;
	}

}
