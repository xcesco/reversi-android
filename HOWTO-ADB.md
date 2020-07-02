# Cnofigure remote connection on device on OSX
Add `adb` to path.
```
export PATH=$PATH:~/Library/Android/sdk/platform-tools/
```
Connect real device with usb. Show all connected devices.
```
adb devices
```
To connect to a particular device on tcp port
```
adb -s <serial-id> tcpip 5555
adb connect <ip>:5555
```

Custom
```
adb -s 30bbdac1 tcpip 5555
adb connect 192.168.1.12:5555
```

```
adb -s ZX1G42BFRH tcpip 5555
adb connect 192.168.1.2:5555
```

## Enable room debug
 adb shell setprop log.tag.SQLiteStatements VERBOSE
 adb root
 adb shell stop
  adb shell start