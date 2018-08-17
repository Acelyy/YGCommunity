package com.yonggang.ygcommunity.grid.house.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.grid.house.HouseInfoActivity
import kotlinx.android.synthetic.main.fragment_house_info.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HouseInfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HouseInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HouseInfoFragment : Fragment() {

    private lateinit var onInfoSubmit: HouseInfoActivity.OnInfoSubmitListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_house_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        submit.setOnClickListener {
            onInfoSubmit.onInfoSubmit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HouseInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(onInfoSubmit: HouseInfoActivity.OnInfoSubmitListener) =
                HouseInfoFragment().apply {
                    this.onInfoSubmit = onInfoSubmit
                }
    }
}
