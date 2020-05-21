package network.solidary.mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.ptech.cc.android.sdk.exported.CitizenCard;
import io.ptech.cc.android.sdk.exported.CitizenCardReader;
import io.ptech.cc.android.sdk.exported.exceptions.CardException;
import io.ptech.cc.android.sdk.exported.exceptions.InvalidLicenseException;
import io.ptech.cc.android.sdk.exported.exceptions.NoCardDetectedException;
import io.ptech.cc.android.sdk.exported.model.Person;

public class MainActivity extends FlutterActivity {

  private static final String TAG = "network.solidary.mobile";
  final String REMOTE_HOST_ADDRESS = "https://api.citizencard.cadsh.com";
  // flutter/android constants: must be inSync with flutter/android
  // define method channel: Make sure to use the same channel name as was used on the Flutter client side.
  private static final String PLATFORM_EVENT_CHANNEL = "channel-events";
  private static final String METHOD_CHANNEL_BATTERY = "network.solidary.mobile/battery";
  private static final String METHOD_CHANNEL_BATTERY_GET_BATTERY_LEVEL = "getBatteryLevel";
  private static final String METHOD_CHANNEL_CITIZEN_CARD = "network.solidary.mobile/citizencard";
  private static final String METHOD_CHANNEL_CITIZEN_CARD_GET_CITIZEN_CARD_DATA = "getCitizenCardData";
  private static final String METHOD_CHANNEL_CITIZEN_CARD_SEND_RECEIVE_MAP_JSON_TEST = "sendReceiveMapJsonTest";

