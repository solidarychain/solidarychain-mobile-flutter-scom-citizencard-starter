import 'dart:convert';

import 'package:SNCitizenCard/payloads/user_payload.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

import 'constants.dart';
import 'types.dart';
import 'events.dart';
import 'payloads/person_payload.dart';

void main() => runApp(MyApp());

// state vars
String _batteryLevel = '';
String _lastCardEventTypeStatus = '';
String _logContent = '';
// store function or null, to enable/disable ui component
dynamic _getCitizenCardDataFunction;
CancelListening _cancelListening;
bool _running = false;
// hide all platform messages
bool _showHeartBeatMessagesPlatformChannel = false;
CardEventTypeState _currentState;

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
    // startListening platform channel
    _cancelListening = startListening((msg) {
      // log receiving messages from platform channel
      if (_showHeartBeatMessagesPlatformChannel) log("platform channel message: '$msg'.");

      setState(() {
        // check msg is a cardEventType
        if (CardEventMapState.containsKey(msg)) {
          // update ui component
          _lastCardEventTypeStatus = msg;
          // assign _currentState
          _currentState = enumTypeFromString(msg);
          // warn only enable if CARD_READY, not on READER_READY, on boot always disabled even we have a card on reader, we must remove and insert card on app start
          // this is because we never catch the CARD_READY
          // enable/disable button, assigning the _getCitizenCardData function or null to be disabled
          _getCitizenCardDataFunction = (_currentState == CardEventTypeState.CARD_READY) ? _getCitizenCardData : null;
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
                onPressed: _getCitizenCardDataFunction,
              ),
              Padding(
                padding: const EdgeInsets.only(top: 5.0, bottom: 5.0),
                child: Text(_lastCardEventTypeStatus),
              ),
              RaisedButton(
                child: Text('Send/Receive Map/Json'),
                onPressed: _sendReceiveMapJson,
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
                          fontSize: 12.0,
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
      final int result = await platformBattery.invokeMethod(METHOD_CHANNEL_BATTERY_GET_BATTERY_LEVEL);
      batteryLevel = 'Battery level at $result %';
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }

    setState(() {
      _batteryLevel = batteryLevel;
    });
  }

  Future<void> _getCitizenCardData() async {
    try {
      setState(() {
        // disable button
        _getCitizenCardDataFunction = null;
      });

      final Map<String, dynamic> result =
          await platformCitizenCard.invokeMapMethod(METHOD_CHANNEL_CITIZEN_CARD_GET_CITIZEN_CARD_DATA);
      setState(() {
        var person = Person.fromJson(result);
        // output card data
        log('${person.firstName}, ${person.lastName}, ${person.identityNumber}, ${person.fiscalNumber}, ${person.beneficiaryNumber}');
        // enable/disable button after readCard
        _getCitizenCardDataFunction = (_currentState == CardEventTypeState.CARD_READY) ? _getCitizenCardData : null;
      });
    } on PlatformException catch (e) {
      log('Failed to get Person Payload: ${e.message}.');
    }
  }

  Future<void> _sendReceiveMapJson() async {
    try {
      final Map<String, dynamic> result = await platformCitizenCard.invokeMapMethod(
          METHOD_CHANNEL_CITIZEN_CARD_SEND_RECEIVE_MAP_JSON_TEST,
          METHOD_CHANNEL_CITIZEN_CARD_SEND_RECEIVE_MAP_JSON_TEST_DATA);
      var user = User.fromJson(result);
      log('${user.userDob}, ${user.userDescription}');
    } on PlatformException catch (e) {
      log('Failed to get User Payload: ${e.message}.');
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
