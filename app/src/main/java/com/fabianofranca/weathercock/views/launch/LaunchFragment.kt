package com.fabianofranca.weathercock.views.launch


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.transition.Fade
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabianofranca.weathercock.views.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_launch.view.*

class LaunchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.fabianofranca.weathercock.R.layout.fragment_launch,
            container,
            false
        )

        ViewCompat.setTransitionName(view.logo, "logo")

        view.post {
            val fragment = HomeFragment()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val enterFade = Fade()
                enterFade.duration = DEFAULT_TIME
                enterFade.startDelay = DEFAULT_TIME / 2
                fragment.enterTransition = enterFade

                val enterTransitionSet = TransitionSet()
                enterTransitionSet.addTransition(
                    TransitionInflater.from(activity).inflateTransition(
                        android.R.transition.move
                    )
                )
                enterTransitionSet.duration = DEFAULT_TIME
                fragment.sharedElementEnterTransition = enterTransitionSet
            }

            val transaction = fragmentManager?.beginTransaction()
            transaction?.addSharedElement(view.logo, ViewCompat.getTransitionName(view.logo))
            transaction?.replace(com.fabianofranca.weathercock.R.id.container, fragment)
            transaction?.setReorderingAllowed(true)
            transaction?.commit()
        }

        return view
    }

    private companion object {
        const val DEFAULT_TIME = 600L
    }
}
