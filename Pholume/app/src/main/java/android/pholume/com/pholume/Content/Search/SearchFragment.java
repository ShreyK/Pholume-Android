package android.pholume.com.pholume.Content.Search;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.Content.Views.DividerItemDecoration;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.Network.RestManager;
import android.pholume.com.pholume.R;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    EditText searchInput;
    ImageView clearButton;
    RecyclerView searchList;
    SearchListAdapter searchListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = (EditText) view.findViewById(R.id.search_input);
        searchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String query = searchInput.getText().toString();
                    if (!query.isEmpty()) {
                        searchForUser(query);
                    }
                    return true;
                }
                return false;
            }
        });

        clearButton = (ImageView) view.findViewById(R.id.clear_query_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput.setText("");
                searchInput.requestFocus();
            }
        });

        searchList = (RecyclerView) view.findViewById(R.id.list_search);
        searchList.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        searchListAdapter = new SearchListAdapter(getActivity(), new ArrayList<User>());
        searchList.setAdapter(searchListAdapter);
        searchList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void searchForUser(String query) {
        RestManager.getInstance().searchForUser(query, new PholumeCallback<List<User>>("SearchForUser") {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    searchListAdapter.setData(response.body());
                    searchListAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
                }
            }
        });
    }
}
