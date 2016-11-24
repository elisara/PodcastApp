package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class EpisodesFragment extends Fragment {

    private ListView listView;
    private EpisodeListArrayAdapter adapter;
    private HttpGetHelper httpGetHelper;
    private String message;
    private ArrayList<PodcastItem> list;
    private PodcastItem pi;
    private MainActivity ma;
    private AlertDialog alertDialog;
    private TextView collectionName;
    private LinearLayout headerBox;
    private PlayerFragment pf;
    public PodcastItems podcastItems = PodcastItems.getInstance();
    private ArrayList<PodcastItem> listAll = podcastItems.getItems();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pi = (PodcastItem) getArguments().getSerializable("message");
        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);
        collectionName = (TextView) view.findViewById(R.id.collectionTitle);
        collectionName.setText(pi.collectionName);

        listView = (ListView) view.findViewById(R.id.single_playlist_list);
        fillList();
        sendToPlaylists();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = list.get(position);
                pf = new PlayerFragment();
                Bundle bundle2 = new Bundle();
                System.out.println("FromYLE: " + pi.fromYLE);
                if (pi.fromYLE == true){
                    new AsyncCaller().execute(pi);
                }
                bundle2.putSerializable("episode", pi);
                pf.setArguments(bundle2);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().addToBackStack("pf")
                        .replace(R.id.frag_container, pf, "pf").commit();

            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, final int position, long rowId) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Description");

                LinearLayout lp = new LinearLayout(getContext());
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(30, 30, 30, 60);

                final TextView description = new TextView(getActivity());
                final Button shareBtn = new Button(getActivity());
                shareBtn.setText("share");
                shareBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        Object obj = list.get(position);
                        PodcastItem podcastItem = (PodcastItem) obj;
                        sendIntent.setAction(Intent.ACTION_SEND);

                        sendIntent.putExtra(Intent.EXTRA_TEXT, podcastItem.title + " " + podcastItem.decryptedURL);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
                String[] splits = list.get(position).description.split("<a>");
                description.setText(splits[0].replaceAll("<br>", "\n\n"));
                description.setMovementMethod(new ScrollingMovementMethod());
                lp.addView(description);
                lp.addView(shareBtn);

                alertDialogBuilder.setView(lp);

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        fillList();



    }

    public URL createUrl(String urli) throws IOException {
        URL url = new URL(urli);
        return url;
    }


    public void fillList(){
        list = new ArrayList<>();
        System.out.println("ListAll size in fill: " + PodcastItems.getInstance().getItems().size());
        if(list.size() == 0) {
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).collectionName.equals(pi.collectionName) && !list.contains(listAll.get(i))) {
                    list.add(listAll.get(i));
                }
            }
        }

        adapter = new EpisodeListArrayAdapter(getContext(), list);
        listView.setAdapter(adapter);

    }

    public void sendToPlaylists(){
        if(adapter.addToPlaylist == true){

        }
    }

}

class AsyncCaller extends AsyncTask<PodcastItem, String, String> {
    //ProgressDialog pdLoading = new ProgressDialog(AsyncExample.this);
    String decryptedURL;
    String resultURL;
    MyCrypt myCrypt = new MyCrypt();
    BufferedReader br;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        //pdLoading.setMessage("\tLoading...");
        //pdLoading.show();
    }
    @Override
    protected String doInBackground(PodcastItem... params) {

        try {
            URL url = new URL(params[0].url);
            System.out.println("URL: " + url);
            URLConnection conn = url.openConnection();
            //conn.setDoOutput(true);
            //conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.connect();

            //OutputStream os = conn.getOutputStream();
            //os.flush();

            /**int responseCode = conn.getResponseCode();
            if (responseCode >= 400 && responseCode <= 499) {
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            }
            else {
                br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
            }*/

            BufferedReader r  = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));

            String output;

            while ((output = r.readLine()) != null) {
                try {
                    JSONObject jObject = new JSONObject(output);
                    JSONArray jArray = jObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++){
                        decryptedURL = jArray.getJSONObject(i).getString("url");
                        System.out.println("DecryptedURL: " + decryptedURL);
                    }
                    //encryptedURL = jObject.getString("url");
                    System.out.println("Output: " + output);
                    resultURL = myCrypt.decryptURL(decryptedURL);

                } catch (JSONException e) {
                    System.out.println(e);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }

        } catch (
                MalformedURLException e
                ) {
            e.printStackTrace();

        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        params[0].setURL(resultURL);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        System.out.println("Result: " + result);

        //this method will be running on UI thread

        //pdLoading.dismiss();
    }

}
