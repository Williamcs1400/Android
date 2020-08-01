package com.williamcoelho.atmconsultoria.ui.sobre;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.williamcoelho.atmconsultoria.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SobreFragment extends Fragment {

    public SobreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String descricao = "Lorem ipsum potenti suscipit ornare aenean quis lorem fames duis pretium quam habitasse elit, curabitur nec proin dui vel egestas euismod nam fringilla habitant metus. aliquam adipiscing sociosqu accumsan tempus tristique eget purus congue ac, quam velit rutrum sagittis blandit aenean erat sagittis posuere, faucibus posuere sit diam magna et blandit mattis. pellentesque massa placerat purus turpis a ultricies laoreet tempor, nisl quis blandit magna quam nunc rhoncus cras donec";

        Element versao = new Element();
        versao.setTitle("Versão 1.0");

        return new AboutPage(getActivity())
                .setImage(R.drawable.logo)
                .setDescription(descricao)

                .addGroup("Entre em contato")
                .addEmail("williamcs1400@gmail.com", "Envie um email")
                .addWebsite("http://google.com/", "Acesse nosso site")

                .addGroup("Redes Sociais")
                .addFacebook("https://www.facebook.com/profile.php?id=100004058720564", "Facebook")
                .addInstagram("williamcoelho14", "Instagram")
                .addGitHub("Williamcs1400", "GitHub")
                .addItem(versao)

                .create();

    }
}
