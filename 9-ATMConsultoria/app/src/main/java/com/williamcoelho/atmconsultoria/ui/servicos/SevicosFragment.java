package com.williamcoelho.atmconsultoria.ui.servicos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.williamcoelho.atmconsultoria.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SevicosFragment extends Fragment {

    public SevicosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sevicos, container, false);
    }
}
