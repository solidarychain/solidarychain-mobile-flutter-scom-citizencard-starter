import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

import 'constants.dart';
import 'types.dart';
import 'events.dart';
import 'person_payload.dart';

void main() => runApp(MyApp());

// state vars
String _batteryLevel = '';
String _lastCardEventTypeStatus = '';
String _logContent = '';
dynamic _readCardFunction = null;
CancelListening _cancelListening;
bool _running = false;

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'SolidaryChain: CitizenCard Reader'),
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

// Call inside a setState({ }) block to be able to reflect changes on screen
void log(String logString) {
  // add to top
  _logContent = '${logString.toString()}\n$_logContent';
}

// state object
class _MyHomePageState extends State<MyHomePage> {
  // method channel: single platform method that returns the battery level: must match METHOD_CHANNEL_BATTERY and METHOD_CHANNEL_CITIZEN_CARD on android
  static const platformBattery = const MethodChannel(METHOD_CHANNEL_BATTERY);
  static const platformCitizenCard = const MethodChannel(METHOD_CHANNEL_CITIZEN_CARD);

  @override
  void initState() {
    super.initState();
    _cancelListening = startListening((msg) {
      setState(() {
        log(msg);
        if (cardEventType.containsKey(msg)) {
          // enable/disable button
          _readCardFunction = (enumTypeFromString(msg) == cardEventTypeState.CARD_READY) ? _getPersonPayload : null;
          _lastCardEventTypeStatus = msg;
        }
      });
    });
  }

  @override
  void dispose() {
    super.dispose();
    _cancelListening();
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Container(
        color: Colors.blueGrey[100],
        child: Container(
          margin: EdgeInsets.only(left: 20.0, top: 40.0, right: 20.0, bottom: 20.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              RaisedButton(
                child: Text('Get Battery Level'),
                onPressed: _getBatteryLevel,
              ),
              Padding(
                padding: const EdgeInsets.only(top: 5.0, bottom: 5.0),
                child: Text(_batteryLevel),
              ),
              RaisedButton(
                child: Text('ReadCard'),
                onPressed: _readCardFunction,
              ),
              Padding(
                padding: const EdgeInsets.only(top: 5.0, bottom: 5.0),
                child: Text(_lastCardEventTypeStatus),
              ),
              RaisedButton(
                child: Text('Playground'),
                onPressed: _runPlayground,
              ),
              Expanded(
                child: Container(
                  decoration: new BoxDecoration(
                    borderRadius: new BorderRadius.circular(1.0),
                    color: Colors.white,
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Container(
                      child: new Text(
                        _logContent,
                        style: new TextStyle(
                          fontSize: 8.0,
                          color: Colors.black87,
                        ),
                        textAlign: TextAlign.left,
                        overflow: TextOverflow.fade,
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
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
    try {
      final Map<String, dynamic> result = await platformCitizenCard.invokeMapMethod('getCitizenCardData');
      setState(() {
        var person = Person.fromJson(result);
        // eventResult log
        log('${person.firstName}, ${person.lastName}');
      });
    } on PlatformException catch (e) {
      log('Failed to get Person Payload: ${e.message}.');
    }
  }

// Main function called when playground is run
  void _runPlayground() async {
    if (_running) return;
    _running = true;
    var cancel = startListening((msg) {
      setState(() {
        log(msg);
      });
    });

    await Future.delayed(Duration(seconds: 10));
    cancel();
    _running = false;
  }
}
