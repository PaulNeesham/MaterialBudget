package com.flatlyapps.materialbudget.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flatlyapps.materialbudget.R;
import com.flatlyapps.materialbudget.list.RecurListAdapter;


public class RecurListFragment extends Fragment {

	private static final String ARG_POSITION = "position";

	public static RecurListFragment newInstance() {
		return new RecurListFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tab_fragment,container,false);
        RecyclerView listRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
		listRecyclerView.setLayoutManager(layoutManager);
		listRecyclerView.setHasFixedSize(true);
		listRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        RecurListAdapter listAdapter = new RecurListAdapter(getActivity());
		listRecyclerView.setAdapter(listAdapter);
		return rootView;
	}

	public static String getTitle() {
		return "Recurring";
	}
}