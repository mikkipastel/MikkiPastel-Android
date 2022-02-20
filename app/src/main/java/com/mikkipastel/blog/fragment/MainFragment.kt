package com.mikkipastel.blog.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikkipastel.blog.R
import com.mikkipastel.blog.activity.SettingsActivity
import com.mikkipastel.blog.adapter.PostListAdapter
import com.mikkipastel.blog.databinding.FragmentMainBinding
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog
import com.mikkipastel.blog.utils.CustomChromeUtils
import com.mikkipastel.blog.utils.ImageLoader
import com.mikkipastel.blog.viewmodel.BlogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), PostListAdapter.PostItemListener {

    private lateinit var binding: FragmentMainBinding

    private val mBlogList = mutableListOf<PostBlog>()
    private val mAdapter by lazy {
        PostListAdapter(mBlogList, this)
    }

    private val mTagItemList = mutableListOf<TagBlog>()
    private val mTagNameList = mutableListOf<String>()
    private lateinit var mTagAdapter: ArrayAdapter<String>
    private var mCurrentTagSlug: String? = null

    private var isLoading = false
    private var mPage = 1

    private var isReloadData = false

    private val blogViewModel: BlogViewModel by viewModel()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadPostData(null)
        loadHashtagData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        setToolbar()

        setHeaderText(getString(R.string.title_tag_all), getString(R.string.description_tag_all))

        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                isReloadData = true
                mPage = 1
                loadPostData(mCurrentTagSlug)
            }

            recyclerView.apply {
                val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                layoutManager = linearLayoutManager
                adapter = mAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)

                        blogViewModel.canLazyLoading.observe(
                            viewLifecycleOwner,
                            Observer {
                                if (it) {
                                    val totalItemCount = linearLayoutManager.itemCount
                                    val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                                    if (!isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                                        isLoading = true
                                        mPage++
                                        loadPostData(mCurrentTagSlug)
                                        lottieProgress.visibility = View.VISIBLE
                                    }
                                }
                            }
                        )
                    }
                })
            }
        }
    }

    private fun setToolbar() {
        binding.apply {
            collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(
                requireContext(),
                R.color.colorMainTextWhite
            ))
            toolbar.apply {
                inflateMenu(R.menu.menu_main)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_settings -> openAppSettingsPage()
                        R.id.action_aboutme -> aboutMe()
                    }
                    return@setOnMenuItemClickListener false
                }
            }
        }
    }

    private fun loadPostData(hashtag: String?) {
        blogViewModel.getBlogPost(mPage, hashtag)
    }

    private fun loadHashtagData() {
        blogViewModel.getBlogTag()
    }

    private fun setupView() {
        blogViewModel.apply {
            allBlogPost.observe(
                viewLifecycleOwner,
                {
                    showBlogContent(it)
                }
            )
            blogPage.observe(
                viewLifecycleOwner,
                {
                    if (it == 1) {
                        binding.recyclerView.smoothScrollToPosition(0)
                    }
                }
            )
            allBlogTag.observe(
                viewLifecycleOwner,
                {
                    showTagContent(it)
                }
            )

            getBlogError.observe(
                viewLifecycleOwner,
                {
                    when (mCurrentTagSlug == null) {
                        true -> getBlogContentError()
                        false -> getBlogErrorView()
                    }
                }
            )
            getTagError.observe(
                viewLifecycleOwner,
                {
                    getTagError()
                }
            )
        }
    }

    private fun showBlogContent(list: MutableList<PostBlog>) {
        binding.apply {
            layoutError.root.visibility = View.GONE
            lottieLoading.visibility = View.GONE
            lottieProgress.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        }

        isLoading = false

        if (isReloadData) {
            mBlogList.clear()
            isReloadData = false
        }

        mBlogList.addAll(list)
        mAdapter.notifyDataSetChanged()
    }

    private fun getBlogErrorView() {
        binding.apply {
            layoutError.apply {
                root.visibility = View.VISIBLE
                layoutError.buttonTryAgain.setOnClickListener {
                    loadHashtagData()
                    loadPostData(mCurrentTagSlug)
                }
            }
            lottieLoading.visibility = View.GONE
            lottieProgress.visibility = View.GONE
        }
    }

    private fun showTagContent(data: MutableList<TagBlog>) {
        binding.layoutDropdownList.visibility = View.VISIBLE

        mTagItemList.addAll(data)
        mTagNameList.add("All")

        mTagItemList.forEach {
            mTagNameList.add((it.name!!))
        }

        mTagAdapter = ArrayAdapter(requireContext(), R.layout.item_hashtag, mTagNameList)
        binding.dropdownList.apply {
            setAdapter(mTagAdapter)
            setOnItemClickListener { adapterView, view, position, id ->
                when (mTagNameList[position] == getString(R.string.default_tag_text)) {
                    true -> setDropdownTextAndReloadData(
                        getString(R.string.default_tag_text),
                        null
                    )
                    false -> setDropdownTextAndReloadData(
                        mTagItemList[position - 1].name,
                        mTagItemList[position - 1].slug
                    )
                }
            }
        }
    }

    private fun getTagError() {
        blogViewModel.localBlogTagList.observe(
            viewLifecycleOwner,
            {
                showTagContent(it)
            }
        )
    }

    private fun getBlogContentError() {
        blogViewModel.localBlogContentList.observe(
            viewLifecycleOwner,
            {
                showBlogContent(it)
            }
        )
    }

    override fun onContentClick(item: PostBlog) {
        CustomChromeUtils().setBlogWebpage(requireContext(), item.url!!, item.title!!)
    }

    override fun onHashtagClick(hashtag: TagBlog) {
        mTagItemList.forEach {
            if (it.name == hashtag.name) {
                setDropdownTextAndReloadData(it.name, it.slug)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun setDropdownTextAndReloadData(hashtag: String?, slug: String?) {
        isReloadData = true
        mPage = 1
        mCurrentTagSlug = slug

        loadPostData(slug)

        binding.apply {
            dropdownList.setText(hashtag, false)
            lottieLoading.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }


        val foundTagList = mTagItemList.filter { tagBlog -> tagBlog.slug == slug }
        when (foundTagList.isNotEmpty()) {
            true -> {
                ImageLoader().setTagCover(
                    requireContext(),
                    foundTagList[0].feature_image,
                    binding.imageTagCover
                )
                setHeaderText(foundTagList[0].name!!, foundTagList[0].description!!)
            }
            false -> {
                ImageLoader().setTagCover(
                    requireContext(),
                    "",
                    binding.imageTagCover
                )
                setHeaderText(getString(R.string.title_tag_all), getString(R.string.description_tag_all))
            }
        }
    }

    private fun setHeaderText(title: String, description: String) {
        val spannable = SpannableStringBuilder().apply {
            bold {
                scale(1.5f) {
                    color(ContextCompat.getColor(requireContext(), R.color.colorMainText)) {
                        append(title)
                    }
                }
            }
            append("\n")
            color(ContextCompat.getColor(requireContext(), R.color.colorMainText)) {
                append(description)
            }
        }
        binding.textHeader.text = spannable
    }

    private fun openAppSettingsPage() {
        startActivity(SettingsActivity.newIntent(requireContext()))
    }

    private fun aboutMe() {
        CustomChromeUtils().setBlogWebpage(
            requireContext(),
            "https://mikkipastel.firebaseapp.com/",
            "About Mikkipastel"
        )
    }
}
