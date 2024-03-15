import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import webserver.HttpRequest;
import webserver.Uri;

public class UriTest {

    @Test
    void fromTest() {
        HttpRequest httpRequest = new HttpRequest("GET /img/sendLink.svg HTTP/1.1", "", List.of(""));

        Uri uri = Uri.from(httpRequest.getUri());

        assertThat(uri).isEqualTo(Uri.DATA);
    }
}
