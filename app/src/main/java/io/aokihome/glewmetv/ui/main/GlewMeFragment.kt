package io.aokihome.glewmetv.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.aokihome.glewmetv.R
import io.aokihome.glewmetv.db.removeAllHookupsOnMain
import io.aokihome.glewmetv.db.removeAllTickersOnMain

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GlewMeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_glewme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_second).setOnClickListener {
//            removeAllHookupsOnMain()
            removeAllTickersOnMain()
        }
    }
}