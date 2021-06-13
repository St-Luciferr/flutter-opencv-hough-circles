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

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape

class HoughCirclesFactory {
    companion object {


        private fun houghCirclesOnly(data: ByteArray,
                                     method: Int, dp: Double, minDist: Double,
                                     param1: Double, param2: Double,
                                     centerWidth: Int, centerColor: String,
                                     circleWidth: Int, circleColor: String,
                                     minRadius: Int, maxRadius: Int): ByteArray? {

            var byteArray = ByteArray(0)
            var intArray = IntArray(0)
            try {
                val circles = Mat()
                val srcGray = Mat()
                var srcNew = Mat()
                // Decode image from input byte array
                val src: Mat = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)

                val small = src
                Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY)

                Imgproc.HoughCircles(srcGray, circles, method, dp, minDist, param1, param2, minRadius, maxRadius)
                if (circles.cols() > 0) {
                    for (x in 0 until circles.cols()) {
                        val circleVec: DoubleArray = circles.get(0, x) ?: break
                        val center = Point(circleVec[0].toDouble(), circleVec[1].toDouble())
                        val row = circleVec[0]
                        val col = circleVec[1]

                        print("--> 54 circleVec[1]  " + circleVec[0].toString())
                        println(" circleVec[1] " + circleVec[1].toString())
                        val radius = circleVec[2].toInt()
                        println("--> 55 radius " + radius.toString())
                        println("--> 55 row " + row.toString())
                        println("--> 55 col " + col.toString())

                        print("--> 55 minRadius " + minRadius.toString())
                        println(" maxRadius " + maxRadius.toString())
                        print("--> 55 centerColor " + centerColor.toString())
                        println(" centerWidth " + centerWidth.toString())
                        print("--> 55 circleColor " + circleColor.toString())
                        println(" circleWidth " + circleWidth.toString())

//                        Imgproc.circle(src, center, 33, Scalar(255.0,0.0,0.0), centerWidth)
//                        Imgproc.circle(src, center, 43, convertColorToScalar(centerColor), centerWidth)
//                        Imgproc.circle(src, center, radius, Scalar(0.0,0.0,255.0), circleWidth)
                        Imgproc.circle(src, center, radius, convertColorToScalar(circleColor), circleWidth)

                        val scal: DoubleArray = small.get(row .toInt(),col.toInt())
                        var scalMean: DoubleArray = small.get(row .toInt(),col.toInt())
                        val b = 0 // blue index 0
                        val g = 0 // green index 1
                        val r = 0 // red index 2

                        var meanBadd: Double = 0.0
                        var meanGadd: Double = 0.0
                        var meanRadd: Double = 0.0
                        var n = 0
                        val size = 100
                        for (x in row.toInt()-size until row.toInt()+size) {
                            for (y in col.toInt()-size until col.toInt()+size) {
                                n++
                                scalMean = small.get(x.toInt(),y.toInt())
                                meanBadd += scalMean[0]
                                meanGadd += scalMean[1]
                                meanRadd += scalMean[2]
                            }
                        }
                        println(" meanBadd = " + meanBadd.toString() + " meanGadd = "+  meanGadd.toString() + " meanRadd = " + meanRadd.toString() )

                        val meanB: Double = meanBadd / n
                        val meanG: Double = meanGadd / n
                        val meanR: Double = meanRadd / n
                        println(" meanB = " + meanB.toString() + " meanG = " + meanG.toString() + " meanR = " + meanR.toString() )

                        println(" B " + meanB.toString())
                        println(" G " + meanG.toString())
                        println(" R " + meanR.toString())

/*                        println(" R " + scal[2].toString())
                        println(" G " + scal[1].toString())
                        println(" B " + scal[0].toString())
                        println(" A " + scal[3].toString())*/

                        val hexValueBGR = toHex(meanB, meanG, meanR)
                        val hexValueRGB = toHex(meanR, meanG, meanB)
                        println(" hexValueBGR " + hexValueBGR.toString())
                        println(" hexValueRGB " + hexValueRGB.toString())

                        // converted to RGB
//                        Imgproc.circle(src, center, radius, Scalar(scal[2], scal[1], scal[0]), circleWidth)
                        // original u BGR

                        val centerCompare = Point((circleVec[0]-radius-130).toDouble(), circleVec[1].toDouble())
                        Imgproc.circle(src, centerCompare, radius/2, Scalar(meanR, meanG, meanB), 100) // bgr
//                        Imgproc.circle(src, centerCompare, radius/2, Scalar(meanB, meanG, meanR), 100) // bgr
//                        Imgproc.circle(src, centerCompare, radius/2, Scalar(scal[0], scal[1], scal[2]), 100) // bgr
//                        Imgproc.circle(src, centerCompare, radius/2, convertColorToScalar("#7fdfe8"), 100)
//                        Imgproc.circle(src, centerCompare, radius, convertColorToScalar("#3bd7fc"), 100)
// #fcd73b to je rgb
                        // fcd73b in BGR : 3bd7fc
                        // e8df7f -> 7fdfe8
                        println(" circleWidth " + circleWidth.toString())
                        Imgproc.putText(src,
                                hexValueRGB,
                                Point(row - radius,col - radius - 10), // Coordinates
                                2, //FONT_HERSHEY_DUPLEX = 0, // Font
//                                6, //FONT_HERSHEY_COMPLEX_SMALL, // Font
                                2.7, // Scale. 2.0 = 2x bigger
                                Scalar(0.0,255.0,255.0), // BGR Color
                                3, // Line Thickness (Optional)
                                16 /*LINE_AA*/); // Anti-alias (Optional)
//                        Scalar scal = Imgproc.at(src, col, row)
                    }
                }
                // instantiating an empty MatOfByte class
                val matOfByte = MatOfByte()
                // Converting the Mat object to MatOfByte
//                Imgcodecs.imencode(".jpg", dst, matOfByte)
                Imgcodecs.imencode(".jpg", src, matOfByte)
                byteArray = matOfByte.toArray()
                return byteArray
            } catch (e: Exception) {
                System.out.println("OpenCV Error: " + e.toString())
                return data
            }
        }

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
                2 -> result.success(houghCirclesOnly(data, method, dp, minDist, param1, param2,
                        centerWidth, centerColor,
                        circleWidth, circleColor,
                        minRadius, maxRadius ))
                3 -> result.success(houghCirclesOnly(data, method, dp, minDist, param1, param2,
                        centerWidth, centerColor,
                        circleWidth, circleColor,
                        minRadius, maxRadius )) }
        }


        fun convertColorToScalar(color: String): Scalar? {
            val on1 : Double = color.substring(1, 3).toInt(16).toDouble()
            val on2 : Double = color.substring(3, 5).toInt(16).toDouble()
            val on3 : Double = color.substring(5, 7).toInt(16).toDouble()
            return Scalar(on1, on2, on3)
        }

        //Displays hex representation of displayed color
        fun toHex(red: Double, green: Double, blue: Double ): String {
            var r: Int = red.toInt()
            var g: Int = green.toInt()
            var b: Int = blue.toInt()
            var strR: String = r.toString(16)
            var strG: String = g.toString(16)
            var strB: String = b.toString(16)
            if(r < 16) strR = "0" + strR;
            if(g < 16) strG = "0" + strG;
            if(r < 16) strB = "0" + strB;
            var hex: String = "#" + strB + strG + strR
            return hex
        }

        private fun houghCirclesS(pathString: String,
                      method: Int, dp: Double, minDist: Double, param1: Double, param2: Double,
                      minRadius: Int, maxRadius: Int,
                      centerWidth: Int, centerColor: String,
                      circleWidth: Int, circleColor: String): ByteArray? {
            val inputStream: InputStream = FileInputStream(pathString.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()
            return houghCirclesOnly(data, method, dp, minDist, param1, param2,
                    centerWidth, centerColor,
                    circleWidth, circleColor,
                    minRadius, maxRadius )

        }
    }

}