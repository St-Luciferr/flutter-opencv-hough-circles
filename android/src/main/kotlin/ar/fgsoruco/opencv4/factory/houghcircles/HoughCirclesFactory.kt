package ar.fgsoruco.opencv4.factory.houghcircles

import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.FileInputStream
import java.io.InputStream
import io.flutter.plugin.common.MethodChannel

class HoughCirclesFactory {
    companion object {
        fun process(pathType: Int, pathString: String, data: ByteArray,
                    method: Int, dp: Double, minDist: Double, param1: Double, param2: Double,
                    minRadius: Int, maxRadius: Int, centerWidth: Int,
                    centerColor: String, circleWidth: Int, circleColor: String,
                    result: MethodChannel.Result) {
            when (pathType) {
                1 -> result.success(HoughCirclesS(pathString, method, dp, minDist, param1, param2,
                        minRadius, maxRadius, centerWidth,
                        centerColor, circleWidth, circleColor))
                2 -> result.success(HoughCirclesB(data, method, dp, minDist, param1, param2,
                        minRadius, maxRadius, centerWidth,
                        centerColor, circleWidth, circleColor))
                3 -> result.success(HoughCirclesB(data, method, dp, minDist, param1, param2,
                        minRadius, maxRadius, centerWidth,
                        centerColor, circleWidth, circleColor))
            }
        }

       private fun houghCirclesS(pathString: String, method: Int, dp: Double, minDist: Double, param1: Double, param2: Double,
                          minRadius: Int, maxRadius: Int, centerWidth: Int, centerColor: String?, circleWidth: Int, circleColor: String?): ByteArray? {
           val inputStream: InputStream = FileInputStream(pathString.replace("file://", ""))
           val data: ByteArray = inputStream.readBytes()
            try {
                var byteArray = ByteArray(0)
                val circles = Mat()
                val srcGray = Mat()
                val dst = Mat()
                val filename = pathString.replace("file://", "")
                val src = Imgcodecs.imread(filename)
                // Decode image from input byte array
                val input: Mat = Imgcodecs.imdecode(MatOfByte(byteData),
                        Imgcodecs.IMREAD_UNCHANGED)
                Imgproc.HoughCircles(input, circles, method, dp, minDist, param1, param2, minRadius, maxRadius)
                if (circles.cols() > 0) {
                    for (x in 0 until circles.cols()) {
                        val circleVec: DoubleArray = circles.get(0, x) ?: break
                        val center = Point(circleVec[0].toInt(), circleVec[1].toInt())
                        val radius = circleVec[2].toInt()
                        Imgproc.circle(input, center, 3, convertColorToScalar(centerColor), centerWidth)
                        Imgproc.circle(input, center, radius, convertColorToScalar(circleColor), circleWidth)
                    }
                }
                // instantiating an empty MatOfByte class
                val matOfByte = MatOfByte()
                // Converting the Mat object to MatOfByte
                Imgcodecs.imencode(".jpg", input, matOfByte)
                byteArray = matOfByte.toArray()
            } catch (e: Exception) {
                System.out.println("OpenCV Error: " + e.toString())
            }
            return byteArray
        }

/*        //Module: Miscellaneous Image Transformations
        private fun HoughCircles(pathString: String, method: Int, dp: Double,
                                 minDist: Double, param1:Double, param2:Double,
                                 minRadius: Int , maxRadius: Int , centerWidth: Int ,
                                 centerColor: String , circleWidth: Int , circleColor: String): ByteArray? {
            val inputStream: InputStream = FileInputStream(pathString.replace("file://", ""))
            val data: ByteArray = inputStream.readBytes()

            try {
                var byteArray = ByteArray(0)
                val srcGray = Mat()
                val dst = Mat()
                val circles = Mat()
                // Decode image from input byte array
                val filename = pathString.replace("file://", "")
                val src = Imgcodecs.imread(filename)

                //
                Imgproc.HoughCircles(src, circles, method, dp,
                        minDist, param1, param2, minRadius, maxRadius)

                /////

                val circles = Mat()
                // Decode image from input byte array
                val input: Mat = Imgcodecs.imdecode(MatOfByte(byteData),
                        Imgcodecs.IMREAD_UNCHANGED)
                Imgproc.HoughCircles(input, circles, method, dp, minDist, param1, param2, minRadius, maxRadius)
                ////


                if (circles.cols() > 0) {
                    val circleVec: DoubleArray = circles.get(0, x) ?: break

                    val center = Point(circleVec[0].toInt(), circleVec[1].toInt())
                    val radius = circleVec[2].toInt()

                    Imgproc.circle(input, center, 3, convertColorToScalar(centerColor), centerWidth)
                    Imgproc.circle(input, center, radius, convertColorToScalar(circleColor), circleWidth)
                }

                // instantiating an empty MatOfByte class
                val matOfByte = MatOfByte()
                // Converting the Mat object to MatOfByte
                Imgcodecs.imencode(".jpg", dst, matOfByte)
                byteArray = matOfByte.toArray()
                return byteArray
            } catch (e: java.lang.Exception) {
                println("OpenCV Error: $e")
                return data
            }*/

    }

    //Module: Miscellaneous Image Transformations
    private fun HoughCirclesB(data: ByteArray, method: Int, dp: Double,
                              minDist: Double, param1: Double, param2: Double,
                              minRadius: Int, maxRadius: Int, centerWidth: Int,
                              centerColor: String, circleWidth: Int, circleColor: String): ByteArray? {

        try {
            var byteArray = ByteArray(0)
            val srcGray = Mat()
            val dst = Mat()
            // Decode image from input byte array
            val src = Imgcodecs.imdecode(MatOfByte(*data), Imgcodecs.IMREAD_UNCHANGED)
            // Convert the image to Gray
            Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY)

            // Adaptive Thresholding
            Imgproc.HoughCircles(srcGray, dst, maxValue, adaptiveMethod, thresholdType, blockSize, constantValue)

            // instantiating an empty MatOfByte class
            val matOfByte = MatOfByte()
            // Converting the Mat object to MatOfByte
            Imgcodecs.imencode(".jpg", dst, matOfByte)
            byteArray = matOfByte.toArray()
            return byteArray
        } catch (e: java.lang.Exception) {
            println("OpenCV Error: $e")
            return data
        }

    }
}
}