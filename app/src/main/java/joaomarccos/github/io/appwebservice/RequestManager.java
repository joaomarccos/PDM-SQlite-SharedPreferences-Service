package joaomarccos.github.io.appwebservice;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import joaomarccos.github.io.appwebservice.util.DataStorer;
import joaomarccos.github.io.appwebservice.util.MD5;

/**
 * Created by joaomarcos on 26/02/16.
 */
public class RequestManager {

    private static final String HOST = "http://192.168.44.101:8080";
    private static final String GETALL = HOST + "/nomes?a=todos";
    private static final String GETONE = HOST + "/nomes?a=um&o=";
    private final DataStorer ds;


    public RequestManager(DataStorer ds) {
        this.ds = ds;
    }

    public boolean isSync() throws NoSuchAlgorithmException {
        String data = getAllValues();
        String hashInCloud = MD5.hash(data);
        String localHash = ds.getLocalHash();
        if (!localHash.equals(hashInCloud)) {
            ds.updateHash(hashInCloud);
            return false;
        }
        return true;
    }

    public String getNextValue() {
        List<String> list = request(GETONE + ds.getCurrentLine());
        if (!list.isEmpty()) {
            ds.incrementCurrentLine();
            return list.get(0);
        }
        return null;
    }

    private String getAllValues() {
        List<String> list = request(GETALL);
        return list!=null?list.toString():"[]";
    }

    private List<String> request(String url) {
        RetrieveNames rn = new RetrieveNames();
        rn.execute(url);
        try {
            List list = rn.get(3, TimeUnit.SECONDS);
            return list;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    class RetrieveNames extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String[] result = restTemplate.getForObject(params[0], String[].class);
            return Arrays.asList(result);
        }
    }
}

