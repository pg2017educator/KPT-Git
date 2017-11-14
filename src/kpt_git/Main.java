package kpt_git;

import server.TinyHttpServer;

public class Main {
	public static void main(String[] args) {
		System.out.println("Start:");
		TinyHttpServer server = new TinyHttpServer(8080);
		server.start();
		System.out.println("End:");
	}
}
