package com.mikkipastel.blog.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.scale
import androidx.recyclerview.widget.RecyclerView
import com.mikkipastel.blog.R
import com.mikkipastel.blog.adapter.PostListAdapter
import com.mikkipastel.blog.manager.BlogPostListener
import com.mikkipastel.blog.manager.BlogPostPresenter
import com.mikkipastel.blog.manager.BlogTagListener
import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog
import com.mikkipastel.blog.utils.CustomChromeUtils
import com.mikkipastel.blog.utils.ImageLoader
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_loading_error.*


class MainFragment : Fragment(), BlogPostListener, PostListAdapter.PostItemListener, BlogTagListener {

    private val mBlogList = mutableListOf<PostBlog>()
    private val mAdapter by lazy {
        PostListAdapter(mBlogList, this)
    }

    private val mTagItemList = mutableListOf<TagBlog>()
    private val mTagNameList = mutableListOf<String>()
    private lateinit var mTagAdapter : ArrayAdapter<String>
    private var mCurrentTagSlug: String? = null

    private var isLoading = false
    private var mPage = 1

    private var isReloadData = false
    private var canLazyLoading = true

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPostData(null)
        loadHashtagData()

        setToolbar()

        setHeaderText(getString(R.string.title_tag_all), getString(R.string.description_tag_all))

        swipeRefreshLayout.setOnRefreshListener {
            isReloadData = true
            mPage = 1
            loadPostData(mCurrentTagSlug)
        }

        recyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            layoutManager = linearLayoutManager
            adapter = mAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (canLazyLoading) {
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
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
//        toolbar.inflateMenu(R.menu.menu_main)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_aboutapp) {
            showDialog()
        } else if (item.itemId == R.id.action_aboutme) {
            aboutMe()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(context!!, android.R.color.white))
    }

    private fun loadPostData(hashtag: String?) {
        BlogPostPresenter().getBlogPost(mPage, hashtag, this)
    }

    private fun loadHashtagData() {
        BlogPostPresenter().getBlogTag(this)
    }

    override fun onGetPostSuccess(data: GhostBlogModel) {
        layoutError.visibility = View.GONE
        lottieLoading.visibility = View.GONE
        lottieProgress.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        swipeRefreshLayout.isRefreshing = false
        isLoading = false

        if (isReloadData) {
            mBlogList.clear()
            isReloadData = false
        }

        mBlogList.addAll(data.posts!!)
        mAdapter.notifyDataSetChanged()

        canLazyLoading = data.meta?.pagination?.next != null

        if (data.meta?.pagination?.page == 1) {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    override fun onGetPostFailure() {
        if (mBlogList.isEmpty()) {
            layoutError.visibility = View.VISIBLE
            lottieLoading.visibility = View.GONE
            buttonTryAgain.setOnClickListener {
                loadPostData(mCurrentTagSlug)
            }
        }
        lottieProgress.visibility = View.GONE
    }

    override fun onGetTagSuccess(data: MutableList<TagBlog>) {
        layoutDropdownList.visibility = View.VISIBLE

        mTagItemList.addAll(data)
        mTagNameList.add("All")

        mTagItemList.forEach {
            mTagNameList.add((it.name!!))
        }

        mTagAdapter = ArrayAdapter(requireContext(), R.layout.item_hashtag, mTagNameList)
        dropdownList.apply {
            setAdapter(mTagAdapter)
            setOnItemClickListener { adapterView, view, position, id ->
                when (mTagNameList[position] == getString(R.string.default_tag_text)) {
                    true -> setDropdownTextAndReloadData(
                            getString(R.string.default_tag_text),
                            null
                    )
                    false -> setDropdownTextAndReloadData(
                            mTagItemList[position-1].name,
                            mTagItemList[position-1].slug
                    )
                }
            }
        }
    }

    override fun onGetTagFailure() {
        layoutDropdownList.visibility = View.GONE
    }

    override fun onClick(item: PostBlog, position: Int) {
        CustomChromeUtils().setBlogWebpage(context!!, item.url!!, item.title!!)
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

        dropdownList.setText(hashtag, false)
        loadPostData(slug)
        lottieLoading.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        val foundTagList = mTagItemList.filter { tagBlog -> tagBlog.slug == slug }
        when (foundTagList.isNotEmpty()) {
            true -> {
                ImageLoader().setTagCover(context!!, foundTagList[0].feature_image, imageTagCover)
                setHeaderText(foundTagList[0].name!!, foundTagList[0].description!!)
            }
            false -> {
                ImageLoader().setTagCover(context!!, "", imageTagCover)
                setHeaderText(getString(R.string.title_tag_all), getString(R.string.description_tag_all))
            }
        }
    }

    private fun setHeaderText(title: String, description: String) {
        val spannable = SpannableStringBuilder().apply {
            bold {
                scale(1.5f) {
                    color(ContextCompat.getColor(context!!, R.color.whitegray2)) {
                        append(title)
                    }
                }
            }
            append("\n")
            color(ContextCompat.getColor(context!!, R.color.whitegray2)) {
                append(description)
            }
        }
        textHeader.text = spannable
    }

    private fun showDialog() {
        val newFragment = AboutAppFragment.newInstance()
        newFragment.show(activity?.supportFragmentManager!!.beginTransaction(), "dialog")
    }

    private fun aboutMe() {
        CustomChromeUtils().setBlogWebpage(
                context!!,
                "https://mikkipastel.firebaseapp.com/",
                "About Mikkipastel"
        )
    }
}