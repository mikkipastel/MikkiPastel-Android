package com.mikkipastel.blog.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mikkipastel.blog.R

@Composable
fun LoadingErrorView(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.Asset("/loading-error.json")
        )
        LottieAnimation(
            composition = composition,
            modifier = Modifier.width(250.dp).height(250.dp)
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(id = R.string.msg_offline),
            color = colorResource(R.color.colorMainTextBlack),
            fontSize = 20.sp
        )
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = onClick
        ) {
            Text(stringResource(id = R.string.action_try_again))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xF5F5F5)
@Composable
fun PreviewLoadingErrorView() {
    LoadingErrorView(
        onClick = {}
    )
}