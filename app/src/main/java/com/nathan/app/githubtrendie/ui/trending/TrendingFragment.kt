package com.nathan.app.githubtrendie.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathan.app.githubtrendie.MainActivity
import com.nathan.app.githubtrendie.R
import com.nathan.app.githubtrendie.databinding.TrendingFragmentBinding
import com.nathan.app.githubtrendie.di.ViewModelFactory
import com.nathan.app.githubtrendie.widget.SimpleLoadingAdapter


class TrendingFragment : Fragment() {

    companion object {
        fun newInstance() = TrendingFragment()
    }

    private lateinit var binding: TrendingFragmentBinding

    private lateinit var viewModel: TrendingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TrendingFragmentBinding.inflate(inflater)

        // For databinding with LiveData
        binding.lifecycleOwner = this

        binding.loadingList.adapter = SimpleLoadingAdapter(IntArray(10))

        binding.repoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val moreIcon = binding.moreIcon
        val popupMenu = PopupMenu(this.context!!, moreIcon)
        popupMenu.inflate(R.menu.menu_trending)
        moreIcon.setOnClickListener {
            popupMenu.show()
        }
        popupMenu.setOnMenuItemClickListener { onOptionsItemSelected(it) }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity as MainActivity))
            .get(TrendingViewModel::class.java)

        binding.offlineLayout.findViewById<Button>(R.id.retry_button)
            .setOnClickListener(viewModel.retryClickListener)

        binding.swipeLayout.setOnRefreshListener(viewModel.swipeRefreshListener)

        binding.repoList.adapter = viewModel.repoAdapter

        binding.viewModel = viewModel

        viewModel.loading.observe(this, Observer { loading ->
            binding.swipeLayout.isRefreshing = loading
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.name_menu_item -> {
                viewModel.repoAdapter.sortByName()
                true
            }
            R.id.stars_menu_item -> {
                viewModel.repoAdapter.sortByStar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
