# Battleship-Calibration

### Assumptions
- ``Rotation Start Point`` and ``Rotation End Point`` will always be integers.
- ``Number of times a turret is tested`` is equal to number of times ``run`` is triggered for a specific turret.
- ``Number of times a turret is tested`` must be able to withstand application reboots.
- None of the turret setting parameters are persisted for future use or to withstand applications reboots. However, a log is maintained.
- ``Run`` always triggers the latest turret setting

### Choice of Technology
``Spring Boot v3.4.3`` on ``JDK21`` has been selected as the backend technology due to the following reasons.
- My familiarity with the technology and my available local setup.
- Boiler code for database access, environment separation, test cases etc is available out-of-the-box.
- Easy to structure production-grade code with different layers of implementation such as controllers, service, repository etc.

### Improvements
- Dockerise to keep a consistent image across environments.
- Using a password to connect to database and encrypting it using JasyptEncryptor.

### Build and Test
``mvn clean install``

### Package
``mvn clean package``

### Run
``mvn spring-boot:run -Dspring-boot.run.profiles=local``