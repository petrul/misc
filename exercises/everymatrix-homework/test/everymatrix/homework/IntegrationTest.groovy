package everymatrix.homework

import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * The integration test actually starts the http server and sends it HTTP requests
 */
class IntegrationTest {

    Registry registry;
    App app;
    Thread appThread;

    @Before
    void init() {
        System.setProperty("http.keepAlive", "false")

        this.registry = new Registry()
        this.app = new App(registry);
        this.appThread = new Thread(this.app);
        this.appThread.start(); // start on a different thread
        println "started app"

    }

    @After
    void afterAll() {
        println "stopping app"
        app.stop()
    }

    @Test
    void testCreateSession() {

        def customer_ids = (1..10).collect { Math.abs(new Random().nextInt()) }
        println customer_ids
        Map customer2session = [:]

        customer_ids.each {
            String sessKey = this.restClient_getSession(it)

            println "created session $sessKey for customer $it"

            assert sessKey != null
            assert sessKey.length() > 5
            assert sessKey.matches("[\\w\\d]+")

            customer2session[it] = sessKey
        }
    }

    String restClient_getSession(int customerId) {

        def url_string = "http://localhost:8001/$customerId/session"
        println "creating session on thread ${Thread.currentThread()}"

        String sessKey
        URLConnection conn
        try {
            URL createSessionUrl = new URL(url_string)
            conn = createSessionUrl.openConnection()
            conn.setRequestProperty("Connection", "close");
            conn.setDoOutput(false);
            conn.setReadTimeout(1000)
            sessKey = conn.getInputStream().getText()
        } finally {
            conn.getInputStream().close()
        }


        return sessKey;
    }

    String restClient_postBet(String sessId, int betOffer, int stake) {
        def surl = "http://localhost:8001/$betOffer/stake?sessionkey=$sessId"
        def url = new URL(surl)
        HttpURLConnection conn = url.openConnection()
        conn.setRequestMethod("POST")
        conn.setDoOutput(true)
        conn.getOutputStream().write(stake.toString().getBytes())
        conn.getOutputStream().close()
        conn.connect();
    }

}
