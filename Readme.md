# Wikipedia Points of Interest App

This Kotlin application leverages Wikipedia's geosearch API to display points of interest based on geographical proximity. The app provides information about nearby locations from Wikipedia, allowing users to explore interesting places around them.

## Features

- **Geographical Proximity:** Utilizes Wikipedia's geosearch API to find points of interest near the user's location.

## Usage

> [!CAUTION]
> If put `android:exported="false"` in manifest, can generate a "open close" the first time you open the app.



### JSON Structure 1:



```json
{
    "batchcomplete": "",
    "query": {
        "geosearch": [
            {
                "pageid": 2239406,
                "ns": 0,
                "title": "Fillmore West",
                "lat": 37.774742,
                "lon": -122.419433,
                "dist": 17.8,
                "primary": ""
            }
        ]  
    }  
}
```

This JSON structure contains information about a list of nearest point around you.

### JSON Structure 2:

```json
{
    "warnings": {
        "main": {
            "*": "Unrecognized parameter: inprop."
        }
    },
    "batchcomplete": "",
    "query": {
        "pages": {
            "7927918": {
                "pageid": 7927918,
                "ns": 0,
                "title": "Facultad de Traducción e Interpretación de Granada (Spain)",
                "coordinates": [
                    {
                        "lat": 37.17555556,
                        "lon": -3.60361111,
                        "primary": "",
                        "globe": "earth"
                    }
                ],
                "thumbnail": {
                    "source": "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Palacio_de_las_columnas.jpg/50px-Palacio_de_las_columnas.jpg",
                    "width": 50,
                    "height": 38
                },
                "pageimage": "Palacio_de_las_columnas.jpg"
            }
        }
    }
}
```

This JSON structure contains information and images about a list of page IDs that you must provide in the url parameters.

