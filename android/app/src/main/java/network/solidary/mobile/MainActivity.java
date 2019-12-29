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
  // define method channel: Make sure to use the same channel name as was used on the Flutter client side.
  private static final String CHANNEL = "samples.flutter.dev/battery";
  private static final String CHANNEL_CITIZEN_CARD = "samples.flutter.dev/citizencard";
  // event channel
  private EventChannel channel;
  // Listeners
  private Map<Object, Runnable> listeners = new HashMap<>();
  // store EventChannel emitter reference, to be used outside of
  private EventChannel.EventSink eventChannelEmitter;
  // private Handler eventChannelHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Prepare channel
    channel = new EventChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), "events");
    channel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object listener, EventChannel.EventSink eventSink) {
        startListening(listener, eventSink);
      }

      @Override
      public void onCancel(Object listener) {
        cancelListening(listener);
      }
    });

    // initialize citizenCardReader
    setupCitizenCardReader();
  }

  @Override
  public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
    GeneratedPluginRegistrant.registerWith(flutterEngine);

    // create a MethodChannel and set a MethodCallHandler inside the onCreate() method
    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
      .setMethodCallHandler(
        (call, result) -> {
          // Note: this method is invoked on the main thread.
          if (call.method.equals("getBatteryLevel")) {
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
    new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL_CITIZEN_CARD)
      .setMethodCallHandler(
        (call, result) -> {
          // Note: this method is invoked on the main thread.
          if (call.method.equals("getCitizenCardData")) {
            PersonPayload payload = getCitizenCardData();
            result.success(payload);
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
      //String packageName = getApplicationContext().getPackageName();
      //Log.i(TAG, String.format("packageName: %s", packageName));
      //Log.i(TAG, String.format("deviceID: %s", deviceID));

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
        String message;

        switch (eventType) {
          case SEARCHING_READER:
            message = "Searching Reader";
            Log.e(TAG, message);
            break;
          case READER_DISCONNECTED:
            message = "Reader disconnected";
            Log.i(TAG, message);
            break;
          case REQUESTING_USB_PERMISSIONS:
            message = "Requesting usb permissions";
            Log.i(TAG, message);
            break;
          case USB_PERMISSIONS_REFUSED:
            message = "Usb permissions refused";
            Log.e(TAG, message);
            break;
          case READER_POWERING_UP:
            message = "Reader powering up";
            Log.i(TAG, message);
            break;
          case READER_POWERUP_FAILED:
            message = "Reader power up failed";
            Log.e(TAG, message);
            break;
          case READER_READY:
            message = "Reader ready";
            Log.i(TAG, message);
            break;
          case CARD_POWERING_UP:
            message = "Card powering up";
            Log.i(TAG, message);
            break;
          case CARD_STATUS_UNKNOWN:
            message = "Card status unknown";
            Log.e(TAG, message);
            break;
          case CARD_INITIALIZING:
            message = "Card initializing";
            Log.i(TAG, message);
            break;
          case CARD_DETECTED:
            message = "Card detected";
            Log.i(TAG, message);
            break;
          case CARD_READY:
            message = "Card ready";
            Log.i(TAG, message);
            // readCard();
            break;
          case CARD_REMOVED:
            message = "Card removed";
            Log.i(TAG, message);
            break;
          case CARD_ERROR:
            message = "Card error";
            Log.e(TAG, message);
            break;
          case BLUETOOTH_PAIRING_CORRUPTED:
            message = "Bluetooth pairing corrupt";
            Log.e(TAG, message);
            break;
        }
        // emmit event
        if (eventChannelEmitter != null) {
          eventChannelEmitter.success(eventType.toString());
        }
      });
    } catch (InvalidLicenseException e) {
      Log.e(TAG, "Detected invalid license: " + e.getMessage());
    }
  }

  void startListening(Object listener, EventChannel.EventSink emitter) {
    eventChannelEmitter = emitter;
    // Prepare a timer like self calling task
    final Handler handler = new Handler();
    listeners.put(listener, new Runnable() {
      @Override
      public void run() {
        if (listeners.containsKey(listener)) {
          // Send some value to callback
          //emitter.success("Hello listener! " + (System.currentTimeMillis() / 1000));
          //handler.postDelayed(this, 1000);
        }
      }
    });
    // Run task
    //handler.postDelayed(listeners.get(listener), 1000);
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
