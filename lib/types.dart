enum cardEventTypeState {
  SEARCHING_READER,
  READER_DISCONNECTED,
  REQUESTING_USB_PERMISSIONS,
  USB_PERMISSIONS_REFUSED,
  READER_POWERING_UP,
  READER_POWERUP_FAILED,
  READER_READY,
  CARD_POWERING_UP,
  CARD_STATUS_UNKNOWN,
  CARD_INITIALIZING,
  CARD_DETECTED,
  CARD_READY,
  CARD_REMOVED,
  CARD_ERROR,
  BLUETOOTH_PAIRING_CORRUPTED,
}

cardEventTypeState enumTypeFromString(String typeString) => cardEventTypeState.values
    .firstWhere((type) => type.toString() == "cardEventTypeState." + typeString);
String enumTypeToString(cardEventTypeState type) => type.toString().split(".")[1];

// Map using Map Literals
Map<String, String> cardEventType = {
  'SEARCHING_READER': 'SEARCHING_READER',
  'READER_DISCONNECTED': 'READER_DISCONNECTED',
  'REQUESTING_USB_PERMISSIONS': 'REQUESTING_USB_PERMISSIONS',
  'USB_PERMISSIONS_REFUSED': 'USB_PERMISSIONS_REFUSED',
  'READER_POWERING_UP': 'READER_POWERING_UP',
  'READER_POWERUP_FAILED': 'READER_POWERUP_FAILED',
  'READER_READY': 'READER_READY',
  'CARD_POWERING_UP': 'CARD_POWERING_UP',
  'CARD_STATUS_UNKNOWN': 'CARD_STATUS_UNKNOWN',
  'CARD_INITIALIZING': 'CARD_INITIALIZING',
  'CARD_DETECTED': 'CARD_DETECTED',
  'CARD_READY': 'CARD_READY',
  'CARD_REMOVED': 'CARD_REMOVED',
  'CARD_ERROR': 'CARD_ERROR',
  'BLUETOOTH_PAIRING_CORRUPTED': 'BLUETOOTH_PAIRING_CORRUPTED'
};