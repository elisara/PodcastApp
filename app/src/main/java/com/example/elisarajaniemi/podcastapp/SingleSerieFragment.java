package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class SingleSerieFragment extends Fragment {

    private ListView listView;
    private SerieArrayAdapter adapter;
    private HttpGetHelper httpGetHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);

        httpGetHelper = new HttpGetHelper();

        final ArrayList<PodcastItem> list = new ArrayList<>();
        /**list.add("podcast1");
        list.add("podcast2");
        list.add("podcast3");
        list.add("podcast4");
*/
        listView = (ListView) view.findViewById(R.id.single_playlist_list);
        adapter = new SerieArrayAdapter(getContext(), httpGetHelper.getResults() );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                String value = httpGetHelper.getResults().get(position).title;
                System.out.println(value);
                PlayerFragment pf = new PlayerFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, pf).addToBackStack( "tag" ).commit();
            }

        });

        return view;
    }

}
