package com.flatlyapps.materialbudget.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flatlyapps.materialbudget.R;
import com.flatlyapps.materialbudget.card.SummaryCardsAdapter;


public class SummaryCardsFragment extends Fragment {

	private static final String ARG_POSITION = "position";

	public static SummaryCardsFragment newInstance() {
		return new SummaryCardsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tab_fragment,container,false);
        RecyclerView cardsRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager cardsLayoutManager = new LinearLayoutManager(getActivity());
		cardsRecyclerView.setLayoutManager(cardsLayoutManager);
		cardsRecyclerView.setHasFixedSize(true);

        SummaryCardsAdapter cardsAdapter = new SummaryCardsAdapter(getActivity());
		cardsRecyclerView.setAdapter(cardsAdapter);
		return rootView;
	}

	public static String getTitle() {
		return "Summary";
	}
}