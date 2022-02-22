package mk.com.findsellgive.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import mk.com.findsellgive.R;
import mk.com.findsellgive.adapters.ProductRecyclerViewAdapter;
import mk.com.findsellgive.listeners.OnProductItemClickListener;


public abstract class HomeBaseFragment extends Fragment implements OnProductItemClickListener {


    public int getLayout() {
        return R.layout.tab_child;
    }

    public View getFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), parent, false);
    }


    abstract void getProductByPurpose(int purpose);


    abstract void getAllProducts();


    public void initRecyclerView(RecyclerView recyclerView, ProductRecyclerViewAdapter adapter) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
    }

    /**
     * metod sto go krie recyclerview-to, a go prikazuva loader i vice versa spored flagot
     *
     * @param show         - flag kojsto odreduva sto da se skrie, a sto da se prikaze
     * @param recyclerView
     * @param progressBar
     */
    public void showLoader(boolean show, RecyclerView recyclerView, ProgressBar progressBar) {
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * metod sto go krie recyclerview-to, a go prikazuva poraka i vice versa spored flagot
     *
     * @param show         - flag kojsto odreduva sto da se skrie, a sto da se prikaze
     * @param recyclerView
     * @param noData
     */
    public void showNoData(boolean show, RecyclerView recyclerView, TextView noData,ProgressBar progressBar) {
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBar.setVisibility(show ? View.GONE : View.VISIBLE);
        noData.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
