package network.solidary.mobile;

import io.ptech.cc.android.sdk.exported.model.Person;

public class Util {

  public static String captializeAllFirstLetter(String input) {
    char[] array = input.toCharArray();
    array[0] = Character.toUpperCase(array[0]);

    for (int i = 1; i < array.length; i++) {
      if (Character.isWhitespace(array[i - 1])) {
        array[i] = Character.toUpperCase(array[i]);
      }
    }

    return new String(array);
  }

  public static Person formatPersonData(Person person) {
    Person result;
    result = person;
    result.setDocumentType(captializeAllFirstLetter(person.getDocumentType().toLowerCase()));
    result.setFatherFirstName(captializeAllFirstLetter(person.getFatherFirstName().toLowerCase()));
    result.setFatherLastName(captializeAllFirstLetter(person.getFatherLastName().toLowerCase()));
    result.setFirstName(captializeAllFirstLetter(person.getFirstName().toLowerCase()));
    result.setLastName(captializeAllFirstLetter(person.getLastName().toLowerCase()));
    result.setMotherFirstName(captializeAllFirstLetter(person.getMotherFirstName().toLowerCase()));
    result.setMotherLastName(captializeAllFirstLetter(person.getMotherLastName().toLowerCase()));
    return result;
  }
}
