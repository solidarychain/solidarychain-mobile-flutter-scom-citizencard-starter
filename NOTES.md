# NOTES

log message on expire license
E/network.solidary.mobile(11759): Detected invalid license: License expired on: Fri Jan 01 00:00:00 GMT+00:00 2021

## TLDR

WARN: expired license don't activate card reader, is like it isn't working

view in logs, comes from setupCitizenCardReader()

I/network.solidary.mobile(24093): packageName: network.solidary.mobile
I/network.solidary.mobile(24093): deviceID: d07ac8d12629634e
D/LicenseManager(24093): Decoded license: LicenseEnvelope{version=1, type=CLIENT, info='LicenseInfo [application=network.solidary.mobile, apiKey=0675969c-ce6b-4735-821d-71fc6aa8bd9f, platforms=[IOS, ANDROID], ops=[READ_PUBLIC_DATA, READ_ADDRESS], expirationDate=Fri Jan 01 00:00:00 GMT+00:00 2021]', signature='P8ek3V/RtD97odGwjQzGE+vmLn/8LNoR4NNtQBrSR2dcGqhrqdCsESHOxs1uqQuy/ZqiXj2cTd7c9uwKrvmWB1EcdwWMwgvyC76M2uQCIs/bzA+Ka1FbLlHe80XtXS9CEv532GHETzV8pVdHW7i34EHNbCVIdNmgbyXtctLp86bj9qxzaJ09l5FAkAhWViSItpl1o7jcyaJVHl/GAA7RY0kJxjvzNIRh1G6RUYoBN4GXpJdmSUyBp7Gui9WTX3fZF0swsQXoar3MC/I8vgBx3uBJ7UTtGOePcZvGiMNot8ZNfLOft/y4k4T3af5KG3//ZdBvax1NWuVzhtTlzRQwcA=='}
E/network.solidary.mobile(24093): Detected invalid license: License expired on: Fri Jan 01 00:00:00 GMT+00:00 2021

to prevent problems

1. always start app without card reader and without card on it
2. start app
3. plug card reader
4. wait for reader ready
5. insert card
6. read card

## Links

### EventChannel

