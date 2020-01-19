package com.nathan.app.githubtrendie.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nathan.app.githubtrendie.R


class TrendingFragment : Fragment() {

    companion object {
        fun newInstance() = TrendingFragment()
    }

    private lateinit var viewModel: TrendingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.trending_fragment, container, false)

        val moreIcon = view.findViewById<ImageView>(R.id.more_icon)
        val popupMenu = PopupMenu(this.context!!, moreIcon)
        popupMenu.inflate(R.menu.menu_trending)
        moreIcon.setOnClickListener {
            popupMenu.show()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrendingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.name_menu_item -> {
                // TODO: User clicked the name button
                true
            }
            R.id.stars_menu_item -> {
                // TODO: User clicked the stars button
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
