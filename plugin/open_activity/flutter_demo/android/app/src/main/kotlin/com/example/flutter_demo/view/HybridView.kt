package com.example.flutter_demo.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import com.example.flutter_demo.MapDialogFragment
import com.example.flutter_demo.MapsActivity
import io.flutter.plugin.platform.PlatformView
import io.flutter.util.ViewUtils.getActivity


@SuppressLint("ResourceType")
internal class HybridView(context: Context, id: Int, creationParams: Map<String?, Any?>?) :
    PlatformView {
    private val rowLayout: LinearLayout
    override fun getView(): View {


        return rowLayout

    }

    override fun dispose() {
    }


    init {
        rowLayout = LinearLayout(context)
//        val fragment = MapsFragment()
//        val fragmentInflated = fragment.onCreateView(LayoutInflater.from(context), rowLayout, null)!!
//        val vParams: ViewGroup.LayoutParams = FrameLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        fragmentInflated.setLayoutParams(vParams);
//        fragmentInflated.setId(id);

//        val mapFragment = MapsActivity().getView();
//
//        val binding = ActivityMapsBinding.inflate(layoutInflater);

//        rowLayout.addView(fragmentInflated);


//
        rowLayout.setOnClickListener {


        }


//        val mTransaction: FragmentTransaction = get().beginTransaction()

//        mTransaction.add(mapLayout.getId(), fragment)
//        mTransaction.commit()

//       rowLayout.id = 1223
//        val myFrag: Fragment = MapsFragment();
//        fragmentInflated.setBackgroundColor(Color.RED);

//
////
//       rowLayout.setBackgroundColor(Color.RED);
//
//       rowLayout.addView(myView);
// add rowLayout to the root layout somewhere here

// add rowLayout to the root layout somewhere here
//        val fragMan: FragmentManager = FragmentActivity().supportFragmentManager;
//        val fragTransaction: FragmentTransaction = fragMan.beginTransaction()
//
//        val myFrag: Fragment = MapsFragment()
//        fragTransaction.add(rowLayout.id, myFrag, "fragment")
//        fragTransaction.commit()
//        textView = TextView(context)
//        textView.textSize = 72f
//        textView.setBackgroundColor(Color.rgb(255, 255, 255))
//        textView.text = "HybridView"
    }

}