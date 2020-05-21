package network.solidary.mobile;

import java.security.cert.X509Certificate;
import java.util.HashMap;

import io.ptech.cc.android.sdk.exported.model.Person;

class PersonPayload extends Person {

  private String cardType;
  private X509Certificate signingCert;
  private X509Certificate signingCACert;
  private X509Certificate authCert;
  private X509Certificate authCACert;
  private X509Certificate rootCACert;

  PersonPayload(String cardType, Person person, X509Certificate signingCert, X509Certificate signingCACert, X509Certificate authCert, X509Certificate authCACert, X509Certificate rootCACert) {
    this.cardType = cardType;
    this.signingCert = signingCert;
    this.signingCACert = signingCACert;
    this.authCert = authCert;
    this.authCACert = authCACert;
    this.rootCACert = rootCACert;
    setEmittingEntity(person.getEmittingEntity());
    setCountry(person.getCountry());
    setDocumentType(person.getDocumentType());
    setDocumentNumber(person.getDocumentNumber());
    setPan(person.getPan());
    setCardVersion(person.getCardVersion());
    setEmissionDate(person.getEmissionDate());
    setRequestLocation(person.getRequestLocation());
    setExpirationDate(person.getExpirationDate());
    setLastName(person.getLastName());
    setFirstName(person.getFirstName());
    setGender(person.getGender());
    setNationality(person.getNationality());
    setBirthDate(person.getBirthDate());
    setHeight(person.getHeight());
    setIdentityNumber(person.getIdentityNumber());
    setMotherLastName(person.getMotherLastName());
    setMotherFirstName(person.getMotherFirstName());
    setFatherLastName(person.getFatherLastName());
    setFatherFirstName(person.getFatherFirstName());
    setFiscalNumber(person.getFiscalNumber());
    setSocialSecurityNumber(person.getSocialSecurityNumber());
    setBeneficiaryNumber(person.getBeneficiaryNumber());
    setOtherInformation(person.getOtherInformation());
  }

  // get hashMap to send respond to platform channel flutter
  public HashMap<String, String> getHashMap() {
    HashMap<String, String> hashMap = new HashMap<String, String>();
    hashMap.put("country", this.getCountry());
    hashMap.put("documentType", this.getDocumentType());
    hashMap.put("documentNumber", this.getDocumentNumber());
    hashMap.put("pan", this.getPan());
    hashMap.put("cardVersion", this.getCardVersion());
    hashMap.put("emissionDate", this.getEmissionDate());
    hashMap.put("requestLocation", this.getRequestLocation());
    hashMap.put("expirationDate", this.getExpirationDate());
    hashMap.put("lastName", this.getLastName());
    hashMap.put("firstName", this.getFirstName());
    hashMap.put("gender", this.getGender());
    hashMap.put("nationality", this.getNationality());
    hashMap.put("birthDate", this.getBirthDate());
    hashMap.put("height", this.getHeight());
    hashMap.put("identityNumber", this.getIdentityNumber());
    hashMap.put("motherLastName", this.getMotherLastName());
    hashMap.put("motherFirstName", this.getMotherFirstName());
    hashMap.put("fatherLastName", this.getFatherLastName());
    hashMap.put("fatherFirstName", this.getFatherFirstName());
    hashMap.put("fiscalNumber", this.getFiscalNumber());
    hashMap.put("socialSecurityNumber", this.getSocialSecurityNumber());
    hashMap.put("beneficiaryNumber", this.getBeneficiaryNumber());
    hashMap.put("otherInformation", this.getOtherInformation());
    hashMap.put("cardType", this.getCardType());
    return hashMap;
  }

  // getters and setters

  public String getCardType() {
    return cardType;
  }

  public void setCardType(String cardType) {
    this.cardType = cardType;
  }

  public X509Certificate getSigningCert() {
    return signingCert;
  }

  public void setSigningCert(X509Certificate signingCert) {
    this.signingCert = signingCert;
  }

  public X509Certificate getSigningCACert() {
    return signingCACert;
  }

  public void setSigningCACert(X509Certificate signingCACert) {
    this.signingCACert = signingCACert;
  }

  public X509Certificate getAuthCert() {
    return authCert;
  }

  public void setAuthCert(X509Certificate authCert) {
    this.authCert = authCert;
  }

  public X509Certificate getAuthCACert() {
    return authCACert;
  }

  public void setAuthCACert(X509Certificate authCACert) {
    this.authCACert = authCACert;
  }

  public X509Certificate getRootCACert() {
    return rootCACert;
  }

  public void setRootCACert(X509Certificate rootCACert) {
    this.rootCACert = rootCACert;
  }
}
