import 'package:flutter/services.dart';

const _channel = const EventChannel('events');

typedef void Listener(dynamic msg);
typedef void CancelListening();

// in case you want to support multiple listeners simultaneously,
// you may want identify them to be able to recognize them during cancellation.
int nextListenerId = 1;

// helper function to initiate the communication with the native side
CancelListening startListening(Listener listener) {
  var subscription = _channel
      .receiveBroadcastStream(nextListenerId++)
      .listen(listener, cancelOnError: true);

  // return subscription.cancel() method to cancel subscription
  return () {
    subscription.cancel();
  };
}
