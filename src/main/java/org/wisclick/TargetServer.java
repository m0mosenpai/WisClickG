package org.wisclick;

import java.io.Console;
import java.net.InetSocketAddress;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import jakarta.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Starts a jetty http server with eight servlets and session management 
 * manage the account of each user.
 * 
 * @author Joseph Eichenhofer, Emma He
 */
public class TargetServer {

	// number of seconds before session expires
	private static final int SESSION_TIMEOUT = 7200;
	// number of seconds between checking for expired sessions
	private static final int SCAVENGE_INTERVAL = 20;

	// for printing logged-in requests
	Console term;
	// server object for configuring and starting handlers
	Server server;

	/**
	 * Constructor. Configures the server context with session management and eight
	 * handlers. Does
	 * not start the server (must call startServer).
	 * 
	 * Instantiate with handle to terminal for output. Console object is
	 * synchronized, so the multithreaded printing/reading will not be a problem.
	 * 
	 * @param terminalHandle
	 *            instantiated object for the output terminal
	 * @throws Exception
	 *             if unable to configure server (caused by setIntervalSec on
	 *             housekeeper)
	 */
	public TargetServer(Console terminalHandle) throws Exception {
		term = terminalHandle;
		server = new Server(new InetSocketAddress("localhost", 8080));

		// Set context for webapp
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/");

		// Point Jetty to extract and use the generated war file
		webapp.setWar("build/libs/Web_Attack.war");
		webapp.setExtractWAR(true);

		// Register the CORS filter
		FilterHolder corsFilter = new FilterHolder(new CorsFilter());
		webapp.addFilter(corsFilter, "/*", EnumSet.of(DispatcherType.REQUEST));

		server.setHandler(webapp);
    	}

	/**
	 * Start the server as configured by constructor. Cleanly handles as many errors
	 * as possible with debug info.
	 * 
	 * @return true if successful or false if errors are detected
	 */
	public boolean startServer() {
		// try to start the server
		try {
			term.printf("Starting web server...\n");
			server.start();
		} catch (Exception e) {
			System.err.println("Unable to start server.");
			e.printStackTrace();
			server.destroy();
			return false;
		}

		// wait for input to exit
		term.printf("Open a web browser and access 'localhost:8080/welcome' to view the web interface.\n"); // Changed to 9000 for testing
		do {
			term.printf("Type 'quit' and press enter to stop the server.\n");
		} while (!term.readLine().equals("quit"));

		// try to stop the server
		try {
			server.stop();
		} catch (Exception e) {
			System.err.println("Unable to stop server. Forcing Quit.");
			e.printStackTrace();
			server.destroy();
			return false;
		}

		// try to join the server threads after stopping
		try {
			server.join();
		} catch (InterruptedException e) {
			System.err.print("Unable to join server. Forcing Quit.");
			server.destroy();
			return false;
		}

		server.destroy();
		term.printf("Server exited.\n");
		return true;
	}

	/**
	 * Start the server with various servlets registered.
	 * 
	 * @param args
	 *            n/a
	 */
	public static void main(String[] args) {
		// get handle to console for input
		Console term = System.console();
		// if (term == null) {
		//	System.err.println("Unable to get console. Are you running from an IDE?");
		//	System.exit(-1);
		//}

		boolean success = false;

		// try server startup
		try {
			TargetServer s = new TargetServer(term);
			success = s.startServer();
		} catch (Exception e) {
			success = false;
			term.printf("fatal error in server startup\n");
			e.printStackTrace();
		}

		if (!success) {
			// exit with error if bad return code
			System.exit(-1);
		}
	}
}
