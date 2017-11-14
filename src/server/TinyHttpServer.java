package server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import git.Contents;
import git.ContentsApi;

public class TinyHttpServer {
    private int port = 0;
    private ExecutorService service = Executors.newCachedThreadPool();

    public TinyHttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket(this.port)) {
            while (true) {
                this.serverProcess(server);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void serverProcess(ServerSocket server) throws IOException {
		Socket socket = server.accept();

		this.service.execute(() -> {
			try (
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
			) {

				HttpRequest request = new HttpRequest(in);
				HttpHeader header = request.getHeader();

                System.out.println("path:" + header.getPath());

                if (header.isGetMethod()) {

					String[] params = header.getPath().split("/");
					System.out.println(Arrays.toString(params));

					if (params[1].equals("html")) {
					    this.respondFile(out, "/" + params[2]);
					} else if(params[1].equals("api")) {
					    this.respondApi(out, params[2], params[3]);
					}
				} else if (header.isPostMethod()) {
                    System.out.println("body:" + request.getBodyText());
					this.respondOk(out);
				}
			} catch (EmptyRequestException e) {
				// ignore
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

    private void respondApi(OutputStream out, String user, String command) throws IOException {
        // Github APIを呼び出す
        ContentsApi api = new ContentsApi();
        Contents contents = api.getContents("db/" + user + ".kpt");


        HttpResponse response = new HttpResponse(Status.OK);
        response.addHeader("Content-Type", "application/json");
        response.setBodyText(contents.getContent());
        response.writeTextTo(out);
    }

    private void respondOk(OutputStream out) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        response.addHeader("Constent-Type", "text/html; charset=utf-8");
        response.setBodyText("OK");
        response.writeTextTo(out);
    }

    private void respondFile(OutputStream out, String path) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        File file = new File("web" + path);
        response.setBodyFile(file);
        response.writeFileTo(out);
    }

}
