# QuakeAlert
QuakeAlert is an Android application designed to detect seismic-like device vibrations using onboard motion sensors and visualize activity trends in real time. The app focuses on early warning concepts, data persistence, and offline-friendly visualization without relying on paid map services.
Key Features

📱 Real-time Shake Detection
Uses the device accelerometer to detect abnormal vibrations that may indicate seismic activity.

📊 Live Magnitude Chart
Detected shake events are stored locally using Room Database and visualized dynamically using MPAndroidChart.

🗺️ Offline Map Visualization (OSMDroid)
Earthquake locations are displayed using OpenStreetMap (OSM) via OSMDroid — no Google Maps or paid API required.

💾 Local Data Storage
Shake events and earthquake data are persisted using Room, enabling historical analysis even when offline.

🔔 Smart Alerts & Notifications
Customizable alerts trigger notifications when vibration thresholds are exceeded.

⚙️ User Settings
Adjust sensor sensitivity and alert thresholds based on user preference.

Provides quick-access emergency actions and safety information.
![mscren](https://github.com/user-attachments/assets/53d0f485-95c8-4de0-9a09-9e7b3126cd2e)
![map](https://github.com/user-attachments/assets/23b8ab7a-3231-47c0-8a7d-f0de2daf0e70)
![set](https://github.com/user-attachments/assets/8a13420c-1120-4ef1-9e33-8974d1a03ff0)

