package com.engineer.gif.revert.internal

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.FutureTarget
import com.engineer.gif.revert.ResFrame
import com.engineer.gif.revert.lib.AnimatedGIFWriter
import com.engineer.gif.revert.lib.AnimatedGifEncoder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Collections.reverse

/**
 * @author rookie
 * @since 07-06-2019
 */
const val TAG = "GifFactory"

internal object _GifFactory :_BaseGifFactory(){

    override fun genGifByFrames(context: Context, frames: List<ResFrame>): String {
        val t1 = TaskTime()

        val os = ByteArrayOutputStream()
        val encoder = AnimatedGifEncoder()
        encoder.start(os)
        encoder.setRepeat(0)
        for (value in frames) {
            val bitmap = BitmapFactory.decodeFile(value.path)
            encoder.setDelay(value.delay)
            val t2 = TaskTime()
            encoder.addFrame(bitmap)
            t2.release("addFrame")
            Log.e("GifFactory", frames.indexOf(value).toString())
            bitmap.recycle()
        }
        val t3 = TaskTime()
        encoder.finish()
        t3.release("finish")

        val path = IOTool.saveStreamToSDCard("test", os)
        os.close()
        t1.release("genGifByFrames")
        IOTool.notifySystemGallery(context, path)
        log(path)
        return path
    }


}