package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.logintest.R;


public class FirstAudioFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_first_audio, null);
        TextView textView = (TextView) view.findViewById(R.id.firstfragment);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String item = bundle.getString("audioInfo");
            textView.setText(item);
        }
        else
        {
            textView.setText("null pointer");
        }
        //return inflater.inflate(R.layout.fragment_second_audio, container, false);
        return view ;
    }


}
