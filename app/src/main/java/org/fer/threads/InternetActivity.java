package org.fer.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class InternetActivity extends AppCompatActivity {

    public static final String LOG_TAG = InternetActivity.class.getSimpleName();
    private static final String POKEMON_LOCATION_URL = "https://pokeapi.co/api/v2/location/1";
    private static final String POKEMON_BERRY_URL = "https://pokeapi.co/api/v2/berry/1";
    private static final String POKEMON_CONTEST_URL = "https://pokeapi.co/api/v2/contest-type/1";
    private static final String POKEMON_NAME_URL = "https://pokeapi..co/api/v2/pokemon/blaziken/";
    private static final String POKEMON_MACHINES_URL = "https://pokeapi.co/api/v2/machine/1";
    private static final String POKEMON_MACHINES_ARRAY_URL = "https://pokeapi.co/api/v2/machine?limit=2&offset=0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        ServerCallAsyncTask task = new ServerCallAsyncTask();
        task.execute(POKEMON_NAME_URL);
    }

    private void updateUi(String response){
        TextView titleTextView = findViewById(R.id.response);
        titleTextView.setText(response);
        Log.v(LOG_TAG, response);
    }



    private class ServerCallAsyncTask extends AsyncTask<String, Void, String> {

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch(MalformedURLException exception){
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null) {
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);//milliseconds
                urlConnection.setConnectTimeout(15000);//milliseconds
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        @Override
        protected String doInBackground(String... urls) {
            // Creamos el objeto URL desde el string que recibimos.
            if (urls.length == 0) return "";
            URL url = createUrl(urls[0]);
            // Hacemos la petición. Ésta puede tirar una exepción.
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                return;
            }
            updateUi(response);
        }

    }

}
