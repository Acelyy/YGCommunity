package com.yonggang.ygcommunity.grid.house.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yonggang.ygcommunity.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HouseExtraFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HouseExtraFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HouseExtraFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_extra, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HouseExtraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = HouseExtraFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}
