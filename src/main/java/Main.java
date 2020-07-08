import java.io.IOException;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.NamedResource;
import org.apache.sshd.common.session.SessionContext;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    log.info("App started");
    SshClient client = SshClient.setUpDefaultClient();
    client.start();

    client.setFilePasswordProvider(
        (SessionContext session, NamedResource resourceKey, int retryIndex) -> "password");

    try {
      ConnectFuture sessionFuture = client.connect("username", "hostname", 22);
      ClientSession session = sessionFuture.verify().getClientSession();
      session.auth().verify();
      session.startLocalPortForwarding(
          new SshdSocketAddress("localhost", 9010), new SshdSocketAddress("hostname", 9010));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
