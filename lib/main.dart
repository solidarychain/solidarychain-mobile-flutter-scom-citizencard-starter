import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

// Get battery level.
String _batteryLevel = 'Unknown battery level.';
dynamic _personPayload = 'Unknown person.';

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  // state object
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

// state object
class _MyHomePageState extends State<MyHomePage> {
  // method channel: single platform method that returns the battery level
  static const platformBattery = const MethodChannel('samples.flutter.dev/battery');
  static const platformCitizenCard = const MethodChannel('samples.flutter.dev/citizencard');

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            RaisedButton(
              child: Text('Get Battery Level'),
              onPressed: _getBatteryLevel,
            ),
            Text(_batteryLevel),
            Divider(),
            RaisedButton(
              child: Text('ReadCard'),
              onPressed: _getPersonPayload,
            ),
            Text(_personPayload),
          ],
        ),
      ),
    );
  }

  Future<void> _getBatteryLevel() async {
    String batteryLevel;
    try {
      final int result = await platformBattery.invokeMethod('getBatteryLevel');
      batteryLevel = 'Battery level at $result %';
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }

    setState(() {
      _batteryLevel = batteryLevel;
    });
  }

  Future<void> _getPersonPayload() async {
    dynamic personPayload;
    try {
      final dynamic result = await platformCitizenCard.invokeMethod('getCitizenCardData');
      personPayload = 'Person Payload $result';
    } on PlatformException catch (e) {
      personPayload = "Failed to get Person Payload: '${e.message}'.";
    }

    setState(() {
      _personPayload = personPayload;
    });
  }

}
