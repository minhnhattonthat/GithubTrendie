package com.nathan.app.githubtrendie.ui.trending

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nathan.app.githubtrendie.R
import com.nathan.app.githubtrendie.binding.FragmentDataBindingComponent
import com.nathan.app.githubtrendie.databinding.TrendingFragmentBinding
import com.nathan.app.githubtrendie.utils.autoCleared
import com.nathan.app.githubtrendie.vo.Repo
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class TrendingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        private const val LOADING_ITEMS_SIZE = 10
        fun newInstance() = TrendingFragment()
    }

    private lateinit var viewModel: TrendingViewModel

    // mutable for testing
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<TrendingFragmentBinding>()

    private var repoAdapter by autoCleared<RepoAdapter>()

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

        binding.repoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.repoList.adapter = LoadingAdapter(IntArray(LOADING_ITEMS_SIZE))

        repoAdapter = RepoAdapter(dataBindingComponent)

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
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(TrendingViewModel::class.java)

        viewModel.loadRepos()

        binding.offlineLayout.setOnInflateListener { _, inflated ->
            inflated.findViewById<Button>(R.id.retry_button)
                .setOnClickListener { viewModel.loadRepos(true) }
            viewModel.hasError.observe(this, Observer { hasError ->
                inflated.visibility = if (hasError) View.VISIBLE else View.GONE
            })
        }

        viewModel.repos.observe(this,
            Observer<List<Repo>> { repos: List<Repo>? ->
                if (repos == null) return@Observer
                if (binding.repoList.adapter !is RepoAdapter) {
                    binding.repoList.adapter = repoAdapter
                }
                repoAdapter.updateList(repos)
            }
        )

        binding.swipeLayout.setOnRefreshListener { viewModel.loadRepos(true) }

        binding.viewModel = viewModel

        viewModel.loading.observe(this, Observer { loading ->
            if (!loading) {
                binding.swipeLayout.isRefreshing = loading
            }
        })
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.name_menu_item -> {
                repoAdapter.sortByName()
                true
            }
            R.id.stars_menu_item -> {
                repoAdapter.sortByStar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
