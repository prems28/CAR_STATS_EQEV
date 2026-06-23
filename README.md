This project CARSTATS is a simple view of the Chevy Equinox battery stats. Its a deriavation of the  VHAL project.

BELOW is the properties and stuff I have used.

https://github.com/chandramouli369/VHAL_AAOS_Project


🚗 
Android Automotive – Car Property & VHAL Integration Demo
This project demonstrates how to work with Android Automotive Car APIs to interact with Vehicle HAL (VHAL) using CarPropertyManager.

The application starts automatically after vehicle boot and runs as a background service. It establishes a connection with the car system, registers required vehicle property IDs, retrieves default values from VHAL, and listens for real-time property changes. When a vehicle property changes, the app processes the update and forwards the signal to a repository layer for further handling, such as UI updates or business logic.

The architecture is designed to be modular and scalable by using a base property handler abstraction. This allows new vehicle properties to be added easily without modifying the core logic. The project also supports sending commands from the application back to the vehicle by setting VHAL properties.

This project is an Android Automotive OS demo application that showcases how to interact with Vehicle HAL (VHAL) using the Car API and CarPropertyManager.

The application runs as a background service, starts automatically after system boot, and listens to vehicle property changes in real time. It also demonstrates how to send commands from the application back to the vehicle using VHAL properties.

The architecture is modular, scalable, and production-oriented, making it suitable for OEM-level Android Automotive development.

✨ Key Features

✅ Automatic startup after vehicle boot

✅ Background service for continuous vehicle signal monitoring

✅ Connection to Android Automotive Car API

✅ Read & write VHAL properties using CarPropertyManager

✅ Real-time property change callbacks

✅ Handler-based architecture for scalability

✅ Support for both Vehicle → App and App → Vehicle communication

✅ Clean separation of concerns


📌 Use Cases

Android Automotive OEM apps

Vehicle signal monitoring

Infotainment integrations

Automotive middleware prototyping

Interview & learning reference project

📝 License

This project is intended for educational and demonstration purposes.
Adapt and extend according to OEM or project requirements.

💬 Final Note

This project demonstrates real-world Android Automotive development patterns using clean architecture, background services, and VHAL integration.

⭐ If you find this useful, consider starring the repository!
