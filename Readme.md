# Wikipedia Points of Interest App

This Kotlin application leverages Wikipedia's geosearch API to display points of interest based on geographical proximity. The app provides information about nearby locations from Wikipedia, allowing users to explore interesting places around them.

## Features

- **Geographical Proximity:** Utilizes Wikipedia's geosearch API to find points of interest near the user's location.

## Usage

> [!WARNING]
> If put `android:exported="false"` in manifest, can generate a "open close" the first time you open the app.



### JSON Structure 1:

`https://en.wikipedia.org/w/api.php?action=query&list=geosearch&gscoord=37.7749|-122.4194&gsradius=10000&format=json`

- **Parámetros:**
  - `action=query`: Indica que se realizará una consulta.
  - `list=geosearch`: Especifica que se desea obtener una lista de resultados de búsqueda geográfica.
  - `gscoord=37.7749|-122.4194`: Coordenadas geográficas de referencia para la búsqueda.
  - `gsradius=10000`: Radio en metros alrededor de las coordenadas de referencia en el que se buscarán páginas.
  - `format=json`: Indica que se desea la respuesta en formato JSON.

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

`https://en.wikipedia.org/w/api.php?action=query&prop=coordinates|pageimages|info&pageids=2239406|1511602&inprop=url&format=json`

- **Parámetros:**
  - `action=query`: Indica que se realizará una consulta.
  - `prop=coordinates|pageimages|info`: Especifica los elementos que se desean obtener (coordenadas, imágenes, información general).
  - `pageids=2239406|1511602`: IDs de las páginas para las cuales se busca información.
  - `inprop=url`: Incluye la URL de las páginas en la respuesta.
  - `format=json`: Indica que se desea la respuesta en formato JSON.

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

