package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 27.10.2016.
 */

public class SerieFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ListView listView;
    private SerieArrayAdapter adapter;
    private ImageButton menuBtn;
    private Spinner spinner;
    private Button categoryBtn;
    private boolean categoryOpen;
    private CategoryFragment cf;
    private PlayerFragment pf;
    private String apiKey;
    public boolean history;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Thread t = new Thread(r);
        //t.start();

        new HttpGetHelper().execute("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_auth/auth.php");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        history = prefs.getBoolean("history", true);
        System.out.println("History in series onCreateView: " + history);

        if (history == false) {
            Toast.makeText(getActivity(), "False",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "True",
                    Toast.LENGTH_LONG).show();
        }

        final ArrayList<String> list = new ArrayList<>();
        list.add("kissat");
        list.add("koira");
        list.add("item2");
        list.add("item3");
        list.add("item4");
        list.add("item5");

        View view = inflater.inflate(R.layout.serie_layout, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        addItemsOnSpinner();

        cf = new CategoryFragment();
        pf = new PlayerFragment();

        categoryBtn = (Button) view.findViewById(R.id.categoryBtn);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(i);
                categoryOpen = true;

            }
        });

        listView = (ListView) view.findViewById(R.id.serieList);
        adapter = new SerieArrayAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                String value = list.get(position);
                System.out.println(value);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, pf).commit();
            }

        });
        return view;
    }

    /**
     * Runnable r = new Runnable() {
     * public void run() {
     * try {
     * URL url = new URL("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_auth/auth.php");
     * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
     * conn.setDoOutput(true);
     * conn.setRequestMethod("POST");
     * conn.setRequestProperty("Content-Type", "application/json");
     * <p>
     * String input = "{\"username\":\"podcast\",\"password\":\"podcast16\"}";
     * <p>
     * OutputStream os = conn.getOutputStream();
     * os.write(input.getBytes());
     * os.flush();
     * <p>
     * BufferedReader br = new BufferedReader(new InputStreamReader(
     * (conn.getInputStream())));
     * <p>
     * String output;
     * System.out.println("Output from Server .... \n");
     * while ((output = br.readLine()) != null) {
     * try {
     * JSONObject jObject = new JSONObject(output);
     * apiKey = jObject.getString("api_key");
     * System.out.println(apiKey);
     * JsonElement jelement = new JsonParser().parse(getJSON("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_audio_search/index.php/?key=" + apiKey + "&category=%20", 20000));
     * JsonArray jsonArray = jelement.getAsJsonArray();
     * System.out.println(jsonArray);
     * //jobject = jobject.getAsJsonObject("data");
     * //new HttpGetHelper().execute("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_audio_search/index.php/?key=" + apiKey + "&category=%20");
     * } catch (JSONException e) {
     * System.out.println(e);
     * }
     * }
     * conn.disconnect();
     * } catch (
     * MalformedURLException e
     * ) {
     * e.printStackTrace();
     * <p>
     * } catch (
     * IOException e
     * )
     * <p>
     * {
     * e.printStackTrace();
     * }
     * }
     * <p>
     * };
     */


    public void addItemsOnSpinner() {
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        System.out.println("added items to spinner");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        if (value.contains("NAME")) {
            System.out.println("SORT: NAME");
        } else if (value.contains("NEW")) {
            System.out.println("SORT: NEW");
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    /**public String getJSON(String url, int timeout) {
     HttpURLConnection c = null;
     try {
     URL u = new URL(url);
     c = (HttpURLConnection) u.openConnection();
     c.setRequestMethod("GET");
     c.setRequestProperty("Content-length", "0");
     c.setUseCaches(false);
     c.setAllowUserInteraction(false);
     c.setConnectTimeout(timeout);
     c.setReadTimeout(timeout);
     c.connect();
     int status = c.getResponseCode();

     switch (status) {
     case 200:
     case 201:
     BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
     StringBuilder sb = new StringBuilder();
     String line;
     while ((line = br.readLine()) != null) {
     sb.append(line+"\n");
     }
     br.close();
     return sb.toString();
     }

     } catch (MalformedURLException ex) {
     Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
     Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
     } finally {
     if (c != null) {
     try {
     c.disconnect();
     } catch (Exception ex) {
     Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
     }
     }
     }
     return null;
     }
     */
}
