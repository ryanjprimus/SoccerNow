package com.primus.utils.color_gen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

interface ColorGenerator {


    suspend fun convertImageUrlToBitmap(
        imageUrl: String
    ): Bitmap?

    fun extractColorsFromBitmap(bitmap: Bitmap): Map<String, String>

    class ColorGeneratorImpl(private val context: Context) : ColorGenerator {
        override suspend fun convertImageUrlToBitmap(imageUrl: String): Bitmap? {
            val loader = ImageLoader(context = context)
            val request = ImageRequest.Builder(context = context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val imageResult = loader.execute(request = request)
            return if (imageResult is SuccessResult) {
                (imageResult.drawable as BitmapDrawable).bitmap
            } else {
                null
            }
        }

        override fun extractColorsFromBitmap(bitmap: Bitmap): Map<String, String> {
            return mapOf(
                "vibrant" to parseColorSwatch(
                    color = Palette.from(bitmap).generate().vibrantSwatch
                ),
                "darkVibrant" to parseColorSwatch(
                    color = Palette.from(bitmap).generate().darkVibrantSwatch
                ),
                "onDarkVibrant" to parseBodyColor(
                    color = Palette.from(bitmap).generate().darkVibrantSwatch?.bodyTextColor
                ),
                "lightVibrant" to parseColorSwatch(
                    color = Palette.from(bitmap).generate().lightVibrantSwatch
                ),
                "domainSwatch" to parseColorSwatch(
                    color = Palette.from(bitmap).generate().dominantSwatch
                ),
                "mutedSwatch" to parseColorSwatch(
                    color = Palette.from(bitmap).generate().mutedSwatch
                ),
                "lightMuted" to parseColorSwatch(
                    color = Palette.from(bitmap).generate().lightMutedSwatch
                ),
                "darkMuted" to parseColorSwatch(
                    color = Palette.from(bitmap).generate().darkMutedSwatch
                ),
            )
        }

        private fun parseColorSwatch(color: Palette.Swatch?): String {
            return if (color != null) {
                val parsedColor = Integer.toHexString(color.rgb)
                return "#$parsedColor"
            } else {
                "#000000"
            }
        }

        private fun parseBodyColor(color: Int?): String {
            return if (color != null) {
                val parsedColor = Integer.toHexString(color)
                "#$parsedColor"
            } else {
                "#FFFFFF"
            }
        }

    }
}