package com.androidstudy.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidstudy.R;

/**TitleFragment extends Fragment
 * 标题栏fragment；
 * @author Eugene
 * @data 2015-1-23
 */
public class TitleFragment extends Fragment {  
  
    private ImageButton mLeftMenu;  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){  
        View view = inflater.inflate(R.layout.frag_title, container, false);  
        
        mLeftMenu = (ImageButton) view.findViewById(R.id.id_title_left_btn);  
        mLeftMenu.setOnClickListener(new OnClickListener(){  
            @Override  
            public void onClick(View v) {  
                Toast.makeText(getActivity(), "I am an ImageButton in TitleFragment!",Toast.LENGTH_SHORT).show();  
            }  
        });  
        return view;  
    }  
}
