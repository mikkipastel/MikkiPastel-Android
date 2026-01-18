package com.mikkipastel.blog.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mikkipastel.blog.R
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog

@Composable
fun PostItemView(
    postBlog: PostBlog,
    onContentClick: (item: PostBlog) -> Unit,
    onHashtagClick: (hashtag: TagBlog) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        onClick = {
            onContentClick(postBlog)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = postBlog.featureImage,
                contentDescription = postBlog.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder_loading),
                error = painterResource(id = R.drawable.placeholder_blog)
            )
            Text(
                text = postBlog.title.orEmpty(),
                fontSize = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(all = 8.dp)
            )
            Text(
                text = postBlog.customExcerpt.orEmpty().replace("\n", ""),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                thickness = 0.5.dp,
                color = colorResource(id = R.color.colorDividerLine)
            )
            LazyRow(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                items(postBlog.tags?.size ?: 0) { index ->
                    val tag = postBlog.tags?.get(index)
                    tag?.let {
                        SuggestionChip(
                            onClick = { onHashtagClick(tag) },
                            label = { Text(text = tag.name.orEmpty()) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F5)
@Composable
fun sampleBlogItem() {
    val postBlog = PostBlog(
        title = "รู้ยัง ใช้ NotebookLM บน Gemini ได้แล้วนะ!",
        featureImage = "https://www.mikkipastel.com/content/images/2025/12/notebooklm-cover.webp",
        customExcerpt = "แล้ว NotebookLM คืออะไร? แล้วใช้งานใน Gemini ได้ยังไง? มาดูกัน",
        tags = arrayListOf(
            TagBlog(
                id = "1",
                name = "NotebookLM",
                slug = "notebooklm",
                description = "NotebookLM"
            ),
            TagBlog(
                id = "2",
                name = "Gemini",
                slug = "gemini",
                description = "Gemini"
            )
        ),
        publishedAt = "2025-12-26T12:55:47.000+07:00",
        url = "https://www.mikkipastel.com/notebooklm-gemini/",
    )
    PostItemView(
        postBlog = postBlog,
        onContentClick = {},
        onHashtagClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F5)
@Composable
fun sampleImageContentScale() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.placeholder_blog),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.None
        )
    }
}