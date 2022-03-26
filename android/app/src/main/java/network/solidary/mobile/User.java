package network.solidary.mobile;

import java.util.HashMap;

public class User {
  private String userDob;
  private String userDescription;
  private String userGender;
  private String userSession;

  public User(HashMap<String, String> hashMap) {
    this.userDob = (hashMap.containsKey("userDob")) ? hashMap.get("userDob") : null;
    this.userDescription = (hashMap.containsKey("userDescription")) ? hashMap.get("userDescription") : null;
    this.userGender = (hashMap.containsKey("userGender")) ? hashMap.get("userGender") : null;
    this.userSession = (hashMap.containsKey("userSession")) ? hashMap.get("userSession") : null;
  }

  // get hashMap to send respond to platform channel flutter
  public HashMap<String, String> getHashMap() {
    HashMap<String, String> hashMap = new HashMap<String, String>();
    hashMap.put("userDob", String.format("*%s*", this.getUserDob()));
    hashMap.put("userDescription", String.format("*%s*", this.getUserDescription()));
    hashMap.put("userGender", String.format("*%s*", this.getUserGender()));
    hashMap.put("userSession", String.format("*%s*", this.getUserSession()));
    return hashMap;
  }

  public String getUserDob() {
    return userDob;
  }

  public void setUserDob(String userDob) {
    this.userDob = userDob;
  }

  public String getUserDescription() {
    return userDescription;
  }

  public void setUserDescription(String userDescription) {
    this.userDescription = userDescription;
  }

  public String getUserGender() {
    return userGender;
  }

  public void setUserGender(String userGender) {
    this.userGender = userGender;
  }

  public String getUserSession() {
    return userSession;
  }

  public void setUserSession(String userSession) {
    this.userSession = userSession;
  }
}
