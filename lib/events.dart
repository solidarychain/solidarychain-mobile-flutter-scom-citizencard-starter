import 'package:flutter/services.dart';

const _channel = const EventChannel('events');

typedef void Listener(dynamic msg);
typedef void CancelListening();

int nextListenerId = 1;

// helper function to initiate the communication with the native side
CancelListening startListening(Listener listener) {
  var subscription = _channel
      .receiveBroadcastStream(nextListenerId++)
      .listen(listener, cancelOnError: true);

  return () {
    subscription.cancel();
  };
}
