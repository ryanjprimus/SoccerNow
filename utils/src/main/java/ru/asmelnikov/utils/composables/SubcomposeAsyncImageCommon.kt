package ru.asmelnikov.utils.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.ui.theme.dimens

@Composable
fun SubComposeAsyncImageCommon(
    modifier: Modifier = Modifier,
    imageUri: Any,
    shape: Shape,
    alpha: Float = 1f,
    size: Dp = MaterialTheme.dimens.champLogoDefaultSize,
    contentDescription: String? = null,
    errorPlaceHolder: Int = R.drawable.placeholder_photo,
    loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = {
        CircularProgressIndicator()
    }
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri)
            .crossfade(true)
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        loading = loading,
        error = {
            painterResource(errorPlaceHolder)
        },
        alpha = alpha,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(shape)
    )
}