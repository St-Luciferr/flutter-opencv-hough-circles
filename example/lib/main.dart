import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

//uncomment when image_picker is installed
import 'package:image_picker/image_picker.dart';
import 'package:opencv_4/factory/pathfrom.dart';
import 'package:opencv_4/opencv_4.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Soil Color',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'Soil Color'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, this.title}) : super(key: key);

  final String? title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  File? _image;
  Uint8List? _byte, salida;
  String _versionOpenCV = 'OpenCV';
  bool _visible = false;

  //uncomment when image_picker is installed
  final picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    _getOpenCVVersion();
  }

  testOpenCV({
    required String pathString,
    required CVPathFrom pathFrom,
    required double thresholdValue,
    required double maxThresholdValue,
    required int thresholdType,
  }) async {
    try {
      //test with threshold
      /* _byte = await Cv2.threshold(
        pathFrom: pathFrom,
        pathString: pathString,
        maxThresholdValue: maxThresholdValue,
        thresholdType: thresholdType,
        thresholdValue: thresholdValue,
      );*/

      _byte = await Cv2.houghCircles(
          pathFrom: pathFrom,
          pathString: pathString,
          method: 3,
          dp: 2.0,
          minDist: 240,
          param1: 100,
          param2: 100,
          minRadius: 100,
          maxRadius: 200,

          centerWidth: 75,
          centerColor: "#c47244",
          circleWidth: 28,
          circleColor: "#000000");

/*
      _byte = await Cv2.scharr(
        pathFrom: pathFrom,
        pathString: pathString,
        depth: Cv2.CV_SCHARR,
        dx: 0,
        dy: 1,
      );*/

      setState(() {
        _visible = false;
      });
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  /// metodo que devuelve la version de OpenCV utilizada
  Future<void> _getOpenCVVersion() async {
    String? versionOpenCV = await Cv2.version();
    setState(() {
      _versionOpenCV = 'Soil Color';
    });
  }

  _testFromAssets() async {
    testOpenCV(
      pathFrom: CVPathFrom.ASSETS,
      pathString: 'assets/etalon4.png',
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: Cv2.THRESH_BINARY,
    );
    setState(() {
      _visible = true;
    });
  }

  _testFromUrl() async {
    testOpenCV(
      pathFrom: CVPathFrom.URL,
      pathString:
          'https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/16fe9f114930481.6044f05fca574.jpeg',
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: Cv2.THRESH_BINARY,
    );
    setState(() {
      _visible = true;
    });
  }

  _testFromCamera() async {
    //uncomment when image_picker is installed
    final pickedFile = await picker.getImage(source: ImageSource.camera);
    _image = File(pickedFile!.path);
    testOpenCV(
      pathFrom: CVPathFrom.GALLERY_CAMERA,
      pathString: _image!.path,
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: Cv2.THRESH_BINARY,
    );

    setState(() {
      _visible = true;
    });
  }

  _testFromGallery() async {
    //uncomment when image_picker is installed
    final pickedFile = await picker.getImage(source: ImageSource.gallery);
    _image = File(pickedFile!.path);
    testOpenCV(
      pathFrom: CVPathFrom.GALLERY_CAMERA,
      pathString: _image!.path,
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: Cv2.THRESH_BINARY,
    );

    setState(() {
      _visible = true;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title!),
      ),
      body: SingleChildScrollView(
        child: Container(
          child: Column(
            children: [
              Container(
                  margin: EdgeInsets.only(top: 20),
                  child: Center(
                    child: Column(
                      children: <Widget>[
                        Text(
                          _versionOpenCV,
                          style: TextStyle(fontSize: 23),
                        ),
                        Container(
                          margin: EdgeInsets.only(top: 5),
                          child: _byte != null
                              ? Image.memory(
                                  _byte!,
                                  width: 300,
                                  height: 300,
                                  fit: BoxFit.fill,
                                )
                              : Container(
                                  width: 300,
                                  height: 300,
                                  child: Icon(
                                    Icons.camera_alt,
                                    color: Colors.grey[800],
                                  ),
                                ),
                        ),
                        Visibility(
                            maintainAnimation: true,
                            maintainState: true,
                            visible: _visible,
                            child:
                                Container(child: CircularProgressIndicator())),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('Image from assets'),
                            onPressed: _testFromAssets,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('Image from url'),
                            onPressed: _testFromUrl,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('Image from gallery'),
                            onPressed: _testFromGallery,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('Image from camara'),
                            onPressed: _testFromCamera,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                      ],
                    ),
                  )),
            ],
          ),
        ),
      ),
    );
  }
}
