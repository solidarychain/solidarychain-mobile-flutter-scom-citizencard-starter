class Person {
  final String emittingEntity;
  final String country;
  final String documentType;
  final String documentNumber;
  final String pan;
  final String cardVersion;
  final String emissionDate;
  final String requestLocation;
  final String expirationDate;
  final String lastName;
  final String firstName;
  final String gender;
  final String nationality;
  final String birthDate;
  final String height;
  final String identityNumber;
  final String motherLastName;
  final String motherFirstName;
  final String fatherLastName;
  final String fatherFirstName;
  final String fiscalNumber;
  final String socialSecurityNumber;
  final String beneficiaryNumber;
  final String otherInformation;
  final String cardType;

  Person(
    this.emittingEntity,
    this.country,
    this.documentType,
    this.documentNumber,
    this.pan,
    this.cardVersion,
    this.emissionDate,
    this.requestLocation,
    this.expirationDate,
    this.lastName,
    this.firstName,
    this.gender,
    this.nationality,
    this.birthDate,
    this.height,
    this.identityNumber,
    this.motherLastName,
    this.motherFirstName,
    this.fatherLastName,
    this.fatherFirstName,
    this.fiscalNumber,
    this.socialSecurityNumber,
    this.beneficiaryNumber,
    this.otherInformation,
    this.cardType,
  );

  Person.fromJson(Map<String, dynamic> json)
      : emittingEntity = json['emittingEntity'],
        country = json['country'],
        documentType = json['documentType'],
        documentNumber = json['documentNumber'],
        pan = json['pan'],
        cardVersion = json['cardVersion'],
        emissionDate = json['emissionDate'],
        requestLocation = json['requestLocation'],
        expirationDate = json['expirationDate'],
        lastName = json['lastName'],
        firstName = json['firstName'],
        gender = json['gender'],
        nationality = json['nationality'],
        birthDate = json['birthDate'],
        height = json['height'],
        identityNumber = json['identityNumber'],
        motherLastName = json['motherLastName'],
        motherFirstName = json['motherFirstName'],
        fatherLastName = json['fatherLastName'],
        fatherFirstName = json['fatherFirstName'],
        fiscalNumber = json['fiscalNumber'],
        socialSecurityNumber = json['socialSecurityNumber'],
        beneficiaryNumber = json['beneficiaryNumber'],
        otherInformation = json['otherInformation'],
        cardType = json['cardType'];

  Map<String, dynamic> toJson() => {
        'emittingEntity': emittingEntity,
        'country': country,
        'documentNumber': documentNumber,
        'pan': pan,
        'cardVersion': cardVersion,
        'emissionDate': emissionDate,
        'requestLocation': requestLocation,
        'expirationDate': expirationDate,
        'lastName': lastName,
        'firstName': firstName,
        'gender': gender,
        'nationality': nationality,
        'birthDate': birthDate,
        'height': height,
        'identityNumber': identityNumber,
        'motherLastName': motherLastName,
        'motherFirstName': motherFirstName,
        'fatherLastName': fatherLastName,
        'fatherFirstName': fatherFirstName,
        'fiscalNumber': fiscalNumber,
        'socialSecurityNumber': socialSecurityNumber,
        'beneficiaryNumber': beneficiaryNumber,
        'otherInformation': otherInformation,
        'cardType': cardType,
      };
}
