import static net.grinder.script.Grinder.grinder
import static org.junit.Assert.*
import static org.hamcrest.Matchers.*
import net.grinder.plugin.http.HTTPRequest
import net.grinder.plugin.http.HTTPPluginControl
import net.grinder.script.GTest
import net.grinder.script.Grinder
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
// import static net.grinder.util.GrinderUtils.* // You can use this if you're using nGrinder after 3.2.3
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Date
import java.util.List
import java.util.ArrayList

import HTTPClient.Cookie
import HTTPClient.CookieModule
import HTTPClient.HTTPResponse
import HTTPClient.NVPair

/**
 * A simple example using the HTTP plugin that shows the retrieval of a
 * single page via HTTP.
 *
 * This script is automatically generated by ngrinder.
 *
 * @author admin
 */
@RunWith(GrinderRunner)
class LoginTest {

    public static GTest test
    public static HTTPRequest request
    public static NVPair[] headers = []
    public static String body = "{\n\"mobile\":\"15800000002\",\n\"password\":\"123456\"\n}"
    public static Cookie[] cookies = []
    public static String login_url="http://192.168.31.250:8080/app/mobile/api/user/login"

    @BeforeProcess
    public static void beforeProcess() {
        HTTPPluginControl.getConnectionDefaults().timeout = 6000
        test = new GTest(1, "login-interface")
        request = new HTTPRequest()
        // Set header datas
        List<NVPair> headerList = new ArrayList<NVPair>()
        headerList.add(new NVPair("Content-Type", "application/json"))
        headers = headerList.toArray()
        grinder.logger.info("before process.");
    }

    @BeforeThread
    public void beforeThread() {
        test.record(this, "test")
        grinder.statistics.delayReports=true;
        grinder.logger.info("before thread.");
    }

    @Before
    public void before() {
        request.setHeaders(headers)
        cookies.each { CookieModule.addCookie(it, HTTPPluginControl.getThreadHTTPClientContext()) }
        grinder.logger.info("before thread. init headers and cookies");
    }

    @Test
    public void test(){
        HTTPResponse result = request.POST(login_url, body.getBytes())
        grinder.logger.info("返回的结果是：" + result.getText());
        if (result.statusCode == 301 || result.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", result.statusCode);
        } else {
            assertThat(result.statusCode, is(200));
            assertThat(result.text,containsString("\"code\":0,\""));
        }
    }
}
