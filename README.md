## Klivvr Challenge Submission
This repository contains my solution for Klivvr assignment.

## Solutions Used
### **Cities Loading**
Loaded the cities from the Json file and serialized using _kotlinx-serialization_, then mapped to a domain data class that represents a City entity in app. Lastly sorted based on a searchKey that consists of [city_name,country]

Loading was done concurrently using Coroutines at the startup of the app. The loading state was observed using a StateFlow with a **LoadState** sealed class.

### **Searching Requirement**
Binary searching the list of cities based on a special search key

### **Country Flag Loading**
There were two solutions that I faced to load the country images
- Storing country image **Drawable** files and mapping them to CityItem.
- Using the **Hex code** of the country based on the 2-digit country letters.

I went with the first solution due to consistency across all devices and the size impact was not huge, it even can be enhanced by converting the png files into webp.

The drawback of the Hex code is that it depends on the device rendering of emojis, which may not be consistent across all devices.


### Libraries used
- Hilt
- Kotlinx-serialization
