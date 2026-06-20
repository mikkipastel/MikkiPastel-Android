package com.mikkipastel.blog.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mikkipastel.blog.model.PostBlog
import com.mikkipastel.blog.model.TagBlog

@Composable
fun MainScreen() {
    //
}

@Composable
fun ShowBlogContent(data: MutableList<PostBlog>) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(data.size) { index ->
                val postBlog = data[index]
                PostItemView(
                    postBlog = postBlog,
                    onContentClick = {},
                    onHashtagClick = {}
                )
            }
        }
        LottieProgress()
    }
}

@Composable
fun LottieProgress() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("progress-loading.json")
    )
    LottieAnimation(
        composition = composition,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )
}

@Composable
fun LottieLoading() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("dino-loading.json")
    )
    LottieAnimation(
        composition = composition,
        modifier = Modifier.fillMaxSize(),
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F5)
@Composable
fun PreviewShowBlogContent() {
    ShowBlogContent(
        data = mutableListOf(
            PostBlog(
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
            ),
            PostBlog(
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
            ),
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F5)
@Composable
fun PreviewLottieProgress() {
    LottieProgress()
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F5)
@Composable
fun PreviewLottieLoading() {
    LottieLoading()
}
