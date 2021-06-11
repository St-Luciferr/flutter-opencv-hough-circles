package ar.fgsoruco.opencv4.factory.houghcircles

import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.FileInputStream

import org.opencv.core.Core

import org.opencv.core.CvType


import org.opencv.core.Point

import org.opencv.core.Rect

import org.opencv.core.Scalar

import org.opencv.core.Size

import java.io.InputStream
import io.flutter.plugin.common.MethodChannel

class HoughCirclesFactory {
    companion object {
        fun process(pathType: Int, pathString: String, data: ByteArray,
                    method: Int, dp: Double, minDist: Double, param1: Double, param2: Double,
                    centerWidth: Int, centerColor: String,
                    circleWidth: Int, circleColor: String,
                    minRadius: Int, maxRadius: Int,
                    result: MethodChannel.Result) {
            when (pathType) {
                1 -> result.success(houghCirclesS(pathString, method, dp, minDist, param1, param2,
                        minRadius, maxRadius, centerWidth,
                        centerColor, circleWidth, circleColor))
               2 -> result.success(
                        houghCirclesB(data,method, dp, minDist,param1, param2,centerWidth, centerColor,circleWidth, circleColor,minRadius, maxRadius))
                3 -> result.success(houghCirclesB(data,method, dp, minDist,param1, param2,centerWidth, centerColor,circleWidth, circleColor,minRadius, maxRadius))
           }
        }


        fun convertColorToScalar(color: String): Scalar? {
            val on1 : Double = color.substring(1, 3).toDouble()
            val on2 : Double = color.substring(3, 5).toDouble()
            val on3 : Double = color.substring(5, 7).toDouble()
            return Scalar(on1, on2, on3)
        }

        private fun houghCirclesS(pathString: String,
                      method: Int, dp: Double, minDist: Double, param1: Double, param2: Double,
                      minRadius: Int, maxRadius: Int,
                      centerWidth: Int, centerColor: String,
                      circleWidth: Int, circleColor: String): ByteArray? {
            val inputStream: InputStream = FileInputStream(pathString.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()
            var byteArray = ByteArray(0)
            try {
                val circles = Mat()
                val srcGray = Mat()
                val dst = Mat()
                val filename = pathString.replace("file://", "")
                val src = Imgcodecs.imread(filename)
                Imgproc.HoughCircles(src, circles, method, dp, minDist, param1, param2, minRadius, maxRadius)
                if (circles.cols() > 0) {
                    for (x in 0 until circles.cols()) {
                        val circleVec: DoubleArray = circles.get(0, x) ?: break
                        val center = Point(circleVec[0].toDouble(), circleVec[1].toDouble())
                        val radius = circleVec[2].toInt()
                        Imgproc.circle(src, center, 3, convertColorToScalar(centerColor), centerWidth)
                        Imgproc.circle(src, center, radius, convertColorToScalar(circleColor), circleWidth)
                    }
                }
                // instantiating an empty MatOfByte class
                val matOfByte = MatOfByte()
                // Converting the Mat object to MatOfByte
                Imgcodecs.imencode(".jpg", src, matOfByte)
                byteArray = matOfByte.toArray()
                return byteArray
            } catch (e: Exception) {
                System.out.println("OpenCV Error: " + e.toString())
                return data
            }
        }
    }


    private fun houghCirclesB(data: ByteArray,
                              method: Int, dp: Double, minDist: Double,
                              param1: Double, param2: Double,
                              centerWidth: Int, centerColor: String,
                              circleWidth: Int, circleColor: String,
                              minRadius: Int, maxRadius: Int): ByteArray? {

        var byteArray = ByteArray(0)
        try {
            val circles = Mat()
            val srcGray = Mat()
            val dst = Mat()
            // Decode image from input byte array
            val src: Mat = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)
            Imgproc.HoughCircles(src, circles, method, dp, minDist, param1, param2, minRadius, maxRadius)
            if (circles.cols() > 0) {
                for (x in 0 until circles.cols()) {
                    val circleVec: DoubleArray = circles.get(0, x) ?: break
                    val center = Point(circleVec[0].toDouble(), circleVec[1].toDouble())
                    val radius = circleVec[2].toInt()
                    Imgproc.circle(src, center, 3, convertColorToScalar(centerColor), centerWidth)
                    Imgproc.circle(src, center, radius, convertColorToScalar(circleColor), circleWidth)
                }
            }
            // instantiating an empty MatOfByte class
            val matOfByte = MatOfByte()
            // Converting the Mat object to MatOfByte
            Imgcodecs.imencode(".jpg", src, matOfByte)
            byteArray = matOfByte.toArray()
            return byteArray
        } catch (e: Exception) {
            System.out.println("OpenCV Error: " + e.toString())
            return data
        }

    }


    //Module: Miscellaneous Image Transformations
    private fun houghCirclesB2(data: ByteArray,
                              method: Int, dp: Double, minDist: Double,
                              param1: Double, param2: Double,
                              centerWidth: Int, centerColor: String,
                              circleWidth: Int, circleColor: String,
                              minRadius: Int, maxRadius: Int): ByteArray?
    {
        var byteArray = ByteArray(0)
        try {
            val circles = Mat()
            val srcGray = Mat()
            val dst = Mat()
            // Decode image from input byte array
            val src: Mat = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)
            Imgproc.HoughCircles(src, circles, method, dp, minDist, param1, param2, minRadius, maxRadius)
            if (circles.cols() > 0) {
                for (x in 0 until circles.cols()) {
                    val circleVec: DoubleArray = circles.get(0, x) ?: break
                    val center = Point(circleVec[0].toDouble(), circleVec[1].toDouble())
                    val radius = circleVec[2].toInt()
                    Imgproc.circle(src, center, 3, convertColorToScalar(centerColor), centerWidth)
                    Imgproc.circle(src, center, radius, convertColorToScalar(circleColor), circleWidth)
                }
            }
            // instantiating an empty MatOfByte class
            val matOfByte = MatOfByte()
            // Converting the Mat object to MatOfByte
            Imgcodecs.imencode(".jpg", src, matOfByte)
            byteArray = matOfByte.toArray()
            return byteArray
        } catch (e: Exception) {
            System.out.println("OpenCV Error: " + e.toString())
            return data
        }
    }
}