  // event channel
  private EventChannel channel;
  // Listeners
  private Map<Object, Runnable> listeners = new HashMap<>();
  // store EventChannel emitter reference, to be used outside of
  private EventChannel.EventSink eventChannelEmitter;
  // timer
  private static final int timerInterval = 5000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Prepare channel: single instance to EventChannel.StreamHandler interface grabs all listener request by itself
    channel = new EventChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), PLATFORM_EVENT_CHANNEL);
    channel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object listener, EventChannel.EventSink eventSink) {
        startListening(listener, eventSink);
        // require to initialize citizenCardReader after initialize eventChannel, init card and don't send initial events to flutter
        setupCitizenCardReader();
      }

      @Override
      public void onCancel(Object listener) {
        cancelListening(listener);
      }
    });
  }

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
    GeneratedPluginRegistrant.registerWith(flutterEngine);

    // create a MethodChannel and set a MethodCallHandler inside the onCreate() method
    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), METHOD_CHANNEL_BATTERY)
        .setMethodCallHandler(
            (call, result) -> {
              // Note: this method is invoked on the main thread.
              if (call.method.equals(METHOD_CHANNEL_BATTERY_GET_BATTERY_LEVEL)) {
                int batteryLevel = getBatteryLevel();

                if (batteryLevel != -1) {
                  result.success(batteryLevel);
                } else {
                  result.error("UNAVAILABLE", "Battery level not available.", null);
                }
              } else {
                result.notImplemented();
              }
            }
        );

    // create a MethodChannel and set a MethodCallHandler inside the onCreate() method
    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), METHOD_CHANNEL_CITIZEN_CARD)
        .setMethodCallHandler(
            (call, result) -> {
              // Note: this method is invoked on the main thread.
              if (call.method.equals(METHOD_CHANNEL_CITIZEN_CARD_GET_CITIZEN_CARD_DATA)) {
              //// emmit event on mainLooper
              //new Handler(Looper.getMainLooper()).post(() -> {
                // call  getCitizenCardData
                PersonPayload payload = getCitizenCardData();
                // get hashMap
                result.success(payload.getHashMap());
              //});
              } else if (call.method.equals(METHOD_CHANNEL_CITIZEN_CARD_SEND_RECEIVE_MAP_JSON_TEST)) {
                User user = new User(((HashMap<String, String>) call.arguments));
                // get hashMap
                result.success(user.getHashMap());
              } else {
                result.notImplemented();
              }
            }
        );
  }

  private int getBatteryLevel() {
    int batteryLevel = -1;
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
      batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    } else {
      Intent intent = new ContextWrapper(getApplicationContext()).
          registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
          intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }

    return batteryLevel;
  }

  private void setupCitizenCardReader() {
    try {
      String encodedLicense = network.solidary.mobile.BuildConfig.encodedLicense;
      String deviceID = getDeviceId(getApplication());
      String packageName = getApplicationContext().getPackageName();
      Log.i(TAG, String.format("packageName: %s", packageName));
      Log.i(TAG, String.format("deviceID: %s", deviceID));

      // your application context
      CitizenCardReader.setup(getApplication(),
          // the base url to the signatures server
          REMOTE_HOST_ADDRESS,
          // your license encoded as Base64
          encodedLicense,
          // your deviceID
          deviceID
      ).connect(eventType -> {
        Log.i(TAG, String.format("onEvent: %s", eventType.toString()));
        String message = "Channel: Unknown Message";

        switch (eventType) {
          case SEARCHING_READER:
            message = "Channel: Searching Reader";
            break;
          case READER_DISCONNECTED:
            message = "Channel: Reader disconnected";
            break;
          case REQUESTING_USB_PERMISSIONS:
            message = "Channel: Requesting usb permissions";
            break;
          case USB_PERMISSIONS_REFUSED:
            message = "Channel: Usb permissions refused";
            break;
          case READER_POWERING_UP:
            message = "Channel: Reader powering up";
            break;
          case READER_POWERUP_FAILED:
            message = "Channel: Reader power up failed";
            break;
          case READER_READY:
            message = "Channel: Reader ready";
            break;
          case CARD_POWERING_UP:
            message = "Channel: Card powering up";
            break;
          case CARD_STATUS_UNKNOWN:
            message = "Channel: Card status unknown";
            break;
          case CARD_INITIALIZING:
            message = "Channel: Card initializing";
            break;
          case CARD_DETECTED:
            message = "Channel: Card detected";
            break;
          case CARD_READY:
            message = "Channel: Card ready";
            // getCitizenCardData();
            break;
          case CARD_REMOVED:
            message = "Channel: Card removed";
            break;
          case CARD_ERROR:
            message = "Channel: Card error";
            break;
          case BLUETOOTH_PAIRING_CORRUPTED:
            message = "Channel: Bluetooth pairing corrupt";
            break;
        }
        Log.i(TAG, message);
        // emmit event on mainLooper
        if (eventChannelEmitter != null) {
          new Handler(Looper.getMainLooper()).post(() -> eventChannelEmitter.success(eventType.toString()));
        }
      });
    } catch (InvalidLicenseException e) {
      Log.e(TAG, "Detected invalid license: " + e.getMessage());
    }
  }

  void startListening(Object listener, EventChannel.EventSink emitter) {
    // store emitter reference
    eventChannelEmitter = emitter;
    // Prepare a timer like self calling task
    final Handler handler = new Handler();
    listeners.put(listener, new Runnable() {
      @Override
      public void run() {
        if (listeners.containsKey(listener)) {
          // Send some value to callback
          emitter.success("Hello listener! " + (System.currentTimeMillis() / timerInterval));
          handler.postDelayed(this, timerInterval);
        }
      }
    });
    // Run task
    handler.postDelayed(listeners.get(listener), timerInterval);
  }

  void cancelListening(Object listener) {
    // Remove callback
    listeners.remove(listener);
  }

  @SuppressLint("HardwareIds")
  public static String getDeviceId(Context context) {
    return Settings.Secure.getString(
        context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  private PersonPayload getCitizenCardData() {
    PersonPayload result = null;
    // At this point you can start using all the card functionality
    String message = "Card reader is ready for operations";

    try {
      // get Picture
      Bitmap picture = CitizenCard.getPicture();
      // get card type
      String cardType = CitizenCard.getCardType();
      // get public info
      Person person = CitizenCard.getPublicData();
      // get certificates
      X509Certificate signingCert = CitizenCard.getSigningCertificate();
      X509Certificate signingCACert = CitizenCard.getCASigningCertificate();
      X509Certificate authCert = CitizenCard.getAuthenticationCertificate();
      X509Certificate authCACert = CitizenCard.getCAAuthCertificate();
      X509Certificate rootCACert = CitizenCard.getCARootCertificate();

      // set PersonPayload
      result = new PersonPayload(cardType, Util.formatPersonData(person), signingCert, signingCACert, authCert, authCACert, rootCACert);
    } catch (NoCardDetectedException e) {
      e.printStackTrace();
    } catch (CardException e) {
      e.printStackTrace();
    }

    return result;
  }
}
