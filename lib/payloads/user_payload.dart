class User {
  final String userDob;
  final String userDescription;
  final String userGender;
  final String userSession;

  User(
    this.userDob,
    this.userDescription,
    this.userGender,
    this.userSession,
  );

  User.fromJson(Map<String, dynamic> json)
      : userDob = json['userDob'],
        userDescription = json['userDescription'],
        userGender = json['userGender'],
        userSession = json['userSession'];

  Map<String, dynamic> toJson() => {
        'userDob': userDob,
        'userDescription': userDescription,
        'userGender': userGender,
        'userSession': userSession,
      };
}
