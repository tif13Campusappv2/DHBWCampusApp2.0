/*
 *      Beschreibung:	Beinhaltet allen Code für ein Newstab
 *      Autoren: 		Daniel Spieker
 *      Projekt:		Campus App 2.0
 *
 *      ╔══════════════════════════════╗
 *      ║ History                      ║
 *      ╠════════════╦═════════════════╣
 *      ║   Datum    ║    Änderung     ║
 *      ╠════════════╬═════════════════╣
 *      ║ 2015-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ╚════════════╩═════════════════╝
 *      Wichtig:           Tabelle sollte mit monospace Schriftart dargestellt werden
 */
package com.dhbwloerrach.dhbwcampusapp20;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class news_fragment extends Fragment {

    private List<NewsContainer.NewsItem> newsItemList;
    private OnListFragmentInteractionListener mListener;
    private MyItemRecyclerViewAdapter adapter;
    private Boolean IsActive;
    RecyclerView recyclerView;

    public news_fragment() {
        newsItemList=new ArrayList<>();
        IsActive=false;
    }

    public static news_fragment newInstance() {
        news_fragment fragment = new news_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        IsActive=true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter=new MyItemRecyclerViewAdapter(newsItemList, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onPause()
    {
        super.onPause();
        IsActive=false;
    }

    // Wird von der übergeordneten Activity aufgerufen um den Inhalt des Fragments zu aktualisieren
    public void UpdateNews(List<NewsContainer.NewsItem> list) {
        newsItemList=list;
        if(this.IsActive)
            adapter.UpdateContent(list);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Stellt eine Schnittstelle für die Kommunikation mit der übergeordneten Activity da
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(NewsContainer.NewsItem item);
    }
}