- [Listeners with EventChannel in Flutter](https://blog.testfairy.com/listeners-with-eventchannel-in-flutter)
- [GitHub Repo](https://github.com/testfairy-blog/FlutterEventChannels)

## Check java version

```shell
$ java -version
openjdk version "11.0.8" 2020-07-14
OpenJDK Runtime Environment (build 11.0.8+10-suse-1.3-x8664)
OpenJDK 64-Bit Server VM (build 11.0.8+10-suse-1.3-x8664, mixed mode)
```

### Failed to install the following Android SDK packages as some licences have not been accepted.

https://stackoverflow.com/questions/54273412/failed-to-install-the-following-android-sdk-packages-as-some-licences-have-not

```
FAILURE: Build failed with an exception.

* What went wrong:
Could not determine the dependencies of task ':app:compileDebugJavaWithJavac'.
> Failed to install the following Android SDK packages as some licences have not been accepted.
```

```shell
$ yes | ~/Android/Sdk/tools/bin/sdkmanager --licenses
Exception in thread "main" java.lang.NoClassDefFoundError: javax/xml/bind/annotation/XmlSchema
        at com.android.repository.api.SchemaModule$SchemaModuleVersion.<init>(SchemaModule.java:156)
        at com.android.repository.api.SchemaModule.<init>(SchemaModule.java:75)
        at com.android.sdklib.repository.AndroidSdkHandler.<clinit>(AndroidSdkHandler.java:81)
        at com.android.sdklib.tool.sdkmanager.SdkManagerCli.main(SdkManagerCli.java:73)
        at com.android.sdklib.tool.sdkmanager.SdkManagerCli.main(SdkManagerCli.java:48)
Caused by: java.lang.ClassNotFoundException: javax.xml.bind.annotation.XmlSchema
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522)
        ... 5 more
```

https://github.com/flutter/flutter/issues/56778
flutter doctor --android-licenses

nano ~/.bashrc


# flutter
export PATH=$PATH:/opt/flutter/bin


source ~/.bashrc

flutter doctor --android-licenses
Exception in thread "main" java.lang.NoClassDefFoundError: javax/xml/bind/annotation/XmlSchema


https://en.opensuse.org/SDB:Installing_Java

rpm -ivh ~/Downloads/jre-8u271-linux-x64.rpm
warning: /home/mario/Downloads/jre-8u271-linux-x64.rpm: Header V3 RSA/SHA256 Signature, key ID ec551f03: NOKEY
Verifying...                          ################################# [100%]
Preparing...                          ################################# [100%]
Updating / installing...
   1:jre1.8-1.8.0_271-fcs             ################################# [100%]
Unpacking JAR files...
	plugin.jar...
	javaws.jar...
	deploy.jar...
	rt.jar...
	jsse.jar...
	charsets.jar...
	localedata.jar...
update-alternatives: using /usr/java/jre1.8.0_271-amd64/bin/java to provide /usr/bin/java (java) in auto mode


fix with
export JAVA_HOME=/usr/java/jre1.8.0_271-amd64/
mario@links-laptop:/media/mario/storage/Documents/Development/@SolidaryChain/solidarychain-mobile-flutter-scom-citizencard-starter> flutter doctor --android-licenses
Warning: File /home/mario/.android/repositories.cfg could not be loaded.
6 of 6 SDK package licenses not accepted. 100% Computing updates...
Review licenses that have not been accepted (y/N)?

add o bashrc
 JAVA_HOME=/usr/java/jre1.8.0_271-amd64/

now we can build project




https://www.linux-kvm.org/page/USB_Host_Device_Assigned_to_Guest

lsusb
Bus 001 Device 005: ID 0d62:d93f Darfon Electronics Corp. Smart Card Reader Interface

ps aux | grep /emulator64
/home/mario/Android/Sdk/emulator/emulator64-crash-service -pipe 4 -ppid 10516 -data-dir /tmp/android-mario/6e2ed797-fa0c-4829-80ce-c7fe07e91f54

-usb -device usb-host,hostbus=1,hostaddr=5
/home/mario/Android/Sdk/emulator/emulator64-crash-service -pipe 4 -ppid 10516 -data-dir /tmp/android-mario/6e2ed797-fa0c-4829-80ce-c7fe07e91f54 -usb -device usb-host,hostbus=1,hostaddr=5

file:///home/mario/.android/avd/Nexus_5_API_24.avd/config.ini

USB passthrough is now available on Windows using -qemu -usb -device usb-host,vendorid=<usb-vendor-id>,productid=<usb-product-id>. (This should also have been workng on Linux and macOS already)
https://developer.android.com/studio/releases/emulator#other_new_features_and_enhancements

Make Android Emulator use physical usb camera
https://stackoverflow.com/questions/44699625/make-android-emulator-use-physical-usb-camera


https://developer.android.com/studio/run/emulator-commandline.html



emulator -avd avd_name [ {-option [value]} … ]


emulator -avd Nexus_5X_API_23 -netdelay none -netspeed full -usb -device usb-host,hostbus=1,hostaddr=5

./emulator -list-avds
Nexus_5_API_24
Pixel_2_API_24
Pixel_3a_API_30_x86

https://stackoverflow.com/questions/7875061/connect-usb-device-to-android-emulator

this works and launch emu
cd ~/Android/Sdk/emulator
./emulator -avd Nexus_5_API_24 -qemu -m 2048 -enable-kvm

almost
./emulator -avd Nexus_5_API_24 -qemu -m 2048 -enable-kvm -usb -device usb-host,hostbus=1,hostaddr=5
libusb: error [get_usbfs_fd] libusb requires write access to USB device nodes




lsusb -t
/:  Bus 01.Port 1: Dev 1, Class=root_hub, Driver=xhci_hcd/16p, 480M
    |__ Port 1: Dev 8, If 0, Class=Chip/SmartCard, Driver=, 480M


Can I pass through a USB Port via qemu Command Line?
https://unix.stackexchange.com/questions/452934/can-i-pass-through-a-usb-port-via-qemu-command-line

sudo nano /etc/udev/rules.d/cardreadervendor.rules

# Bus 001 Device 005: ID 0d62:d93f Darfon Electronics Corp. Smart Card Reader Interface
SUBSYSTEM=="usb", ATTRS{idVendor}=="0d62", MODE="0666"
SUBSYSTEM=="usb_device", ATTRS{idVendor}=="0d62", MODE="0666"

do not forget to reload rules with:
$ sudo udevadm control --reload-rules




FINAL WORKING LINE AT LAST

mario@links-laptop:~/Android/Sdk/emulator> ./emulator -avd Pixel_2_API_24 -qemu -m 2048 -enable-kvm -usb -device usb-host,hostbus=1,hostport=2
